// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.block.BlockState;
import java.util.function.Function;
import com.mojang.authlib.GameProfile;
import iskallia.vault.util.McClientHelper;
import net.minecraft.state.Property;
import iskallia.vault.block.VaultChampionTrophy;
import net.minecraft.util.Direction;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import iskallia.vault.block.entity.VaultChampionTrophyTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class VaultChampionTrophyRenderer extends TileEntityRenderer<VaultChampionTrophyTileEntity>
{
    public VaultChampionTrophyRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    public void render(@Nonnull final VaultChampionTrophyTileEntity tileEntity, final float partialTicks, @Nonnull final MatrixStack matrixStack, @Nonnull final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final ClientWorld world = (ClientWorld)tileEntity.getLevel();
        if (world == null) {
            return;
        }
        final BlockState blockState = tileEntity.getBlockState();
        final Direction facing = (Direction)blockState.getValue((Property)VaultChampionTrophy.FACING);
        final String ownerNickname = McClientHelper.getOnlineProfile(tileEntity.getOwnerUUID()).map((Function<? super GameProfile, ? extends String>)GameProfile::getName).orElse(tileEntity.getOwnerNickname());
        final int score = tileEntity.getScore();
        this.drawNameplate(matrixStack, buffer, ownerNickname, score, facing, combinedLight, combinedOverlay);
    }
    
    private void drawNameplate(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final String displayName, final int score, final Direction direction, final int combinedLight, final int combinedOverlay) {
        final IReorderingProcessor text = new StringTextComponent(displayName).withStyle(TextFormatting.BLACK).getVisualOrderText();
        final IReorderingProcessor scoreText = new StringTextComponent(String.valueOf(score)).withStyle(TextFormatting.BLACK).getVisualOrderText();
        final FontRenderer fr = this.renderer.getFont();
        final int width = fr.width(text);
        final int scoreWidth = fr.width(scoreText);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.2, 0.5);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.translate(0.0, 0.0, 0.255);
        matrixStack.scale(0.01f, -0.01f, 0.01f);
        fr.drawInBatch(text, -width / 2.0f, 0.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        fr.drawInBatch(scoreText, -scoreWidth / 2.0f, 8.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        matrixStack.popPose();
    }
}
