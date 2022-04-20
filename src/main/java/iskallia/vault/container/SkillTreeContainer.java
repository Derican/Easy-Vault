package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class SkillTreeContainer extends Container {
    private final AbilityTree abilityTree;
    private final TalentTree talentTree;

    public SkillTreeContainer(final int windowId, final AbilityTree abilityTree, final TalentTree talentTree) {
        super((ContainerType) ModContainers.SKILL_TREE_CONTAINER, windowId);
        this.abilityTree = abilityTree;
        this.talentTree = talentTree;
    }

    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public AbilityTree getAbilityTree() {
        return this.abilityTree;
    }

    public TalentTree getTalentTree() {
        return this.talentTree;
    }

}
