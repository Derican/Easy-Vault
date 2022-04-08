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

public class VaultBehaviour
        implements IVaultCondition, IVaultTask, INBTSerializable<CompoundNBT> {
    private VaultCondition condition;
    private VaultTask task;

    protected VaultBehaviour() {
    }

    public VaultBehaviour(VaultCondition condition, VaultTask task) {
        this.condition = condition;
        this.task = task;
    }

    public VaultCondition getCondition() {
        return this.condition;
    }

    public VaultTask getTask() {
        return this.task;
    }


    public boolean test(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        return this.condition.test(vault, player, world);
    }


    public void execute(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        this.task.execute(vault, player, world);
    }

    public void tick(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        if (test(vault, player, world)) {
            execute(vault, player, world);
        }
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Condition", (INBT) this.condition.serializeNBT());
        nbt.put("Task", (INBT) this.task.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.condition = VaultCondition.fromNBT(nbt.getCompound("Condition"));
        this.task = VaultTask.fromNBT(nbt.getCompound("Task"));
    }

    public static VaultBehaviour fromNBT(CompoundNBT nbt) {
        VaultBehaviour behaviour = new VaultBehaviour();
        behaviour.deserializeNBT(nbt);
        return behaviour;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\behaviour\VaultBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */