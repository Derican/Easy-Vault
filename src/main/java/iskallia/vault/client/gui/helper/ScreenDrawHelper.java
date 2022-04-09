// 
// Decompiled by Procyon v0.6.0
// 

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

public class ScreenDrawHelper
{
    public static void drawQuad(final Consumer<BufferBuilder> fn) {
        draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, fn);
    }
    
    public static void draw(final int drawMode, final VertexFormat format, final Consumer<BufferBuilder> fn) {
        draw(drawMode, format, bufferBuilder -> {
            fn.accept(bufferBuilder);
            return null;
        });
    }
    
    public static <R> R draw(final int drawMode, final VertexFormat format, final Function<BufferBuilder, R> fn) {
        final BufferBuilder buf = Tessellator.getInstance().getBuilder();
        buf.begin(drawMode, format);
        final R result = fn.apply(buf);
        buf.end();
        WorldVertexBufferUploader.end(buf);
        return result;
    }
    
    public static QuadBuilder rect(final IVertexBuilder buf, final MatrixStack renderStack) {
        return new QuadBuilder(buf, renderStack);
    }
    
    public static QuadBuilder rect(final IVertexBuilder buf, final MatrixStack renderStack, final float width, final float height) {
        return rect(buf, renderStack, 0.0f, 0.0f, 0.0f, width, height);
    }
    
    public static QuadBuilder rect(final IVertexBuilder buf, final MatrixStack renderStack, final float offsetX, final float offsetY, final float offsetZ, final float width, final float height) {
        return new QuadBuilder(buf, renderStack, offsetX, offsetY, offsetZ, width, height);
    }
    
    public static class QuadBuilder
    {
        private final IVertexBuilder buf;
        private final MatrixStack renderStack;
        private float offsetX;
        private float offsetY;
        private float offsetZ;
        private float width;
        private float height;
        private float u;
        private float v;
        private float uWidth;
        private float vWidth;
        private Color color;
        
        private QuadBuilder(final IVertexBuilder buf, final MatrixStack renderStack) {
            this.u = 0.0f;
            this.v = 0.0f;
            this.uWidth = 1.0f;
            this.vWidth = 1.0f;
            this.color = Color.WHITE;
            this.buf = buf;
            this.renderStack = renderStack;
        }
        
        private QuadBuilder(final IVertexBuilder buf, final MatrixStack renderStack, final float offsetX, final float offsetY, final float offsetZ, final float width, final float height) {
            this.u = 0.0f;
            this.v = 0.0f;
            this.uWidth = 1.0f;
            this.vWidth = 1.0f;
            this.color = Color.WHITE;
            this.buf = buf;
            this.renderStack = renderStack;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.width = width;
            this.height = height;
        }
        
        public QuadBuilder at(final float offsetX, final float offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }
        
        public QuadBuilder zLevel(final float offsetZ) {
            this.offsetZ = offsetZ;
            return this;
        }
        
        public QuadBuilder dim(final float width, final float height) {
            this.width = width;
            this.height = height;
            return this;
        }
        
        public QuadBuilder tex(final TextureAtlasSprite tas) {
            return this.tex(tas.getU0(), tas.getV0(), tas.getU1() - tas.getU0(), tas.getV1() - tas.getV0());
        }
        
        public QuadBuilder texVanilla(final float pxU, final float pxV, final float pxWidth, final float pxHeight) {
            return this.texTexturePart(pxU, pxV, pxWidth, pxHeight, 256.0f, 256.0f);
        }
        
        public QuadBuilder texTexturePart(final float pxU, final float pxV, final float pxWidth, final float pxHeight, final float texPxWidth, final float texPxHeight) {
            return this.tex(pxU / texPxWidth, pxV / texPxHeight, pxWidth / texPxWidth, pxHeight / texPxHeight);
        }
        
        public QuadBuilder tex(final float u, final float v, final float uWidth, final float vWidth) {
            this.u = u;
            this.v = v;
            this.uWidth = uWidth;
            this.vWidth = vWidth;
            return this;
        }
        
        public QuadBuilder color(final Color color) {
            this.color = color;
            return this;
        }
        
        public QuadBuilder color(final int color) {
            return this.color(new Color(color, true));
        }
        
        public QuadBuilder color(final int r, final int g, final int b, final int a) {
            return this.color(new Color(r, g, b, a));
        }
        
        public QuadBuilder color(final float r, final float g, final float b, final float a) {
            return this.color(new Color(r, g, b, a));
        }
        
        public QuadBuilder draw() {
            final int r = this.color.getRed();
            final int g = this.color.getGreen();
            final int b = this.color.getBlue();
            final int a = this.color.getAlpha();
            final Matrix4f offset = this.renderStack.last().pose();
            this.buf.vertex(offset, this.offsetX, this.offsetY + this.height, this.offsetZ).color(r, g, b, a).uv(this.u, this.v + this.vWidth).endVertex();
            this.buf.vertex(offset, this.offsetX + this.width, this.offsetY + this.height, this.offsetZ).color(r, g, b, a).uv(this.u + this.uWidth, this.v + this.vWidth).endVertex();
            this.buf.vertex(offset, this.offsetX + this.width, this.offsetY, this.offsetZ).color(r, g, b, a).uv(this.u + this.uWidth, this.v).endVertex();
            this.buf.vertex(offset, this.offsetX, this.offsetY, this.offsetZ).color(r, g, b, a).uv(this.u, this.v).endVertex();
            return this;
        }
    }
}
