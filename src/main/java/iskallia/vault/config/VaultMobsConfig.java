// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.util.NetcodeUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.modifier.LevelModifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.*;
import java.util.function.Consumer;

public class VaultMobsConfig extends Config
{
    public static final Item[] LEATHER_ARMOR;
    public static final Item[] GOLDEN_ARMOR;
    public static final Item[] CHAINMAIL_ARMOR;
    public static final Item[] IRON_ARMOR;
    public static final Item[] DIAMOND_ARMOR;
    public static final Item[] NETHERITE_ARMOR;
    public static final Item[] WOODEN_WEAPONS;
    public static final Item[] STONE_WEAPONS;
    public static final Item[] GOLDEN_WEAPONS;
    public static final Item[] IRON_WEAPONS;
    public static final Item[] DIAMOND_WEAPONS;
    public static final Item[] NETHERITE_WEAPONS;
    @Expose
    private Map<String, List<Mob.AttributeOverride>> ATTRIBUTE_OVERRIDES;
    @Expose
    private List<Level> LEVEL_OVERRIDES;
    
    public VaultMobsConfig() {
        this.ATTRIBUTE_OVERRIDES = new LinkedHashMap<String, List<Mob.AttributeOverride>>();
        this.LEVEL_OVERRIDES = new ArrayList<Level>();
    }
    
    public Level getForLevel(final int level) {
        int i = 0;
        while (i < this.LEVEL_OVERRIDES.size()) {
            if (level < this.LEVEL_OVERRIDES.get(i).MIN_LEVEL) {
                if (i == 0) {
                    break;
                }
                return this.LEVEL_OVERRIDES.get(i - 1);
            }
            else {
                if (i == this.LEVEL_OVERRIDES.size() - 1) {
                    return this.LEVEL_OVERRIDES.get(i);
                }
                ++i;
            }
        }
        return Level.EMPTY;
    }
    
    @Override
    public String getName() {
        return "vault_mobs";
    }
    
    @Override
    protected void reset() {
        final List<Mob.AttributeOverride> attributes = new ArrayList<Mob.AttributeOverride>();
        attributes.add(new Mob.AttributeOverride(ModAttributes.CRIT_CHANCE, 0.0, 0.5, "set", 0.8, 0.05));
        attributes.add(new Mob.AttributeOverride(ModAttributes.CRIT_MULTIPLIER, 0.0, 0.1, "set", 0.8, 0.1));
        this.ATTRIBUTE_OVERRIDES.put(EntityType.ZOMBIE.getRegistryName().toString(), attributes);
        this.LEVEL_OVERRIDES.add(new Level(0).mobAdd(Items.WOODEN_SWORD, 1).mobAdd(Items.STONE_SWORD, 2).bossAdd(Items.STONE_SWORD, 1).bossAdd(Items.GOLDEN_SWORD, 2).raffleAdd(Items.DIAMOND_SWORD, 1).mob((EntityType<? extends LivingEntity>)EntityType.ZOMBIE, 1).boss((EntityType<? extends LivingEntity>)ModEntities.ROBOT, 1).raffle((EntityType<? extends LivingEntity>)ModEntities.ARENA_BOSS, 1).mobMisc(3, 1, new VaultSpawner.Config().withStartMaxMobs(5).withMinDistance(10.0).withMaxDistance(24.0).withDespawnDistance(26.0)).bossMisc(3, 1).raffleMisc(3, 1));
    }
    
    static {
        LEATHER_ARMOR = new Item[] { Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS };
        GOLDEN_ARMOR = new Item[] { Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS };
        CHAINMAIL_ARMOR = new Item[] { Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS };
        IRON_ARMOR = new Item[] { Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS };
        DIAMOND_ARMOR = new Item[] { Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS };
        NETHERITE_ARMOR = new Item[] { Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS };
        WOODEN_WEAPONS = new Item[] { Items.WOODEN_SWORD, Items.WOODEN_AXE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE };
        STONE_WEAPONS = new Item[] { Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_HOE };
        GOLDEN_WEAPONS = new Item[] { Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE };
        IRON_WEAPONS = new Item[] { Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE };
        DIAMOND_WEAPONS = new Item[] { Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE };
        NETHERITE_WEAPONS = new Item[] { Items.NETHERITE_SWORD, Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE };
    }
    
