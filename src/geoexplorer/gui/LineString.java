package geoexplorer.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a line string. Basically a sequence of points.
 * @author Sylvain B.
 * @version 1.0
 */
public class LineString implements GraphicalPrimitive, Iterable<Point> {
    /**
     * The drawing color of the line string.
     */
    public Color drawColor;

    private List<Point> points;
    
    /**
     * Builds and initializes a line string with no point at the beginning.
     * @param dc the drawing color.
     */
    public LineString(Color dc) {
        this.drawColor = dc;
        this.points = new LinkedList<>();
    }
    
    /**
     * Builds and initializes a line string with a default drawing color (light gray).
     */
    public LineString() {
        this(new Color(50, 50, 50, 255));
    }
    
    @Override
    public void draw(Graphics2D g2d, CoordinateConverter converter) {
        Color oldDC = g2d.getColor();
        g2d.setColor(this.drawColor);
        Iterator<Point> iter = this.points.iterator();
        Point p1 = iter.next();        
        while (iter.hasNext()) {
            Point p2 = iter.next();
            g2d.drawLine(converter.xMapToScreen(p1.getX()), converter.yMapToScreen(p1.getY()), converter.xMapToScreen(p2.getX()), converter.yMapToScreen(p2.getY()));
            p1 = p2;
        }
        g2d.setColor(oldDC);
    }

    /**
     * Adds a point at the end of the line string.
     * @param p the point to be added.
     */
    public void addPoint(Point p) {
        this.points.add(p);
    }
    
    /**
     * Gets an iterator over the points of the line string.
     * @return an iterator over the points.
     */
    @Override
    public Iterator<Point> iterator() {
        return this.points.iterator();
    }

    @Override
    public BoundingBox getBoundingBox() {        
        Iterator<Point> iter = this.points.iterator();
        Point p1 = iter.next();
        BoundingBox b = p1.getBoundingBox();
        while (iter.hasNext()) {
            b.extendBoundingBox(iter.next().getBoundingBox());
        }
        return b;
    }
}
