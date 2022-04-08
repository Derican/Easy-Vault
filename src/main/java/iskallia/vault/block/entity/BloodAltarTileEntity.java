package iskallia.vault.block.entity;

import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.Optional;


public class BloodAltarTileEntity
        extends FillableAltarTileEntity {
    public BloodAltarTileEntity() {
        super(ModBlocks.BLOOD_ALTAR_TILE_ENTITY);
    }


    public ITextComponent getRequirementName() {
        return (ITextComponent) new StringTextComponent("Health Points");
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.BENEVOLENT;
    }


    public ITextComponent getRequirementUnit() {
        return (ITextComponent) new StringTextComponent("hearts");
    }


    public net.minecraft.util.text.Color getFillColor() {
        Color color = new Color(-5570816);
        return net.minecraft.util.text.Color.fromRgb(color.getRGB());
    }


    protected Optional<Integer> calcMaxProgress(VaultRaid vault) {
        return vault.getProperties().<Integer, IntegerAttribute>getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            float multiplier = ((Float) vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(Float.valueOf(1.0F))).floatValue();
            int progress = 3 + vaultLevel.intValue() / 5;
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\BloodAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */