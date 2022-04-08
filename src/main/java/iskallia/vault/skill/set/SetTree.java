package iskallia.vault.skill.set;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SetTree implements INBTSerializable<CompoundNBT> {
    private final UUID uuid;
    private final List<SetNode<?>> nodes = new ArrayList<>();

    public SetTree(UUID uuid) {
        this.uuid = uuid;
    }

    public List<SetNode<?>> getNodes() {
        return this.nodes;
    }

    public SetNode<?> getNodeOf(SetGroup<?> setGroup) {
        return getNodeByName(setGroup.getParentName());
    }

    public SetNode<?> getNodeByName(String name) {
        Optional<SetNode<?>> talentWrapped = this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name)).findFirst();
        if (!talentWrapped.isPresent()) {
            SetNode<?> talentNode = new SetNode(ModConfigs.SETS.getByName(name), 0);
            this.nodes.add(talentNode);
            return talentNode;
        }
        return talentWrapped.get();
    }


    public SetTree add(MinecraftServer server, SetNode<?>... nodes) {
        for (SetNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isActive()) {
                    node.getSet().onAdded((PlayerEntity) player);
                }
            });
            this.nodes.add(node);
        }

        return this;
    }

    public SetTree tick(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> {
            this.nodes.removeIf(());


            List<SetNode<?>> toRemove = (List<SetNode<?>>) this.nodes.stream().filter(SetNode::isActive).filter(()).collect(Collectors.toList());


            toRemove.forEach(());


            ModConfigs.SETS.getAll().stream().filter(()).forEach(());


            this.nodes.forEach(());
        });


        return this;
    }

    public SetTree remove(MinecraftServer server, SetNode<?>... nodes) {
        for (SetNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isActive()) {
                    node.getSet().onRemoved((PlayerEntity) player);
                }
            });
            this.nodes.remove(node);
        }

        return this;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        ListNBT list = new ListNBT();
        this.nodes.stream().map(SetNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", (INBT) list);

        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Nodes", 10);
        this.nodes.clear();
        for (int i = 0; i < list.size(); i++) {
            add(null, (SetNode<?>[]) new SetNode[]{SetNode.fromNBT(list.getCompound(i), PlayerSet.class)});
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\SetTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */