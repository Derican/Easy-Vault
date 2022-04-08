package iskallia.vault.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.block.ScavengerChestBlock;
import iskallia.vault.block.VaultChestBlock;
import iskallia.vault.block.entity.VaultChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class VaultISTER
        extends ItemStackTileEntityRenderer {
    public static final VaultISTER INSTANCE = new VaultISTER();


    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ClientWorld clientWorld = (Minecraft.getInstance()).level;
        if (clientWorld != null && stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            if (block instanceof VaultChestBlock) {
                TileEntity te = ((VaultChestBlock) block).newBlockEntity((IBlockReader) clientWorld);
                if (te instanceof VaultChestTileEntity) {
                    ((VaultChestTileEntity) te).setRenderState(block.defaultBlockState());

                    TileEntityRendererDispatcher.instance.renderItem(te, matrixStack, buffer, combinedLight, combinedOverlay);
                }
            }
            if (block instanceof ScavengerChestBlock) {
                TileEntity te = ((ScavengerChestBlock) block).newBlockEntity((IBlockReader) clientWorld);
                if (te instanceof iskallia.vault.block.entity.ScavengerChestTileEntity)
                    TileEntityRendererDispatcher.instance.renderItem(te, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\render\VaultISTER.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */