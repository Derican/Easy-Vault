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

public class VaultMobsConfig extends Config {
    public static final Item[] LEATHER_ARMOR = new Item[]{Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS};

    public static final Item[] GOLDEN_ARMOR = new Item[]{Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS};

    public static final Item[] CHAINMAIL_ARMOR = new Item[]{Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS};

    public static final Item[] IRON_ARMOR = new Item[]{Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS};

    public static final Item[] DIAMOND_ARMOR = new Item[]{Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS};

    public static final Item[] NETHERITE_ARMOR = new Item[]{Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS};


    public static final Item[] WOODEN_WEAPONS = new Item[]{Items.WOODEN_SWORD, Items.WOODEN_AXE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE};

    public static final Item[] STONE_WEAPONS = new Item[]{Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_HOE};

    public static final Item[] GOLDEN_WEAPONS = new Item[]{Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE};

    public static final Item[] IRON_WEAPONS = new Item[]{Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE};

    public static final Item[] DIAMOND_WEAPONS = new Item[]{Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE};

    public static final Item[] NETHERITE_WEAPONS = new Item[]{Items.NETHERITE_SWORD, Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE};

    @Expose
    private Map<String, List<Mob.AttributeOverride>> ATTRIBUTE_OVERRIDES = new LinkedHashMap<>();
    @Expose
    private List<Level> LEVEL_OVERRIDES = new ArrayList<>();

    public Level getForLevel(int level) {
        for (int i = 0; i < this.LEVEL_OVERRIDES.size(); i++) {
            if (level < ((Level) this.LEVEL_OVERRIDES.get(i)).MIN_LEVEL) {
                if (i == 0)
                    break;
                return this.LEVEL_OVERRIDES.get(i - 1);
            }
            if (i == this.LEVEL_OVERRIDES.size() - 1) {
                return this.LEVEL_OVERRIDES.get(i);
            }
        }

        return Level.EMPTY;
    }


    public String getName() {
        return "vault_mobs";
    }


    protected void reset() {
        List<Mob.AttributeOverride> attributes = new ArrayList<>();
        attributes.add(new Mob.AttributeOverride(ModAttributes.CRIT_CHANCE, 0.0D, 0.5D, "set", 0.8D, 0.05D));
        attributes.add(new Mob.AttributeOverride(ModAttributes.CRIT_MULTIPLIER, 0.0D, 0.1D, "set", 0.8D, 0.1D));

        this.ATTRIBUTE_OVERRIDES.put(EntityType.ZOMBIE.getRegistryName().toString(), attributes);

        this.LEVEL_OVERRIDES.add((new Level(0))
                .mobAdd(Items.WOODEN_SWORD, 1)
                .mobAdd(Items.STONE_SWORD, 2)
                .bossAdd(Items.STONE_SWORD, 1)
                .bossAdd(Items.GOLDEN_SWORD, 2)
                .raffleAdd(Items.DIAMOND_SWORD, 1)
                .mob(EntityType.ZOMBIE, 1)
                .boss(ModEntities.ROBOT, 1)
                .raffle(ModEntities.ARENA_BOSS, 1)
                .mobMisc(3, 1, (new VaultSpawner.Config())

                        .withStartMaxMobs(5)
                        .withMinDistance(10.0D)
                        .withMaxDistance(24.0D)
                        .withDespawnDistance(26.0D))
                .bossMisc(3, 1)
                .raffleMisc(3, 1));
    }

    public static class Level {
        public static final Level EMPTY = new Level(0);
        @Expose
        public int MIN_LEVEL;
        @Expose
        public Map<String, WeightedList<String>> MOB_LOOT;
        @Expose
        public Map<String, WeightedList<String>> BOSS_LOOT;
        @Expose
        public Map<String, WeightedList<String>> RAFFLE_BOSS_LOOT;
        @Expose
        public WeightedList<VaultMobsConfig.Mob> MOB_POOL;
        @Expose
        public WeightedList<VaultMobsConfig.Mob> BOSS_POOL;
        @Expose
        public WeightedList<VaultMobsConfig.Mob> RAFFLE_BOSS_POOL;
        @Expose
        public VaultMobsConfig.MobMisc MOB_MISC;
        @Expose
        public VaultMobsConfig.BossMisc BOSS_MISC;
        @Expose
        public VaultMobsConfig.BossMisc RAFFLE_BOSS_MISC;

