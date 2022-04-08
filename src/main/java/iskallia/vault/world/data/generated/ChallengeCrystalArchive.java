package iskallia.vault.world.data.generated;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChallengeCrystalArchive {
    private static final List<ItemStack> generatedCrystals = new ArrayList<>();

    public static ItemStack getRandom() {
        return getRandom(new Random());
    }

    public static ItemStack getRandom(Random rand) {
        if (generatedCrystals.isEmpty()) {
            initialize();
        }
        return (ItemStack) MiscUtils.getRandomEntry(generatedCrystals, rand);
    }

    private static void initialize() {
        CrystalData grail = baseData();
        grail.setType(CrystalData.Type.CLASSIC);
        grail.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        grail.setTargetObjectiveCount(6);
        grail.addModifier("Treasure");
        grail.addModifier("Treasure");
        grail.addModifier("Treasure");
        grail.addModifier("Safe Zone");
        grail.addModifier("Locked");
        grail.addModifier("Rush");
        grail.addModifier("Rush");
        generatedCrystals.add(make(grail));

        CrystalData gambler = baseData();
        gambler.setType(CrystalData.Type.CLASSIC);
        gambler.setSelectedObjective(((ScavengerHuntObjective) VaultRaid.SCAVENGER_HUNT.get()).getId());
        gambler.setTargetObjectiveCount(6);
        gambler.addGuaranteedRoom("digsite", 10);
        gambler.addModifier("Super Lucky");
        gambler.addModifier("Super Lucky");
        gambler.addModifier("Rotten");
        generatedCrystals.add(make(gambler));

        CrystalData speed = baseData();
        speed.setType(CrystalData.Type.CLASSIC);
        speed.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        speed.setTargetObjectiveCount(10);
        speed.addModifier("Odyssey");
        speed.addModifier("Odyssey");
        speed.addModifier("Rush");
        speed.addModifier("Rush");
        speed.addModifier("Locked");
        speed.addModifier("Rotten");
        generatedCrystals.add(make(speed));

        CrystalData miner = baseData();
        miner.setType(CrystalData.Type.CLASSIC);
        miner.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        miner.setTargetObjectiveCount(6);
        gambler.addGuaranteedRoom("mineshaft", 10);
        miner.addModifier("Copious");
        miner.addModifier("Copious");
        miner.addModifier("Copious");
        miner.addModifier("Rush");
        miner.addModifier("Rush");
        miner.addModifier("Locked");
        generatedCrystals.add(make(miner));

        CrystalData impossible = baseData();
        impossible.setType(CrystalData.Type.CLASSIC);
        impossible.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        impossible.setTargetObjectiveCount(10);
        impossible.addModifier("Impossible");
        impossible.addModifier("Impossible");
        impossible.addModifier("Frenzy");
        impossible.addModifier("Odyssey");
        impossible.addModifier("Odyssey");
        impossible.addModifier("Locked");
        generatedCrystals.add(make(impossible));

        CrystalData trap = baseData();
        trap.setType(CrystalData.Type.CLASSIC);
        trap.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        trap.setTargetObjectiveCount(1);
        trap.addModifier("Treasure");
        trap.addModifier("Treasure");
        trap.addModifier("Treasure");
        trap.addModifier("Treasure");
        trap.addModifier("Trapped");
        trap.addModifier("Trapped");
        trap.addModifier("Trapped");
        trap.addModifier("Super Lucky");
        trap.addModifier("Super Lucky");
        trap.addModifier("Super Lucky");
        generatedCrystals.add(make(trap));

        CrystalData dream = new CrystalData();
        dream.setModifiable(false);
        dream.setCanTriggerInfluences(false);
        dream.setCanGenerateTreasureRooms(false);
        dream.setType(CrystalData.Type.CLASSIC);
        dream.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        dream.setTargetObjectiveCount(10);
        dream.addModifier("Prismatic");
        dream.addModifier("Prismatic");
        dream.addModifier("Prismatic");
        dream.addModifier("Crowded");
        dream.addModifier("Safe Zone");
        dream.addModifier("Rotten");
        dream.addModifier("Locked");
        generatedCrystals.add(make(dream));

        CrystalData soul = baseData();
        soul.setType(CrystalData.Type.CLASSIC);
        soul.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        soul.setTargetObjectiveCount(10);
        soul.addModifier("Impossible");
        soul.addModifier("Impossible");
        soul.addModifier("Impossible");
        soul.addModifier("Locked");
        soul.addModifier("Destructive");
        soul.addModifier("Raging");
        generatedCrystals.add(make(soul));

        CrystalData dejavu = baseData();
        dejavu.setType(CrystalData.Type.CLASSIC);
        dejavu.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        dejavu.setTargetObjectiveCount(10);
        dejavu.addGuaranteedRoom("dungeons", 500);
        dejavu.addModifier("Exploration");
        dejavu.addModifier("Copious");
        generatedCrystals.add(make(dejavu));

        CrystalData village = baseData();
        village.setType(CrystalData.Type.CLASSIC);
        village.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        village.setTargetObjectiveCount(5);
        village.addGuaranteedRoom("village", 500);
        village.addModifier("Rush");
        village.addModifier("Rotten");
        village.addModifier("Lucky");
        generatedCrystals.add(make(village));

        CrystalData puzzle = baseData();
        puzzle.setType(CrystalData.Type.CLASSIC);
        puzzle.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        puzzle.setTargetObjectiveCount(5);
        puzzle.addGuaranteedRoom("puzzle_cube", 500);
        village.addModifier("Rush");
        village.addModifier("Rotten");
        village.addModifier("Trapped");
        village.addModifier("Trapped");
        generatedCrystals.add(make(puzzle));

        CrystalData frenzied = baseData();
        frenzied.setType(CrystalData.Type.CLASSIC);
        frenzied.setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        frenzied.setTargetObjectiveCount(5);
        gambler.addGuaranteedRoom("digsite", 500);
        frenzied.addModifier("Frenzy");
        frenzied.addModifier("Impossible");
        frenzied.addModifier("Impossible");
        frenzied.addModifier("Safe Zone");
        frenzied.addModifier("Super Lucky");
        frenzied.addModifier("Locked");
        generatedCrystals.add(make(frenzied));
    }

    private static ItemStack make(CrystalData data) {
        ItemStack crystal = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        crystal.getOrCreateTag().put("CrystalData", (INBT) data.serializeNBT());
        return crystal;
    }

    private static CrystalData baseData() {
        CrystalData data = new CrystalData();
        data.setModifiable(false);
        data.setCanTriggerInfluences(false);
        data.setCanGenerateTreasureRooms(false);
        data.setPreventsRandomModifiers(true);
        return data;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\generated\ChallengeCrystalArchive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */