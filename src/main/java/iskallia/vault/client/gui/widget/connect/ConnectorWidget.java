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
    private Color color = new Color(11184810);
    private final ConnectableWidget source;
    private final ConnectableWidget target;

    public ConnectorWidget(ConnectableWidget source, ConnectableWidget target, ConnectorType type) {
        this(buildWidgetBox(source, target), source, target, type);
    }

    private ConnectorWidget(Connector connector, ConnectableWidget source, ConnectableWidget target, ConnectorType type) {
        super(connector.rct.x, connector.rct.y, connector.rct.width, connector.rct.height, (ITextComponent) new StringTextComponent("Connector"));
        this.type = type;
        this.connector = connector;
        this.source = source;
        this.target = target;
    }

    public ConnectorWidget setColor(Color color) {
        this.color = color;
        return this;
    }

    private static Connector buildWidgetBox(ConnectableWidget source, ConnectableWidget target) {
        Point2D.Double from = source.getRenderPosition();
        Point2D.Double to = target.getRenderPosition();

        Vector2f dir = new Vector2f((float) (to.x - from.x), (float) (to.y - from.y));
        float angle = (float) Math.atan2(dir.x, dir.y);
        double angleDeg = Math.toDegrees(angle) - 90.0D;

        from = source.getPointOnEdge(angleDeg);
        from.x += source.getRenderWidth() / 2.0D;
        from.y += source.getRenderHeight() / 2.0D;
        Vector2f fromV = new Vector2f((float) from.x, (float) from.y);

        to = target.getPointOnEdge(angleDeg - 180.0D);
        to.x += source.getRenderWidth() / 2.0D;
        to.y += source.getRenderHeight() / 2.0D;
        Vector2f toV = new Vector2f((float) to.x, (float) to.y);

        Point2D.Double min = new Point2D.Double(Math.min(from.x, to.x), Math.min(from.y, to.y));
        Point2D.Double max = new Point2D.Double(Math.max(from.x, to.x), Math.max(from.y, to.y));

        return new Connector(angleDeg, fromV, toV, new Rectangle(MathHelper.floor(min.x), MathHelper.floor(min.y),
                MathHelper.ceil(max.x), MathHelper.ceil(max.y)));
    }

    public void renderConnection(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float viewportScale) {
        int drawColor = this.color.getRGB();

        RenderSystem.disableTexture();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderSystem.lineWidth(6.0F * viewportScale);

        ScreenDrawHelper.draw(3, DefaultVertexFormats.POSITION_COLOR, buf -> {
            drawLine((IVertexBuilder) buf, matrixStack, this.connector.from.x, this.connector.from.y, this.connector.to.x, this.connector.to.y, drawColor);


            if (this.type == ConnectorType.ARROW || this.type == ConnectorType.DOUBLE_ARROW) {
                Vector2f arrowP1 = VectorHelper.rotateDegrees(this.connector.dir, 35.0F);


                drawLine((IVertexBuilder) buf, matrixStack, (-arrowP1.x * 10.0F + this.connector.to.x), (-arrowP1.y * 10.0F + this.connector.to.y), this.connector.to.x, this.connector.to.y, drawColor);


                Vector2f arrowP2 = VectorHelper.rotateDegrees(this.connector.dir, -35.0F);


                drawLine((IVertexBuilder) buf, matrixStack, (-arrowP2.x * 10.0F + this.connector.to.x), (-arrowP2.y * 10.0F + this.connector.to.y), this.connector.to.x, this.connector.to.y, drawColor);
            }


            if (this.type == ConnectorType.DOUBLE_ARROW) {
                Vector2f arrowP1 = VectorHelper.rotateDegrees(this.connector.dir, 35.0F);

                drawLine((IVertexBuilder) buf, matrixStack, this.connector.from.x, this.connector.from.y, (arrowP1.x * 10.0F + this.connector.from.x), (arrowP1.y * 10.0F + this.connector.from.y), drawColor);

                Vector2f arrowP2 = VectorHelper.rotateDegrees(this.connector.dir, -35.0F);

                drawLine((IVertexBuilder) buf, matrixStack, this.connector.from.x, this.connector.from.y, (arrowP2.x * 10.0F + this.connector.from.x), (arrowP2.y * 10.0F + this.connector.from.y), drawColor);
            }
        });

        RenderSystem.lineWidth(2.0F);
        GL11.glDisable(2848);
        RenderSystem.enableTexture();
    }

    private void drawLine(IVertexBuilder buf, MatrixStack renderStack, double lx, double ly, double hx, double hy, int color) {
        Matrix4f offset = renderStack.last().pose();
        buf.vertex(offset, (float) lx, (float) ly, 0.0F)
                .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255)
                .endVertex();
        buf.vertex(offset, (float) hx, (float) hy, 0.0F)
                .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255)
                .endVertex();
    }

    private static class Connector {
        private final float angleDeg;
        private final Vector2f from;
        private final Vector2f to;
        private final Vector2f dir;
        private final Rectangle rct;

        private Connector(double angleDeg, Vector2f from, Vector2f to, Rectangle rectangle) {
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
        DOUBLE_ARROW;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\connect\ConnectorWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */