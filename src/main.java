import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Utils;

public class main {

	public static void main(String[] args) throws SQLException {
		question9("Dom__ne _niversit%");
	}
	
	public static void question8() {
		Utils.getConnection();
		Utils.closeConnection();
	}
	
	public static void question9(String arg) throws SQLException {
		Connection conn = Utils.getConnection();
		//TODO : coding style (Alex)
		PreparedStatement stmt =  conn.prepareStatement("SELECT tags->'name', ST_X(geom), ST_Y(geom) FROM nodes WHERE tags->'name' LIKE ?");
		stmt.setString(1, arg);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			//TODO : improve display (Alex)
			System.out.println("Nom = " + res.getString(1) + "; X = " + res.getFloat(2) + "; Y = " + res.getFloat(3));
		}
		
		System.out.println("Closing connection");
		Utils.closeConnection();
	}

}
