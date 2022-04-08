package iskallia.vault.block.entity;

import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.Optional;

public class TimeAltarTileEntity
        extends FillableAltarTileEntity {
    public TimeAltarTileEntity() {
        super(ModBlocks.TIME_ALTAR_TILE_ENTITY);
    }


    public ITextComponent getRequirementName() {
        return (ITextComponent) new StringTextComponent("Vault Time");
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.TIMEKEEPER;
    }


    public ITextComponent getRequirementUnit() {
        return (ITextComponent) new StringTextComponent("minutes");
    }


    public net.minecraft.util.text.Color getFillColor() {
        Color color = new Color(14590);
        return net.minecraft.util.text.Color.fromRgb(color.getRGB());
    }


    protected Optional<Integer> calcMaxProgress(VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            float multiplier = ((Float) vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(Float.valueOf(1.0F))).floatValue();
            int progress = Math.min(1 + vaultLevel.intValue() / 20, 3);
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\TimeAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */