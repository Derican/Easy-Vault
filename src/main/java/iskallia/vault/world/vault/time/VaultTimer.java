package iskallia.vault.world.vault.time;

import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public class VaultTimer
        implements INBTSerializable<CompoundNBT> {
    public int startTime;
    public int totalTime;
    public int runTime;
    protected VListNBT<TimeExtension, CompoundNBT> extensions = VListNBT.of(TimeExtension::fromNBT);
    protected List<BiConsumer<VaultTimer, TimeExtension>> extensionAddedListeners = new ArrayList<>();
    protected List<BiConsumer<VaultTimer, TimeExtension>> extensionAppliedListeners = new ArrayList<>();


    public int getStartTime() {
        return this.startTime;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public int getRunTime() {
        return this.runTime;
    }

    public int getTimeLeft() {
        return this.totalTime - this.runTime;
    }

    public VaultTimer addTime(TimeExtension extension, int delay) {
        extension.setExecutionTime((this.runTime + delay));
        this.extensions.add(extension);
        this.extensionAddedListeners.forEach(listener -> listener.accept(this, extension));
        return this;
    }

    public VaultTimer onExtensionAdded(BiConsumer<VaultTimer, TimeExtension> listener) {
        this.extensionAddedListeners.add(listener);
        return this;
    }

    public VaultTimer onExtensionApplied(BiConsumer<VaultTimer, TimeExtension> listener) {
        this.extensionAppliedListeners.add(listener);
        return this;
    }

    public VaultTimer start(int startTime) {
        this.runTime = 0;
        this.startTime = startTime;
        this.totalTime = this.startTime;
        return this;
    }

    public void tick() {
        this.extensions.forEach(extension -> {
            if (extension.getExecutionTime() == this.runTime) {
                extension.apply(this);

                this.extensionAppliedListeners.forEach(());
            }
        });
        this.runTime++;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("StartTime", this.startTime);
        nbt.putInt("TotalTime", this.totalTime);
        nbt.putInt("RunTime", this.runTime);
        nbt.put("TimeExtensions", (INBT) this.extensions.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.startTime = nbt.getInt("StartTime");
        this.totalTime = nbt.getInt("TotalTime");
        this.runTime = nbt.getInt("RunTime");
        this.extensions.deserializeNBT(nbt.getList("TimeExtensions", 10));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\VaultTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */