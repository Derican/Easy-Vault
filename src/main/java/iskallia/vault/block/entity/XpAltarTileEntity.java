package iskallia.vault.block.entity;

import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.Optional;

public class XpAltarTileEntity
        extends FillableAltarTileEntity {
    public XpAltarTileEntity() {
        super(ModBlocks.XP_ALTAR_TILE_ENTITY);
    }


    public ITextComponent getRequirementName() {
        return (ITextComponent) new StringTextComponent("EXP Levels");
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.OMNISCIENT;
    }


    public ITextComponent getRequirementUnit() {
        return (ITextComponent) new StringTextComponent("levels");
    }


    public net.minecraft.util.text.Color getFillColor() {
        Color color = new Color(13842220);
        return net.minecraft.util.text.Color.fromRgb(color.getRGB());
    }


    protected Optional<Integer> calcMaxProgress(VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            float multiplier = ((Float) vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(Float.valueOf(1.0F))).floatValue();
            float progress = Math.max(10.0F, vaultLevel.intValue() * 2.0F);
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\XpAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */