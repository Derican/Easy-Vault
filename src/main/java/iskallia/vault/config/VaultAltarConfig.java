package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.altar.RequiredItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.LuckyAltarTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VaultAltarConfig extends Config {
    @Expose
    public List<AltarConfigItem> ITEMS = new ArrayList<>();

    @Expose
    public float PULL_SPEED;

    @Expose
    public double PLAYER_RANGE_CHECK;

    public String getName() {
        return "vault_altar";
    }

    @Expose
    public double ITEM_RANGE_CHECK;
    @Expose
    public int INFUSION_TIME;
    @Expose
    public float LUCKY_ALTAR_CHANCE;

    protected void reset() {
        this.ITEMS.add(new AltarConfigItem("minecraft:cobblestone", 1000, 6000));
        this.ITEMS.add(new AltarConfigItem("minecraft:gold_ingot", 300, 900));
        this.ITEMS.add(new AltarConfigItem("minecraft:iron_ingot", 400, 1300));
        this.ITEMS.add(new AltarConfigItem("minecraft:sugar_cane", 800, 1600));
        this.ITEMS.add(new AltarConfigItem("minecraft:oak_log", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:spruce_log", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:acacia_log", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:jungle_log", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:dark_oak_log", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:apple", 400, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:redstone", 400, 1000));
        this.ITEMS.add(new AltarConfigItem("minecraft:ink_sac", 300, 600));
        this.ITEMS.add(new AltarConfigItem("minecraft:slime_ball", 200, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:rotten_flesh", 500, 1500));
        this.ITEMS.add(new AltarConfigItem("minecraft:blaze_rod", 80, 190));
        this.ITEMS.add(new AltarConfigItem("minecraft:brick", 500, 1500));
        this.ITEMS.add(new AltarConfigItem("minecraft:bone", 500, 1500));
        this.ITEMS.add(new AltarConfigItem("minecraft:spider_eye", 150, 400));
        this.ITEMS.add(new AltarConfigItem("minecraft:melon_slice", 1000, 5000));
        this.ITEMS.add(new AltarConfigItem("minecraft:pumpkin", 1000, 5000));
        this.ITEMS.add(new AltarConfigItem("minecraft:sand", 1000, 5000));
        this.ITEMS.add(new AltarConfigItem("minecraft:gravel", 1000, 5000));
        this.ITEMS.add(new AltarConfigItem("minecraft:wheat", 1000, 2000));
        this.ITEMS.add(new AltarConfigItem("minecraft:wheat_seeds", 1000, 2000));
        this.ITEMS.add(new AltarConfigItem("minecraft:carrot", 1000, 2000));
        this.ITEMS.add(new AltarConfigItem("minecraft:potato", 1000, 2000));
        this.ITEMS.add(new AltarConfigItem("minecraft:obsidian", 100, 300));
        this.ITEMS.add(new AltarConfigItem("minecraft:leather", 300, 800));
        this.ITEMS.add(new AltarConfigItem("minecraft:string", 500, 1200));

        this.PULL_SPEED = 1.0F;
        this.PLAYER_RANGE_CHECK = 32.0D;
        this.ITEM_RANGE_CHECK = 8.0D;
        this.INFUSION_TIME = 5;
        this.LUCKY_ALTAR_CHANCE = 0.1F;
    }


    public List<RequiredItem> getRequiredItemsFromConfig(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        LootContext ctx = (new LootContext.Builder(world)).withParameter(LootParameters.THIS_ENTITY, player).withRandom(world.random).withLuck(player.getLuck()).create(LootParameterSets.PIGLIN_BARTER);

        int vaultLevel = PlayerVaultStatsData.get(world).getVaultStats((PlayerEntity) player).getVaultLevel();
        int altarLevel = PlayerStatsData.get(world).get(player.getUUID()).getCrystals().size();

        float amtMultiplier = 1.0F;
        float luckyAltarChance = this.LUCKY_ALTAR_CHANCE;

        GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getCrystalCost();
        amtMultiplier = (float) (amtMultiplier * difficulty.getMultiplier());

        TalentTree talents = PlayerTalentsData.get(world).getTalents((PlayerEntity) player);
        for (LuckyAltarTalent talent : talents.getTalents(LuckyAltarTalent.class)) {
            luckyAltarChance += talent.getLuckyAltarChance();
        }

        if (rand.nextFloat() < luckyAltarChance) {
            amtMultiplier = 0.1F;
            spawnLuckyEffects((World) world, pos);
        }

        LootTable lootTable = world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(vaultLevel).getAltar());
        RequiredItem resource = new RequiredItem(ItemStack.EMPTY, 0, 0);
        RequiredItem richity = new RequiredItem(ItemStack.EMPTY, 0, 0);
        RequiredItem farmable = new RequiredItem(ItemStack.EMPTY, 0, 0);
        RequiredItem misc = new RequiredItem(ItemStack.EMPTY, 0, 0);

        lootTable.getPool("Resource").addRandomItems(resource::setItem, ctx);
        lootTable.getPool("Richity").addRandomItems(richity::setItem, ctx);
        lootTable.getPool("Farmable").addRandomItems(farmable::setItem, ctx);
        lootTable.getPool("Misc").addRandomItems(misc::setItem, ctx);

        double m1 = 800.0D * Math.atan(altarLevel / 250.0D) / Math.PI + 1.0D;
        double m2 = 200.0D / (1.0D + Math.exp((-altarLevel + 260.0D) / 50.0D));
        double m3 = 400.0D / (1.0D + Math.exp((-altarLevel + 200.0D) / 40.0D));

        resource.setAmountRequired((int) Math.round(resource.getItem().getCount() * m1 * amtMultiplier));
        richity.setAmountRequired((int) Math.round(richity.getItem().getCount() * m2 * amtMultiplier));
        farmable.setAmountRequired((int) Math.round(farmable.getItem().getCount() * m3 * amtMultiplier));
        misc.setAmountRequired(misc.getItem().getCount());

        resource.getItem().setCount(1);
        richity.getItem().setCount(1);
        farmable.getItem().setCount(1);
        misc.getItem().setCount(1);

        return Arrays.asList(new RequiredItem[]{resource, richity, farmable, misc});
    }

    private void spawnLuckyEffects(World world, BlockPos pos) {
        for (int i = 0; i < 30; i++) {
            Vector3d offset = MiscUtils.getRandomOffset(pos, rand, 2.0F);

            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, offset.x, offset.y, offset.z, 3, 0.0D, 0.0D, 0.0D, 1.0D);
        }


        world.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public class AltarConfigItem {
        @Expose
        public String ITEM_ID;
        @Expose
        public int MIN;
        @Expose
        public int MAX;

        public AltarConfigItem(String item, int min, int max) {
            this.ITEM_ID = item;
            this.MIN = min;
            this.MAX = max;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultAltarConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */