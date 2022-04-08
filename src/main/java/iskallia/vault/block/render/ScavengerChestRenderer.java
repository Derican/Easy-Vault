package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.block.ScavengerChestBlock;
import iskallia.vault.block.entity.ScavengerChestTileEntity;
import iskallia.vault.block.model.ScavengerChestModel;
import iskallia.vault.init.ModBlocks;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.Property;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class ScavengerChestRenderer extends TileEntityRenderer<ScavengerChestTileEntity> {
    public static final RenderMaterial MATERIAL = new RenderMaterial(Atlases.CHEST_SHEET, Vault.id("entity/chest/scavenger_chest"));
    private static final ScavengerChestModel CHEST_MODEL = new ScavengerChestModel();

    public ScavengerChestRenderer(TileEntityRendererDispatcher terd) {
        super(terd);
    }


    public void render(ScavengerChestTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        boolean isInWorldRender = tileEntity.hasLevel();

        BlockState renderState = isInWorldRender ? tileEntity.getBlockState() : (BlockState) ModBlocks.SCAVENGER_CHEST.defaultBlockState().setValue((Property) ChestBlock.FACING, (Comparable) Direction.SOUTH);
        float hAngle = ((Direction) renderState.getValue((Property) ChestBlock.FACING)).toYRot();
        TileEntityMerger.ICallbackWrapper<? extends ChestTileEntity> lidCallback = TileEntityMerger.ICallback::acceptNone;

        float lidRotation = ((Float2FloatFunction) lidCallback.apply(ScavengerChestBlock.opennessCombiner((IChestLid) tileEntity))).get(partialTicks);
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;

        CHEST_MODEL.setLidAngle(lidRotation);
        int combinedLidLight = ((Int2IntFunction) lidCallback.apply((TileEntityMerger.ICallback) new DualBrightnessCallback())).applyAsInt(combinedLight);

        IVertexBuilder vb = MATERIAL.buffer(buffer, RenderType::entityCutout);

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-hAngle));
        CHEST_MODEL.renderToBuffer(matrixStack, vb, combinedLidLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\ScavengerChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */