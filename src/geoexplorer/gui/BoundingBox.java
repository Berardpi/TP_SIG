package geoexplorer.gui;

/**
 * This class represents a geometrical bounding box.
 * @author Sylvain B.
 * @version 1.0
 */
public class BoundingBox {
    /**
     * the leftmost coordinate.
     */
    public double xMin;
    /**
     * the rightmost coordinate.
     */
    public double xMax;
    /**
     * the bottommost coordinate.
     */
    public double yMin;
    /**
     * the topmost coordinate.
     */
    public double yMax;
    
    /**
     * Initializes a bounding box with the bottom left and top right coordinates
     * @param xMin leftmost coordinate
     * @param yMin bottommost coordinate
     * @param xMax rightmost coordinate
     * @param yMax topmost coordinate
     */
    public BoundingBox(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }
    
    /**
     * Initializes a bounding box by setting all coordinates to zero.
     */
    public BoundingBox() {
        this(0, 0, 0, 0);
    }

    /**
     * Extends the current bounding box to the minimal bounding box including both this bounding box and the bounding box in parameter.
     * @param b the bounding box to include.
     */
    public void extendBoundingBox(BoundingBox b) {
        this.xMin = b.xMin < this.xMin ? b.xMin : this.xMin;
        this.yMin = b.yMin < this.yMin ? b.yMin : this.yMin;
        this.xMax = b.xMax > this.xMax ? b.xMax : this.xMax;
        this.yMax = b.yMax > this.yMax ? b.yMax : this.yMax;
    }
    
    /**
     * Gets the width of the bounding box.
     * @return the width of the bounding box (xMax - xMin)
     */
    public double getWidth() {
        return this.xMax - this.xMin;
    }
    
    /**
     * Gets the height of the bounding box.
     * @return the height of the bounding box (yMax - yMin)
     */
    public double getHeight() {
        return this.yMax - this.yMin;
    }
    
    @Override
    public String toString() {
        return "BBOX((" + this.xMin + ", " + this.yMin + "), (" + this.xMax + ", " + this.yMax + "))";
    }
}
