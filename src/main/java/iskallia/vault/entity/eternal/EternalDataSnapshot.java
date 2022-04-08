package iskallia.vault.entity.eternal;

import iskallia.vault.util.calc.ParryHelper;
import iskallia.vault.util.calc.ResistanceHelper;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class EternalDataSnapshot implements EternalDataAccess {
    public static final String ATTR_HEALTH = Attributes.MAX_HEALTH.getRegistryName().toString();
    public static final String ATTR_DAMAGE = Attributes.ATTACK_DAMAGE.getRegistryName().toString();
    public static final String ATTR_SPEED = Attributes.MOVEMENT_SPEED.getRegistryName().toString();

    private final UUID eternalUUID;
    private final long seed;
    private final String eternalName;
    private final Map<EquipmentSlotType, ItemStack> equipment;
    private final Map<String, Float> attributes;
    private final float parry;
    private final float resistance;
    private final int level;
    private final int usedLevels;
    private final int maxLevel;
    private final float levelPercent;
    private final boolean alive;
    private final boolean ancient;
    private final String abilityName;

    public EternalDataSnapshot(UUID eternalUUID, long seed, String eternalName, Map<EquipmentSlotType, ItemStack> equipment, Map<String, Float> attributes, float parry, float resistance, int level, int usedLevels, int maxLevel, float levelPercent, boolean alive, boolean ancient, String abilityName) {
        this.eternalUUID = eternalUUID;
        this.seed = seed;
        this.eternalName = eternalName;
        this.equipment = equipment;
        this.attributes = attributes;
        this.parry = parry;
        this.resistance = resistance;
        this.level = level;
        this.usedLevels = usedLevels;
        this.maxLevel = maxLevel;
        this.levelPercent = levelPercent;
        this.alive = alive;
        this.ancient = ancient;
        this.abilityName = abilityName;
    }

    public static EternalDataSnapshot getFromEternal(EternalsData.EternalGroup playerGroup, EternalData eternal) {
        UUID eternalUUID = eternal.getId();
        long seed = eternal.getSeed();
        String eternalName = eternal.getName();
        Map<EquipmentSlotType, ItemStack> equipment = new HashMap<>();
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            ItemStack stack = eternal.getStack(slotType);
            if (!stack.isEmpty()) {
                equipment.put(slotType, stack.copy());
            }
        }
        EternalAttributes eternalAttributes = eternal.getAttributes();
        Map<String, Float> attributes = new HashMap<>();
        float value = ((Float) eternalAttributes.getAttributeValue(Attributes.MAX_HEALTH).orElse(Float.valueOf(0.0F))).floatValue();
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.MAX_HEALTH, value);
        attributes.put(ATTR_HEALTH, Float.valueOf(value));

        value = ((Float) eternalAttributes.getAttributeValue(Attributes.ATTACK_DAMAGE).orElse(Float.valueOf(0.0F))).floatValue();
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.ATTACK_DAMAGE, value);
        attributes.put(ATTR_DAMAGE, Float.valueOf(value));

        value = ((Float) eternalAttributes.getAttributeValue(Attributes.MOVEMENT_SPEED).orElse(Float.valueOf(0.0F))).floatValue();
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.MOVEMENT_SPEED, value);
        attributes.put(ATTR_SPEED, Float.valueOf(value));

        float parry = ParryHelper.getGearParryChance(eternal::getStack);
        float resistance = ResistanceHelper.getGearResistanceChance(eternal::getStack);
        int level = eternal.getLevel();
        int usedLevels = eternal.getUsedLevels();
        int maxLevel = eternal.getMaxLevel();
        float levelPercent = eternal.getLevelPercent();
        boolean alive = eternal.isAlive();
        boolean ancient = eternal.isAncient();
        String abilityName = (eternal.getAura() != null) ? eternal.getAura().getAuraName() : null;

        return new EternalDataSnapshot(eternalUUID, seed, eternalName, equipment, attributes, parry, resistance, level, usedLevels, maxLevel, levelPercent, alive, ancient, abilityName);
    }


    public UUID getId() {
        return this.eternalUUID;
    }


    public long getSeed() {
        return this.seed;
    }


    public String getName() {
        return this.eternalName;
    }


    public Map<EquipmentSlotType, ItemStack> getEquipment() {
        return Collections.unmodifiableMap(this.equipment);
    }

    public ItemStack getEquipment(EquipmentSlotType slotType) {
        return ((ItemStack) this.equipment.getOrDefault(slotType, ItemStack.EMPTY)).copy();
    }

    public Map<String, Float> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }


    public Map<Attribute, Float> getEntityAttributes() {
        return (Map<Attribute, Float>) getAttributes().entrySet().stream()
                .map(e -> {
                    Attribute attr = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation((String) e.getKey()));


                    return (attr != null) ? new Tuple(attr, e.getValue()) : null;
                }).filter(Objects::nonNull)
                .collect(Collectors.<Tuple<Attribute,Float>,Attribute,Float>toMap(Tuple::getA, Tuple::getB));
    }

    public float getParry() {
        return this.parry;
    }

    public float getResistance() {
        return this.resistance;
    }


    public int getLevel() {
        return this.level;
    }

    public int getUsedLevels() {
        return this.usedLevels;
    }


    public int getMaxLevel() {
        return this.maxLevel;
    }

    public float getLevelPercent() {
        return this.levelPercent;
    }


    public boolean isAlive() {
        return this.alive;
    }


    public boolean isAncient() {
        return this.ancient;
    }


    @Nullable
    public String getAbilityName() {
        return this.abilityName;
    }

    public boolean areStatisticsEqual(EternalDataSnapshot other) {
        if (this.alive != other.alive || !Objects.equals(this.abilityName, other.abilityName)) {
            return false;
        }
        if (this.level != other.level || this.maxLevel != other.maxLevel || this.usedLevels != other.usedLevels) {
            return false;
        }
        if (this.parry != other.parry || this.resistance != other.resistance || this.levelPercent != other.levelPercent) {
            return false;
        }
        float thisVal = ((Float) this.attributes.get(ATTR_HEALTH)).floatValue();
        float thatVal = ((Float) other.attributes.get(ATTR_HEALTH)).floatValue();
        if (thisVal != thatVal) {
            return false;
        }
        thisVal = ((Float) this.attributes.get(ATTR_DAMAGE)).floatValue();
        thatVal = ((Float) other.attributes.get(ATTR_DAMAGE)).floatValue();
        if (thisVal != thatVal) {
            return false;
        }
        thisVal = ((Float) this.attributes.get(ATTR_SPEED)).floatValue();
        thatVal = ((Float) other.attributes.get(ATTR_SPEED)).floatValue();
        if (thisVal != thatVal) {
            return false;
        }
        return true;
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeUUID(this.eternalUUID);
        buffer.writeLong(this.seed);
        buffer.writeUtf(this.eternalName);

        buffer.writeInt(this.equipment.size());
        this.equipment.forEach((slot, stack) -> {
            buffer.writeEnum((Enum) slot);

            buffer.writeItem(stack);
        });
        buffer.writeInt(this.attributes.size());
        this.attributes.forEach((attr, value) -> {
            buffer.writeUtf(attr);

            buffer.writeFloat(value.floatValue());
        });
        buffer.writeFloat(this.parry);
        buffer.writeFloat(this.resistance);
        buffer.writeInt(this.level);
        buffer.writeInt(this.usedLevels);
        buffer.writeInt(this.maxLevel);
        buffer.writeFloat(this.levelPercent);
        buffer.writeBoolean(this.alive);
        buffer.writeBoolean(this.ancient);

        buffer.writeBoolean((this.abilityName != null));
        if (this.abilityName != null) {
            buffer.writeUtf(this.abilityName);
        }
    }

    public static EternalDataSnapshot deserialize(PacketBuffer buffer) {
        UUID eternalUUID = buffer.readUUID();
        long seed = buffer.readLong();
        String eternalName = buffer.readUtf(32767);
        Map<EquipmentSlotType, ItemStack> equipment = new HashMap<>();
        int equipmentSize = buffer.readInt();
        for (int i = 0; i < equipmentSize; i++) {
            EquipmentSlotType type = (EquipmentSlotType) buffer.readEnum(EquipmentSlotType.class);
            ItemStack stack = buffer.readItem();
            equipment.put(type, stack);
        }
        Map<String, Float> attributes = new HashMap<>();
        int attrSize = buffer.readInt();
        for (int j = 0; j < attrSize; j++) {
            String attribute = buffer.readUtf(32767);
            float val = buffer.readFloat();
            attributes.put(attribute, Float.valueOf(val));
        }
        float parry = buffer.readFloat();
        float resistance = buffer.readFloat();
        int level = buffer.readInt();
        int usedLevels = buffer.readInt();
        int maxLevel = buffer.readInt();
        float levelPercent = buffer.readFloat();
        boolean alive = buffer.readBoolean();
        boolean ancient = buffer.readBoolean();
        String abilityName = buffer.readBoolean() ? buffer.readUtf(32767) : null;

        return new EternalDataSnapshot(eternalUUID, seed, eternalName, equipment, attributes, parry, resistance, level, usedLevels, maxLevel, levelPercent, alive, ancient, abilityName);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalDataSnapshot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */