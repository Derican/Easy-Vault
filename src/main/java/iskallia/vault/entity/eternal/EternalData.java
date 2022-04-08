package iskallia.vault.entity.eternal;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.*;

public class EternalData implements INBTSerializable<CompoundNBT>, EternalDataAccess {
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String originalName;
    private long eternalSeed = 3274487651937260739L;

    private final Map<EquipmentSlotType, ItemStack> equipment = new HashMap<>();
    private EternalAttributes attributes = new EternalAttributes();
    private EternalAura ability = null;

    private int level = 0, usedLevels = 0;
    private int levelExp = 0;

    private boolean alive = true;
    private boolean ancient = false;
    private final EternalsData dataDelegate;

    private EternalData(EternalsData dataDelegate, String name, boolean isAncient) {
        this.dataDelegate = dataDelegate;
        this.name = name;
        this.originalName = name;
        this.ancient = isAncient;
        this.attributes.initializeAttributes();
    }

    private EternalData(EternalsData dataDelegate, CompoundNBT nbt) {
        this.dataDelegate = dataDelegate;
        deserializeNBT(nbt);
    }

    public static EternalData createEternal(EternalsData data, String name, boolean isAncient) {
        return new EternalData(data, name, isAncient);
    }

    public static EternalData fromNBT(EternalsData data, CompoundNBT nbt) {
        return new EternalData(data, nbt);
    }


    public UUID getId() {
        return this.uuid;
    }


    public long getSeed() {
        return this.eternalSeed;
    }

    public void shuffleSeed() {
        this.eternalSeed = (new Random()).nextLong();
        this.dataDelegate.setDirty();
    }

    public void setName(String name) {
        this.name = name;
        this.dataDelegate.setDirty();
    }


    public String getName() {
        return this.name;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public EternalAttributes getAttributes() {
        return this.attributes;
    }

    public void addAttributeValue(Attribute attribute, float value) {
        if (this.usedLevels >= this.level) {
            return;
        }
        this.usedLevels++;

        this.attributes.addAttributeValue(attribute, value);
        this.dataDelegate.setDirty();
    }


    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUsedLevels() {
        return this.usedLevels;
    }


    public int getMaxLevel() {
        UUID playerId = this.dataDelegate.getOwnerOf(getId());
        if (playerId == null) {
            return 0;
        }
        return this.dataDelegate.getEternals(playerId).getNonAncientEternalCount() + (
                isAncient() ? 5 : 0);
    }

    public float getLevelPercent() {
        int expNeeded = ModConfigs.ETERNAL.getExpForLevel(getLevel() + 1);
        return this.levelExp / expNeeded;
    }


    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        this.dataDelegate.setDirty();
    }


    public boolean isAncient() {
        return this.ancient;
    }

    public void setAncient(boolean ancient) {
        this.ancient = ancient;
        this.dataDelegate.setDirty();
    }

    public boolean addExp(int xp) {
        if (this.level >= getMaxLevel()) {
            return false;
        }
        this.levelExp += xp;
        int expNeeded = ModConfigs.ETERNAL.getExpForLevel(getLevel() + 1);
        if (this.levelExp >= expNeeded) {
            this.level++;
            this.levelExp -= expNeeded;
        }
        this.dataDelegate.setDirty();
        return true;
    }

    @Nullable
    public EternalAura getAura() {
        return this.ability;
    }

    public void setAura(@Nullable String auraName) {
        if (auraName == null) {
            this.ability = null;
        } else {
            this.ability = new EternalAura(auraName);
        }
        this.dataDelegate.setDirty();
    }


    @Nullable
    public String getAbilityName() {
        return (this.ability == null) ? null : this.ability.getAuraName();
    }


    public Map<EquipmentSlotType, ItemStack> getEquipment() {
        return Collections.unmodifiableMap(this.equipment);
    }

    public ItemStack getStack(EquipmentSlotType slot) {
        return this.equipment.getOrDefault(slot, ItemStack.EMPTY);
    }

    public void setStack(EquipmentSlotType slot, ItemStack stack) {
        this.equipment.put(slot, stack);
        this.dataDelegate.setDirty();
    }


    public Map<Attribute, Float> getEntityAttributes() {
        return Collections.unmodifiableMap(this.attributes.getAttributes());
    }

    public EquipmentInventory getEquipmentInventory(Runnable onChange) {
        return new EquipmentInventory(this, onChange);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("Id", getId());
        nbt.putString("Name", getName());
        nbt.putString("originalName", getOriginalName());
        nbt.putLong("eternalSeed", getSeed());

        CompoundNBT tag = new CompoundNBT();
        this.equipment.forEach((slot, stack) -> tag.put(slot.getName(), (INBT) stack.serializeNBT()));
        nbt.put("equipment", (INBT) tag);

        if (getAura() != null) {
            nbt.put("ability", (INBT) getAura().serializeNBT());
        }

        nbt.putInt("level", this.level);
        nbt.putInt("usedLevels", this.usedLevels);
        nbt.putInt("levelExp", this.levelExp);
        nbt.putBoolean("alive", this.alive);
        nbt.putBoolean("ancient", this.ancient);
        nbt.put("attributes", (INBT) this.attributes.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.uuid = nbt.getUUID("Id");
        this.name = nbt.getString("Name");
        this
                .originalName = nbt.contains("originalName", 8) ? nbt.getString("originalName") : this.name;
        this
                .eternalSeed = nbt.contains("eternalSeed", 4) ? nbt.getLong("eternalSeed") : 3274487651937260739L;

        this.equipment.clear();
        CompoundNBT equipment = nbt.getCompound("equipment");
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (equipment.contains(slot.getName(), 10)) {
                ItemStack stack = ItemStack.of(equipment.getCompound(slot.getName()));
                this.equipment.put(slot, stack);
            }
        }

        if (nbt.contains("ability", 10)) {
            this.ability = new EternalAura(nbt.getCompound("ability"));
        } else {
            this.ability = null;
        }

        this.level = nbt.contains("level", 3) ? nbt.getInt("level") : 0;
        this.usedLevels = nbt.contains("usedLevels", 3) ? nbt.getInt("usedLevels") : 0;
        this.levelExp = nbt.getInt("levelExp");
        this.alive = (!nbt.contains("alive", 1) || nbt.getBoolean("alive"));
        this.ancient = (nbt.contains("ancient", 1) && nbt.getBoolean("ancient"));

        if (!nbt.contains("attributes", 10)) {
            this.attributes = new EternalAttributes();
            this.attributes.initializeAttributes();
        } else {
            this.attributes = EternalAttributes.fromNBT(nbt.getCompound("attributes"));
        }


        if (nbt.contains("MainSlots")) {
            ListNBT mainSlotsList = nbt.getList("MainSlots", 10);
            for (int i = 0; i < Math.min(mainSlotsList.size(), (EquipmentSlotType.values()).length); i++) {
                EquipmentSlotType slot = EquipmentSlotType.values()[i];
                if (slot != EquipmentSlotType.OFFHAND) {
                    this.equipment.put(slot, ItemStack.of(mainSlotsList.getCompound(i)));
                }
            }
        }
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EternalData)) return false;
        EternalData other = (EternalData) o;
        return this.uuid.equals(other.uuid);
    }


    public int hashCode() {
        return this.uuid.hashCode();
    }

    public static class EquipmentInventory
            implements IInventory {
        private final EternalData eternal;
        private final Runnable onChange;

        public EquipmentInventory(EternalData eternal, Runnable onChange) {
            this.eternal = eternal;
            this.onChange = onChange;
        }


        public int getContainerSize() {
            return 5;
        }


        public boolean isEmpty() {
            return this.eternal.getEquipment().entrySet().stream()
                    .anyMatch(entry -> !((ItemStack) entry.getValue()).isEmpty());
        }


        public ItemStack getItem(int index) {
            return this.eternal.getStack(getSlotFromIndex(index));
        }


        public ItemStack removeItem(int index, int count) {
            ItemStack stack = getItem(index);
            if (!stack.isEmpty() && count > 0) {
                ItemStack split = stack.split(count);
                setItem(index, stack);
                if (!split.isEmpty()) {
                    setChanged();
                }
                return split;
            }
            return ItemStack.EMPTY;
        }


        public ItemStack removeItemNoUpdate(int index) {
            EquipmentSlotType slotType = getSlotFromIndex(index);
            ItemStack equipment = this.eternal.getStack(slotType);
            this.eternal.setStack(slotType, ItemStack.EMPTY);
            setChanged();
            return equipment;
        }


        public void setItem(int index, ItemStack stack) {
            this.eternal.setStack(getSlotFromIndex(index), stack.copy());
            setChanged();
        }


        public void setChanged() {
            this.onChange.run();
            this.eternal.dataDelegate.setDirty();
        }


        public boolean stillValid(PlayerEntity player) {
            return true;
        }


        public void clearContent() {
            for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                this.eternal.setStack(slotType, ItemStack.EMPTY);
            }
            setChanged();
        }

        private EquipmentSlotType getSlotFromIndex(int index) {
            if (index == 0) {
                return EquipmentSlotType.MAINHAND;
            }
            return EquipmentSlotType.byTypeAndIndex(EquipmentSlotType.Group.ARMOR, index - 1);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */