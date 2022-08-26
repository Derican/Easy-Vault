package iskallia.vault.block.render;

import java.util.ArrayList;
import iskallia.vault.Vault;
import net.minecraft.tileentity.TileEntity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import iskallia.vault.client.util.RenderTypeDecorator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.block.entity.HourglassTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class HourglassRenderer extends TileEntityRenderer<HourglassTileEntity>
{
    private static final ResourceLocation SAND_TEXTURE;
    private static List<AxisAlignedBB> SAND_BOXES;
    private static final int totalHeight;
    
    public HourglassRenderer(final TileEntityRendererDispatcher terd) {
        super(terd);
    }
    
    public void render(final HourglassTileEntity te, final float partialTicks, final MatrixStack renderStack, final IRenderTypeBuffer buffers, final int combinedLight, final int combinedOverlay) {
        final ModelRenderer sandBoxes = this.prepareSandRender(te.getFilledPercentage());
        final RenderType wrapped = RenderTypeDecorator.decorate(RenderType.solid(), () -> Minecraft.getInstance().getTextureManager().bind(HourglassRenderer.SAND_TEXTURE), () -> Minecraft.getInstance().getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS));
        renderStack.pushPose();
        renderStack.translate(0.01, 0.125, 0.01);
        renderStack.scale(0.98f, 1.0f, 0.98f);
        final IVertexBuilder vb = buffers.getBuffer(wrapped);
        sandBoxes.render(renderStack, vb, combinedLight, combinedOverlay);
        renderStack.popPose();
        buffers.getBuffer(RenderType.lines());
    }
    
    private ModelRenderer prepareSandRender(final float percentage) {
        float heightPart = HourglassRenderer.totalHeight * MathHelper.clamp(percentage, 0.0f, 1.0f);
        final ModelRenderer renderer = new ModelRenderer(16, 16, 0, 0);
        for (final AxisAlignedBB box : HourglassRenderer.SAND_BOXES) {
            final float ySize = (float)box.getYsize();
            final float remainingHeight = heightPart - ySize;
            if (remainingHeight < 0.0f) {
                final float part = heightPart / ySize;
                renderer.addBox((float)box.minX, (float)box.minY, (float)box.minZ, (float)box.getXsize(), (float)box.getYsize() * part, (float)box.getZsize());
                break;
            }
            renderer.addBox((float)box.minX, (float)box.minY, (float)box.minZ, (float)box.getXsize(), (float)box.getYsize(), (float)box.getZsize());
            heightPart -= (float)box.getYsize();
        }
        return renderer;
    }
    
    private static void shiftY(final float y) {
        HourglassRenderer.SAND_BOXES = HourglassRenderer.SAND_BOXES.stream().map(box -> box.move(0.0, (double)y, 0.0)).collect(Collectors.toList());
    }
    
    private static AxisAlignedBB makeBox(final double x, final double y, final double z, final double width, final double height, final double depth) {
        return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
    }
    
    static {
        SAND_TEXTURE = Vault.id("textures/block/hourglass_sand.png");
        (HourglassRenderer.SAND_BOXES = new ArrayList<AxisAlignedBB>()).add(makeBox(2.0, 0.0, 2.0, 12.0, 7.0, 12.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(3.0, 7.0, 3.0, 10.0, 3.0, 10.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(4.0, 10.0, 4.0, 8.0, 2.0, 8.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(5.0, 12.0, 5.0, 6.0, 1.0, 6.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(6.0, 13.0, 6.0, 4.0, 1.0, 4.0));
        shiftY(-0.02f);
        HourglassRenderer.SAND_BOXES.add(makeBox(6.0, 14.0, 6.0, 4.0, 1.0, 4.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(5.0, 15.0, 5.0, 6.0, 1.0, 6.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(4.0, 16.0, 4.0, 8.0, 2.0, 8.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(3.0, 18.0, 3.0, 10.0, 3.0, 10.0));
        HourglassRenderer.SAND_BOXES.add(makeBox(2.0, 21.0, 2.0, 12.0, 7.0, 12.0));
        shiftY(0.01f);
        totalHeight = 28;
    }
}
