package iskallia.vault.config;

import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nullable;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import java.util.stream.Stream;

import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.logic.objective.architect.modifier.FinalVaultTimeModifier;
import iskallia.vault.world.vault.logic.objective.architect.modifier.FinalVaultModifierModifier;
import iskallia.vault.world.vault.logic.objective.architect.modifier.FinalMobHealthModifier;
import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.logic.objective.architect.modifier.FinalKnowledgeModifier;

import java.util.List;

public class FinalArchitectEventConfig extends Config {
    @Expose
    private List<FinalKnowledgeModifier> KNOWLEDGE_MODIFIERS;
    @Expose
    private List<FinalMobHealthModifier> MOB_HEALTH_MODIFIERS;
    @Expose
    private List<FinalVaultModifierModifier> VAULT_MODIFIER_MODIFIERS;
    @Expose
    private List<FinalVaultTimeModifier> VAULT_TIME_MODIFIERS;
    @Expose
    private int bossKillsNeeded;
    @Expose
    private int totalKnowledgeNeeded;
    @Expose
    private WeightedList<ModifierPair> pairs;

    @Override
    public String getName() {
        return "final_architect_event";
    }

    public List<VoteModifier> getAll() {
//        return (List<VoteModifier>) Stream.of((List[])new List[] { this.KNOWLEDGE_MODIFIERS, this.MOB_HEALTH_MODIFIERS, this.VAULT_MODIFIER_MODIFIERS, this.VAULT_TIME_MODIFIERS }).flatMap(Collection::stream).collect(Collectors.toList());
        List<VoteModifier> voteModifiers = new ArrayList<>();
        voteModifiers.add((VoteModifier) this.KNOWLEDGE_MODIFIERS);
        voteModifiers.add((VoteModifier) this.MOB_HEALTH_MODIFIERS);
        voteModifiers.add((VoteModifier) this.VAULT_MODIFIER_MODIFIERS);
        voteModifiers.add((VoteModifier) this.VAULT_TIME_MODIFIERS);
        return voteModifiers;
    }

    @Nullable
    public VoteModifier getModifier(final String modifierName) {
        if (modifierName == null) {
            return null;
        }
        return this.getAll().stream().filter(modifier -> modifierName.equalsIgnoreCase(modifier.getName())).findFirst().orElse(null);
    }

    public ModifierPair getRandomPair() {
        return this.pairs.getRandom(FinalArchitectEventConfig.rand);
    }

    public int getBossKillsNeeded() {
        return this.bossKillsNeeded;
    }

    public int getTotalKnowledgeNeeded() {
        return this.totalKnowledgeNeeded;
    }

    @Override
    protected void reset() {
        this.KNOWLEDGE_MODIFIERS = new ArrayList<>();
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("AddKnowledge2", "+2 Knowledge", "2555903", 0, 2));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("AddKnowledge3", "+3 Knowledge", "2555903", 0, 3));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("AddKnowledge4", "+4 Knowledge", "2555903", 0, 4));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("AddKnowledge5", "+5 Knowledge", "2555903", 0, 5));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("RemoveKnowledge2", "-2 Knowledge", "4784041", 0, -2));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("RemoveKnowledge3", "-3 Knowledge", "4784041", 0, -3));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("RemoveKnowledge4", "-4 Knowledge", "4784041", 0, -4));
        this.KNOWLEDGE_MODIFIERS.add(new FinalKnowledgeModifier("RemoveKnowledge5", "-5 Knowledge", "4784041", 0, -5));
        this.MOB_HEALTH_MODIFIERS = new ArrayList<>();
        this.MOB_HEALTH_MODIFIERS.add(new FinalMobHealthModifier("MobHealth20", "+20% Mob Health", "16720188", 0, 0.2f));
        this.MOB_HEALTH_MODIFIERS.add(new FinalMobHealthModifier("MobHealth5", "+5% Mob Health", "16720188", 0, 0.05f));
        this.MOB_HEALTH_MODIFIERS.add(new FinalMobHealthModifier("MobHealth10", "+10% Mob Health", "16720188", 0, 0.1f));
        this.MOB_HEALTH_MODIFIERS.add(new FinalMobHealthModifier("MobHealth50", "+50% Mob Health", "16720188", 0, 0.5f));
        this.VAULT_MODIFIER_MODIFIERS = new ArrayList<>();
        this.VAULT_MODIFIER_MODIFIERS.add(new FinalVaultModifierModifier("AddMob", "+1 Mobs", "15220516", 0, "Mob"));
        this.VAULT_MODIFIER_MODIFIERS.add(new FinalVaultModifierModifier("RemoveMob", "-1 Mobs", "16364031", 0, "Silence"));
        this.VAULT_MODIFIER_MODIFIERS.add(new FinalVaultModifierModifier("AddAwkward", "-15% Parry", "13338221", 0, "Awkward"));
        this.VAULT_MODIFIER_MODIFIERS.add(new FinalVaultModifierModifier("AddExposed", "-15% Resistance", "13277787", 0, "Exposed"));
        this.VAULT_TIME_MODIFIERS = new ArrayList<>();
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("AddTime60", "+60 Seconds", "16769340", 0, 60));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("AddTime90", "+90 Seconds", "16769340", 0, 90));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("AddTime120", "+120 Seconds", "16769340", 0, 120));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("AddTime30", "+30 Seconds", "16769340", 0, 30));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("RemoveTime60", "-60 Seconds", "15368539", 0, -60));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("RemoveTime30", "-30 Seconds", "15368539", 0, -30));
        this.VAULT_TIME_MODIFIERS.add(new FinalVaultTimeModifier("RemoveTime15", "-15 Seconds", "15368539", 0, -15));
        this.bossKillsNeeded = 10;
        this.totalKnowledgeNeeded = 20;
        this.pairs = new WeightedList<>();
        this.pairs.add(new ModifierPair("AddKnowledge2", "AddAwkward"), 10);
        this.pairs.add(new ModifierPair("AddKnowledge2", "AddExposed"), 10);
        this.pairs.add(new ModifierPair("AddKnowledge2", "MobHealth10"), 16);
        this.pairs.add(new ModifierPair("AddKnowledge2", "AddMob"), 16);
        this.pairs.add(new ModifierPair("AddKnowledge3", "AddAwkward"), 10);
        this.pairs.add(new ModifierPair("AddKnowledge3", "AddExposed"), 8);
        this.pairs.add(new ModifierPair("AddKnowledge3", "MobHealth10"), 6);
        this.pairs.add(new ModifierPair("AddKnowledge3", "RemoveTime30"), 8);
        this.pairs.add(new ModifierPair("AddKnowledge3", "AddMob"), 14);
        this.pairs.add(new ModifierPair("AddKnowledge4", "AddAwkward"), 8);
        this.pairs.add(new ModifierPair("AddKnowledge4", "AddExposed"), 4);
        this.pairs.add(new ModifierPair("AddKnowledge4", "MobHealth10"), 6);
        this.pairs.add(new ModifierPair("AddKnowledge4", "RemoveTime15"), 4);
        this.pairs.add(new ModifierPair("AddKnowledge4", "RemoveTime30"), 4);
        this.pairs.add(new ModifierPair("AddKnowledge4", "AddMob"), 8);
        this.pairs.add(new ModifierPair("AddKnowledge5", "AddAwkward"), 2);
        this.pairs.add(new ModifierPair("AddKnowledge5", "AddExposed"), 1);
        this.pairs.add(new ModifierPair("AddKnowledge5", "MobHealth20"), 4);
        this.pairs.add(new ModifierPair("AddKnowledge5", "RemoveTime30"), 2);
        this.pairs.add(new ModifierPair("AddKnowledge5", "RemoveTime30"), 6);
        this.pairs.add(new ModifierPair("AddKnowledge5", "RemoveTime60"), 2);
        this.pairs.add(new ModifierPair("AddKnowledge5", "AddMob"), 8);
        this.pairs.add(new ModifierPair("AddTime30", "AddAwkward"), 12);
        this.pairs.add(new ModifierPair("AddTime30", "AddExposed"), 6);
        this.pairs.add(new ModifierPair("AddTime30", "MobHealth5"), 10);
        this.pairs.add(new ModifierPair("AddTime30", "MobHealth10"), 6);
        this.pairs.add(new ModifierPair("AddTime30", "RemoveKnowledge4"), 12);
        this.pairs.add(new ModifierPair("AddTime30", "RemoveKnowledge2"), 6);
        this.pairs.add(new ModifierPair("AddTime30", "AddMob"), 8);
        this.pairs.add(new ModifierPair("AddTime60", "AddAwkward"), 4);
        this.pairs.add(new ModifierPair("AddTime60", "AddExposed"), 1);
        this.pairs.add(new ModifierPair("AddTime60", "MobHealth10"), 4);
        this.pairs.add(new ModifierPair("AddTime60", "RemoveKnowledge5"), 6);
        this.pairs.add(new ModifierPair("AddTime60", "RemoveKnowledge4"), 6);
        this.pairs.add(new ModifierPair("AddTime60", "RemoveKnowledge2"), 10);
        this.pairs.add(new ModifierPair("AddTime60", "AddMob"), 6);
        this.pairs.add(new ModifierPair("AddTime90", "AddMob"), 6);
        this.pairs.add(new ModifierPair("AddTime90", "AddAwkward"), 4);
        this.pairs.add(new ModifierPair("AddTime90", "RemoveKnowledge5"), 10);
        this.pairs.add(new ModifierPair("AddTime120", "AddMob"), 6);
        this.pairs.add(new ModifierPair("AddTime120", "AddAwkward"), 4);
        this.pairs.add(new ModifierPair("AddTime90", "RemoveKnowledge5"), 8);
        this.pairs.add(new ModifierPair("RemoveMob", "RemoveTime30"), 10);
        this.pairs.add(new ModifierPair("RemoveMob", "RemoveTime15"), 6);
        this.pairs.add(new ModifierPair("RemoveMob", "RemoveKnowledge2"), 4);
        this.pairs.add(new ModifierPair("RemoveMob", "RemoveKnowledge3"), 5);
        this.pairs.add(new ModifierPair("RemoveMob", "RemoveKnowledge4"), 6);
        this.pairs.add(new ModifierPair("RemoveMob", "AddAwkward"), 8);
        this.pairs.add(new ModifierPair("RemoveMob", "AddExposed"), 6);
        this.pairs.add(new ModifierPair("RemoveMob", "MobHealth10"), 8);
        this.pairs.add(new ModifierPair("RemoveMob", "MobHealth5"), 6);
    }

    public static class ModifierPair {
        @Expose
        private final String positive;
        @Expose
        private final String negative;

        public ModifierPair(final String positive, final String negative) {
            this.positive = positive;
            this.negative = negative;
        }

        public String getPositive() {
            return this.positive;
        }

        public String getNegative() {
            return this.negative;
        }
    }
}
