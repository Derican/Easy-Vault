package iskallia.vault.world.vault.logic.behaviour;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.condition.IVaultCondition;
import iskallia.vault.world.vault.logic.condition.VaultCondition;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public class VaultBehaviour implements IVaultCondition, IVaultTask, INBTSerializable<CompoundNBT> {
    private VaultCondition condition;
    private VaultTask task;

    protected VaultBehaviour() {
    }

    public VaultBehaviour(final VaultCondition condition, final VaultTask task) {
        this.condition = condition;
        this.task = task;
    }

    public VaultCondition getCondition() {
        return this.condition;
    }

    public VaultTask getTask() {
        return this.task;
    }

    @Override
    public boolean test(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        return this.condition.test(vault, player, world);
    }

    @Override
    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        this.task.execute(vault, player, world);
    }

    public void tick(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        if (this.test(vault, player, world)) {
            this.execute(vault, player, world);
        }
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("Condition", this.condition.serializeNBT());
        nbt.put("Task", this.task.serializeNBT());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.condition = VaultCondition.fromNBT(nbt.getCompound("Condition"));
        this.task = VaultTask.fromNBT(nbt.getCompound("Task"));
    }

    public static VaultBehaviour fromNBT(final CompoundNBT nbt) {
        final VaultBehaviour behaviour = new VaultBehaviour();
        behaviour.deserializeNBT(nbt);
        return behaviour;
    }
}
