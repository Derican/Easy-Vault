// 
// Decompiled by Procyon v0.6.0
// 

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

public abstract class RaidModifier
{
    protected static final Random rand;
    @Expose
    private final boolean isPercentage;
    @Expose
    private final boolean isPositive;
    @Expose
    private final String name;
    
    protected RaidModifier(final boolean isPercentage, final boolean isPositive, final String name) {
        this.isPercentage = isPercentage;
        this.isPositive = isPositive;
        this.name = name;
    }
    
    public boolean isPercentage() {
        return this.isPercentage;
    }
    
    public boolean isPositive() {
        return this.isPositive;
    }
    
    public String getName() {
        return this.name;
    }
    
    public abstract void affectRaidMob(final MobEntity p0, final float p1);
    
    public abstract void onVaultRaidFinish(final VaultRaid p0, final ServerWorld p1, final BlockPos p2, final ActiveRaid p3, final float p4);
    
    public abstract ITextComponent getDisplay(final float p0);
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RaidModifier modifier = (RaidModifier)o;
        return Objects.equals(this.name, modifier.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
    
    static {
        rand = new Random();
    }
}
