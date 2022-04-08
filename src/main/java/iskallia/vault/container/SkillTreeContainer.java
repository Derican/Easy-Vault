package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;


public class SkillTreeContainer
        extends Container {
    private final AbilityTree abilityTree;
    private final TalentTree talentTree;
    private final ResearchTree researchTree;

    public SkillTreeContainer(int windowId, AbilityTree abilityTree, TalentTree talentTree, ResearchTree researchTree) {
        super(ModContainers.SKILL_TREE_CONTAINER, windowId);
        this.abilityTree = abilityTree;
        this.talentTree = talentTree;
        this.researchTree = researchTree;
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public AbilityTree getAbilityTree() {
        return this.abilityTree;
    }

    public TalentTree getTalentTree() {
        return this.talentTree;
    }

    public ResearchTree getResearchTree() {
        return this.researchTree;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\SkillTreeContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */