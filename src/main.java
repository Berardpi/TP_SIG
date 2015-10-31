
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
        Map map = new Map(5.75, 45.15, 0.05, 2154);

        System.out.println("Question 9:");
        //question9("Dom__ne _niversit%");
        System.out.println("Question 10:");
        question10b(map);
        question10a(map);
        System.out.println("Question 11:");
        question11a(map);
        question11b(map);

    }

    public static void question8() {
        Utils.getConnection();
        Utils.closeConnection();
    }

    public static void question9(String arg) throws SQLException {
        Connection conn = Utils.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT tags->'name', ST_X(geom), ST_Y(geom) FROM nodes WHERE tags->'name' LIKE ?");
        stmt.setString(1, arg);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            System.out.println("Nom = " + res.getString(1) + "; X = " + res.getFloat(2) + "; Y = " + res.getFloat(3));
        }

        System.out.println("Closing connection");
        Utils.closeConnection();
    }
    
    public static void question10a(Map map) throws SQLException{
        map.drawRoads();
    }
    
    public static void question10b(Map map) throws SQLException{
        map.drawBuildings();
    }
    
    public static void question11a(Map map) throws SQLException{
         map.drawBakeryPerBlock();
    }
    
    public static void question11b(Map map) throws SQLException{
        map.drawLoudAreas();
    }

}
