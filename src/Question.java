import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Utils;


public class Question {	

	public static void launch(String s, Map map) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
//		Question.class.getMethod(s).invoke(null, map);
		
		switch(s) {
		case "question10a":
			Question.question10a(map);
			break;
		case "question10b":
			Question.question10b(map);
			break;
		case "question11a":
			Question.question11a(map);
			break;
		case "question11b":
			Question.question11b(map);
			break;
		}
	}
	
    public static void question9(String arg) throws SQLException {
        Connection conn = Utils.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT tags->'name', ST_X(geom), ST_Y(geom) FROM nodes WHERE tags->'name' LIKE ?");
        stmt.setString(1, arg);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            System.out.println("Nom = " + res.getString(1) + "; X = " + res.getFloat(2) + "; Y = " + res.getFloat(3));
        }
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
