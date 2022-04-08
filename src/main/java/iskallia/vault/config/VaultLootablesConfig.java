package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.modifier.LootableModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class VaultLootablesConfig extends Config {
    @Expose
    public Lootable ORE = Lootable.defaultConfig();
    @Expose
    public Lootable DOOR = Lootable.defaultConfig();
    @Expose
    public Lootable RICHITY = Lootable.defaultConfig();
    @Expose
    public Lootable RESOURCE = Lootable.defaultConfig();
    @Expose
    public Lootable MISC = Lootable.defaultConfig();
    @Expose
    public Lootable VAULT_CHEST = Lootable.defaultConfig();
    @Expose
    public Lootable VAULT_TREASURE = Lootable.defaultConfig();


    public String getName() {
        return "vault_lootables";
    }


    protected void reset() {
    }

    public static class Lootable {
        @Expose
        private WeightedList<String> DEFAULT = new WeightedList();
        @Expose
        private Map<String, WeightedList<String>> OVERRIDES = new LinkedHashMap<>();


        @Nonnull
        public BlockState get(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
            RandomListAccess randomListAccess = null;
            WeightedList<String> weightedList = getPool(playerUUID);
            VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
            if (vault != null) {
                for (LootableModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new UUID[]{playerUUID} ), LootableModifier.class)) {
                    randomListAccess = modifier.adjustLootWeighting(poolName, (RandomListAccess) weightedList);
                }
            }
            return ((Block) Registry.BLOCK.getOptional(new ResourceLocation((String) randomListAccess.getRandom(random))).orElse(Blocks.AIR)).defaultBlockState();
        }

        public WeightedList<String> getPool(@Nullable UUID playerUUID) {
            WeightedList<String> pool = new WeightedList();

            if (playerUUID != null) {
                pool.addAll((Collection) this.OVERRIDES.getOrDefault(playerUUID.toString(), new WeightedList()));
            }

            this.DEFAULT.forEach(entry -> {
                if (!pool.containsValue(entry.value)) {
                    pool.add(entry);
                }
            });
            return pool;
        }

        public static Lootable defaultConfig() {
            Lootable lootable = new Lootable();
            lootable.DEFAULT.add(Blocks.AIR.getRegistryName().toString(), 1);
            ((WeightedList) lootable.OVERRIDES.computeIfAbsent("cc821d6c-a2f4-4307-955d-8b30c2fc505d", key -> new WeightedList()))
                    .add(Blocks.STONE.getRegistryName().toString(), 1);
            return lootable;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultLootablesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */