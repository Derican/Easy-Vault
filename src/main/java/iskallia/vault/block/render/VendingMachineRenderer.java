// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.VendingMachineBlock;
import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.init.ModItems;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class VendingMachineRenderer extends TileEntityRenderer<VendingMachineTileEntity>
{
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL;
    
    public VendingMachineRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    public void render(final VendingMachineTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final TraderCore renderCore = tileEntity.getRenderCore();
        if (renderCore == null) {
            return;
        }
        final Minecraft minecraft = Minecraft.getInstance();
        boolean shouldOutline = false;
        if (minecraft.player != null && minecraft.player.getMainHandItem().getItem() == ModItems.TRADER_CORE) {
            final ItemStack heldStack = minecraft.player.getMainHandItem();
            if (heldStack.hasTag()) {
                final CompoundNBT nbt = heldStack.getTag();
                final CompoundNBT coreNBT = nbt.getCompound("core");
                if (coreNBT.getString("NAME").equals(renderCore.getName())) {
                    shouldOutline = true;
                }
            }
        }
        final BlockState blockState = tileEntity.getBlockState();
        final ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        if (shouldOutline) {
            final IVertexBuilder outlineBuffer = buffer.getBuffer(RenderType.outline(skinLocation));
            this.renderTrader(matrixStack, blockState, renderCore, outlineBuffer, combinedLight, combinedOverlay, 0.5f);
        }
        this.renderTrader(matrixStack, blockState, renderCore, buffer.getBuffer(VendingMachineRenderer.PLAYER_MODEL.renderType(skinLocation)), combinedLight, combinedOverlay, 1.0f);
        final BlockPos pos = tileEntity.getBlockPos();
        this.drawString(matrixStack, (blockState.getValue(VendingMachineBlock.FACING)).getOpposite(), tileEntity.getSkin().getLatestNickname(), 0.375f, pos.getX(), pos.getY(), pos.getZ(), 0.01f);
    }
    
    public void renderTrader(final MatrixStack matrixStack, final BlockState blockState, final TraderCore renderCore, final IVertexBuilder vertexBuilder, final int combinedLight, final int combinedOverlay, final float alpha) {
        final Direction direction = blockState.getValue(VendingMachineBlock.FACING);
        final float scale = 0.9f;
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.3, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        VendingMachineRenderer.PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, -0.6200000047683716);
        VendingMachineRenderer.PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.popPose();
        VendingMachineRenderer.PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        VendingMachineRenderer.PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.popPose();
    }
    
    public void drawString(final MatrixStack matrixStack, final Direction facing, final String text, final float yOffset, final double x, final double y, final double z, final float scale) {
        final FontRenderer fontRenderer = Minecraft.getInstance().font;
        final float size = fontRenderer.width(text) * scale;
        final float textCenter = (1.0f + size) / 2.0f;
        matrixStack.pushPose();
        if (facing == Direction.NORTH) {
            matrixStack.translate((double)textCenter, (double)yOffset, -0.025000005960464478);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        }
        else if (facing == Direction.SOUTH) {
            matrixStack.translate((double)(-textCenter + 1.0f), (double)yOffset, 1.024999976158142);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        }
        else if (facing == Direction.EAST) {
            matrixStack.translate(1.024999976158142, (double)yOffset, (double)textCenter);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        }
        else if (facing == Direction.WEST) {
            matrixStack.translate(-0.025000005960464478, (double)yOffset, (double)(-textCenter + 1.0f));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(270.0f));
        }
        matrixStack.translate(0.0, 0.0, 0.03125);
        matrixStack.scale(scale, scale, scale);
        fontRenderer.draw(matrixStack, text, 0.0f, 0.0f, -1);
        matrixStack.popPose();
    }
    
    private int getLightAtPos(final World world, final BlockPos pos) {
        final int blockLight = world.getBrightness(LightType.BLOCK, pos);
        final int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
    
    static {
        PLAYER_MODEL = new StatuePlayerModel<PlayerEntity>(0.1f, true);
    }
}
