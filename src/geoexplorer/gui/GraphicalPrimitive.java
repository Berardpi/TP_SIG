
package geoexplorer.gui;

import java.awt.Graphics2D;

/**
 * Represents any graphical primitive which can be displayed in the map panel. The coordinates here are
 * expressed in the map reference. They are only converted at the time of drawing (see method draw).
 * @author Sylvain B.
 * @version 1.0
 */
public interface GraphicalPrimitive {
    /**
     * The function that draws the primitive
     * @param g2d the Graphics2D object on which the shape must be drawn.
     * @param converter the coordinate converter used to convert the primitive's coordinates into the screen (hence Graphics2D) coordinates.
     */
    public void draw(Graphics2D g2d, CoordinateConverter converter);
    /**
     * Computes the bounding box of the graphical primitive.
     * @return the bounding box of the primitive.
     */
    public BoundingBox getBoundingBox();
}
