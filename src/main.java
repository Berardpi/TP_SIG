
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
import org.postgis.LinearRing;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

public class main {

    public static void main(String[] args) throws SQLException {
        double x = 5.75;
        double y = 45.15;
        double width = 0.05;

        // Create the map (in GeoExplorer GUI) : 
        MapPanel map = new MapPanel(x, y, width);
        GeoMainFrame frame = new GeoMainFrame("Map", map);
        
        System.out.println("Question 9:");
        //question9("Dom__ne _niversit%");
        System.out.println("Question 10:");
        question10b(map, x, y, width, 2154);
        question10a(map, x, y, width, 2154);
        System.out.println("Question 11:");
        //question11a();
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
    public static void question10a(MapPanel map, double x, double y, double width, int srid) throws SQLException{
        
        // Connect to the database :
        Connection conn = Utils.getConnection();
        
        // Query to find the roads and paths in the area :
        String query = 
                "SELECT tags->'highway', ST_Intersection(ST_Transform(w.linestring, ?), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?)) "
                + "FROM ways w "
                + "WHERE tags->'highway' <> '' "//"WHERE tags ? 'highway' "
                + "AND ST_Intersects(ST_Transform(w.linestring, ?), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?))"; 
        
        // Prepare the query :
        PreparedStatement stmt = conn.prepareStatement(query);
        int baseSrid = 4326;

        stmt.setInt(1, srid);
        stmt.setDouble(2, x - width);
        stmt.setDouble(3, y - width);
        stmt.setDouble(4, x + width);
        stmt.setDouble(5, y + width);
        stmt.setInt(6, baseSrid);
        stmt.setInt(7, srid);
        stmt.setInt(8, srid);
        stmt.setDouble(9, x - width);
        stmt.setDouble(10, y - width);
        stmt.setDouble(11, x + width);
        stmt.setDouble(12, y + width);
        stmt.setInt(13, baseSrid);
        stmt.setInt(14, srid);

        // Execute the query & Print the roads : 
        ResultSet res = stmt.executeQuery();
        System.out.println("Loading data from database (may take 30secs) ...");
        while (res.next()) {
            geoexplorer.gui.LineString lineGE = null;
                        
            // Get result of SQL request for current path :
            String pathType = res.getString(1);
            PGgeometry geom = (PGgeometry)res.getObject(2); 
            
            // Extract PostGis LineString and create GeoExplorer Linestring from it
            if(geom.getGeoType() == Geometry.LINESTRING) { 
                lineGE = new geoexplorer.gui.LineString(
                        MapLegend.getColor(pathType), 
                        MapLegend.getLineStrength(pathType));
                LineString linePG = (LineString)geom.getGeometry();
                
                for(Point p : linePG.getPoints()){
                    lineGE.addPoint(new geoexplorer.gui.Point(p.getX(), p.getY()));
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
    
    public static void question10b(MapPanel map, double x, double y, double width, int srid) throws SQLException{
        
        // Connect to the database :
        Connection conn = Utils.getConnection();
        
        // Query to find the roads and paths in the area :
        String query = 
                "SELECT ST_Intersection(ST_Transform(w.linestring, ?), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?)) "
                + "FROM ways w "
                + "WHERE tags->'building' = 'yes' "//"WHERE tags ? 'highway' "
                + "AND ST_Intersects(ST_Transform(w.linestring, ?), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?))"; 
        
        // Prepare the query :
        PreparedStatement stmt = conn.prepareStatement(query);
        int baseSrid = 4326;

        stmt.setInt(1, srid);
        stmt.setDouble(2, x - width);
        stmt.setDouble(3, y - width);
        stmt.setDouble(4, x + width);
        stmt.setDouble(5, y + width);
        stmt.setInt(6, baseSrid);
        stmt.setInt(7, srid);
        stmt.setInt(8, srid);
        stmt.setDouble(9, x - width);
        stmt.setDouble(10, y - width);
        stmt.setDouble(11, x + width);
        stmt.setDouble(12, y + width);
        stmt.setInt(13, baseSrid);
        stmt.setInt(14, srid);

        // Execute the query & Print the roads : 
        ResultSet res = stmt.executeQuery();
        System.out.println("Loading data from database (may take 30secs) ...");
        while (res.next()) {
            geoexplorer.gui.LineString lineGE = null;

            // Get result of SQL request for current path :
            PGgeometry geom = (PGgeometry)res.getObject(1); 
                        
            // Extract PostGis LineString and create GeoExplorer Linestring from it
            if(geom.getGeoType() == Geometry.LINESTRING) { 
                lineGE = new geoexplorer.gui.LineString(Color.decode("0x5E5E5E"));
                LineString linePG = (LineString)geom.getGeometry();
                
                for(Point p : linePG.getPoints()){
                    lineGE.addPoint(new geoexplorer.gui.Point(p.getX(), p.getY()));
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
    
    public static void question11a() throws SQLException{
         int srid = 4326;
         int newSrid = 2154;
         String type = "bakery";
         
         String query = 
                 "SELECT q.the_geom, COUNT(*) as nb"
                 + " FROM nodes n"
                 + " INNER JOIN quartier q"
                 + " ON ST_Intersects(ST_Transform(n.geom,?), ST_Transform(q.the_geom,?))"
                 + " WHERE  n.tags->'shop' = ?"
                 + " GROUP BY q.the_geom"
                 + " ORDER BY nb";

         Color[] colors = new Color[12];
         for(int i=0; i<12;i++){
        	 colors[i]= new Color(255, i*255/12,0);
         }
         
         // Create the map : 
         MapPanel map = new MapPanel(5.758102, 45.187485, 1);
         GeoMainFrame frame = new GeoMainFrame("Map", map);
         
         // Connect to the database :
         Connection conn = Utils.getConnection();
         
         // Ask the query to find the roads and paths in the area : 
         PreparedStatement stmt = conn.prepareStatement(query);
         stmt.setInt(1, newSrid);
         stmt.setInt(2, newSrid);
         stmt.setString(3, type);

         // Print the roads :
         System.out.println("Loading data from database (may take 30secs) ...");
         ResultSet res = stmt.executeQuery();
         while (res.next()) {
             geoexplorer.gui.Polygon polygonGE = null;
                         
             // Extract PostGis LineString and create GeoExplorer Linestring from it
             PGgeometry geom = (PGgeometry)res.getObject(1); 
             int nbBakery = res.getInt(2);
             if(geom.getGeoType() == Geometry.MULTIPOLYGON) { 
            	 
            	 polygonGE = new geoexplorer.gui.Polygon(Color.black, colors[nbBakery]);
            	 
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
         }
         System.out.println("Data loaded, and map printed.");
         map.autoAdjust();

         // Close the database :
         System.out.println("Closing connection");
         Utils.closeConnection();
    }

}
