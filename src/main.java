
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Utils;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;

public class main {

    public static void main(String[] args) throws SQLException {
        System.out.println("Question 9:");
        //question9("Dom__ne _niversit%");
        System.out.println("Question 10:");
        question10a();
    }

    public static void question8() {
        Utils.getConnection();
        Utils.closeConnection();
    }

    public static void question9(String arg) throws SQLException {
        Connection conn = Utils.getConnection();
        //TODO : coding style (Alex)
        PreparedStatement stmt = conn.prepareStatement("SELECT tags->'name', ST_X(geom), ST_Y(geom) FROM nodes WHERE tags->'name' LIKE ?");
        stmt.setString(1, arg);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            //TODO : improve display (Alex)
            System.out.println("Nom = " + res.getString(1) + "; X = " + res.getFloat(2) + "; Y = " + res.getFloat(3));
        }

        System.out.println("Closing connection");
        Utils.closeConnection();
    }
    
    // TODO : Split the code in objects and functions
    // TODO [DONE] : Color the roads in different colors given the value of tags->'highway'
    // TODO : Change the SRID for a conic one (more adapted to France)
    public static void question10a() throws SQLException{
        double xmin = 5.7;
        double xmax = 5.8;
        double ymin = 45.1;
        double ymax = 45.2;
        int srid = 4326;
        
        HashMap<String, Color> colors = new HashMap();
        colors.put("motorway", Color.decode("0x23B0DB"));
        colors.put("motorway_link", Color.decode("0x23B0DB"));
        colors.put("tunk", Color.decode("0xA7DB23"));
        colors.put("trunk_link", Color.decode("0xA7DB23")); 
        colors.put("primary", Color.decode("0xDB234B"));
        colors.put("primary_link", Color.decode("0xDB234B"));
        colors.put("secondary", Color.decode("0xDB7623"));
        colors.put("secondary_link", Color.decode("0xDB7623"));
        colors.put("residential", Color.decode("0x8C8C8C"));
        colors.put("cycleway", Color.decode("0xD1CF97"));
        colors.put("path", Color.decode("0xBF9F5A"));
        
        HashMap<String, Integer> lineWidths = new HashMap();
        lineWidths.put("motorway", 2);
        lineWidths.put("motorway_link", 2);
        lineWidths.put("tunk", 2);
        lineWidths.put("trunk_link", 2); 
        lineWidths.put("primary", 2);
        lineWidths.put("primary_link", 1);
        lineWidths.put("secondary", 2);
        lineWidths.put("secondary_link", 1);
        lineWidths.put("cycleway", 1);
        
        String query = 
                "SELECT tags->'highway', ST_Intersection(w.linestring, ST_MakeEnvelope(?, ?, ?, ?, ?)) "
                + "FROM ways w "
                + "WHERE tags->'highway' <> '' "//"WHERE tags ? 'highway' "
                + "AND ST_Intersects(w.linestring, ST_MakeEnvelope(?, ?, ?, ?, ?))"; 
        
        // Create the map : 
        MapPanel map = new MapPanel(5.758102, 45.187485, 1);
        GeoMainFrame frame = new GeoMainFrame("Map", map);
        
        // Connect to the database :
        Connection conn = Utils.getConnection();
        
        // Ask the query to find the roads and paths in the area : 
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setDouble(1, xmin);
        stmt.setDouble(2, ymin);
        stmt.setDouble(3, xmax);
        stmt.setDouble(4, ymax);
        stmt.setInt(5, srid);
        //stmt.setString(6, "?");
        stmt.setDouble(6, xmin);
        stmt.setDouble(7, ymin);
        stmt.setDouble(8, xmax);
        stmt.setDouble(9, ymax);
        stmt.setInt(10, srid);
        // Print the roads : 
        ResultSet res = stmt.executeQuery();
        System.out.println("Loading data from database (may take 30secs) ...");
        while (res.next()) {
            String pathType = res.getString(1);
            geoexplorer.gui.LineString lineGE = null;
                        
            // Extract PostGis LineString and create GeoExplorer Linestring from it
            PGgeometry geom = (PGgeometry)res.getObject(2); 
            if(geom.getGeoType() == Geometry.LINESTRING) { 
                Color color = colors.getOrDefault(pathType, Color.black);
                Integer lineWidth = lineWidths.getOrDefault(pathType, 1);
                lineGE = new geoexplorer.gui.LineString(color, lineWidth);
                LineString linePG = (LineString)geom.getGeometry();
                
                Point[] pts = linePG.getPoints();
                for(int i = 0; i < pts.length; ++i){
                    //System.out.println("Point: " + pts[i]);
                    lineGE.addPoint(new geoexplorer.gui.Point(pts[i].getX(), pts[i].getY()));
                } 
            }
            
            // Print the GeoExplorer line : 
            map.addPrimitive(lineGE);
        }
        System.out.println("Data loaded, and map printed.");
        map.autoAdjust();

        // Close the database :
        System.out.println("Closing connection");
        Utils.closeConnection();
    }
    
    public static void question10b(){
        /*
        PGgeometry geom = (PGgeometry)r.getObject(1); 
        if( geom.getGeoType() == Geometry.POLYGON ) { 
          Polygon pl = (Polygon)geom.getGeometry(); 
          for( int r = 0; r < pl.numRings(); r++) { 
            LinearRing rng = pl.getRing(r); 
            System.out.println("Ring: " + r); 
            for( int p = 0; p < rng.numPoints(); p++ ) { 
              Point pt = rng.getPoint(p); 
              System.out.println("Point: " + p);
              System.out.println(pt.toString()); 
            } 
          } 
        }
        */

    }

}
