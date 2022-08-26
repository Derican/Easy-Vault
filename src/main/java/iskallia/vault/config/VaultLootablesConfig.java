package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.modifier.LootableModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class VaultLootablesConfig extends Config {
    @Expose
    public Lootable ORE;
    @Expose
    public Lootable DOOR;
    @Expose
    public Lootable RICHITY;
    @Expose
    public Lootable RESOURCE;
    @Expose
    public Lootable MISC;
    @Expose
    public Lootable VAULT_CHEST;
    @Expose
    public Lootable VAULT_TREASURE;

    public VaultLootablesConfig() {
        this.ORE = Lootable.defaultOreConfig();
        this.DOOR = Lootable.defaultDoorConfig();
        this.RICHITY = Lootable.defaultRichityConfig();
        this.RESOURCE = Lootable.defaultResourceConfig();
        this.MISC = Lootable.defaultMiscConfig();
        this.VAULT_CHEST = Lootable.defaultVaultChestConfig();
        this.VAULT_TREASURE = Lootable.defaultVaultTreasureConfig();
    }

    @Override
    public String getName() {
        return "vault_lootables";
    }

    @Override
    protected void reset() {
    }

    public static class Lootable {
        @Expose
        private final WeightedList<String> DEFAULT;
        @Expose
        private final Map<String, WeightedList<String>> OVERRIDES;

        public Lootable() {
            this.DEFAULT = new WeightedList<String>();
            this.OVERRIDES = new LinkedHashMap<String, WeightedList<String>>();
        }

        @Nonnull
        public BlockState get(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
            RandomListAccess<String> pool = this.getPool(playerUUID);
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault != null) {
                for (final LootableModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(playerUUID), LootableModifier.class)) {
                    pool = modifier.adjustLootWeighting(poolName, pool);
                }
            }
            return Registry.BLOCK.getOptional(new ResourceLocation(pool.getRandom(random))).orElse(Blocks.AIR).defaultBlockState();
        }

        public WeightedList<String> getPool(@Nullable final UUID playerUUID) {
            final WeightedList<String> pool = new WeightedList<String>();
            if (playerUUID != null) {
                pool.addAll(this.OVERRIDES.getOrDefault(playerUUID.toString(), new WeightedList<String>()));
            }
            this.DEFAULT.forEach(entry -> {
                if (!pool.containsValue(entry.value)) {
                    pool.add(entry);
                }
                return;
            });
            return pool;
        }

        public static Lootable defaultConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add(Blocks.AIR.getRegistryName().toString(), 1);
            lootable.OVERRIDES.computeIfAbsent("cc821d6c-a2f4-4307-955d-8b30c2fc505d", key -> new WeightedList<>()).add(Blocks.STONE.getRegistryName().toString(), 1);
            return lootable;
        }

        public static Lootable defaultOreConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("minecraft:basalt", 900);
            lootable.DEFAULT.add("minecraft:stone", 1000);
            lootable.DEFAULT.add("minecraft:coal_ore", 680);
            lootable.DEFAULT.add("minecraft:iron_ore", 600);
            lootable.DEFAULT.add("minecraft:lapis_ore", 520);
            lootable.DEFAULT.add("minecraft:redstone_ore", 520);
            lootable.DEFAULT.add("minecraft:gold_ore", 480);
            lootable.DEFAULT.add("minecraft:emerald_ore", 432);
            lootable.DEFAULT.add("minecraft:diamond_ore", 360);
            lootable.DEFAULT.add("minecraft:ancient_debris", 128);
            lootable.DEFAULT.add("the_vault:ore_benitoite", 120);
            lootable.DEFAULT.add("the_vault:ore_larimar", 120);
            lootable.DEFAULT.add("the_vault:ore_alexandrite", 50);
            lootable.DEFAULT.add("the_vault:ore_wutodie", 50);
            lootable.DEFAULT.add("the_vault:ore_painite", 34);
            lootable.DEFAULT.add("the_vault:ore_black_opal", 12);
            lootable.DEFAULT.add("the_vault:ore_echo", 1);
            lootable.DEFAULT.add("the_vault:ore_iskallium", 1);
            lootable.DEFAULT.add("the_vault:ore_puffium", 1);
            lootable.DEFAULT.add("the_vault:ore_gorginite", 1);
            lootable.DEFAULT.add("the_vault:ore_sparkletine", 1);
            lootable.DEFAULT.add("the_vault:ore_ashium", 1);
            lootable.DEFAULT.add("the_vault:ore_bomignite", 1);
            lootable.DEFAULT.add("the_vault:ore_funsoide", 1);
            lootable.DEFAULT.add("the_vault:ore_tubium", 1);
            lootable.DEFAULT.add("the_vault:ore_upaline", 1);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_iskallium", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_puffium", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_gorginite", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_sparkletine", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_ashium", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_bomignite", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_funsoide", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_tubium", 5);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:ore_upaline", 5);
            lootable.OVERRIDES.computeIfAbsent("be0cfbc2-f626-4835-80c3-924268faf27b", key -> new WeightedList<>()).add("the_vault:ore_echo", 12);
            lootable.OVERRIDES.computeIfAbsent("39d98779-bfa8-4c6a-89dc-183515854dc2", key -> new WeightedList<>()).add("the_vault:ore_echo", 12);
            lootable.OVERRIDES.computeIfAbsent("7ac3c39f-23d5-472a-a7c9-24798265fa15", key -> new WeightedList<>()).add("the_vault:ore_echo", 12);
            return lootable;
        }

        public static Lootable defaultDoorConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("the_vault:door_iskallium", 20);
            lootable.DEFAULT.add("the_vault:door_gorginite", 20);
            lootable.DEFAULT.add("the_vault:door_sparkletine", 20);
            lootable.DEFAULT.add("the_vault:door_upaline", 20);
            lootable.DEFAULT.add("the_vault:door_ashium", 20);
            lootable.DEFAULT.add("the_vault:door_bomignite", 20);
            lootable.DEFAULT.add("the_vault:door_funsoide", 20);
            lootable.DEFAULT.add("the_vault:door_tubium", 20);
            lootable.DEFAULT.add("the_vault:door_puffium", 20);
            lootable.OVERRIDES.computeIfAbsent("cfaefb14-46d5-473b-9e8e-67ecbf119df7", key -> new WeightedList<>()).add("the_vault:door_gorginite", 1);
            lootable.OVERRIDES.computeIfAbsent("7ed3587b-e656-4689-90d6-08e11daaf907", key -> new WeightedList<>()).add("the_vault:door_iskallium", 1);
            lootable.OVERRIDES.computeIfAbsent("5f820c39-5883-4392-b174-3125ac05e38c", key -> new WeightedList<>()).add("the_vault:door_sparkletine", 1);
            lootable.OVERRIDES.computeIfAbsent("d974cbae-e62b-4e34-a1b8-0175a2d41d9a", key -> new WeightedList<>()).add("the_vault:door_bomignite", 1);
            lootable.OVERRIDES.computeIfAbsent("ad425147-a229-48a0-930b-ec58f9c5dd84", key -> new WeightedList<>()).add("the_vault:door_ashium", 1);
            lootable.OVERRIDES.computeIfAbsent("06f7cd12-4f00-4342-85f6-956b1ca48d38", key -> new WeightedList<>()).add("the_vault:door_tubium", 1);
            lootable.OVERRIDES.computeIfAbsent("a0e8e55d-c5bd-44b2-bb5f-5b666415b8dd", key -> new WeightedList<>()).add("the_vault:door_funsoide", 1);
            lootable.OVERRIDES.computeIfAbsent("0f5e0db0-13a0-4125-a97a-e9f8e872d521", key -> new WeightedList<>()).add("the_vault:door_upaline", 1);
            lootable.OVERRIDES.computeIfAbsent("1b5fc4e3-d5a6-424f-a761-30c38b363015", key -> new WeightedList<>()).add("the_vault:door_puffium", 1);
            return lootable;
        }

        public static Lootable defaultRichityConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("minecraft:basalt", 160);
            lootable.DEFAULT.add("minecraft:gravel", 160);
            lootable.DEFAULT.add("minecraft:coal_block", 8);
            lootable.DEFAULT.add("minecraft:iron_block", 8);
            lootable.DEFAULT.add("minecraft:lapis_block", 8);
            lootable.DEFAULT.add("minecraft:redstone_block", 8);
            lootable.DEFAULT.add("minecraft:gold_block", 6);
            lootable.DEFAULT.add("minecraft:emerald_block", 6);
            lootable.DEFAULT.add("minecraft:diamond_block", 6);
            lootable.DEFAULT.add("minecraft:ancient_debris", 4);
            lootable.DEFAULT.add("minecraft:netherite_block", 1);
            return lootable;
        }

        public static Lootable defaultResourceConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("minecraft:stone", 1400);
            lootable.DEFAULT.add("minecraft:cobblestone", 40);
            lootable.DEFAULT.add("minecraft:sand", 40);
            lootable.DEFAULT.add("minecraft:gravel", 40);
            lootable.DEFAULT.add("minecraft:netherrack", 40);
            lootable.DEFAULT.add("minecraft:soulsand", 8);
            lootable.DEFAULT.add("minecraft:clay", 8);
            lootable.DEFAULT.add("minecraft:dirt", 12);
            lootable.DEFAULT.add("minecraft:endstone", 12);
            lootable.DEFAULT.add("minecraft:obsidian", 4);
            lootable.DEFAULT.add("minecraft:andesite", 40);
            lootable.DEFAULT.add("minecraft:diorite", 40);
            lootable.DEFAULT.add("minecraft:granite", 40);
            lootable.OVERRIDES.computeIfAbsent("cc821d6c-a2f4-4307-955d-8b30c2fc505d", key -> new WeightedList<>()).add("minecraft:stone", 1);
            return lootable;
        }

        public static Lootable defaultMiscConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("minecraft:netherite_block", 2);
            lootable.DEFAULT.add("minecraft:ancient_debris", 24);
            lootable.DEFAULT.add("minecraft:diamond_block", 24);
            lootable.DEFAULT.add("the_vault:vault_diamond_block", 1);
            return lootable;
        }

        public static Lootable defaultVaultChestConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("minecraft:air", 2);
            lootable.DEFAULT.add("the_vault:vault_chest", 5);
            return lootable;
        }

        public static Lootable defaultVaultTreasureConfig() {
            final Lootable lootable = new Lootable();
            lootable.DEFAULT.add("the_vault:vault_treasure_chest", 1);
            return lootable;
        }
    }
}
