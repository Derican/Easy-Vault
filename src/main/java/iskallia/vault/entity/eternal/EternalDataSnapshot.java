package iskallia.vault.entity.eternal;

import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;

import net.minecraft.util.Tuple;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.entity.ai.attributes.Attribute;

import java.util.Collections;

import iskallia.vault.util.calc.ResistanceHelper;
import iskallia.vault.util.calc.ParryHelper;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.HashMap;

import iskallia.vault.world.data.EternalsData;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.Map;
import java.util.UUID;

public class EternalDataSnapshot implements EternalDataAccess {
    public static final String ATTR_HEALTH;
    public static final String ATTR_DAMAGE;
    public static final String ATTR_SPEED;
    private final UUID eternalUUID;
    private final long seed;
    private final String eternalName;
    private final Map<EquipmentSlotType, ItemStack> equipment;
    private final Map<String, Float> attributes;
    private final float parry;
    private final float resistance;
    private final float armor;
    private final int level;
    private final int usedLevels;
    private final int maxLevel;
    private final float levelPercent;
    private final boolean alive;
    private final boolean ancient;
    private final String abilityName;

    public EternalDataSnapshot(final UUID eternalUUID, final long seed, final String eternalName, final Map<EquipmentSlotType, ItemStack> equipment, final Map<String, Float> attributes, final float parry, final float resistance, final float armor, final int level, final int usedLevels, final int maxLevel, final float levelPercent, final boolean alive, final boolean ancient, final String abilityName) {
        this.eternalUUID = eternalUUID;
        this.seed = seed;
        this.eternalName = eternalName;
        this.equipment = equipment;
        this.attributes = attributes;
        this.parry = parry;
        this.resistance = resistance;
        this.armor = armor;
        this.level = level;
        this.usedLevels = usedLevels;
        this.maxLevel = maxLevel;
        this.levelPercent = levelPercent;
        this.alive = alive;
        this.ancient = ancient;
        this.abilityName = abilityName;
    }

    public static EternalDataSnapshot getFromEternal(final EternalsData.EternalGroup playerGroup, final EternalData eternal) {
        final UUID eternalUUID = eternal.getId();
        final long seed = eternal.getSeed();
        final String eternalName = eternal.getName();
        final Map<EquipmentSlotType, ItemStack> equipment = new HashMap<EquipmentSlotType, ItemStack>();
        for (final EquipmentSlotType slotType : EquipmentSlotType.values()) {
            final ItemStack stack = eternal.getStack(slotType);
            if (!stack.isEmpty()) {
                equipment.put(slotType, stack.copy());
            }
        }
        final EternalAttributes eternalAttributes = eternal.getAttributes();
        final Map<String, Float> attributes = new HashMap<String, Float>();
        float value = eternalAttributes.getAttributeValue(Attributes.MAX_HEALTH).orElse(0.0f);
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.MAX_HEALTH, value);
        attributes.put(EternalDataSnapshot.ATTR_HEALTH, value);
        value = eternalAttributes.getAttributeValue(Attributes.ATTACK_DAMAGE).orElse(0.0f);
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.ATTACK_DAMAGE, value);
        attributes.put(EternalDataSnapshot.ATTR_DAMAGE, value);
        value = eternalAttributes.getAttributeValue(Attributes.MOVEMENT_SPEED).orElse(0.0f);
        value = EternalHelper.getEternalGearModifierAdjustments(eternal, Attributes.MOVEMENT_SPEED, value);
        attributes.put(EternalDataSnapshot.ATTR_SPEED, value);
        final float parry = ParryHelper.getGearParryChance(eternal::getStack);
        final float resistance = ResistanceHelper.getGearResistanceChance(eternal::getStack);
        final float armor = EternalHelper.getEternalGearModifierAdjustments(eternal.getEquipment(), Attributes.ARMOR, 0.0f);
        final int level = eternal.getLevel();
        final int usedLevels = eternal.getUsedLevels();
        final int maxLevel = eternal.getMaxLevel();
        final float levelPercent = eternal.getLevelPercent();
        final boolean alive = eternal.isAlive();
        final boolean ancient = eternal.isAncient();
        final String abilityName = (eternal.getAura() != null) ? eternal.getAura().getAuraName() : null;
        return new EternalDataSnapshot(eternalUUID, seed, eternalName, equipment, attributes, parry, resistance, armor, level, usedLevels, maxLevel, levelPercent, alive, ancient, abilityName);
    }

    @Override
    public UUID getId() {
        return this.eternalUUID;
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public String getName() {
        return this.eternalName;
    }

    @Override
    public Map<EquipmentSlotType, ItemStack> getEquipment() {
        return Collections.unmodifiableMap(this.equipment);
    }

    public ItemStack getEquipment(final EquipmentSlotType slotType) {
        return this.equipment.getOrDefault(slotType, ItemStack.EMPTY).copy();
    }

    public Map<String, Float> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    @Override
    public Map<Attribute, Float> getEntityAttributes() {
        return this.getAttributes().entrySet().stream().map(e -> {
            final Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(e.getKey()));
            if (attr != null) {
                return new Tuple<>(attr, e.getValue());
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toMap(Tuple::getA, Tuple::getB));
    }

    public float getParry() {
        return this.parry;
    }

    public float getResistance() {
        return this.resistance;
    }

    public float getArmor() {
        return this.armor;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    public int getUsedLevels() {
        return this.usedLevels;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    public float getLevelPercent() {
        return this.levelPercent;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public boolean isAncient() {
        return this.ancient;
    }

    @Nullable
    @Override
    public String getAbilityName() {
        return this.abilityName;
    }

    public boolean areStatisticsEqual(final EternalDataSnapshot other) {
        if (this.alive != other.alive || !Objects.equals(this.abilityName, other.abilityName)) {
            return false;
        }
        if (this.level != other.level || this.maxLevel != other.maxLevel || this.usedLevels != other.usedLevels) {
            return false;
        }
        if (this.parry != other.parry || this.resistance != other.resistance || this.levelPercent != other.levelPercent) {
            return false;
        }
        float thisVal = this.attributes.get(EternalDataSnapshot.ATTR_HEALTH);
        float thatVal = other.attributes.get(EternalDataSnapshot.ATTR_HEALTH);
        if (thisVal != thatVal) {
            return false;
        }
        thisVal = this.attributes.get(EternalDataSnapshot.ATTR_DAMAGE);
        thatVal = other.attributes.get(EternalDataSnapshot.ATTR_DAMAGE);
        if (thisVal != thatVal) {
            return false;
        }
        thisVal = this.attributes.get(EternalDataSnapshot.ATTR_SPEED);
        thatVal = other.attributes.get(EternalDataSnapshot.ATTR_SPEED);
        return thisVal == thatVal;
    }

    public void serialize(final PacketBuffer buffer, final boolean useEquipment) {
        buffer.writeUUID(this.eternalUUID);
        buffer.writeLong(this.seed);
        buffer.writeUtf(this.eternalName);
        buffer.writeInt(this.equipment.size());
        this.equipment.forEach((slot, stack) -> {
            buffer.writeEnum(slot);
            buffer.writeItem(useEquipment ? stack : ItemStack.EMPTY);
            return;
        });
        buffer.writeInt(this.attributes.size());
        this.attributes.forEach((attr, value) -> {
            buffer.writeUtf(attr);
            buffer.writeFloat(value);
            return;
        });
        buffer.writeFloat(this.parry);
        buffer.writeFloat(this.resistance);
        buffer.writeFloat(this.armor);
        buffer.writeInt(this.level);
        buffer.writeInt(this.usedLevels);
        buffer.writeInt(this.maxLevel);
        buffer.writeFloat(this.levelPercent);
        buffer.writeBoolean(this.alive);
        buffer.writeBoolean(this.ancient);
        buffer.writeBoolean(this.abilityName != null);
        if (this.abilityName != null) {
            buffer.writeUtf(this.abilityName);
        }
    }

    public static EternalDataSnapshot deserialize(final PacketBuffer buffer) {
        final UUID eternalUUID = buffer.readUUID();
        final long seed = buffer.readLong();
        final String eternalName = buffer.readUtf(32767);
        final Map<EquipmentSlotType, ItemStack> equipment = new HashMap<EquipmentSlotType, ItemStack>();
        for (int equipmentSize = buffer.readInt(), i = 0; i < equipmentSize; ++i) {
            final EquipmentSlotType type = (EquipmentSlotType) buffer.readEnum((Class) EquipmentSlotType.class);
            final ItemStack stack = buffer.readItem();
            equipment.put(type, stack);
        }
        final Map<String, Float> attributes = new HashMap<String, Float>();
        for (int attrSize = buffer.readInt(), j = 0; j < attrSize; ++j) {
            final String attribute = buffer.readUtf(32767);
            final float val = buffer.readFloat();
            attributes.put(attribute, val);
        }
        final float parry = buffer.readFloat();
        final float resistance = buffer.readFloat();
        final float armor = buffer.readFloat();
        final int level = buffer.readInt();
        final int usedLevels = buffer.readInt();
        final int maxLevel = buffer.readInt();
        final float levelPercent = buffer.readFloat();
        final boolean alive = buffer.readBoolean();
        final boolean ancient = buffer.readBoolean();
        final String abilityName = buffer.readBoolean() ? buffer.readUtf(32767) : null;
        return new EternalDataSnapshot(eternalUUID, seed, eternalName, equipment, attributes, parry, resistance, armor, level, usedLevels, maxLevel, levelPercent, alive, ancient, abilityName);
    }

    static {
        ATTR_HEALTH = Attributes.MAX_HEALTH.getRegistryName().toString();
        ATTR_DAMAGE = Attributes.ATTACK_DAMAGE.getRegistryName().toString();
        ATTR_SPEED = Attributes.MOVEMENT_SPEED.getRegistryName().toString();
    }
}
