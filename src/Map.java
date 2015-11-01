
import database.Utils;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgis.Geometry;
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
public class Map {
	private double x;
	private double y;
	private double width;
	private int srid;

	private MapPanel map;

	public Map(double x, double y, double width, int srid) {
		// Init map params : 
		this.x = x;
		this.y = y;
		this.width = width;
		this.srid = srid;

		// Create the map (in GeoExplorer GUI) : 
		map = new MapPanel(x, y, width);
		GeoMainFrame frame = new GeoMainFrame("Map", map);
	}

	public void drawRoads() throws SQLException {
		Connection conn = Utils.getConnection();

		// Query to find the roads and paths in the area :
		String query = 
				"SELECT tags->'highway', ST_Intersection(ST_Transform(w.linestring, ?), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?)) "
						+ "FROM ways w "
						+ "WHERE (tags->'highway') IS NOT NULL "//"WHERE tags ? 'highway' "
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
			String pathType = res.getString(1);
			GeometryPrinter.print((PGgeometry)res.getObject(2), map, 
					MapLegend.getColor(pathType), MapLegend.getLineStrength(pathType));
		}
		System.out.println("Data loaded, and map printed.");
		map.autoAdjust();
	}


	public void drawBuildings() throws SQLException{
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
			GeometryPrinter.print((PGgeometry)res.getObject(1), map, Color.decode("0x5E5E5E"));
		}
		System.out.println("Data loaded, and map printed.");
		map.autoAdjust();
	}

	public void drawBakeryPerBlock() throws SQLException{
		Connection conn = Utils.getConnection();

		int baseSrid = 4326;
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
			colors[i]= new Color(255, i*255/12,0, 60);
		}

		// Ask the query to find the roads and paths in the area : 
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, srid);
		stmt.setInt(2, srid);
		stmt.setString(3, type);

		// Print the roads :
		System.out.println("Loading data from database (may take 30secs) ...");
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			int nbBakery = res.getInt(2);
			GeometryPrinter.print((PGgeometry)res.getObject(1), map, colors[nbBakery], colors[nbBakery]);
		}
		System.out.println("Data loaded, and map printed.");
		map.autoAdjust();
	}

	public void drawLoudAreas() throws SQLException{
		Connection conn = Utils.getConnection();

		String query = 
				"SELECT ST_Intersection(ST_Buffer(ST_Transform(w.linestring, ?),80), ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?)) "
						+ "FROM ways w "
						+ "WHERE (tags->'highway' = 'motorway' "
						+ "OR tags->'highway' = 'primary' "
						+ "OR tags->'railway' ='rail' "
						+ "OR (tags->'aeroway') IS NOT NULL) "//"WHERE tags ? 'aeroway' "
						+ "AND ST_Intersects(ST_Transform(w.linestring, ?), "
						+ "                  ST_Transform(ST_MakeEnvelope(?, ?, ?, ?, ?), ?)) ";

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

		// Print the roads :
		System.out.println("Loading data from database (may take 30secs) ...");
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			GeometryPrinter.print((PGgeometry)res.getObject(1), map, new Color(255, 0, 0, 30));
		}
		System.out.println("Data loaded, and map printed.");
		map.autoAdjust();
	}

}
