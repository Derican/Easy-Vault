package iskallia.vault.client.gui.widget.connect;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface ConnectableWidget {
    Point2D.Double getRenderPosition();

    double getRenderWidth();

    double getRenderHeight();

    default Rectangle2D.Double getRenderBox() {
        return new Rectangle2D.Double((getRenderPosition()).x, (getRenderPosition()).y,
                getRenderWidth(), getRenderHeight());
    }

    default Point2D.Double getPointOnEdge(double angleDeg) {
        double twoPI = 6.283185307179586D;
        double theta = angleDeg * Math.PI / 180.0D;

        while (theta < -3.141592653589793D) {
            theta += twoPI;
        }
        while (theta > Math.PI) {
            theta -= twoPI;
        }

        double width = getRenderWidth();
        double height = getRenderHeight();
        double rectAtan = Math.atan2(height, width);
        double tanTheta = Math.tan(theta);

        double xFactor = 1.0D;
        double yFactor = 1.0D;

        boolean horizontal = false;
        if (theta > -rectAtan && theta <= rectAtan) {
            horizontal = true;
            yFactor = -1.0D;
        } else if (theta > rectAtan && theta <= Math.PI - rectAtan) {
            yFactor = -1.0D;
        } else if (theta > Math.PI - rectAtan || theta <= -(Math.PI - rectAtan)) {
            horizontal = true;
            xFactor = -1.0D;
        } else {
            xFactor = -1.0D;
        }

        Point2D.Double pos = getRenderPosition();
        if (horizontal) {
            return new Point2D.Double(pos.x + xFactor * width / 2.0D, pos.y + yFactor * width / 2.0D * tanTheta);
        }


        return new Point2D.Double(pos.x + xFactor * height / 2.0D * tanTheta, pos.y + yFactor * height / 2.0D);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\connect\ConnectableWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */