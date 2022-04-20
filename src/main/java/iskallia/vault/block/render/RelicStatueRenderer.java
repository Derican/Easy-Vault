package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.block.RelicStatueBlock;
import iskallia.vault.block.entity.RelicStatueTileEntity;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.item.RelicItem;
import iskallia.vault.util.RelicSet;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RelicStatueRenderer extends TileEntityRenderer<RelicStatueTileEntity> {
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL;
    public static final ResourceLocation TWOLF999_SKIN;
    public static final ResourceLocation SHIELDMANH_SKIN;

    public RelicStatueRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public void render(final RelicStatueTileEntity statue, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final RelicSet relicSet = RelicSet.REGISTRY.get(statue.getRelicSet());
        final BlockState state = statue.getBlockState();
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.0, 0.5);
        final float horizontalAngle = (state.getValue(RelicStatueBlock.FACING)).toYRot();
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(180.0f + horizontalAngle));
        if (relicSet == RelicSet.DRAGON) {
            matrixStack.translate(0.0, 0.0, 0.15);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0f));
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 0.7f, 7.0f, (Item) Registry.ITEM.get(Vault.id("statue_dragon")));
        } else if (relicSet == RelicSet.MINER) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(0));
        } else if (relicSet == RelicSet.WARRIOR) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(1));
        } else if (relicSet == RelicSet.RICHITY) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(2));
        } else if (relicSet == RelicSet.TWITCH) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(3));
        } else if (relicSet == RelicSet.CUPCAKE) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(4));
        } else if (relicSet == RelicSet.ELEMENT) {
            this.renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2.0f, RelicItem.withCustomModelData(5));
        } else if (relicSet == RelicSet.TWOLF999) {
            final IVertexBuilder vertexBuilder = this.getPlayerVertexBuilder(RelicStatueRenderer.TWOLF999_SKIN, buffer);
            this.renderPlayer(matrixStack, state, vertexBuilder, combinedLight, combinedOverlay);
        } else if (relicSet == RelicSet.SHIELDMANH) {
            final IVertexBuilder vertexBuilder = this.getPlayerVertexBuilder(RelicStatueRenderer.SHIELDMANH_SKIN, buffer);
            this.renderPlayer(matrixStack, state, vertexBuilder, combinedLight, combinedOverlay);
        }
        matrixStack.popPose();
    }

    public IVertexBuilder getPlayerVertexBuilder(final ResourceLocation skinTexture, final IRenderTypeBuffer buffer) {
        final RenderType renderType = RelicStatueRenderer.PLAYER_MODEL.renderType(skinTexture);
        return buffer.getBuffer(renderType);
    }

    public void renderPlayer(final MatrixStack matrixStack, final BlockState blockState, final IVertexBuilder vertexBuilder, final int combinedLight, final int combinedOverlay) {
        final Direction direction = blockState.getValue(RelicStatueBlock.FACING);
        matrixStack.pushPose();
        matrixStack.translate(0.0, 1.6, 0.0);
        matrixStack.scale(0.4f, 0.4f, 0.4f);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        RelicStatueRenderer.PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, -0.6200000047683716);
        RelicStatueRenderer.PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
        RelicStatueRenderer.PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        RelicStatueRenderer.PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }

    private void renderItem(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final int overlay, final float yOffset, final float scale, final Item item) {
        this.renderItem(matrixStack, buffer, lightLevel, overlay, yOffset, scale, new ItemStack((IItemProvider) item));
    }

    private void renderItem(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final int overlay, final float yOffset, final float scale, final ItemStack itemStack) {
        final Minecraft minecraft = Minecraft.getInstance();
        matrixStack.pushPose();
        matrixStack.translate(0.0, (double) yOffset, 0.0);
        matrixStack.scale(scale, scale, scale);
        final IBakedModel ibakedmodel = minecraft.getItemRenderer().getModel(itemStack, (World) null, (LivingEntity) null);
        minecraft.getItemRenderer().render(itemStack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, overlay, ibakedmodel);
        matrixStack.popPose();
    }

    static {
        PLAYER_MODEL = new StatuePlayerModel<PlayerEntity>(0.1f, true);
        TWOLF999_SKIN = Vault.id("textures/block/statue_twolf999.png");
        SHIELDMANH_SKIN = Vault.id("textures/block/statue_shieldmanh.png");
    }
}
