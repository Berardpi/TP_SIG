
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Utils;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    // TODO : Color the roads in different colors given the value of tags->'highway'
    // TODO : Change the SRID for a conic one (more adapted to France)
    public static void question10a() throws SQLException{
        double xmin = 5.7;
        double xmax = 5.8;
        double ymin = 45.1;
        double ymax = 45.2;
        int srid = 4326;
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
                lineGE = new geoexplorer.gui.LineString();
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
