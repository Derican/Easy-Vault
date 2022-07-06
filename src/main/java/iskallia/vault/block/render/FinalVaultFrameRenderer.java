// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.render;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.block.BlockState;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.block.VaultPortalBlock;
import net.minecraft.util.math.vector.Vector3f;
import iskallia.vault.client.util.LightmapUtil;
import org.lwjgl.opengl.ARBShaderObjects;
import java.awt.Color;
import iskallia.vault.client.util.ShaderUtil;
import net.minecraft.state.Property;
import iskallia.vault.block.FinalVaultFrameBlock;
import net.minecraft.util.Direction;
import java.util.function.Function;
import com.mojang.authlib.GameProfile;
import iskallia.vault.util.McClientHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.block.entity.FinalVaultFrameTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class FinalVaultFrameRenderer extends TileEntityRenderer<FinalVaultFrameTileEntity>
{
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL;
    private static final Map<BlockPos, Long> PARTICLE_SPAWN_TIMESTAMPS;
    
    public FinalVaultFrameRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    public void render(@Nonnull final FinalVaultFrameTileEntity tileEntity, final float partialTicks, @Nonnull final MatrixStack matrixStack, @Nonnull final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final ClientWorld world = (ClientWorld)tileEntity.getLevel();
        if (world == null) {
            return;
        }
        final boolean ownerOnline = McClientHelper.getOnlineProfile(tileEntity.getOwnerUUID()).map((Function<? super GameProfile, ?>)GameProfile::getId).filter(uuid -> uuid.equals(tileEntity.getOwnerUUID())).isPresent();
        final ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        final RenderType renderType = FinalVaultFrameRenderer.PLAYER_MODEL.renderType(skinLocation);
        final IVertexBuilder vertexBuilder = buffer.getBuffer(renderType);
        final BlockPos blockPos = tileEntity.getBlockPos();
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = (Direction)blockState.getValue((Property)FinalVaultFrameBlock.FACING);
        matrixStack.pushPose();
        ShaderUtil.useShader(ShaderUtil.COLORIZE_SHADER, () -> {
            final Color color = new Color(-6646101);
            final int colorR = ShaderUtil.getUniformLocation(ShaderUtil.COLORIZE_SHADER, "colorR");
            final int colorG = ShaderUtil.getUniformLocation(ShaderUtil.COLORIZE_SHADER, "colorG");
            final int colorB = ShaderUtil.getUniformLocation(ShaderUtil.COLORIZE_SHADER, "colorB");
            final int brightness = ShaderUtil.getUniformLocation(ShaderUtil.COLORIZE_SHADER, "brightness");
            final int grayscaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.COLORIZE_SHADER, "grayscaleFactor");
            ARBShaderObjects.glUniform1fARB(colorR, color.getRed() / 255.0f);
            ARBShaderObjects.glUniform1fARB(colorG, color.getGreen() / 255.0f);
            ARBShaderObjects.glUniform1fARB(colorB, color.getBlue() / 255.0f);
            ARBShaderObjects.glUniform1fARB(brightness, LightmapUtil.getLightmapBrightness(combinedLight));
            ARBShaderObjects.glUniform1fARB(grayscaleFactor, ownerOnline ? 0.45f : 1.0f);
            return;
        });
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        final float headScale = 0.75f;
        matrixStack.scale(headScale, headScale, 1.0f);
        matrixStack.translate(0.0, -0.25, -0.2750000059604645);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        FinalVaultFrameRenderer.PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        FinalVaultFrameRenderer.PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        if (buffer instanceof IRenderTypeBuffer.Impl) {
            ((IRenderTypeBuffer.Impl)buffer).endBatch(renderType);
        }
        ShaderUtil.releaseShader();
        final boolean portalFormed = VaultPortalSize.getPortalSize((IWorld)world, blockPos.north(), Direction.Axis.Z, VaultPortalBlock.FRAME).isPresent() || VaultPortalSize.getPortalSize((IWorld)world, blockPos.south(), Direction.Axis.Z, VaultPortalBlock.FRAME).isPresent() || VaultPortalSize.getPortalSize((IWorld)world, blockPos.west(), Direction.Axis.X, VaultPortalBlock.FRAME).isPresent() || VaultPortalSize.getPortalSize((IWorld)world, blockPos.east(), Direction.Axis.X, VaultPortalBlock.FRAME).isPresent();
        if (portalFormed) {
            final long now = System.currentTimeMillis();
            final long prevTime = FinalVaultFrameRenderer.PARTICLE_SPAWN_TIMESTAMPS.computeIfAbsent(blockPos, p -> now);
            final long dt = now - prevTime;
            if (dt >= 300L && world.random.nextBoolean()) {
                addFlameParticle(world, blockPos, direction, 0.375f);
                addFlameParticle(world, blockPos, direction, -0.375f);
                FinalVaultFrameRenderer.PARTICLE_SPAWN_TIMESTAMPS.put(blockPos, now);
            }
        }
        matrixStack.popPose();
    }
    
    private static void addFlameParticle(final ClientWorld world, final BlockPos blockPos, final Direction direction, final float offset) {
        float x = blockPos.getX() + 0.5f + direction.getStepX() * 0.625f;
        final float y = blockPos.getY() + 0.8125f;
        float z = blockPos.getZ() + 0.5f + direction.getStepZ() * 0.625f;
        if (direction.getAxis() == Direction.Axis.Z) {
            x += offset;
        }
        if (direction.getAxis() == Direction.Axis.X) {
            z += offset;
        }
        final float xSpeed = 0.0f;
        final float ySpeed = 0.01f;
        final float zSpeed = 0.0f;
        world.addParticle((IParticleData)ParticleTypes.SOUL_FIRE_FLAME, (double)x, (double)y, (double)z, (double)xSpeed, (double)ySpeed, (double)zSpeed);
    }
    
    static {
        PLAYER_MODEL = new StatuePlayerModel<PlayerEntity>(0.1f, true);
        PARTICLE_SPAWN_TIMESTAMPS = new HashMap<BlockPos, Long>();
    }
}
