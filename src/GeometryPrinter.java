
import geoexplorer.gui.GraphicalPrimitive;
import geoexplorer.gui.MapPanel;
import java.awt.Color;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiLineString;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alexandre
 */
public class GeometryPrinter {
    public static void print(PGgeometry geom, MapPanel map, Color color, int lineStrength){
        print(geom, map, color, color, lineStrength);
    }
    
    public static void print(PGgeometry geom, MapPanel map, Color color){
        print(geom, map, color, color, 1);
    }
    
    public static void print(PGgeometry geom, MapPanel map, Color drawColor, Color fillColor){
        print(geom, map, drawColor, fillColor, 1);
    }
    
    
    public static void print(PGgeometry geom, MapPanel map, Color drawColor, Color fillColor, int lineStrength){
        switch(geom.getGeoType()){
            case(Geometry.LINESTRING):
                printLinestring(geom, map, drawColor, lineStrength);
                break;
            case(Geometry.MULTILINESTRING):
                printMultiLinestring(geom, map, drawColor, lineStrength);
                break;
            case(Geometry.POLYGON):
                printPolygon(geom, map, drawColor, fillColor); 
                break;
            case(Geometry.MULTIPOLYGON):
                printMultiPolygon(geom, map, drawColor, fillColor); 
                break;
            default:
                System.err.println("Print not existing or not yet implemented for this Geometry.("+geom.getGeoType()+")");
        }
    }
    
    private static void printPolygon(PGgeometry geom, MapPanel map , Color drawColor, Color fillColor){
        // Extract PostGis LineString and create GeoExplorer Linestring from it
        geoexplorer.gui.Polygon polygonGE = new geoexplorer.gui.Polygon(drawColor, fillColor);
        Polygon polygonPG = (Polygon)geom.getGeometry();
        for(int i = 0; i < polygonPG.numRings(); ++i) { 
            LinearRing ring = polygonPG.getRing(i);
            for(Point p : ring.getPoints()) { 
                polygonGE.addPoint(new geoexplorer.gui.Point(p.getX(), p.getY()));
            }
            map.addPrimitive(polygonGE);
        } 
    }
    
    private static void printMultiPolygon(PGgeometry geom, MapPanel map , Color drawColor, Color fillColor){
        geoexplorer.gui.Polygon polygonGE = new geoexplorer.gui.Polygon(drawColor, fillColor);
            	 
        MultiPolygon multiPolygonePG = (MultiPolygon)geom.getGeometry();
        Polygon[] polygons = multiPolygonePG.getPolygons();
        for( int i = 0; i < multiPolygonePG.numPolygons(); i++) { 
            for( int p = 0; p < polygons[i].numPoints(); p++ ) { 
                Point pt = polygons[i].getPoint(p); 
                polygonGE.addPoint(new geoexplorer.gui.Point(pt.getX(), pt.getY()));
            }     
            // Print the GeoExplorer line : 
            map.addPrimitive(polygonGE);
        } 
    }

    private static void printLinestring(PGgeometry geom, MapPanel map, Color drawColor, int lineStrength) {
        // Extract PostGis LineString and create GeoExplorer Linestring from it
        geoexplorer.gui.LineString lineGE = new geoexplorer.gui.LineString(drawColor, lineStrength);
        LineString linePG = (LineString)geom.getGeometry();
             
        for(Point p : linePG.getPoints()){
            lineGE.addPoint(new geoexplorer.gui.Point(p.getX(), p.getY()));
        } 
    
        // Print the GeoExplorer line : 
        map.addPrimitive(lineGE);    
    }
    
    private static void printMultiLinestring(PGgeometry geom, MapPanel map, Color drawColor, int lineStrength) {
        MultiLineString multiLinePG = (MultiLineString)geom.getGeometry();
        for(int i = 0; i < multiLinePG.numLines(); ++i){
            printLinestring(new PGgeometry(multiLinePG.getLine(i)), map, drawColor, lineStrength);
        } 
    }
}
