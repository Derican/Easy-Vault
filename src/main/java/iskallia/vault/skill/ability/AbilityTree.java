package iskallia.vault.skill.ability;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityActivityMessage;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AbilityTree implements INBTSerializable<CompoundNBT> {
    private static final Comparator<AbilityNode<?, ?>> ABILITY_COMPARATOR;

    static {
        ABILITY_COMPARATOR = Comparator.comparing(node -> node.getGroup().getParentName());
    }

    private final UUID uuid;
    private final SortedSet<AbilityNode<?, ?>> nodes = new TreeSet<>(ABILITY_COMPARATOR);
    private final HashMap<AbilityNode<?, ?>, Integer> cooldowns = new HashMap<>();

    private AbilityNode<?, ?> selectedAbility = null;

    private boolean active = false;
    private boolean swappingPerformed = false;
    private boolean swappingLocked = false;

    public AbilityTree(UUID uuid) {
        this.uuid = uuid;
        add((MinecraftServer) null, (Collection<AbilityNode<?, ?>>) ModConfigs.ABILITIES.getAll().stream()
                .map(abilityGroup -> new AbilityNode<>(abilityGroup.getParentName(), 0, null))
                .collect(Collectors.toList()));
    }

    public Set<AbilityNode<?, ?>> getNodes() {
        return this.nodes;
    }

    public List<AbilityNode<?, ?>> getLearnedNodes() {
        return (List<AbilityNode<?, ?>>) getNodes().stream()
                .filter(AbilityNode::isLearned)
                .sorted(ABILITY_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Nullable
    public AbilityNode<?, ?> getSelectedAbility() {
        updateSelectedAbility();
        return this.selectedAbility;
    }

    @Nullable
    private AbilityNode<?, ?> setSelectedAbility(@Nullable AbilityNode<?, ?> abilityNode) {
        this.selectedAbility = abilityNode;
        return getSelectedAbility();
    }

    public AbilityNode<?, ?> getNodeOf(AbilityGroup<?, ?> abilityGroup) {
        return getNodeByName(abilityGroup.getParentName());
    }

    public AbilityNode<?, ?> getNodeOf(AbilityEffect<?> ability) {
        return getNodeByName(ability.getAbilityGroupName());
    }

    public AbilityNode<?, ?> getNodeByName(String name) {
        return getNodes().stream()
                .filter(node -> node.getGroup().getParentName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    AbilityGroup<?, ?> group = ModConfigs.ABILITIES.getAbilityGroupByName(name);
                    AbilityNode<?, ?> abilityNode = new AbilityNode<>(group.getParentName(), 0, null);
                    this.nodes.add(abilityNode);
                    return abilityNode;
                });
    }

    public boolean isActive() {
        return this.active;
    }

    public void setSwappingLocked(boolean swappingLocked) {
        this.swappingLocked = swappingLocked;
    }


    public AbilityTree scrollUp(MinecraftServer server) {
        return updateNewSelectedAbility(server, selected -> {
            List<AbilityNode<?, ?>> learnedNodes = getLearnedNodes();
            int abilityIndex = learnedNodes.indexOf(selected);
            abilityIndex = ++abilityIndex % learnedNodes.size();
            return learnedNodes.get(abilityIndex);
        });
    }


    public AbilityTree scrollDown(MinecraftServer server) {
        return updateNewSelectedAbility(server, selected -> {
            List<AbilityNode<?, ?>> learnedNodes = getLearnedNodes();
            int abilityIndex = learnedNodes.indexOf(selected);
            if (--abilityIndex < 0) {
                abilityIndex += learnedNodes.size();
            }
            return learnedNodes.get(abilityIndex);
        });
    }


    private AbilityTree updateNewSelectedAbility(MinecraftServer server, Function<AbilityNode<?, ?>, AbilityNode<?, ?>> changeNodeFn) {
        List<AbilityNode<?, ?>> learnedNodes = getLearnedNodes();

        if (this.swappingLocked) {
            return this;
        }

        if (!learnedNodes.isEmpty()) {
            boolean prevActive = this.active;
            this.active = false;

            AbilityNode<?, ?> selectedAbilityNode = getSelectedAbility();
            if (selectedAbilityNode != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                    AbilityConfig selectedAbilityConfig = (AbilityConfig) selectedAbilityNode.getAbilityConfig();

                    selectedAbilityNode.onBlur((PlayerEntity) player);

                    if (prevActive) {
                        if (selectedAbilityConfig.getBehavior() == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
                            if (selectedAbilityNode.onAction(player, false)) {
                                putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                            }
                        } else if (selectedAbilityConfig.getBehavior() != AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
                            putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                        }
                    }
                });
            }
            AbilityNode<?, ?> nextAttempt = changeNodeFn.apply(selectedAbilityNode);
            AbilityNode<?, ?> nextSelection = setSelectedAbility(nextAttempt);

            if (nextSelection != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, nextSelection::onFocus);
            }

            this.swappingPerformed = true;
            syncFocusedIndex(server);
            notifyActivity(server);
        }
        return this;
    }

    public void keyDown(MinecraftServer server) {
        AbilityNode<?, ?> focusedAbilityNode = getSelectedAbility();
        if (focusedAbilityNode == null) {
            return;
        }

        AbilityConfig focusedAbilityConfig = (AbilityConfig) focusedAbilityNode.getAbilityConfig();
        if (focusedAbilityConfig.getBehavior() == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = true;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                focusedAbilityNode.onAction(player, true);
                notifyActivity(server, focusedAbilityNode.getGroup(), 0, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player), true);
            });
        }
    }


    public void keyUp(MinecraftServer server) {
        this.swappingLocked = false;
        AbilityNode<?, ?> focusedAbilityNode = getSelectedAbility();
        if (focusedAbilityNode == null) {
            return;
        }

        if (this.swappingPerformed) {
            this.swappingPerformed = false;

            return;
        }
        if (isOnCooldown(focusedAbilityNode)) {
            return;
        }

        AbilityConfig focusedAbilityConfig = (AbilityConfig) focusedAbilityNode.getAbilityConfig();
        AbilityConfig.Behavior behavior = focusedAbilityConfig.getBehavior();

        if (behavior == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
            this.active = !this.active;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (focusedAbilityNode.onAction(player, this.active)) {
                    putOnCooldown(server, focusedAbilityNode, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player));
                }
            });
        } else if (behavior == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = false;
            NetcodeUtils.runIfPresent(server, this.uuid, player -> focusedAbilityNode.onAction(player, this.active));


            notifyActivity(server);
        } else if (behavior == AbilityConfig.Behavior.RELEASE_TO_PERFORM) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (focusedAbilityNode.onAction(player, this.active)) {
                    putOnCooldown(server, focusedAbilityNode, ModConfigs.ABILITIES.getCooldown(focusedAbilityNode, player));
                }
            });
        }
    }

    public void quickSelectAbility(MinecraftServer server, String selectAbility) {
        List<AbilityNode<?, ?>> learnedNodes = getLearnedNodes();

        if (!learnedNodes.isEmpty()) {
            boolean prevActive = this.active;
            this.active = false;

            AbilityNode<?, ?> selectedAbilityNode = getSelectedAbility();

            if (selectedAbilityNode != null) {
                AbilityConfig abilityConfig = (AbilityConfig) selectedAbilityNode.getAbilityConfig();

                NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                    selectedAbilityNode.onBlur((PlayerEntity) player);

                    if (prevActive) {
                        if (abilityConfig.getBehavior() == AbilityConfig.Behavior.PRESS_TO_TOGGLE) {
                            if (selectedAbilityNode.onAction(player, this.active)) {
                                putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                            }
                        } else if (abilityConfig.getBehavior() != AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
                            putOnCooldown(server, selectedAbilityNode, ModConfigs.ABILITIES.getCooldown(selectedAbilityNode, player));
                        }
                    }
                });
            }
            AbilityNode<?, ?> toSelect = null;
            for (AbilityNode<?, ?> learnedNode : learnedNodes) {
                if (learnedNode.getGroup().getParentName().equals(selectAbility)) {
                    toSelect = learnedNode;
                    break;
                }
            }
            AbilityNode<?, ?> newFocused = setSelectedAbility(toSelect);

            if (newFocused != null) {
                NetcodeUtils.runIfPresent(server, this.uuid, newFocused::onFocus);
            }

            syncFocusedIndex(server);
        }
    }

    public void cancelKeyDown(MinecraftServer server) {
        AbilityNode<?, ?> focusedAbility = getSelectedAbility();

        if (focusedAbility == null)
            return;
        AbilityConfig.Behavior behavior = focusedAbility.getAbilityConfig().getBehavior();

        if (behavior == AbilityConfig.Behavior.HOLD_TO_ACTIVATE) {
            this.active = false;
            this.swappingLocked = false;
            this.swappingPerformed = false;
        }

        notifyActivity(server);
    }

    public void upgradeAbility(MinecraftServer server, AbilityNode<?, ?> abilityNode) {
        remove(server, (AbilityNode<?, ?>[]) new AbilityNode[]{abilityNode});

        AbilityNode<?, ?> upgradedAbilityNode = new AbilityNode<>(abilityNode.getGroup().getParentName(), abilityNode.getLevel() + 1, abilityNode.getSpecialization());
        add(server, (AbilityNode<?, ?>[]) new AbilityNode[]{upgradedAbilityNode});
        setSelectedAbility(upgradedAbilityNode);
    }

    public void downgradeAbility(MinecraftServer server, AbilityNode<?, ?> abilityNode) {
        remove(server, (AbilityNode<?, ?>[]) new AbilityNode[]{abilityNode});

        int targetLevel = abilityNode.getLevel() - 1;
        AbilityNode<?, ?> downgradedAbilityNode = new AbilityNode<>(abilityNode.getGroup().getParentName(), Math.max(targetLevel, 0), abilityNode.getSpecialization());
        add(server, (AbilityNode<?, ?>[]) new AbilityNode[]{downgradedAbilityNode});
        if (targetLevel > 0) {
            setSelectedAbility(downgradedAbilityNode);
        } else {
            updateSelectedAbility();
        }
    }

    public boolean selectSpecialization(String ability, @Nullable String specialization) {
        AbilityNode<?, ?> node = getNodeByName(ability);
        if (node != null) {
            node.setSpecialization(specialization);
            return true;
        }
        return false;
    }


    public AbilityTree add(@Nullable MinecraftServer server, AbilityNode<?, ?>... nodes) {
        return add(server, Arrays.asList(nodes));
    }

    public AbilityTree add(@Nullable MinecraftServer server, Collection<AbilityNode<?, ?>> nodes) {
        for (AbilityNode<?, ?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.onAdded((PlayerEntity) player);
                }
            });
            this.nodes.add(node);
        }

        updateSelectedAbility();
        return this;
    }

    public AbilityTree remove(MinecraftServer server, AbilityNode<?, ?>... nodes) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            for (AbilityNode<?, ?> node : getLearnedNodes()) {
                putOnCooldown(server, node, 0, ModConfigs.ABILITIES.getCooldown(node, player));
            }
        });

        for (AbilityNode<?, ?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.onRemoved((PlayerEntity) player);
                }
            });
            this.nodes.remove(node);
        }

        updateSelectedAbility();
        return this;
    }

    private void updateSelectedAbility() {
        if (getLearnedNodes().isEmpty()) {
            this.selectedAbility = null;
            return;
        }
        if (this.selectedAbility == null) {
            this.selectedAbility = (AbilityNode<?, ?>) Iterables.getFirst(getLearnedNodes(), null);
        } else {
            boolean containsSelected = false;
            for (AbilityNode<?, ?> ability : getLearnedNodes()) {
                if (ability.getGroup().equals(this.selectedAbility.getGroup())) {
                    containsSelected = true;
                    break;
                }
            }
            if (!containsSelected) {
                this.selectedAbility = (AbilityNode<?, ?>) Iterables.getFirst(getLearnedNodes(), null);
            }
        }
    }


    public void tick(ServerPlayerEntity sPlayer) {
        AbilityNode<?, ?> selectedAbility = getSelectedAbility();

        if (selectedAbility != null) {
            selectedAbility.onTick((PlayerEntity) sPlayer, isActive());
        }

        for (AbilityNode<?, ?> ability : this.cooldowns.keySet()) {
            this.cooldowns.computeIfPresent(ability, (index, cooldown) -> Integer.valueOf(cooldown.intValue() - 1));
            notifyCooldown(sPlayer.getServer(), ability.getGroup(), ((Integer) this.cooldowns
                    .getOrDefault(ability, Integer.valueOf(0))).intValue(), ModConfigs.ABILITIES.getCooldown(ability, sPlayer));
        }
        this.cooldowns.entrySet().removeIf(cooldown -> (((Integer) cooldown.getValue()).intValue() <= 0));
    }

    public void sync(MinecraftServer server) {
        syncTree(server);
        syncFocusedIndex(server);
        notifyActivity(server);
    }

    public void syncTree(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> ModNetwork.CHANNEL.sendTo(new AbilityKnownOnesMessage(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public void syncFocusedIndex(MinecraftServer server) {
        AbilityNode<?, ?> selected = getSelectedAbility();
        if (selected == null) {
            return;
        }
        NetcodeUtils.runIfPresent(server, this.uuid, player -> ModNetwork.CHANNEL.sendTo(new AbilityFocusMessage(selected.getGroup()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public void notifyActivity(MinecraftServer server) {
        AbilityNode<?, ?> selected = getSelectedAbility();
        if (selected == null) {
            return;
        }

        NetcodeUtils.runIfPresent(server, this.uuid, player -> notifyActivity(server, selected.getGroup(), ((Integer) this.cooldowns.getOrDefault(selected, Integer.valueOf(0))).intValue(), ModConfigs.ABILITIES.getCooldown(selected, player), this.active));
    }


    public boolean isOnCooldown(AbilityNode<?, ?> abilityNode) {
        return (getCooldown(abilityNode) > 0);
    }

    public int getCooldown(AbilityNode<?, ?> abilityNode) {
        return ((Integer) this.cooldowns.getOrDefault(abilityNode, Integer.valueOf(0))).intValue();
    }

    public void putOnCooldown(MinecraftServer server, @Nonnull AbilityNode<?, ?> ability, int cooldownTicks) {
        putOnCooldown(server, ability, cooldownTicks, cooldownTicks);
    }

    public void putOnCooldown(MinecraftServer server, @Nonnull AbilityNode<?, ?> ability, int cooldownTicks, int maxCooldown) {
        this.cooldowns.put(ability, Integer.valueOf(cooldownTicks));
        notifyCooldown(server, ability.getGroup(), cooldownTicks, maxCooldown);
    }

    public void notifyCooldown(MinecraftServer server, @Nonnull AbilityGroup<?, ?> ability, int cooldown, int maxCooldown) {
        notifyActivity(server, ability, cooldown, maxCooldown, ActivityFlag.NO_OP);
    }

    public void notifyActivity(MinecraftServer server, @Nonnull AbilityGroup<?, ?> ability, int cooldown, int maxCooldown, boolean active) {
        notifyActivity(server, ability, cooldown, maxCooldown, active ? ActivityFlag.ACTIVATE_ABILITY : ActivityFlag.DEACTIVATE_ABILITY);
    }

    public void notifyActivity(MinecraftServer server, @Nonnull AbilityGroup<?, ?> ability, int cooldown, int maxCooldown, ActivityFlag activeFlag) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> ModNetwork.CHANNEL.sendTo(new AbilityActivityMessage(ability, cooldown, maxCooldown, activeFlag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        ListNBT list = new ListNBT();
        this.nodes.stream().map(AbilityNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", (INBT) list);

        AbilityNode<?, ?> selected = getSelectedAbility();
        if (selected != null) {
            nbt.putString("SelectedAbility", selected.getGroup().getParentName());
        }

        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Nodes", 10);
        this.nodes.clear();
        for (int i = 0; i < list.size(); i++) {
            add((MinecraftServer) null, (AbilityNode<?, ?>[]) new AbilityNode[]{AbilityNode.fromNBT(list.getCompound(i))});
        }

        if (nbt.contains("SelectedAbility", 8)) {
            setSelectedAbility(getNodeByName(nbt.getString("SelectedAbility")));
        }
    }

    public enum ActivityFlag {
        NO_OP,
        DEACTIVATE_ABILITY,
        ACTIVATE_ABILITY;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\AbilityTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */