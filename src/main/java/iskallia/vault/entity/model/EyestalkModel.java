// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.model;

import net.minecraft.entity.Entity;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import com.google.common.collect.ImmutableList;
import iskallia.vault.entity.EyestalkEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;

public class EyestalkModel extends SegmentedModel<EyestalkEntity>
{
    private final ImmutableList<ModelRenderer> segments;
    private final ModelRenderer body;
    private final ModelRenderer tail;
    
    public EyestalkModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        final ImmutableList.Builder<ModelRenderer> segmentBuilder = ImmutableList.builder();
        (this.body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.body.texOffs(0, 0).addBox(-4.0f, 4.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.0f, false);
        (this.tail = new ModelRenderer(this)).setPos(0.0f, 11.5f, 1.0f);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.2618f, 0.0f, 0.0f);
        this.tail.texOffs(0, 16).addBox(-2.0f, -1.3775f, -2.0694f, 4.0f, 8.0f, 4.0f, 0.0f, false);
        segmentBuilder.add(this.tail);
        this.segments = segmentBuilder.build();
    }
    
    @Nonnull
    public Iterable<ModelRenderer> parts() {
        return this.segments;
    }
    
    public void setupAnim(@Nonnull final EyestalkEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch) {
        this.tail.xRot = 0.5f + 0.1f * MathHelper.sin(ageInTicks * 0.3f);
        this.body.yRot = netHeadYaw * 0.017453292f;
        this.body.xRot = headPitch * 0.017453292f;
    }
    
    public void renderToBuffer(@Nonnull final MatrixStack matrixStack, @Nonnull final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }
    
    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
