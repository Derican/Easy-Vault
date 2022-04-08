package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.vector.Matrix4f;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;


public class ScreenDrawHelper {
    public static void drawQuad(Consumer<BufferBuilder> fn) {
        draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, fn);
    }

    public static void draw(int drawMode, VertexFormat format, Consumer<BufferBuilder> fn) {
        draw(drawMode, format, bufferBuilder -> {
            fn.accept(bufferBuilder);
            return null;
        });
    }

    public static <R> R draw(int drawMode, VertexFormat format, Function<BufferBuilder, R> fn) {
        BufferBuilder buf = Tessellator.getInstance().getBuilder();
        buf.begin(drawMode, format);
        R result = fn.apply(buf);
        buf.end();
        WorldVertexBufferUploader.end(buf);
        return result;
    }

    public static QuadBuilder rect(IVertexBuilder buf, MatrixStack renderStack) {
        return new QuadBuilder(buf, renderStack);
    }

    public static QuadBuilder rect(IVertexBuilder buf, MatrixStack renderStack, float width, float height) {
        return rect(buf, renderStack, 0.0F, 0.0F, 0.0F, width, height);
    }

    public static QuadBuilder rect(IVertexBuilder buf, MatrixStack renderStack, float offsetX, float offsetY, float offsetZ, float width, float height) {
        return new QuadBuilder(buf, renderStack, offsetX, offsetY, offsetZ, width, height);
    }


    public static class QuadBuilder {
        private final IVertexBuilder buf;
        private final MatrixStack renderStack;
        private float offsetX;
        private float u = 0.0F;
        private float offsetY;
        private float offsetZ;
        private float width;
        private float height;
        private float v = 0.0F;
        private float uWidth = 1.0F;
        private float vWidth = 1.0F;
        private Color color = Color.WHITE;

        private QuadBuilder(IVertexBuilder buf, MatrixStack renderStack) {
            this.buf = buf;
            this.renderStack = renderStack;
        }

        private QuadBuilder(IVertexBuilder buf, MatrixStack renderStack, float offsetX, float offsetY, float offsetZ, float width, float height) {
            this.buf = buf;
            this.renderStack = renderStack;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.width = width;
            this.height = height;
        }

        public QuadBuilder at(float offsetX, float offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }

        public QuadBuilder zLevel(float offsetZ) {
            this.offsetZ = offsetZ;
            return this;
        }

        public QuadBuilder dim(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public QuadBuilder tex(TextureAtlasSprite tas) {
            return tex(tas.getU0(), tas.getV0(), tas.getU1() - tas.getU0(), tas.getV1() - tas.getV0());
        }

        public QuadBuilder texVanilla(float pxU, float pxV, float pxWidth, float pxHeight) {
            return texTexturePart(pxU, pxV, pxWidth, pxHeight, 256.0F, 256.0F);
        }

        public QuadBuilder texTexturePart(float pxU, float pxV, float pxWidth, float pxHeight, float texPxWidth, float texPxHeight) {
            return tex(pxU / texPxWidth, pxV / texPxHeight, pxWidth / texPxWidth, pxHeight / texPxHeight);
        }

        public QuadBuilder tex(float u, float v, float uWidth, float vWidth) {
            this.u = u;
            this.v = v;
            this.uWidth = uWidth;
            this.vWidth = vWidth;
            return this;
        }

        public QuadBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public QuadBuilder color(int color) {
            return color(new Color(color, true));
        }

        public QuadBuilder color(int r, int g, int b, int a) {
            return color(new Color(r, g, b, a));
        }

        public QuadBuilder color(float r, float g, float b, float a) {
            return color(new Color(r, g, b, a));
        }

        public QuadBuilder draw() {
            int r = this.color.getRed();
            int g = this.color.getGreen();
            int b = this.color.getBlue();
            int a = this.color.getAlpha();
            Matrix4f offset = this.renderStack.last().pose();
            this.buf.vertex(offset, this.offsetX, this.offsetY + this.height, this.offsetZ).color(r, g, b, a).uv(this.u, this.v + this.vWidth).endVertex();
            this.buf.vertex(offset, this.offsetX + this.width, this.offsetY + this.height, this.offsetZ).color(r, g, b, a).uv(this.u + this.uWidth, this.v + this.vWidth).endVertex();
            this.buf.vertex(offset, this.offsetX + this.width, this.offsetY, this.offsetZ).color(r, g, b, a).uv(this.u + this.uWidth, this.v).endVertex();
            this.buf.vertex(offset, this.offsetX, this.offsetY, this.offsetZ).color(r, g, b, a).uv(this.u, this.v).endVertex();
            return this;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\ScreenDrawHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */