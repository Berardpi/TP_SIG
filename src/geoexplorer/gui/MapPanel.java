/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geoexplorer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * The map panel is the main graphical component (a JPanel), on which the map will be displayed.
 * It manages graphical primitives, whose coordinates are expressed in the map reference. It uses
 * a single coordinate converter to convert all those coordinates into the screen reference.
 * @author Sylvain B.
 * @version 1.0
 */
public class MapPanel extends JPanel {
    /**
     * The background color of the map.
     */
    public Color bgColor = new Color(220, 220, 220);
    /**
     * The default foreground color of the map (mostly unused, since all the graphical primitives have their own drawing color).
     */
    public Color fgColor = new Color(80, 80, 80);
    
    private List<GraphicalPrimitive> primitives;
    private CoordinateConverter converter;
    private double centerX, centerY, mapWidth;

    // To manage sliding
    private int translateX = 0;
    private int translateY = 0;
    
    // mouse coordinates
    private double xCursor = 0;
    private double yCursor = 0;
    
    /**
     * Initializes a map panel.
     * @param centerX the x center of the map (in map coordinates)
     * @param centerY the y center of the map (in map coordinates)
     * @param mapWidth the map width (in map coordinates)
     */
    public MapPanel(double centerX, double centerY, double mapWidth) {
        this.primitives = new LinkedList<>();
        this.centerX = centerX;
        this.centerY = centerY;
        this.mapWidth = mapWidth;
        MouseController m = new MouseController();
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
        this.addMouseMotionListener(m);
    }
    
    @Override public void paintComponent(Graphics g) {
        this.converter = new CoordinateConverter(this.getWidth(), this.getHeight(), this.centerX, this.centerY, this.mapWidth);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setBackground(this.bgColor);
        g2d.setColor(this.fgColor);
        //    styleSheet("default")
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        g2d.translate(this.translateX, this.translateY);
        this.drawPrimitives(g2d);

        g2d.translate(-this.translateX, -this.translateY);       
        this.drawInfoBar(g2d);
        
        g2d.dispose();
    }
    
    private void drawInfoBar(Graphics2D g) {
       Graphics2D g2d = (Graphics2D) g.create();
       g2d.setColor(new Color(255, 255, 255, 80));
       g2d.fillRect(0, this.getHeight() - 20, this.getWidth(), 20);
       g2d.setColor(new Color(255, 255, 255, 120));
       g2d.drawLine(0, this.getHeight() - 20, this.getWidth(), this.getHeight() - 20);
       g2d.setColor(new Color(50, 50, 50));
       g2d.drawString("Coordonnées curseur : (" + this.xCursor + ", " + this.yCursor + ")", 5, this.getHeight() - 5);
       g2d.dispose();
    }
    
    /**
     * Clears all the graphical primitives and repaints.
     */
    public void reset() {
        this.primitives.clear();
        this.repaint();
    }
    
    /**
     * Adds a graphical primitive to the list of objects to be displayed.
     * @param p the primitive to be added.
     */
    public void addPrimitive(GraphicalPrimitive p) {
        this.primitives.add(p);
    }
    
    private void drawPrimitives(Graphics2D g2d) {
        for (GraphicalPrimitive p: this.primitives) {
            p.draw(g2d, this.converter);
        }
    }
    
    /**
     * Automatically adjusts the center and the width of the map to fit the graphical elements
     * displayed.
     */
    public void autoAdjust() {
        Logger.getLogger(GeoMainFrame.class.getName()).log(Level.INFO, "Auto-adjusting panel...");
        if (this.primitives.isEmpty()) return;
        Iterator<GraphicalPrimitive> iter = this.primitives.iterator();
        BoundingBox b = iter.next().getBoundingBox();
        while(iter.hasNext()) {
            b.extendBoundingBox(iter.next().getBoundingBox());
        }
        this.centerX = (b.xMin + b.xMax) / 2;
        this.centerY = (b.yMin + b.yMax) / 2;
        if (this.getWidth() < this.getHeight() * b.getWidth() / b.getHeight()) { // needs to use width as the main adjustment variable
            this.mapWidth = b.xMax - b.xMin;
        } else {
            this.mapWidth = (b.yMax - b.yMin) * this.getWidth() / this.getHeight();
        }
        this.repaint();
    }
   
    
    private class MouseController extends MouseAdapter {    
        private int initX, initY;
        private boolean panning = false;
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double newMapWidth = Math.pow(2, e.getWheelRotation()) * MapPanel.this.mapWidth;
            int xClicked = e.getX();
            int yClicked = e.getY();
            double mapXClicked = converter.xScreenToMap(xClicked);
            double mapYClicked = converter.yScreenToMap(yClicked);
            
            MapPanel.this.centerX -= ((new CoordinateConverter(MapPanel.this.getWidth(), MapPanel.this.getHeight(), MapPanel.this.centerX, MapPanel.this.centerY, newMapWidth).xScreenToMap(xClicked)) - mapXClicked);
            MapPanel.this.centerY -= ((new CoordinateConverter(MapPanel.this.getWidth(), MapPanel.this.getHeight(), MapPanel.this.centerX, MapPanel.this.centerY, newMapWidth).yScreenToMap(yClicked)) - mapYClicked);
            
            MapPanel.this.mapWidth = newMapWidth;
            MapPanel.this.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.initX = e.getX();
            this.initY = e.getY();
            this.panning = true;
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            MapPanel.this.centerX -= MapPanel.this.converter.dxScreenToMap(MapPanel.this.translateX);
            MapPanel.this.centerY += MapPanel.this.converter.dyScreenToMap(MapPanel.this.translateY);
                        
            MapPanel.this.translateX = 0;
            MapPanel.this.translateY = 0;
            this.panning = false;
            MapPanel.this.repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (this.panning) {
                MapPanel.this.translateX = e.getX() - this.initX;
                MapPanel.this.translateY = e.getY() - this.initY;
                MapPanel.this.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            MapPanel.this.xCursor = MapPanel.this.converter.xScreenToMap(e.getX());
            MapPanel.this.yCursor = MapPanel.this.converter.yScreenToMap(e.getY());
            MapPanel.this.repaint();
        }
        
    }
}