    public static class Level
    {
        public static final Level EMPTY;
        @Expose
        public int MIN_LEVEL;
        @Expose
        public Map<String, WeightedList<String>> MOB_LOOT;
        @Expose
        public Map<String, WeightedList<String>> BOSS_LOOT;
        @Expose
        public Map<String, WeightedList<String>> RAFFLE_BOSS_LOOT;
        @Expose
        public WeightedList<Mob> MOB_POOL;
        @Expose
        public WeightedList<Mob> BOSS_POOL;
        @Expose
        public WeightedList<Mob> RAFFLE_BOSS_POOL;
        @Expose
        public MobMisc MOB_MISC;
        @Expose
        public BossMisc BOSS_MISC;
        @Expose
        public BossMisc RAFFLE_BOSS_MISC;
        
        public Level(final int minLevel) {
            this.MIN_LEVEL = minLevel;
            this.MOB_LOOT = new LinkedHashMap<String, WeightedList<String>>();
            this.BOSS_LOOT = new LinkedHashMap<String, WeightedList<String>>();
            this.RAFFLE_BOSS_LOOT = new LinkedHashMap<String, WeightedList<String>>();
            this.MOB_POOL = new WeightedList<Mob>();
            this.BOSS_POOL = new WeightedList<Mob>();
            this.RAFFLE_BOSS_POOL = new WeightedList<Mob>();
            this.MOB_MISC = new MobMisc(0, 0, new VaultSpawner.Config());
            this.BOSS_MISC = new BossMisc(0, 0);
            this.RAFFLE_BOSS_MISC = new BossMisc(0, 0);
        }
        
        public Level mobAdd(final Item item, final int weight) {
            if (item instanceof ArmorItem) {
                this.MOB_LOOT.computeIfAbsent(((ArmorItem)item).getSlot().getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            else {
                this.MOB_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
                this.MOB_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            return this;
        }
        
        public Level bossAdd(final Item item, final int weight) {
            if (item instanceof ArmorItem) {
                this.BOSS_LOOT.computeIfAbsent(((ArmorItem)item).getSlot().getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            else {
                this.BOSS_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
                this.BOSS_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            return this;
        }
        
        public Level raffleAdd(final Item item, final int weight) {
            if (item instanceof ArmorItem) {
                this.RAFFLE_BOSS_LOOT.computeIfAbsent(((ArmorItem)item).getSlot().getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            else {
                this.RAFFLE_BOSS_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
                this.RAFFLE_BOSS_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList()).add(item.getRegistryName().toString(), weight);
            }
            return this;
        }
        
        public Level mobMisc(final int level, final int trials, final VaultSpawner.Config spawner) {
            this.MOB_MISC = new MobMisc(level, trials, spawner);
            return this;
        }
        
        public Level bossMisc(final int level, final int trials) {
            this.BOSS_MISC = new BossMisc(level, trials);
            return this;
        }
        
        public Level raffleMisc(final int level, final int trials) {
            this.RAFFLE_BOSS_MISC = new BossMisc(level, trials);
            return this;
        }
        
        public Level mob(final EntityType<? extends LivingEntity> type, final int weight) {
            this.MOB_POOL.add(new Mob(type), weight);
            return this;
        }
        
        public Level mob(final EntityType<? extends LivingEntity> type, final int weight, final Consumer<Mob> action) {
            final Mob mob = new Mob(type);
            action.accept(mob);
            this.MOB_POOL.add(mob, weight);
            return this;
        }
        
        public Level boss(final EntityType<? extends LivingEntity> type, final int weight) {
            this.BOSS_POOL.add(new Mob(type), weight);
            return this;
        }
        
        public Level boss(final EntityType<? extends LivingEntity> type, final int weight, final Consumer<Mob> action) {
            final Mob mob = new Mob(type);
            action.accept(mob);
            this.BOSS_POOL.add(mob, weight);
            return this;
        }
        
        public Level raffle(final EntityType<? extends LivingEntity> type, final int weight) {
            this.RAFFLE_BOSS_POOL.add(new Mob(type), weight);
            return this;
        }
        
        public Level raffle(final EntityType<? extends LivingEntity> type, final int weight, final Consumer<Mob> action) {
            final Mob mob = new Mob(type);
            action.accept(mob);
            this.RAFFLE_BOSS_POOL.add(mob, weight);
            return this;
        }
        
        public ItemStack getForMob(final EquipmentSlotType slot) {
            if (this.MOB_LOOT.isEmpty() || !this.MOB_LOOT.containsKey(slot.getName())) {
                return ItemStack.EMPTY;
            }
            final String itemStr = this.MOB_LOOT.get(slot.getName()).getRandom(new Random());
            if (itemStr.contains("{")) {
                final int part = itemStr.indexOf(123);
                final String itemName = itemStr.substring(0, part);
                final String nbt = itemStr.substring(part);
                final Item item = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                final ItemStack itemStack = new ItemStack((IItemProvider)item);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                }
                catch (final CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            final Item item2 = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider)item2);
        }
        
        public ItemStack getForBoss(final EquipmentSlotType slot) {
            if (this.BOSS_LOOT.isEmpty() || !this.BOSS_LOOT.containsKey(slot.getName())) {
                return ItemStack.EMPTY;
            }
            final String itemStr = this.BOSS_LOOT.get(slot.getName()).getRandom(new Random());
            if (itemStr.contains("{")) {
                final int part = itemStr.indexOf(123);
                final String itemName = itemStr.substring(0, part);
                final String nbt = itemStr.substring(part);
                final Item item = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                final ItemStack itemStack = new ItemStack((IItemProvider)item);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                }
                catch (final CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            final Item item2 = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider)item2);
        }
        
        public ItemStack getForRaffle(final EquipmentSlotType slot) {
            if (this.RAFFLE_BOSS_LOOT.isEmpty() || !this.RAFFLE_BOSS_LOOT.containsKey(slot.getName())) {
                return ItemStack.EMPTY;
            }
            final String itemStr = this.RAFFLE_BOSS_LOOT.get(slot.getName()).getRandom(new Random());
            if (itemStr.contains("{")) {
                final int part = itemStr.indexOf(123);
                final String itemName = itemStr.substring(0, part);
                final String nbt = itemStr.substring(part);
                final Item item = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                final ItemStack itemStack = new ItemStack((IItemProvider)item);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                }
                catch (final CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            final Item item2 = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider)item2);
        }
        
        public Optional<Mob> getMob(final LivingEntity entity) {
            return this.MOB_POOL.stream().map(entry -> entry.value).filter(mob -> mob.NAME.equals(entity.getType().getRegistryName().toString())).findFirst();
        }
        
        static {
            EMPTY = new Level(0);
        }
    }
    
    public static class Mob
    {
        @Expose
        private String NAME;
        
        public Mob(final EntityType<?> type) {
            this.NAME = type.getRegistryName().toString();
        }
        
        public EntityType<?> getType() {
            return (EntityType<?>)Registry.ENTITY_TYPE.getOptional(new ResourceLocation(this.NAME)).orElse(EntityType.BAT);
        }
        
        public static LivingEntity scale(final LivingEntity entity, final VaultRaid vault, final GlobalDifficultyData.Difficulty vaultDifficulty) {
            int level = vault.getProperties().getValue(VaultRaid.LEVEL);
            final UUID host = vault.getProperties().getValue(VaultRaid.HOST);
            final MinecraftServer srv = (MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            if (srv != null) {
                level += NetcodeUtils.runIfPresent(srv, host, (ServerPlayerEntity sPlayer) -> ModConfigs.PLAYER_SCALING.getMobLevelAdjustment(sPlayer.getName().getString())).orElse(0);
            }
            for (final LevelModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), LevelModifier.class)) {
                level += modifier.getLevelAddend();
            }
            final int mobLevel = Math.max(level, 0);
            final List<AttributeOverride> attributes = ModConfigs.VAULT_MOBS.ATTRIBUTE_OVERRIDES.get(entity.getType().getRegistryName().toString());
            if (attributes != null) {
                for (final AttributeOverride override : attributes) {
                    if (entity.level.random.nextDouble() >= override.ROLL_CHANCE) {
                        continue;
                    }
                    Registry.ATTRIBUTE.getOptional(new ResourceLocation(override.NAME)).ifPresent(attribute -> {
                        final ModifiableAttributeInstance instance = entity.getAttribute(attribute);
                        if (instance != null) {
                            double multiplier = 1.0;
                            if (attribute == Attributes.MAX_HEALTH || attribute == Attributes.ATTACK_DAMAGE) {
                                multiplier = vaultDifficulty.getMultiplier();
                            }
                            instance.setBaseValue(override.getValue(instance.getBaseValue(), mobLevel, entity.level.getRandom()) * multiplier);
                        }
                        return;
                    });
                }
            }
            entity.setHealth(1.0f);
            entity.heal(1000000.0f);
            return entity;
        }
        
        public LivingEntity create(final World world) {
            return (LivingEntity)this.getType().create(world);
        }
        
        public static class AttributeOverride
        {
            @Expose
            public String NAME;
            @Expose
            public double MIN;
            @Expose
            public double MAX;
            @Expose
            public String OPERATOR;
            @Expose
            public double ROLL_CHANCE;
            @Expose
            public double SCALE_PER_LEVEL;
            
            public AttributeOverride(final Attribute attribute, final double min, final double max, final String operator, final double rollChance, final double scalePerLevel) {
                this.NAME = attribute.getRegistryName().toString();
                this.MIN = min;
                this.MAX = max;
                this.OPERATOR = operator;
                this.ROLL_CHANCE = rollChance;
                this.SCALE_PER_LEVEL = scalePerLevel;
            }
            
            public double getValue(final double baseValue, final int level, final Random random) {
                double value = this.getStartValue(baseValue, random);
                for (int i = 0; i < level; ++i) {
                    value += this.getStartValue(baseValue, random) * this.SCALE_PER_LEVEL;
                }
                return value;
            }
            
            public double getStartValue(final double baseValue, final Random random) {
                final double value = Math.min(this.MIN, this.MAX) + random.nextFloat() * Math.abs(this.MAX - this.MIN);
                if (this.OPERATOR.equalsIgnoreCase("multiply")) {
                    return baseValue * value;
                }
                if (this.OPERATOR.equalsIgnoreCase("add")) {
                    return baseValue + value;
                }
                if (this.OPERATOR.equalsIgnoreCase("set")) {
                    return value;
                }
                return baseValue;
            }
        }
    }
    
    public static class MobMisc
    {
        @Expose
        public int ENCH_LEVEL;
        @Expose
        public int ENCH_TRIALS;
        @Expose
        public VaultSpawner.Config SPAWNER;
        
        public MobMisc(final int level, final int trials, final VaultSpawner.Config spawner) {
            this.ENCH_LEVEL = level;
            this.ENCH_TRIALS = trials;
            this.SPAWNER = spawner;
        }
    }
    
    public static class BossMisc
    {
        @Expose
        public int ENCH_LEVEL;
        @Expose
        public int ENCH_TRIALS;
        
        public BossMisc(final int level, final int trials) {
            this.ENCH_LEVEL = level;
            this.ENCH_TRIALS = trials;
        }
    }
}
