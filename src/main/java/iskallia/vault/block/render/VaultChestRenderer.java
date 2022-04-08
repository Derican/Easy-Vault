package iskallia.vault.block.render;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.VaultChestTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;

public class VaultChestRenderer<T extends VaultChestTileEntity>
        extends ChestTileEntityRenderer<T> {
    public static final RenderMaterial NORMAL = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_chest"));
    public static final RenderMaterial TREASURE = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_treasure_chest"));
    public static final RenderMaterial ALTAR = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_altar_chest"));
    public static final RenderMaterial COOP = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_coop_chest"));
    public static final RenderMaterial BONUS = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_bonus_chest"));

    public VaultChestRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }


    protected RenderMaterial getMaterial(T tileEntity, ChestType chestType) {
        BlockState state = tileEntity.getBlockState();

        if (state.getBlock() == ModBlocks.VAULT_CHEST)
            return NORMAL;
        if (state.getBlock() == ModBlocks.VAULT_TREASURE_CHEST)
            return TREASURE;
        if (state.getBlock() == ModBlocks.VAULT_ALTAR_CHEST)
            return ALTAR;
        if (state.getBlock() == ModBlocks.VAULT_COOP_CHEST)
            return COOP;
        if (state.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
            return BONUS;
        }

        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\VaultChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */