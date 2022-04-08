package iskallia.vault.skill;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentTree;

import java.util.*;

public class SkillGates {
    @Expose
    private final Map<String, Entry> entries = new HashMap<>();


    public void addEntry(String skillName, Entry entry) {
        this.entries.put(skillName, entry);
    }

    public List<AbilityGroup<?, ?>> getDependencyAbilities(String abilityName) {
        List<AbilityGroup<?, ?>> abilities = new LinkedList<>();
        Entry entry = this.entries.get(abilityName);
        if (entry == null) return abilities;
        entry.dependsOn.forEach(dependencyName -> {
            AbilityGroup<?, ?> dependency = ModConfigs.ABILITIES.getAbilityGroupByName(dependencyName);
            abilities.add(dependency);
        });
        return abilities;
    }

    public List<AbilityGroup<?, ?>> getLockedByAbilities(String abilityName) {
        List<AbilityGroup<?, ?>> abilities = new LinkedList<>();
        Entry entry = this.entries.get(abilityName);
        if (entry == null) return abilities;
        entry.lockedBy.forEach(dependencyName -> {
            AbilityGroup<?, ?> dependency = ModConfigs.ABILITIES.getAbilityGroupByName(dependencyName);
            abilities.add(dependency);
        });
        return abilities;
    }

    public List<AbilityGroup<?, ?>> getAbilitiesDependingOn(String abilityName) {
        List<AbilityGroup<?, ?>> abilities = new LinkedList<>();
        AbilityGroup<?, ?> ability = ModConfigs.ABILITIES.getAbilityGroupByName(abilityName);
        for (AbilityGroup<?, ?> otherAbility : (Iterable<AbilityGroup<?, ?>>) ModConfigs.ABILITIES.getAll()) {
            List<AbilityGroup<?, ?>> dependencies = ModConfigs.SKILL_GATES.getGates().getDependencyAbilities(otherAbility.getParentName());
            if (dependencies.contains(ability)) {
                abilities.add(otherAbility);
            }
        }
        return abilities;
    }

    public List<TalentGroup<?>> getDependencyTalents(String talentName) {
        List<TalentGroup<?>> talents = new LinkedList<>();
        Entry entry = this.entries.get(talentName);
        if (entry == null) return talents;
        entry.dependsOn.forEach(dependencyName -> {
            TalentGroup<?> dependency = ModConfigs.TALENTS.getByName(dependencyName);
            talents.add(dependency);
        });
        return talents;
    }

    public List<TalentGroup<?>> getLockedByTalents(String talentName) {
        List<TalentGroup<?>> talents = new LinkedList<>();
        Entry entry = this.entries.get(talentName);
        if (entry == null) return talents;
        entry.lockedBy.forEach(dependencyName -> {
            TalentGroup<?> dependency = ModConfigs.TALENTS.getByName(dependencyName);
            talents.add(dependency);
        });
        return talents;
    }

    public List<TalentGroup<?>> getTalentsDependingOn(String talentName) {
        List<TalentGroup<?>> talents = new LinkedList<>();
        TalentGroup<?> talent = ModConfigs.TALENTS.getByName(talentName);
        for (TalentGroup<?> otherTalent : (Iterable<TalentGroup<?>>) ModConfigs.TALENTS.getAll()) {
            List<TalentGroup<?>> dependencies = ModConfigs.SKILL_GATES.getGates().getDependencyTalents(otherTalent.getParentName());
            if (dependencies.contains(talent)) {
                talents.add(otherTalent);
            }
        }
        return talents;
    }

    public List<Research> getDependencyResearches(String researchName) {
        List<Research> researches = new LinkedList<>();
        Entry entry = this.entries.get(researchName);
        if (entry == null) return researches;
        entry.dependsOn.forEach(dependencyName -> {
            Research dependency = ModConfigs.RESEARCHES.getByName(dependencyName);
            researches.add(dependency);
        });
        return researches;
    }

    public List<Research> getLockedByResearches(String researchName) {
        List<Research> researches = new LinkedList<>();
        Entry entry = this.entries.get(researchName);
        if (entry == null) return researches;
        entry.lockedBy.forEach(dependencyName -> {
            Research dependency = ModConfigs.RESEARCHES.getByName(dependencyName);
            researches.add(dependency);
        });
        return researches;
    }

    public boolean isLocked(String researchName, ResearchTree researchTree) {
        SkillGates gates = ModConfigs.SKILL_GATES.getGates();

        List<String> researchesDone = researchTree.getResearchesDone();

        for (Research dependencyResearch : gates.getDependencyResearches(researchName)) {
            if (!researchesDone.contains(dependencyResearch.getName())) {
                return true;
            }
        }
        for (Research lockedByResearch : gates.getLockedByResearches(researchName)) {
            if (researchesDone.contains(lockedByResearch.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocked(TalentGroup<?> talent, TalentTree talentTree) {
        SkillGates gates = ModConfigs.SKILL_GATES.getGates();

        if (talent instanceof iskallia.vault.skill.talent.ArchetypeTalentGroup &&
                talentTree.getLearnedNodes().stream()
                        .filter(other -> !other.getGroup().getParentName().equals(talent.getParentName()))
                        .anyMatch(t -> t.getGroup() instanceof iskallia.vault.skill.talent.ArchetypeTalentGroup)) {
            return true;
        }


        for (TalentGroup<?> dependencyTalent : gates.getDependencyTalents(talent.getParentName())) {
            if (!talentTree.getNodeOf(dependencyTalent).isLearned()) {
                return true;
            }
        }
        for (TalentGroup<?> lockedByTalent : gates.getLockedByTalents(talent.getParentName())) {
            if (talentTree.getNodeOf(lockedByTalent).isLearned()) {
                return true;
            }
        }
        return false;
    }


    public static class Entry {
        @Expose
        private List<String> dependsOn = new LinkedList<>();
        @Expose
        private List<String> lockedBy = new LinkedList<>();


        public void setDependsOn(String... skills) {
            this.dependsOn.addAll(Arrays.asList(skills));
        }

        public void setLockedBy(String... skills) {
            this.lockedBy.addAll(Arrays.asList(skills));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\SkillGates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */