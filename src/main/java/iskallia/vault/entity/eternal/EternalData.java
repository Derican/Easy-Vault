// 
// Decompiled by Procyon v0.6.0
// 

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

public class EternalData implements INBTSerializable<CompoundNBT>, EternalDataAccess
{
    private UUID uuid;
    private String name;
    private String originalName;
    private long eternalSeed;
    private final Map<EquipmentSlotType, ItemStack> equipment;
    private EternalAttributes attributes;
    private EternalAura ability;
    private int level;
    private int usedLevels;
    private int levelExp;
    private boolean alive;
    private boolean ancient;
    private final EternalsData dataDelegate;
    
    private EternalData(final EternalsData dataDelegate, final String name, final boolean isAncient) {
        this.uuid = UUID.randomUUID();
        this.eternalSeed = 3274487651937260739L;
        this.equipment = new HashMap<EquipmentSlotType, ItemStack>();
        this.attributes = new EternalAttributes();
        this.ability = null;
        this.level = 0;
        this.usedLevels = 0;
        this.levelExp = 0;
        this.alive = true;
        this.ancient = false;
        this.dataDelegate = dataDelegate;
        this.name = name;
        this.originalName = name;
        this.ancient = isAncient;
        this.attributes.initializeAttributes();
    }
    
    private EternalData(final EternalsData dataDelegate, final CompoundNBT nbt) {
        this.uuid = UUID.randomUUID();
        this.eternalSeed = 3274487651937260739L;
        this.equipment = new HashMap<EquipmentSlotType, ItemStack>();
        this.attributes = new EternalAttributes();
        this.ability = null;
        this.level = 0;
        this.usedLevels = 0;
        this.levelExp = 0;
        this.alive = true;
        this.ancient = false;
        this.dataDelegate = dataDelegate;
        this.deserializeNBT(nbt);
    }
    
    public static EternalData createEternal(final EternalsData data, final String name, final boolean isAncient) {
        return new EternalData(data, name, isAncient);
    }
    
    public static EternalData fromNBT(final EternalsData data, final CompoundNBT nbt) {
        return new EternalData(data, nbt);
    }
    
    public UUID getId() {
        return this.uuid;
    }
    
    public long getSeed() {
        return this.eternalSeed;
    }
    
    public void shuffleSeed() {
        this.eternalSeed = new Random().nextLong();
        this.dataDelegate.setDirty();
    }
    
    public void setName(final String name) {
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
    
    public void addAttributeValue(final Attribute attribute, final float value) {
        if (this.usedLevels >= this.level) {
            return;
        }
        ++this.usedLevels;
        this.attributes.addAttributeValue(attribute, value);
        this.dataDelegate.setDirty();
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public int getUsedLevels() {
        return this.usedLevels;
    }
    
    public int getMaxLevel() {
        final UUID playerId = this.dataDelegate.getOwnerOf(this.getId());
        if (playerId == null) {
            return 0;
        }
        return this.dataDelegate.getEternals(playerId).getNonAncientEternalCount() + (this.isAncient() ? 5 : 0);
    }
    
    public float getLevelPercent() {
        final int expNeeded = ModConfigs.ETERNAL.getExpForLevel(this.getLevel() + 1);
        return this.levelExp / (float)expNeeded;
    }
    
    public boolean isAlive() {
        return this.alive;
    }
    
    public void setAlive(final boolean alive) {
        this.alive = alive;
        this.dataDelegate.setDirty();
    }
    
    public boolean isAncient() {
        return this.ancient;
    }
    
    public void setAncient(final boolean ancient) {
        this.ancient = ancient;
        this.dataDelegate.setDirty();
    }
    
    public boolean addExp(final int xp) {
        if (this.level >= this.getMaxLevel()) {
            return false;
        }
        this.levelExp += xp;
        final int expNeeded = ModConfigs.ETERNAL.getExpForLevel(this.getLevel() + 1);
        if (this.levelExp >= expNeeded) {
            ++this.level;
            this.levelExp -= expNeeded;
        }
        this.dataDelegate.setDirty();
        return true;
    }
    
    @Nullable
    public EternalAura getAura() {
        return this.ability;
    }
    
    public void setAura(@Nullable final String auraName) {
        if (auraName == null) {
            this.ability = null;
        }
        else {
            this.ability = new EternalAura(auraName);
        }
        this.dataDelegate.setDirty();
    }
    
    @Nullable
    public String getAbilityName() {
        return (this.ability == null) ? null : this.ability.getAuraName();
    }
    
    public Map<EquipmentSlotType, ItemStack> getEquipment() {
        return Collections.unmodifiableMap((Map<? extends EquipmentSlotType, ? extends ItemStack>)this.equipment);
    }
    
    public ItemStack getStack(final EquipmentSlotType slot) {
        return this.equipment.getOrDefault(slot, ItemStack.EMPTY);
    }
    
    public void setStack(final EquipmentSlotType slot, final ItemStack stack) {
        this.equipment.put(slot, stack);
        this.dataDelegate.setDirty();
    }
    
    public Map<Attribute, Float> getEntityAttributes() {
        return Collections.unmodifiableMap((Map<? extends Attribute, ? extends Float>)this.attributes.getAttributes());
    }
    
    public EquipmentInventory getEquipmentInventory(final Runnable onChange) {
        return new EquipmentInventory(this, onChange);
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("Id", this.getId());
        nbt.putString("Name", this.getName());
        nbt.putString("originalName", this.getOriginalName());
        nbt.putLong("eternalSeed", this.getSeed());
        final CompoundNBT tag = new CompoundNBT();
        this.equipment.forEach((slot, stack) -> tag.put(slot.getName(), (INBT)stack.serializeNBT()));
        nbt.put("equipment", (INBT)tag);
        if (this.getAura() != null) {
            nbt.put("ability", (INBT)this.getAura().serializeNBT());
        }
        nbt.putInt("level", this.level);
        nbt.putInt("usedLevels", this.usedLevels);
        nbt.putInt("levelExp", this.levelExp);
        nbt.putBoolean("alive", this.alive);
        nbt.putBoolean("ancient", this.ancient);
        nbt.put("attributes", (INBT)this.attributes.serializeNBT());
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.uuid = nbt.getUUID("Id");
        this.name = nbt.getString("Name");
        this.originalName = (nbt.contains("originalName", 8) ? nbt.getString("originalName") : this.name);
        this.eternalSeed = (nbt.contains("eternalSeed", 4) ? nbt.getLong("eternalSeed") : 3274487651937260739L);
        this.equipment.clear();
        final CompoundNBT equipment = nbt.getCompound("equipment");
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (equipment.contains(slot.getName(), 10)) {
                final ItemStack stack = ItemStack.of(equipment.getCompound(slot.getName()));
                this.equipment.put(slot, stack);
            }
        }
        if (nbt.contains("ability", 10)) {
            this.ability = new EternalAura(nbt.getCompound("ability"));
        }
        else {
            this.ability = null;
        }
        this.level = (nbt.contains("level", 3) ? nbt.getInt("level") : 0);
        this.usedLevels = (nbt.contains("usedLevels", 3) ? nbt.getInt("usedLevels") : 0);
        this.levelExp = nbt.getInt("levelExp");
        this.alive = (!nbt.contains("alive", 1) || nbt.getBoolean("alive"));
        this.ancient = (nbt.contains("ancient", 1) && nbt.getBoolean("ancient"));
        if (!nbt.contains("attributes", 10)) {
            (this.attributes = new EternalAttributes()).initializeAttributes();
        }
        else {
            this.attributes = EternalAttributes.fromNBT(nbt.getCompound("attributes"));
        }
        if (nbt.contains("MainSlots")) {
            final ListNBT mainSlotsList = nbt.getList("MainSlots", 10);
            for (int i = 0; i < Math.min(mainSlotsList.size(), EquipmentSlotType.values().length); ++i) {
                final EquipmentSlotType slot2 = EquipmentSlotType.values()[i];
                if (slot2 != EquipmentSlotType.OFFHAND) {
                    this.equipment.put(slot2, ItemStack.of(mainSlotsList.getCompound(i)));
                }
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EternalData)) {
            return false;
        }
        final EternalData other = (EternalData)o;
        return this.uuid.equals(other.uuid);
    }
    
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
    
    public static class EquipmentInventory implements IInventory
    {
        private final EternalData eternal;
        private final Runnable onChange;
        
        public EquipmentInventory(final EternalData eternal, final Runnable onChange) {
            this.eternal = eternal;
            this.onChange = onChange;
        }
        
        public int getContainerSize() {
            return 5;
        }
        
        public boolean isEmpty() {
            return this.eternal.getEquipment().entrySet().stream().anyMatch(entry -> !entry.getValue().isEmpty());
        }
        
        public ItemStack getItem(final int index) {
            return this.eternal.getStack(this.getSlotFromIndex(index));
        }
        
        public ItemStack removeItem(final int index, final int count) {
            final ItemStack stack = this.getItem(index);
            if (!stack.isEmpty() && count > 0) {
                final ItemStack split = stack.split(count);
                this.setItem(index, stack);
                if (!split.isEmpty()) {
                    this.setChanged();
                }
                return split;
            }
            return ItemStack.EMPTY;
        }
        
        public ItemStack removeItemNoUpdate(final int index) {
            final EquipmentSlotType slotType = this.getSlotFromIndex(index);
            final ItemStack equipment = this.eternal.getStack(slotType);
            this.eternal.setStack(slotType, ItemStack.EMPTY);
            this.setChanged();
            return equipment;
        }
        
        public void setItem(final int index, final ItemStack stack) {
            this.eternal.setStack(this.getSlotFromIndex(index), stack.copy());
            this.setChanged();
        }
        
        public void setChanged() {
            this.onChange.run();
            this.eternal.dataDelegate.setDirty();
        }
        
        public boolean stillValid(final PlayerEntity player) {
            return true;
        }
        
        public void clearContent() {
            for (final EquipmentSlotType slotType : EquipmentSlotType.values()) {
                this.eternal.setStack(slotType, ItemStack.EMPTY);
            }
            this.setChanged();
        }
        
        private EquipmentSlotType getSlotFromIndex(final int index) {
            if (index == 0) {
                return EquipmentSlotType.MAINHAND;
            }
            return EquipmentSlotType.byTypeAndIndex(EquipmentSlotType.Group.ARMOR, index - 1);
        }
    }
}
