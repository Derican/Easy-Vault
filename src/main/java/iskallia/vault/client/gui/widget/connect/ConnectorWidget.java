package iskallia.vault.client.gui.widget.connect;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.util.VectorHelper;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Point2D;

public class ConnectorWidget extends Widget {
    private final ConnectorType type;
    private final Connector connector;
    private final ConnectableWidget source;
    private final ConnectableWidget target;
    private Color color;

    public ConnectorWidget(final ConnectableWidget source, final ConnectableWidget target, final ConnectorType type) {
        this(buildWidgetBox(source, target), source, target, type);
    }

    private ConnectorWidget(final Connector connector, final ConnectableWidget source, final ConnectableWidget target, final ConnectorType type) {
        super(connector.rct.x, connector.rct.y, connector.rct.width, connector.rct.height, new StringTextComponent("Connector"));
        this.color = new Color(11184810);
        this.type = type;
        this.connector = connector;
        this.source = source;
        this.target = target;
    }

    public ConnectorWidget setColor(final Color color) {
        this.color = color;
        return this;
    }

    private static Connector buildWidgetBox(final ConnectableWidget source, final ConnectableWidget target) {
        Point2D.Double from = source.getRenderPosition();
        Point2D.Double to = target.getRenderPosition();
        final Vector2f dir = new Vector2f((float) (to.x - from.x), (float) (to.y - from.y));
        final float angle = (float) Math.atan2(dir.x, dir.y);
        final double angleDeg = Math.toDegrees(angle) - 90.0;
        final Point2D.Double pointOnEdge;
        from = (pointOnEdge = source.getPointOnEdge(angleDeg));
        pointOnEdge.x += source.getRenderWidth() / 2.0;
        final Point2D.Double double1 = from;
        double1.y += source.getRenderHeight() / 2.0;
        final Vector2f fromV = new Vector2f((float) from.x, (float) from.y);
        final Point2D.Double pointOnEdge2;
        to = (pointOnEdge2 = target.getPointOnEdge(angleDeg - 180.0));
        pointOnEdge2.x += source.getRenderWidth() / 2.0;
        final Point2D.Double double2 = to;
        double2.y += source.getRenderHeight() / 2.0;
        final Vector2f toV = new Vector2f((float) to.x, (float) to.y);
        final Point2D.Double min = new Point2D.Double(Math.min(from.x, to.x), Math.min(from.y, to.y));
        final Point2D.Double max = new Point2D.Double(Math.max(from.x, to.x), Math.max(from.y, to.y));
        return new Connector(angleDeg, fromV, toV, new Rectangle(MathHelper.floor(min.x), MathHelper.floor(min.y), MathHelper.ceil(max.x), MathHelper.ceil(max.y)));
    }

    public void renderConnection(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks, final float viewportScale) {
        final int drawColor = this.color.getRGB();
        RenderSystem.disableTexture();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderSystem.lineWidth(6.0f * viewportScale);
        ScreenDrawHelper.draw(3, DefaultVertexFormats.POSITION_COLOR, buf -> {
            this.drawLine(buf, matrixStack, this.connector.from.x, this.connector.from.y, this.connector.to.x, this.connector.to.y, drawColor);
            if (this.type == ConnectorType.ARROW || this.type == ConnectorType.DOUBLE_ARROW) {
                final Vector2f arrowP1 = VectorHelper.rotateDegrees(this.connector.dir, 35.0f);
                this.drawLine(buf, matrixStack, -arrowP1.x * 10.0f + this.connector.to.x, -arrowP1.y * 10.0f + this.connector.to.y, this.connector.to.x, this.connector.to.y, drawColor);
                final Vector2f arrowP2 = VectorHelper.rotateDegrees(this.connector.dir, -35.0f);
                this.drawLine(buf, matrixStack, -arrowP2.x * 10.0f + this.connector.to.x, -arrowP2.y * 10.0f + this.connector.to.y, this.connector.to.x, this.connector.to.y, drawColor);
            }
            if (this.type == ConnectorType.DOUBLE_ARROW) {
                final Vector2f arrowP3 = VectorHelper.rotateDegrees(this.connector.dir, 35.0f);
                this.drawLine(buf, matrixStack, this.connector.from.x, this.connector.from.y, arrowP3.x * 10.0f + this.connector.from.x, arrowP3.y * 10.0f + this.connector.from.y, drawColor);
                final Vector2f arrowP4 = VectorHelper.rotateDegrees(this.connector.dir, -35.0f);
                this.drawLine(buf, matrixStack, this.connector.from.x, this.connector.from.y, arrowP4.x * 10.0f + this.connector.from.x, arrowP4.y * 10.0f + this.connector.from.y, drawColor);
            }
            return;
        });
        RenderSystem.lineWidth(2.0f);
        GL11.glDisable(2848);
        RenderSystem.enableTexture();
    }

    private void drawLine(final IVertexBuilder buf, final MatrixStack renderStack, final double lx, final double ly, final double hx, final double hy, final int color) {
        final Matrix4f offset = renderStack.last().pose();
        buf.vertex(offset, (float) lx, (float) ly, 0.0f).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).endVertex();
        buf.vertex(offset, (float) hx, (float) hy, 0.0f).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).endVertex();
    }

    private static class Connector {
        private final float angleDeg;
        private final Vector2f from;
        private final Vector2f to;
        private final Vector2f dir;
        private final Rectangle rct;

        private Connector(final double angleDeg, final Vector2f from, final Vector2f to, final Rectangle rectangle) {
            this.angleDeg = (float) angleDeg;
            this.from = from;
            this.to = to;
            this.rct = rectangle;
            this.dir = VectorHelper.normalize(new Vector2f(this.to.x - this.from.x, this.to.y - this.from.y));
        }
    }

    public enum ConnectorType {
        LINE,
        ARROW,
        DOUBLE_ARROW
    }
}
