// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TimeAltarTileEntity extends FillableAltarTileEntity
{
    public TimeAltarTileEntity() {
        super(ModBlocks.TIME_ALTAR_TILE_ENTITY);
    }
    
    @Override
    public ITextComponent getRequirementName() {
        return (ITextComponent)new StringTextComponent("Vault Time");
    }
    
    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.TIMEKEEPER;
    }
    
    @Override
    public ITextComponent getRequirementUnit() {
        return (ITextComponent)new StringTextComponent("minutes");
    }
    
    @Override
    public Color getFillColor() {
        return new Color(-14590);
    }
    
    @Override
    protected Optional<Integer> calcMaxProgress(final VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            final float multiplier = vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(1.0f);
            final int progress = Math.min(1 + vaultLevel / 20, 3);
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}
