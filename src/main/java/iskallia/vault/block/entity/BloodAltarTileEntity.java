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

public class BloodAltarTileEntity extends FillableAltarTileEntity
{
    public BloodAltarTileEntity() {
        super(ModBlocks.BLOOD_ALTAR_TILE_ENTITY);
    }
    
    @Override
    public ITextComponent getRequirementName() {
        return (ITextComponent)new StringTextComponent("Health Points");
    }
    
    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.BENEVOLENT;
    }
    
    @Override
    public ITextComponent getRequirementUnit() {
        return (ITextComponent)new StringTextComponent("hearts");
    }
    
    @Override
    public Color getFillColor() {
        return new Color(-5570816);
    }
    
    @Override
    protected Optional<Integer> calcMaxProgress(final VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            final float multiplier = vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(1.0F);
            final int progress = 3 + vaultLevel / 5;
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}