        public Level(int minLevel) {
            this.MIN_LEVEL = minLevel;
            this.MOB_LOOT = new LinkedHashMap<>();
            this.BOSS_LOOT = new LinkedHashMap<>();
            this.RAFFLE_BOSS_LOOT = new LinkedHashMap<>();

            this.MOB_POOL = new WeightedList();
            this.BOSS_POOL = new WeightedList();
            this.RAFFLE_BOSS_POOL = new WeightedList();

            this.MOB_MISC = new VaultMobsConfig.MobMisc(0, 0, new VaultSpawner.Config());
            this.BOSS_MISC = new VaultMobsConfig.BossMisc(0, 0);
            this.RAFFLE_BOSS_MISC = new VaultMobsConfig.BossMisc(0, 0);
        }

        public Level mobAdd(Item item, int weight) {
            if (item instanceof ArmorItem) {
                ((WeightedList) this.MOB_LOOT.computeIfAbsent(((ArmorItem) item).getSlot().getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            } else {
                ((WeightedList) this.MOB_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
                ((WeightedList) this.MOB_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            }

            return this;
        }

        public Level bossAdd(Item item, int weight) {
            if (item instanceof ArmorItem) {
                ((WeightedList) this.BOSS_LOOT.computeIfAbsent(((ArmorItem) item).getSlot().getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            } else {
                ((WeightedList) this.BOSS_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
                ((WeightedList) this.BOSS_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            }

            return this;
        }

        public Level raffleAdd(Item item, int weight) {
            if (item instanceof ArmorItem) {
                ((WeightedList) this.RAFFLE_BOSS_LOOT.computeIfAbsent(((ArmorItem) item).getSlot().getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            } else {
                ((WeightedList) this.RAFFLE_BOSS_LOOT.computeIfAbsent(EquipmentSlotType.MAINHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
                ((WeightedList) this.RAFFLE_BOSS_LOOT.computeIfAbsent(EquipmentSlotType.OFFHAND.getName(), s -> new WeightedList())).add(item.getRegistryName().toString(), weight);
            }

            return this;
        }

        public Level mobMisc(int level, int trials, VaultSpawner.Config spawner) {
            this.MOB_MISC = new VaultMobsConfig.MobMisc(level, trials, spawner);
            return this;
        }

        public Level bossMisc(int level, int trials) {
            this.BOSS_MISC = new VaultMobsConfig.BossMisc(level, trials);
            return this;
        }

        public Level raffleMisc(int level, int trials) {
            this.RAFFLE_BOSS_MISC = new VaultMobsConfig.BossMisc(level, trials);
            return this;
        }

        public Level mob(EntityType<? extends LivingEntity> type, int weight) {
            this.MOB_POOL.add(new VaultMobsConfig.Mob(type), weight);
            return this;
        }

        public Level mob(EntityType<? extends LivingEntity> type, int weight, Consumer<VaultMobsConfig.Mob> action) {
            VaultMobsConfig.Mob mob = new VaultMobsConfig.Mob(type);
            action.accept(mob);
            this.MOB_POOL.add(mob, weight);
            return this;
        }

        public Level boss(EntityType<? extends LivingEntity> type, int weight) {
            this.BOSS_POOL.add(new VaultMobsConfig.Mob(type), weight);
            return this;
        }

        public Level boss(EntityType<? extends LivingEntity> type, int weight, Consumer<VaultMobsConfig.Mob> action) {
            VaultMobsConfig.Mob mob = new VaultMobsConfig.Mob(type);
            action.accept(mob);
            this.BOSS_POOL.add(mob, weight);
            return this;
        }

        public Level raffle(EntityType<? extends LivingEntity> type, int weight) {
            this.RAFFLE_BOSS_POOL.add(new VaultMobsConfig.Mob(type), weight);
            return this;
        }

        public Level raffle(EntityType<? extends LivingEntity> type, int weight, Consumer<VaultMobsConfig.Mob> action) {
            VaultMobsConfig.Mob mob = new VaultMobsConfig.Mob(type);
            action.accept(mob);
            this.RAFFLE_BOSS_POOL.add(mob, weight);
            return this;
        }

        public ItemStack getForMob(EquipmentSlotType slot) {
            if (this.MOB_LOOT.isEmpty() || !this.MOB_LOOT.containsKey(slot.getName())) return ItemStack.EMPTY;
            String itemStr = (String) ((WeightedList) this.MOB_LOOT.get(slot.getName())).getRandom(new Random());
            if (itemStr.contains("{")) {
                int part = itemStr.indexOf('{');
                String itemName = itemStr.substring(0, part);
                String nbt = itemStr.substring(part);

                Item item1 = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                ItemStack itemStack = new ItemStack((IItemProvider) item1);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                } catch (CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            Item item = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider) item);
        }


        public ItemStack getForBoss(EquipmentSlotType slot) {
            if (this.BOSS_LOOT.isEmpty() || !this.BOSS_LOOT.containsKey(slot.getName())) return ItemStack.EMPTY;
            String itemStr = (String) ((WeightedList) this.BOSS_LOOT.get(slot.getName())).getRandom(new Random());
            if (itemStr.contains("{")) {
                int part = itemStr.indexOf('{');
                String itemName = itemStr.substring(0, part);
                String nbt = itemStr.substring(part);

                Item item1 = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                ItemStack itemStack = new ItemStack((IItemProvider) item1);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                } catch (CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            Item item = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider) item);
        }


        public ItemStack getForRaffle(EquipmentSlotType slot) {
            if (this.RAFFLE_BOSS_LOOT.isEmpty() || !this.RAFFLE_BOSS_LOOT.containsKey(slot.getName()))
                return ItemStack.EMPTY;
            String itemStr = (String) ((WeightedList) this.RAFFLE_BOSS_LOOT.get(slot.getName())).getRandom(new Random());
            if (itemStr.contains("{")) {
                int part = itemStr.indexOf('{');
                String itemName = itemStr.substring(0, part);
                String nbt = itemStr.substring(part);

                Item item1 = Registry.ITEM.getOptional(new ResourceLocation(itemName)).orElse(Items.AIR);
                ItemStack itemStack = new ItemStack((IItemProvider) item1);
                try {
                    itemStack.setTag(JsonToNBT.parseTag(nbt));
                } catch (CommandSyntaxException e) {
                    return ItemStack.EMPTY;
                }
                return itemStack;
            }
            Item item = Registry.ITEM.getOptional(new ResourceLocation(itemStr)).orElse(Items.AIR);
            return new ItemStack((IItemProvider) item);
        }


        public Optional<VaultMobsConfig.Mob> getMob(LivingEntity entity) {
            return this.MOB_POOL.stream()
                    .map(entry -> (VaultMobsConfig.Mob) entry.value)
                    .filter(mob -> mob.NAME.equals(entity.getType().getRegistryName().toString()))
                    .findFirst();
        }
    }

    public static class Mob {
        @Expose
        private String NAME;

        public Mob(EntityType<?> type) {
            this.NAME = type.getRegistryName().toString();
        }

        public EntityType<?> getType() {
            return Registry.ENTITY_TYPE.getOptional(new ResourceLocation(this.NAME)).orElse(EntityType.BAT);
        }

        public static LivingEntity scale(LivingEntity entity, VaultRaid vault, GlobalDifficultyData.Difficulty vaultDifficulty) {
            int level = ((Integer) vault.getProperties().getValue(VaultRaid.LEVEL)).intValue();
            UUID host = (UUID) vault.getProperties().getValue(VaultRaid.HOST);
            MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            if (srv != null) {
                level += ((Integer) NetcodeUtils.runIfPresent(srv, host, (ServerPlayerEntity sPlayer) -> Integer.valueOf(ModConfigs.PLAYER_SCALING.getMobLevelAdjustment(sPlayer.getName().getString())))

                        .orElse(Integer.valueOf(0))).intValue();
            }
            for (LevelModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), LevelModifier.class)) {
                level += modifier.getLevelAddend();
            }

            int mobLevel = Math.max(level, 0);
            List<AttributeOverride> attributes = (List<AttributeOverride>) ModConfigs.VAULT_MOBS.ATTRIBUTE_OVERRIDES.get(entity.getType().getRegistryName().toString());
            if (attributes != null) {
                for (AttributeOverride override : attributes) {
                    if (entity.level.random.nextDouble() >= override.ROLL_CHANCE) {
                        continue;
                    }
                    Registry.ATTRIBUTE.getOptional(new ResourceLocation(override.NAME)).ifPresent(attribute -> {
                        ModifiableAttributeInstance instance = entity.getAttribute(attribute);

                        if (instance != null) {
                            double multiplier = 1.0D;
                            if (attribute == Attributes.MAX_HEALTH || attribute == Attributes.ATTACK_DAMAGE) {
                                multiplier = vaultDifficulty.getMultiplier();
                            }
                            instance.setBaseValue(override.getValue(instance.getBaseValue(), mobLevel, entity.level.getRandom()) * multiplier);
                        }
                    });
                }
            }
            entity.setHealth(1.0F);
            entity.heal(1000000.0F);
            return entity;
        }

        public LivingEntity create(World world) {
            return (LivingEntity) getType().create(world);
        }

        public static class AttributeOverride {
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

            public AttributeOverride(Attribute attribute, double min, double max, String operator, double rollChance, double scalePerLevel) {
                this.NAME = attribute.getRegistryName().toString();
                this.MIN = min;
                this.MAX = max;
                this.OPERATOR = operator;
                this.ROLL_CHANCE = rollChance;
                this.SCALE_PER_LEVEL = scalePerLevel;
            }

            public double getValue(double baseValue, int level, Random random) {
                double value = getStartValue(baseValue, random);

                for (int i = 0; i < level; i++) {
                    value += getStartValue(baseValue, random) * this.SCALE_PER_LEVEL;
                }

                return value;
            }

            public double getStartValue(double baseValue, Random random) {
                double value = Math.min(this.MIN, this.MAX) + random.nextFloat() * Math.abs(this.MAX - this.MIN);

                if (this.OPERATOR.equalsIgnoreCase("multiply"))
                    return baseValue * value;
                if (this.OPERATOR.equalsIgnoreCase("add"))
                    return baseValue + value;
                if (this.OPERATOR.equalsIgnoreCase("set")) {
                    return value;
                }

                return baseValue;
            }
        }
    }

    public static class AttributeOverride {
        @Expose
        public String NAME;
        @Expose
        public double MIN;
        @Expose
        public double MAX;
        @Expose
        public String OPERATOR;

        public double getStartValue(double baseValue, Random random) {
            double value = Math.min(this.MIN, this.MAX) + random.nextFloat() * Math.abs(this.MAX - this.MIN);
            if (this.OPERATOR.equalsIgnoreCase("multiply")) return baseValue * value;
            if (this.OPERATOR.equalsIgnoreCase("add")) return baseValue + value;
            if (this.OPERATOR.equalsIgnoreCase("set")) return value;
            return baseValue;
        }

        @Expose
        public double ROLL_CHANCE;
        @Expose
        public double SCALE_PER_LEVEL;

        public AttributeOverride(Attribute attribute, double min, double max, String operator, double rollChance, double scalePerLevel) {
            this.NAME = attribute.getRegistryName().toString();
            this.MIN = min;
            this.MAX = max;
            this.OPERATOR = operator;
            this.ROLL_CHANCE = rollChance;
            this.SCALE_PER_LEVEL = scalePerLevel;
        }

        public double getValue(double baseValue, int level, Random random) {
            double value = getStartValue(baseValue, random);
            for (int i = 0; i < level; i++)
                value += getStartValue(baseValue, random) * this.SCALE_PER_LEVEL;
            return value;
        }
    }

    public static class MobMisc {
        @Expose
        public int ENCH_LEVEL;
        @Expose
        public int ENCH_TRIALS;
        @Expose
        public VaultSpawner.Config SPAWNER;

        public MobMisc(int level, int trials, VaultSpawner.Config spawner) {
            this.ENCH_LEVEL = level;
            this.ENCH_TRIALS = trials;
            this.SPAWNER = spawner;
        }
    }

    public static class BossMisc {
        @Expose
        public int ENCH_LEVEL;
        @Expose
        public int ENCH_TRIALS;

        public BossMisc(int level, int trials) {
            this.ENCH_LEVEL = level;
            this.ENCH_TRIALS = trials;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultMobsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */