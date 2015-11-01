/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sylvain
 */
public class Utils {
    private static Connection connection;
    private static String login = "berardpi";
    private static String password = "berardpi";
    
    
    public static Connection getConnection() {
        if (connection != null) return connection;
        return createConnection();
    }
    
    private static Connection createConnection() {
	Connection conn = null;
	try {
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Loading PostgreSQL driver...");
	    Class.forName("org.postgresql.Driver");
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Trying to connect to Grenoble database...");
	    String url = "jdbc:postgresql://ensibd.imag.fr:5432/osm";
	    conn = DriverManager.getConnection(url, login, password);
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Connected.");
	    ((org.postgresql.PGConnection) conn).addDataType("geometry",Class.forName("org.postgis.PGgeometry"));
	    ((org.postgresql.PGConnection) conn).addDataType("box3d",Class.forName("org.postgis.PGbox3d"));
	} catch (SQLException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Connection error: {0}", e.toString());
	} catch (ClassNotFoundException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Error while loading postgis extensions: {0}", e.toString());
	}
	connection = conn;
	return conn;	
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Connection closed.");
            } catch (SQLException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
