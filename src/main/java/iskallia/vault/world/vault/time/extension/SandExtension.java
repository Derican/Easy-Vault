// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class SandExtension extends TimeExtension
{
    public static final ResourceLocation ID;
    protected UUID player;
    protected int amount;
    
    public SandExtension() {
    }
    
    public SandExtension(final UUID player, final int amount, final long extraTime) {
        this(SandExtension.ID, player, amount, extraTime);
    }
    
    public SandExtension(final ResourceLocation id, final UUID player, final int amount, final long extraTime) {
        super(id, extraTime);
        this.player = player;
        this.amount = amount;
    }
    
    public UUID getPlayer() {
        return this.player;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putString("Player", this.player.toString());
        nbt.putInt("Amount", this.amount);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.player = UUID.fromString(nbt.getString("Player"));
        this.amount = nbt.getInt("Amount");
    }
    
    static {
        ID = Vault.id("sand");
    }
}
