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
import net.minecraft.item.Items;
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
    public List<AltarConfigItem> ITEMS;
    @Expose
    public float PULL_SPEED;
    @Expose
    public double PLAYER_RANGE_CHECK;
    @Expose
    public double ITEM_RANGE_CHECK;
    @Expose
    public int INFUSION_TIME;
    @Expose
    public float LUCKY_ALTAR_CHANCE;

    public VaultAltarConfig() {
        this.ITEMS = new ArrayList<AltarConfigItem>();
    }

    @Override
    public String getName() {
        return "vault_altar";
    }

    @Override
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
        this.PULL_SPEED = 1.0f;
        this.PLAYER_RANGE_CHECK = 32.0;
        this.ITEM_RANGE_CHECK = 8.0;
        this.INFUSION_TIME = 5;
        this.LUCKY_ALTAR_CHANCE = 0.1f;
    }

    public List<RequiredItem> getRequiredItemsFromConfig(final ServerWorld world, final BlockPos pos, final ServerPlayerEntity player) {
        final LootContext ctx = new LootContext.Builder(world).withParameter(LootParameters.THIS_ENTITY, player).withRandom(world.random).withLuck(player.getLuck()).create(LootParameterSets.PIGLIN_BARTER);
        final int vaultLevel = PlayerVaultStatsData.get(world).getVaultStats(player).getVaultLevel();
        final int altarLevel = PlayerStatsData.get(world).get(player.getUUID()).getCrystals().size();
        float amtMultiplier = 1.0f;
        float luckyAltarChance = this.LUCKY_ALTAR_CHANCE;
        final GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getCrystalCost();
        amtMultiplier *= (float) difficulty.getMultiplier();
        final TalentTree talents = PlayerTalentsData.get(world).getTalents(player);
        for (final Object talent : talents.getTalents(LuckyAltarTalent.class)) {
            luckyAltarChance += ((LuckyAltarTalent) talent).getLuckyAltarChance();
        }
        if (VaultAltarConfig.rand.nextFloat() < luckyAltarChance) {
            amtMultiplier = 0.1f;
            this.spawnLuckyEffects(world, pos);
        }
        final LootTable lootTable = world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(vaultLevel).getAltar());
        final RequiredItem resource = new RequiredItem(Items.CLAY_BALL, 0, 0);
        final RequiredItem richity = new RequiredItem(Items.DIAMOND, 0, 0);
        final RequiredItem farmable = new RequiredItem(Items.APPLE, 0, 0);
        final RequiredItem misc = new RequiredItem(Items.EGG, 0, 0);
        try {
            lootTable.getPool("Resource").addRandomItems(resource::setItem, ctx);
            lootTable.getPool("Richity").addRandomItems(richity::setItem, ctx);
            lootTable.getPool("Farmable").addRandomItems(farmable::setItem, ctx);
            lootTable.getPool("Misc").addRandomItems(misc::setItem, ctx);
        }
        catch (final Exception ex) {}
        lootTable.getPool("Resource").addRandomItems(resource::setItem, ctx);
        lootTable.getPool("Richity").addRandomItems(richity::setItem, ctx);
        lootTable.getPool("Farmable").addRandomItems(farmable::setItem, ctx);
        lootTable.getPool("Misc").addRandomItems(misc::setItem, ctx);
        final double m1 = 800.0 * Math.atan(altarLevel / 250.0) / 3.141592653589793 + 1.0;
        final double m2 = 200.0 / (1.0 + Math.exp((-altarLevel + 260.0) / 50.0));
        final double m3 = 400.0 / (1.0 + Math.exp((-altarLevel + 200.0) / 40.0));
        resource.setAmountRequired((int) Math.round(resource.getItem().getCount() * m1 * amtMultiplier));
        richity.setAmountRequired((int) Math.round(richity.getItem().getCount() * m2 * amtMultiplier));
        farmable.setAmountRequired((int) Math.round(farmable.getItem().getCount() * m3 * amtMultiplier));
        misc.setAmountRequired(misc.getItem().getCount());
        resource.getItem().setCount(1);
        richity.getItem().setCount(1);
        farmable.getItem().setCount(1);
        misc.getItem().setCount(1);
        return Arrays.asList(resource, richity, farmable, misc);
    }

    private void spawnLuckyEffects(final World world, final BlockPos pos) {
        for (int i = 0; i < 30; ++i) {
            final Vector3d offset = MiscUtils.getRandomOffset(pos, VaultAltarConfig.rand, 2.0f);
            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, offset.x, offset.y, offset.z, 3, 0.0, 0.0, 0.0, 1.0);
        }
        world.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    public class AltarConfigItem {
        @Expose
        public String ITEM_ID;
        @Expose
        public int MIN;
        @Expose
        public int MAX;

        public AltarConfigItem(final String item, final int min, final int max) {
            this.ITEM_ID = item;
            this.MIN = min;
            this.MAX = max;
        }
    }
}
