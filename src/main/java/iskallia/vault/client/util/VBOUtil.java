package iskallia.vault.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VBOUtil {
    public static VertexBuffer batch(final EntityModel<?> model, final RenderType renderType, final int packedLight, final int packedOverlay) {
        final BufferBuilder buf = Tessellator.getInstance().getBuilder();
        final VertexBuffer vbo = new VertexBuffer(renderType.format());
        buf.begin(renderType.mode(), renderType.format());
        model.renderToBuffer(new MatrixStack(), buf, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        buf.end();
        vbo.upload(buf);
        return vbo;
    }
}
