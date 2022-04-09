// 
// Decompiled by Procyon v0.6.0
// 

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

public class VaultChestRenderer<T extends VaultChestTileEntity> extends ChestTileEntityRenderer<T>
{
    public static final RenderMaterial NORMAL;
    public static final RenderMaterial TREASURE;
    public static final RenderMaterial ALTAR;
    public static final RenderMaterial COOP;
    public static final RenderMaterial BONUS;
    
    public VaultChestRenderer(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    
    protected RenderMaterial getMaterial(final T tileEntity, final ChestType chestType) {
        final BlockState state = tileEntity.getBlockState();
        if (state.getBlock() == ModBlocks.VAULT_CHEST) {
            return VaultChestRenderer.NORMAL;
        }
        if (state.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
            return VaultChestRenderer.TREASURE;
        }
        if (state.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
            return VaultChestRenderer.ALTAR;
        }
        if (state.getBlock() == ModBlocks.VAULT_COOP_CHEST) {
            return VaultChestRenderer.COOP;
        }
        if (state.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
            return VaultChestRenderer.BONUS;
        }
        return null;
    }
    
    static {
        NORMAL = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_chest"));
        TREASURE = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_treasure_chest"));
        ALTAR = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_altar_chest"));
        COOP = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_coop_chest"));
        BONUS = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/vault_bonus_chest"));
    }
}
