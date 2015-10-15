package geoexplorer.gui;

/**
 * A converter from map coordinates to screen coordinates. The maps coordinates are real values, whereas the screen coordinates are
 * integer values (pixels). By default, the y axis is oriented from bottom to top for the map coordinate, the other way around
 * for the screen coordinates. The top left pixel of the screen has coordinates (0, 0).
 * @author Sylvain B.
 * @version 1.0
 */
public class CoordinateConverter {
    private int screenWidth, screenHeight;
    private double mapWidth, mapHeight, xLeft, yBottom;
    
    /**
     * The default constructor of the coordinate convertor.
     * @param screenWidth the screen width
     * @param screenHeight the screen height
     * @param centerX the x coordinate of the map center.
     * @param centerY the y coordinate of the map center.
     * @param mapWidth the map width (the map height is fixed to mapWidth × screenHeight / screenWidth, provided that
     * we do not want the map to be deformed by the transformation).
     */
    public CoordinateConverter(int screenWidth, int screenHeight, double centerX, double centerY, double mapWidth) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.xLeft = centerX - mapWidth / 2;
        this.mapWidth = mapWidth;
        this.mapHeight = screenHeight * mapWidth / screenWidth;                
        this.yBottom = centerY - mapHeight / 2;
    }
    
    /**
     * Converts an x coordinate from the map reference to the screen reference
     * @param x the x coordinate in the map reference to convert.
     * @return  the coordinate converted to the screen reference.
     */
    public int xMapToScreen(double x) {
        return (int) ((x - this.xLeft) * this.screenWidth / this.mapWidth);
    }

    /**
     * Converts a y coordinate from the map reference to the screen reference
     * @param y the y coordinate in the map reference to convert.
     * @return  the coordinate converted to the screen reference.
     */
    public int yMapToScreen(double y) {
        return this.screenHeight - ((int) ((y - this.yBottom) * this.screenHeight / this.mapHeight));
    }

     /**
     * Converts an x coordinate from the screen reference to the map reference
     * @param x the x coordinate in the screen reference to convert.
     * @return  the coordinate converted to the map reference.
     */
   public double xScreenToMap(int x) {
        return this.xLeft + x * mapWidth / screenWidth;
    }
    
    /**
     * Converts a y coordinate from the screen reference to the map reference
     * @param y the y coordinate in the screen reference to convert.
     * @return  the coordinate converted to the map reference.
     */
    public double yScreenToMap(int y) {
        return this.yBottom + (screenHeight - y) * mapHeight / screenHeight;
    }
    
    /**
     * Converts an x distance from the screen reference to the map reference (the only difference with
     * xScreenToMap is that it does not take into account the origin of the map reference.
     * @param dx the distance to convert (in the screen reference)
     * @return  the distance converted to the map reference.
     */
    public double dxScreenToMap(int dx) {
        return (dx * this.mapWidth / this.screenWidth);
    }

    /**
     * Converts a y distance from the screen reference to the map reference (the only difference with
     * yScreenToMap is that it does not take into account the origin of the map reference.
     * @param dy the distance to convert (in the screen reference)
     * @return  the distance converted to the map reference.
     */
    public double dyScreenToMap(int dy) {
        return (dy * this.mapHeight / this.screenHeight);
    }

}
