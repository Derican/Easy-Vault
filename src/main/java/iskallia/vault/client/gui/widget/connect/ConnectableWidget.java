// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.widget.connect;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface ConnectableWidget
{
    Point2D.Double getRenderPosition();
    
    double getRenderWidth();
    
    double getRenderHeight();
    
    default Rectangle2D.Double getRenderBox() {
        return new Rectangle2D.Double(this.getRenderPosition().x, this.getRenderPosition().y, this.getRenderWidth(), this.getRenderHeight());
    }
    
    default Point2D.Double getPointOnEdge(final double angleDeg) {
        double twoPI;
        double theta;
        for (twoPI = 6.283185307179586, theta = angleDeg * 3.141592653589793 / 180.0; theta < -3.141592653589793; theta += twoPI) {}
        while (theta > 3.141592653589793) {
            theta -= twoPI;
        }
        final double width = this.getRenderWidth();
        final double height = this.getRenderHeight();
        final double rectAtan = Math.atan2(height, width);
        final double tanTheta = Math.tan(theta);
        double xFactor = 1.0;
        double yFactor = 1.0;
        boolean horizontal = false;
        if (theta > -rectAtan && theta <= rectAtan) {
            horizontal = true;
            yFactor = -1.0;
        }
        else if (theta > rectAtan && theta <= 3.141592653589793 - rectAtan) {
            yFactor = -1.0;
        }
        else if (theta > 3.141592653589793 - rectAtan || theta <= -(3.141592653589793 - rectAtan)) {
            horizontal = true;
            xFactor = -1.0;
        }
        else {
            xFactor = -1.0;
        }
        final Point2D.Double pos = this.getRenderPosition();
        if (horizontal) {
            return new Point2D.Double(pos.x + xFactor * (width / 2.0), pos.y + yFactor * (width / 2.0) * tanTheta);
        }
        return new Point2D.Double(pos.x + xFactor * (height / (2.0 * tanTheta)), pos.y + yFactor * (height / 2.0));
    }
}
