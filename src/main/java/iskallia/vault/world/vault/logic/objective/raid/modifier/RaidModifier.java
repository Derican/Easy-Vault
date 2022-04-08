package iskallia.vault.world.vault.logic.objective.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;
import java.util.Random;


public abstract class RaidModifier {
    protected static final Random rand = new Random();

    @Expose
    private final boolean isPercentage;


    protected RaidModifier(boolean isPercentage, boolean isPositive, String name) {
        this.isPercentage = isPercentage;
        this.isPositive = isPositive;
        this.name = name;
    }

    @Expose
    private final boolean isPositive;
    @Expose
    private final String name;

    public boolean isPercentage() {
        return this.isPercentage;
    }

    public boolean isPositive() {
        return this.isPositive;
    }

    public String getName() {
        return this.name;
    }


    public abstract void affectRaidMob(MobEntity paramMobEntity, float paramFloat);

    public abstract void onVaultRaidFinish(VaultRaid paramVaultRaid, ServerWorld paramServerWorld, BlockPos paramBlockPos, ActiveRaid paramActiveRaid, float paramFloat);

    public abstract ITextComponent getDisplay(float paramFloat);

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RaidModifier modifier = (RaidModifier) o;
        return Objects.equals(this.name, modifier.name);
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.name});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\RaidModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */