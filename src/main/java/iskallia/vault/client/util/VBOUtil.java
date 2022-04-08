package iskallia.vault.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VBOUtil {
    public static VertexBuffer batch(EntityModel<?> model, RenderType renderType, int packedLight, int packedOverlay) {
        BufferBuilder buf = Tessellator.getInstance().getBuilder();
        VertexBuffer vbo = new VertexBuffer(renderType.format());
        buf.begin(renderType.mode(), renderType.format());
        model.renderToBuffer(new MatrixStack(), (IVertexBuilder) buf, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        buf.end();
        vbo.upload(buf);
        return vbo;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\VBOUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */