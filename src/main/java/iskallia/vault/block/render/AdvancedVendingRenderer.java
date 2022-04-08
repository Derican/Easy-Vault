package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.VendingMachineBlock;
import iskallia.vault.block.entity.AdvancedVendingTileEntity;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.List;

public class AdvancedVendingRenderer extends TileEntityRenderer<AdvancedVendingTileEntity> {
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL = new StatuePlayerModel(0.1F, true);

    public static final long CAROUSEL_CYCLE_TICKS = 5000L;

    public AdvancedVendingRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(AdvancedVendingTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        List<TraderCore> cores = tileEntity.getCores();

        if (cores.size() == 0)
            return;
        TraderCore renderCore = cores.get((int) (System.currentTimeMillis() / 5000L % cores.size()));

        if (renderCore == null) {
            return;
        }
        tileEntity.getSkin().updateSkin(renderCore.getName());

        float scale = 0.9F;

        ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        RenderType renderType = PLAYER_MODEL.renderType(skinLocation);
        IVertexBuilder vertexBuilder = buffer.getBuffer(renderType);

        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) VendingMachineBlock.FACING);

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.3D, 0.5D);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, -0.6200000047683716D);
        PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();

        BlockPos pos = tileEntity.getBlockPos();

        drawString(matrixStack, ((Direction) blockState

                .getValue((Property) VendingMachineBlock.FACING)).getOpposite(), tileEntity
                .getSkin().getLatestNickname(), 0.375F, pos
                .getX(), pos.getY(), pos.getZ(), 0.01F);
    }


    public void drawString(MatrixStack matrixStack, Direction facing, String text, float yOffset, double x, double y, double z, float scale) {
        FontRenderer fontRenderer = (Minecraft.getInstance()).font;
        float size = fontRenderer.width(text) * scale;
        float textCenter = (1.0F + size) / 2.0F;

        matrixStack.pushPose();

        if (facing == Direction.NORTH) {
            matrixStack.translate(textCenter, yOffset, -0.025000005960464478D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        } else if (facing == Direction.SOUTH) {
            matrixStack.translate((-textCenter + 1.0F), yOffset, 1.024999976158142D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        } else if (facing == Direction.EAST) {
            matrixStack.translate(1.024999976158142D, yOffset, textCenter);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        } else if (facing == Direction.WEST) {
            matrixStack.translate(-0.025000005960464478D, yOffset, (-textCenter + 1.0F));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(270.0F));
        }

        matrixStack.translate(0.0D, 0.0D, 0.03125D);
        matrixStack.scale(scale, scale, scale);


        fontRenderer.draw(matrixStack, text, 0.0F, 0.0F, -1);


        matrixStack.popPose();
    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getBrightness(LightType.BLOCK, pos);
        int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\AdvancedVendingRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */