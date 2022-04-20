package iskallia.vault.client;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.network.message.AbilityActivityMessage;
import iskallia.vault.network.message.AbilityFocusMessage;
import iskallia.vault.network.message.AbilityKnownOnesMessage;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ClientAbilityData {
    private static final Map<String, CooldownData> cooldowns;
    private static List<AbilityNode<?, ?>> learnedAbilities;
    private static AbilityGroup<?, ?> selectedAbility;
    private static boolean active;

    public static AbilityGroup<?, ?> getSelectedAbility() {
        return ClientAbilityData.selectedAbility;
    }

    public static boolean isActive() {
        return ClientAbilityData.active;
    }

    @Nonnull
    public static List<AbilityNode<?, ?>> getLearnedAbilityNodes() {
        return Collections.unmodifiableList((List<? extends AbilityNode<?, ?>>) ClientAbilityData.learnedAbilities);
    }

    public static int getIndexOf(final AbilityNode<?, ?> node) {
        return getLearnedAbilityNodes().indexOf(node);
    }

    public static int getIndexOf(final AbilityGroup<?, ?> group) {
        final List<AbilityNode<?, ?>> nodes = getLearnedAbilityNodes();
        for (int i = 0; i < nodes.size(); ++i) {
            final AbilityNode<?, ?> node = nodes.get(i);
            if (node.getGroup().equals(group)) {
                return i;
            }
        }
        return -1;
    }

    public static int getCooldown(final AbilityGroup<?, ?> abilityGroup) {
        return getCooldown(abilityGroup.getParentName());
    }

    public static int getCooldown(final String abilityGroupName) {
        if (!ClientAbilityData.cooldowns.containsKey(abilityGroupName)) {
            return 0;
        }
        return ClientAbilityData.cooldowns.get(abilityGroupName).getCooldownTicks();
    }

    public static int getMaxCooldown(final AbilityGroup<?, ?> abilityGroup) {
        return getMaxCooldown(abilityGroup.getParentName());
    }

    public static int getMaxCooldown(final String abilityGroupName) {
        if (!ClientAbilityData.cooldowns.containsKey(abilityGroupName)) {
            return 0;
        }
        return ClientAbilityData.cooldowns.get(abilityGroupName).getMaxCooldownTicks();
    }

    @Nullable
    public static AbilityNode<?, ?> getLearnedAbilityNode(final AbilityGroup<?, ?> ability) {
        return getLearnedAbilityNode(ability.getParentName());
    }

    @Nullable
    public static AbilityNode<?, ?> getLearnedAbilityNode(final String abilityName) {
        for (final AbilityNode<?, ?> node : ClientAbilityData.learnedAbilities) {
            if (node.getGroup().getParentName().equals(abilityName)) {
                return node;
            }
        }
        return null;
    }

    public static void updateAbilities(final AbilityKnownOnesMessage pkt) {
        ClientAbilityData.learnedAbilities = pkt.getLearnedAbilities();
    }

    public static void updateActivity(final AbilityActivityMessage pkt) {
        ClientAbilityData.cooldowns.put(pkt.getSelectedAbility(), new CooldownData(pkt.getCooldownTicks(), pkt.getMaxCooldownTicks()));
        if (pkt.getActiveFlag() != AbilityTree.ActivityFlag.NO_OP) {
            ClientAbilityData.active = (pkt.getActiveFlag() == AbilityTree.ActivityFlag.ACTIVATE_ABILITY);
        }
    }

    public static void updateSelectedAbility(final AbilityFocusMessage pkt) {
        ClientAbilityData.selectedAbility = ModConfigs.ABILITIES.getAbilityGroupByName(pkt.getSelectedAbility());
    }

    static {
        cooldowns = new HashMap<String, CooldownData>();
        ClientAbilityData.learnedAbilities = new ArrayList<AbilityNode<?, ?>>();
    }

    public static class CooldownData {
        private final int cooldownTicks;
        private final int maxCooldownTicks;

        public CooldownData(final int cooldownTicks, final int maxCooldownTicks) {
            this.cooldownTicks = cooldownTicks;
            this.maxCooldownTicks = maxCooldownTicks;
        }

        public int getCooldownTicks() {
            return this.cooldownTicks;
        }

        public int getMaxCooldownTicks() {
            return this.maxCooldownTicks;
        }
    }
}
