package iskallia.vault.skill.ability;

import com.google.common.collect.Iterables;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityActivityMessage;
import iskallia.vault.network.message.AbilityFocusMessage;
import iskallia.vault.network.message.AbilityKnownOnesMessage;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbilityTree implements INBTSerializable<CompoundNBT> {
    private static final Comparator<AbilityNode<?, ?>> ABILITY_COMPARATOR;
    private final UUID uuid;
    private final SortedSet<AbilityNode<?, ?>> nodes;
    private final HashMap<AbilityNode<?, ?>, Integer> cooldowns;
    private AbilityNode<?, ?> selectedAbility;
    private boolean active;
    private boolean swappingPerformed;
    private boolean swappingLocked;

    public AbilityTree(final UUID uuid) {
        this.nodes = new TreeSet<AbilityNode<?, ?>>(AbilityTree.ABILITY_COMPARATOR);
        this.cooldowns = new HashMap<AbilityNode<?, ?>, Integer>();
        this.selectedAbility = null;
        this.active = false;
        this.swappingPerformed = false;
        this.swappingLocked = false;
        this.uuid = uuid;
        List<AbilityGroup<?, ?>> list = ModConfigs.ABILITIES.getAll();
        Stream<AbilityNode<?, ?>> stream = list.stream().map(abilityGroup -> new AbilityNode(abilityGroup.getParentName(), 0, null));
        this.add(null, stream.collect(Collectors.toList()));
//        this.add(null, ModConfigs.ABILITIES.getAll().stream().map(abilityGroup -> new AbilityNode(abilityGroup.getParentName(), 0, null)).collect(Collectors.toList()));
    }

    public Set<AbilityNode<?, ?>> getNodes() {
        return this.nodes;
    }

    public List<AbilityNode<?, ?>> getLearnedNodes() {
        return this.getNodes().stream().filter(AbilityNode::isLearned).sorted(AbilityTree.ABILITY_COMPARATOR).collect(Collectors.toList());
    }

    @Nullable
    public AbilityNode<?, ?> getSelectedAbility() {
        this.updateSelectedAbility();
        return this.selectedAbility;
    }

    @Nullable
    private AbilityNode<?, ?> setSelectedAbility(@Nullable final AbilityNode<?, ?> abilityNode) {
        this.selectedAbility = abilityNode;
        return this.getSelectedAbility();
    }

    public AbilityNode<?, ?> getNodeOf(final AbilityGroup<?, ?> abilityGroup) {
        return this.getNodeByName(abilityGroup.getParentName());
    }

    public AbilityNode<?, ?> getNodeOf(final AbilityEffect<?> ability) {
        return this.getNodeByName(ability.getAbilityGroupName());
    }

    public AbilityNode<?, ?> getNodeByName(final String name) {
        return this.getNodes().stream().filter(node -> node.getGroup().getParentName().equals(name)).findFirst().orElseGet(() -> {
            final AbilityGroup<?, ?> group = ModConfigs.ABILITIES.getAbilityGroupByName(name);
            final AbilityNode abilityNode = new AbilityNode<>(group.getParentName(), 0, null);
            this.nodes.add(abilityNode);
            return abilityNode;
        });
    }

    public boolean isActive() {
        return this.active;
    }

    public void setSwappingLocked(final boolean swappingLocked) {
        this.swappingLocked = swappingLocked;
    }

    public AbilityTree scrollUp(final MinecraftServer server) {
        return this.updateNewSelectedAbility(server, selected -> {
            final List<AbilityNode<?, ?>> learnedNodes = this.getLearnedNodes();
            int abilityIndex = learnedNodes.indexOf(selected);
            final int abilityIndex2 = ++abilityIndex % learnedNodes.size();
            return learnedNodes.get(abilityIndex2);
        });
    }

    public AbilityTree scrollDown(final MinecraftServer server) {
        return this.updateNewSelectedAbility(server, selected -> {
            final List<AbilityNode<?, ?>> learnedNodes = this.getLearnedNodes();
            int abilityIndex = learnedNodes.indexOf(selected);
            if (--abilityIndex < 0) {
                abilityIndex += learnedNodes.size();
            }
            return learnedNodes.get(abilityIndex);
        });
    }

    private AbilityTree updateNewSelectedAbility(final MinecraftServer server, final Function<AbilityNode<?, ?>, AbilityNode<?, ?>> changeNodeFn) {
        final List<AbilityNode<?, ?>> learnedNodes = this.getLearnedNodes();
        if (this.swappingLocked) {
            return this;
        }
        if (!learnedNodes.isEmpty()) {
            final boolean prevActive = this.active;
            this.active = false;
            final AbilityNode<?, ?> selectedAbilityNode = this.getSelectedAbility();
            if (selectedAbilityNode != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                    final AbilityConfig selectedAbilityConfig = selectedAbilityNode.getAbilityConfig();
                    selectedAbilityNode.onBlur(player);
                    if (prevActive) {
                        if (selectedAbilityConfig.getBehavior() == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
                            if (selectedAbilityNode.onAction(player, false)) {
                                this.putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                            }
                        } else if (selectedAbilityConfig.getBehavior() != AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
                            this.putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                        }
                    }
                    return;
                });
            }
            final AbilityNode<?, ?> nextAttempt = changeNodeFn.apply(selectedAbilityNode);
            final AbilityNode<?, ?> nextSelection = this.setSelectedAbility(nextAttempt);
            if (nextSelection != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, nextSelection::onFocus);
            }
            this.swappingPerformed = true;
            this.syncFocusedIndex(server);
            this.notifyActivity(server);
        }
        return this;
    }

    public void keyDown(final MinecraftServer server) {
        final AbilityNode<?, ?> focusedAbilityNode = this.getSelectedAbility();
        if (focusedAbilityNode == null) {
            return;
        }
        final AbilityConfig focusedAbilityConfig = focusedAbilityNode.getAbilityConfig();
        if (focusedAbilityConfig.getBehavior() == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = true;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbilityNode.onAction(player, true);
                this.notifyActivity(server, focusedAbilityNode.getGroup(), 0, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player), true);
            });
        }
    }

    public void keyUp(final MinecraftServer server) {
        this.swappingLocked = false;
        final AbilityNode<?, ?> focusedAbilityNode = this.getSelectedAbility();
        if (focusedAbilityNode == null) {
            return;
        }
        if (this.swappingPerformed) {
            this.swappingPerformed = false;
            return;
        }
        if (this.isOnCooldown(focusedAbilityNode)) {
            return;
        }
        final AbilityConfig focusedAbilityConfig = focusedAbilityNode.getAbilityConfig();
        final AbilityConfig.Behavior behavior = focusedAbilityConfig.getBehavior();
        if (behavior == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
            this.active = !this.active;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (focusedAbilityNode.onAction(player, this.active)) {
                    this.putOnCooldown(server, focusedAbilityNode, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player));
                }
            });
        } else if (behavior == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = false;
            NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> focusedAbilityNode.onAction(player, this.active));
            this.notifyActivity(server);
        } else if (behavior == AbilityConfig.Behavior.RELEASE_TO_PERFORM) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (focusedAbilityNode.onAction(player, this.active)) {
                    this.putOnCooldown(server, focusedAbilityNode, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player));
                }
            });
        }
    }

    public void quickSelectAbility(final MinecraftServer server, final String selectAbility) {
        final List<AbilityNode<?, ?>> learnedNodes = this.getLearnedNodes();
        if (!learnedNodes.isEmpty()) {
            final boolean prevActive = this.active;
            this.active = false;
            final AbilityNode<?, ?> selectedAbilityNode = this.getSelectedAbility();
            if (selectedAbilityNode != null) {
                final AbilityConfig abilityConfig = selectedAbilityNode.getAbilityConfig();
                NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                    selectedAbilityNode.onBlur(player);
                    if (prevActive) {
                        if (abilityConfig.getBehavior() == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
                            if (selectedAbilityNode.onAction(player, this.active)) {
                                this.putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                            }
                        } else if (abilityConfig.getBehavior() != AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
                            this.putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                        }
                    }
                    return;
                });
            }
            AbilityNode<?, ?> toSelect = null;
            for (final AbilityNode<?, ?> learnedNode : learnedNodes) {
                if (learnedNode.getGroup().getParentName().equals(selectAbility)) {
                    toSelect = learnedNode;
                    break;
                }
            }
            final AbilityNode<?, ?> newFocused = this.setSelectedAbility(toSelect);
            if (newFocused != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, newFocused::onFocus);
            }
            this.syncFocusedIndex(server);
        }
    }

    public void cancelKeyDown(final MinecraftServer server) {
        final AbilityNode<?, ?> focusedAbility = this.getSelectedAbility();
        if (focusedAbility == null) {
            return;
        }
        final AbilityConfig.Behavior behavior = focusedAbility.getAbilityConfig().getBehavior();
        if (behavior == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = false;
            this.swappingLocked = false;
            this.swappingPerformed = false;
        }
        this.notifyActivity(server);
    }

    public void upgradeAbility(final MinecraftServer server, final AbilityNode<?, ?> abilityNode) {
        this.remove(server, abilityNode);
        final AbilityNode<?, ?> upgradedAbilityNode = new AbilityNode<>(abilityNode.getGroup().getParentName(), abilityNode.getLevel() + 1, abilityNode.getSpecialization());
        this.add(server, upgradedAbilityNode);
        this.setSelectedAbility(upgradedAbilityNode);
    }

    public void downgradeAbility(final MinecraftServer server, final AbilityNode<?, ?> abilityNode) {
        this.remove(server, abilityNode);
        final int targetLevel = abilityNode.getLevel() - 1;
        final AbilityNode<?, ?> downgradedAbilityNode = new AbilityNode<>(abilityNode.getGroup().getParentName(), Math.max(targetLevel, 0), abilityNode.getSpecialization());
        this.add(server, downgradedAbilityNode);
        if (targetLevel > 0) {
            this.setSelectedAbility(downgradedAbilityNode);
        } else {
            this.updateSelectedAbility();
        }
    }

    public boolean selectSpecialization(final String ability, @Nullable final String specialization) {
        final AbilityNode<?, ?> node = this.getNodeByName(ability);
        if (node != null) {
            node.setSpecialization(specialization);
            return true;
        }
        return false;
    }

    public AbilityTree add(@Nullable final MinecraftServer server, final AbilityNode<?, ?>... nodes) {
        return this.add(server, Arrays.asList(nodes));
    }

    public AbilityTree add(@Nullable final MinecraftServer server, final Collection<AbilityNode<?, ?>> nodes) {
        for (final AbilityNode<?, ?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.onAdded(player);
                }
                return;
            });
            this.nodes.add(node);
        }
        this.updateSelectedAbility();
        return this;
    }

    public AbilityTree remove(final MinecraftServer server, final AbilityNode<?, ?>... nodes) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {

            for (AbilityNode<?, ?> node2 : this.getLearnedNodes()) {
                this.putOnCooldown(server, node2, 0, ModConfigs.ABILITIES.getCooldown(node2, player));
            }
            return;
        });
        for (final AbilityNode<?, ?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.onRemoved(player);
                }
                return;
            });
            this.nodes.remove(node);
        }
        this.updateSelectedAbility();
        return this;
    }

    private void updateSelectedAbility() {
        if (this.getLearnedNodes().isEmpty()) {
            this.selectedAbility = null;
            return;
        }
        if (this.selectedAbility == null) {
            this.selectedAbility = (AbilityNode) Iterables.getFirst((Iterable) this.getLearnedNodes(), null);
        } else {
            boolean containsSelected = false;
            for (final AbilityNode<?, ?> ability : this.getLearnedNodes()) {
                if (ability.getGroup().equals(this.selectedAbility.getGroup())) {
                    containsSelected = true;
                    break;
                }
            }
            if (!containsSelected) {
                this.selectedAbility = (AbilityNode) Iterables.getFirst((Iterable) this.getLearnedNodes(), null);
            }
        }
    }

    public void tick(final ServerPlayerEntity sPlayer) {
        final AbilityNode<?, ?> selectedAbility = this.getSelectedAbility();
        if (selectedAbility != null) {
            selectedAbility.onTick(sPlayer, this.isActive());
        }
        for (final AbilityNode<?, ?> ability : this.cooldowns.keySet()) {
            this.cooldowns.computeIfPresent(ability, (index, cooldown) -> cooldown - 1);
            this.notifyCooldown(sPlayer.getServer(), ability.getGroup(), this.cooldowns.getOrDefault(ability, 0), ModConfigs.ABILITIES.getCooldown(ability, sPlayer));
        }
        this.cooldowns.entrySet().removeIf(cooldown -> cooldown.getValue() <= 0);
    }

    public void sync(final MinecraftServer server) {
        this.syncTree(server);
        this.syncFocusedIndex(server);
        this.notifyActivity(server);
    }

    public void syncTree(final MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new AbilityKnownOnesMessage(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }

    public void syncFocusedIndex(final MinecraftServer server) {
        final AbilityNode<?, ?> selected = this.getSelectedAbility();
        if (selected == null) {
            return;
        }
        NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new AbilityFocusMessage(selected.getGroup()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }

    public void notifyActivity(final MinecraftServer server) {
        final AbilityNode<?, ?> selected = this.getSelectedAbility();
        if (selected == null) {
            return;
        }
        NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> this.notifyActivity(server, selected.getGroup(), this.cooldowns.getOrDefault(selected, 0), ModConfigs.ABILITIES.getCooldown(selected, player), this.active));
    }

    public boolean isOnCooldown(final AbilityNode<?, ?> abilityNode) {
        return this.getCooldown(abilityNode) > 0;
    }

    public int getCooldown(final AbilityNode<?, ?> abilityNode) {
        return this.cooldowns.getOrDefault(abilityNode, 0);
    }

    public void putOnCooldown(final MinecraftServer server, @Nonnull final AbilityNode<?, ?> ability, final int cooldownTicks) {
        this.putOnCooldown(server, ability, cooldownTicks, cooldownTicks);
    }

    public void putOnCooldown(final MinecraftServer server, @Nonnull final AbilityNode<?, ?> ability, final int cooldownTicks, final int maxCooldown) {
        this.cooldowns.put(ability, cooldownTicks);
        this.notifyCooldown(server, ability.getGroup(), cooldownTicks, maxCooldown);
    }

    public void notifyCooldown(final MinecraftServer server, @Nonnull final AbilityGroup<?, ?> ability, final int cooldown, final int maxCooldown) {
        this.notifyActivity(server, ability, cooldown, maxCooldown, ActivityFlag.NO_OP);
    }

    public void notifyActivity(final MinecraftServer server, @Nonnull final AbilityGroup<?, ?> ability, final int cooldown, final int maxCooldown, final boolean active) {
        this.notifyActivity(server, ability, cooldown, maxCooldown, active ? ActivityFlag.ACTIVATE_ABILITY : ActivityFlag.DEACTIVATE_ABILITY);
    }

    public void notifyActivity(final MinecraftServer server, @Nonnull final AbilityGroup<?, ?> ability, final int cooldown, final int maxCooldown, final ActivityFlag activeFlag) {
        NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new AbilityActivityMessage(ability, cooldown, maxCooldown, activeFlag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT list = new ListNBT();
        this.nodes.stream().map(AbilityNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", list);
        final AbilityNode<?, ?> selected = this.getSelectedAbility();
        if (selected != null) {
            nbt.putString("SelectedAbility", selected.getGroup().getParentName());
        }
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        final ListNBT list = nbt.getList("Nodes", 10);
        this.nodes.clear();
        for (int i = 0; i < list.size(); ++i) {
            this.add(null, AbilityNode.fromNBT(list.getCompound(i)));
        }
        if (nbt.contains("SelectedAbility", 8)) {
            this.setSelectedAbility(this.getNodeByName(nbt.getString("SelectedAbility")));
        }
    }

    static {
        ABILITY_COMPARATOR = Comparator.comparing(node -> node.getGroup().getParentName());
    }

    public enum ActivityFlag {
        NO_OP,
        DEACTIVATE_ABILITY,
        ACTIVATE_ABILITY
    }
}
