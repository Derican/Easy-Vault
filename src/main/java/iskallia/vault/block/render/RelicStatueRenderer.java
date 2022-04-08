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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;

public class RelicStatueRenderer extends TileEntityRenderer<RelicStatueTileEntity> {
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL = new StatuePlayerModel(0.1F, true);
    public static final ResourceLocation TWOLF999_SKIN = Vault.id("textures/block/statue_twolf999.png");
    public static final ResourceLocation SHIELDMANH_SKIN = Vault.id("textures/block/statue_shieldmanh.png");

    public RelicStatueRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(RelicStatueTileEntity statue, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RelicSet relicSet = (RelicSet) RelicSet.REGISTRY.get(statue.getRelicSet());
        BlockState state = statue.getBlockState();

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        float horizontalAngle = ((Direction) state.getValue((Property) RelicStatueBlock.FACING)).toYRot();
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(180.0F + horizontalAngle));

        if (relicSet == RelicSet.DRAGON) {
            matrixStack.translate(0.0D, 0.0D, 0.15D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 0.7F, 7.0F, (Item) Registry.ITEM.get(Vault.id("statue_dragon")));
        } else if (relicSet == RelicSet.MINER) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(0));
        } else if (relicSet == RelicSet.WARRIOR) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(1));
        } else if (relicSet == RelicSet.RICHITY) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(2));
        } else if (relicSet == RelicSet.TWITCH) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(3));
        } else if (relicSet == RelicSet.CUPCAKE) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(4));
        } else if (relicSet == RelicSet.ELEMENT) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2F, 2.0F, RelicItem.withCustomModelData(5));
        } else if (relicSet == RelicSet.TWOLF999) {
            IVertexBuilder vertexBuilder = getPlayerVertexBuilder(TWOLF999_SKIN, buffer);
            renderPlayer(matrixStack, state, vertexBuilder, combinedLight, combinedOverlay);
        } else if (relicSet == RelicSet.SHIELDMANH) {
            IVertexBuilder vertexBuilder = getPlayerVertexBuilder(SHIELDMANH_SKIN, buffer);
            renderPlayer(matrixStack, state, vertexBuilder, combinedLight, combinedOverlay);
        }

        matrixStack.popPose();
    }

    public IVertexBuilder getPlayerVertexBuilder(ResourceLocation skinTexture, IRenderTypeBuffer buffer) {
        RenderType renderType = PLAYER_MODEL.renderType(skinTexture);
        return buffer.getBuffer(renderType);
    }

    public void renderPlayer(MatrixStack matrixStack, BlockState blockState, IVertexBuilder vertexBuilder, int combinedLight, int combinedOverlay) {
        Direction direction = (Direction) blockState.getValue((Property) RelicStatueBlock.FACING);

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 1.6D, 0.0D);
        matrixStack.scale(0.4F, 0.4F, 0.4F);

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
    }


    private void renderItem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, int overlay, float yOffset, float scale, Item item) {
        renderItem(matrixStack, buffer, lightLevel, overlay, yOffset, scale, new ItemStack((IItemProvider) item));
    }


    private void renderItem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, int overlay, float yOffset, float scale, ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        matrixStack.pushPose();
        matrixStack.translate(0.0D, yOffset, 0.0D);
        matrixStack.scale(scale, scale, scale);

        IBakedModel ibakedmodel = minecraft.getItemRenderer().getModel(itemStack, null, null);
        minecraft.getItemRenderer()
                .render(itemStack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, overlay, ibakedmodel);

        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\RelicStatueRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */