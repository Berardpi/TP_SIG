package geoexplorer.gui;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A point (mostly two coordinates, a color, a size, and a display shape).
 * @author Sylvain B.
 * @version 1.0
 */
public class Point implements GraphicalPrimitive {
    /**
     * A constant representing the cross shape symbol for displaying the point.
     */
    public static final int CROSS = 0;
    /**
     * A constant representing the square shape symbol for displaying the point.
     */
    public static final int SQUARE = 1;
    /**
     * A constant representing the circle shape symbol for displaying the point.
     */
    public static final int CIRCLE = 2;
    
    private int size;
    private int shape = CIRCLE;
    
    /**
     * The drawing color.
     */
    public Color drawColor;
    
    private double x;
    private double y;

    /**
     * Initializes a point with its coordinates and a default drawing color (strong gray).
     * @param x the x coordinate (in the map reference).
     * @param y the y coordinate (in the map reference).
     */
    public Point(double x, double y) {
        this(x, y, new Color(50, 50, 50, 255));
    }
    
    /**
     * Initializes a point with its coordinates and drawing color.
     * @param x the x coordinate (in the map reference).
     * @param y the y coordinate (in the map reference).
     * @param dc the drawing color.
     */
    public Point(double x, double y, Color dc) {
        this.x = x;
        this.y = y;
        this.size = 10;
        this.drawColor = dc;
    }
    
    /**
     * Sets the size of the symbol to display.
     * @param size the size (in pixels).
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the symbol to display (see the shape constants).
     * @param shape the symbol to display (cross, circle, square...).
     */
    public void setShape(int shape) {
        this.shape = shape;
    }
    
    @Override
    public void draw(Graphics2D g2d, CoordinateConverter converter) {
        int sx = converter.xMapToScreen(x);
        int sy = converter.yMapToScreen(y);
        
        Color oldDC = g2d.getColor();
        g2d.setColor(this.drawColor);
        switch (this.shape) {
            case CIRCLE:
                g2d.setColor(this.drawColor.brighter());
                g2d.fillOval(sx, sy, this.size, this.size);
                g2d.setColor(Color.WHITE);
                g2d.drawOval(sx, sy, this.size, this.size);
                break;
            case CROSS:
                g2d.drawLine(sx - this.size / 2, sy, sx + this.size / 2, sy);
                g2d.drawLine(sx, sy - this.size / 2, sx, sy + this.size / 2);
                break;
            case SQUARE:
                g2d.drawRect(sx - this.size / 2, sy - this.size / 2, this.size, this.size);                
        }
        g2d.setColor(oldDC);
    }
    
    /**
     * Gets the x coordinate of the point.
     * @return the x coordinate (in the map reference).
     */
    public double getX() {
        return this.x;        
    }
    
    /**
     * Gets the y coordinate of the point.
     * @return the y coordinate (in the map reference).
     */
    public double getY() {
        return this.y;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(this.getX(), this.getY(), this.getX(), this.getY());
    }
}
