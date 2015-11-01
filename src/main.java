
import java.lang.reflect.InvocationTargetException;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

public class main {

    public static void main(String[] args) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    	Scanner reader = new Scanner(System.in);
    	String answer;
    	
    	System.out.println("Bienvenue !");
    	Utils.getConnection();
    	
    	System.out.println("Souhaitez vous accedez à l'affichage textuelle (question 9) [t] ou à l'affichage graphiques (questions 10 et 11) [g] ?");
    	answer = reader.next();
    	
    	if (answer.equalsIgnoreCase("t")) {
    		System.out.println("Question 9 :");
    		System.out.println("Entrez une chaine de caractère, ou [d] pour utiliser celle par défaut (Dom__ne _niversit%)");
    		answer = reader.next();
    		
    		if (answer.equals("d")) {
    			answer = "Dom__ne _niversit%";
    		}
    		
    		System.out.println("Noms et coordonnées géographiques des points dont le nom ressemble à " + answer + " :");
    		Question.question9(answer);
    	} else if (answer.equalsIgnoreCase("g")) {
    		Map map = new Map(5.75, 45.15, 0.05, 2154);
    		HashMap<String, String> displayable = new HashMap<String, String>();
    		displayable.put("10a", "routes autour de Grenoble");
    		displayable.put("10b", "batiments autour de Grenoble");
    		displayable.put("11a", "boulangeries de Grenoble");
    		displayable.put("11b", "nuisances sonores autour de Grenoble");
    		
    		System.out.println("Affichage graphique");
    		askDisplayable(displayable, reader, map);
    		
    		Boolean continuer = true;
    		while(continuer && !displayable.isEmpty()) {
    			System.out.println("Opération(s) terminée(s)");
    			System.out.println("Souhaitez vous ajouter des éléments à la carte ? [y/exit]");
    			answer = reader.next();
    			if (answer.equalsIgnoreCase("y")) {
    				askDisplayable(displayable, reader, map);
    			} else {
    				continuer = false;
    			}
    		}
    	}
        
        reader.close();
        Utils.closeConnection();
        System.out.println("Fin du programme");
    }
    
    public static void askDisplayable(HashMap<String, String> displayable, Scanner reader, Map map) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
    	Iterator<Entry<String, String>> it = displayable.entrySet().iterator();
    	Question q = new Question();
    	
    	while (it.hasNext()) {
    		HashMap.Entry<String, String> pair = (HashMap.Entry<String, String>) it.next();
    		System.out.println("Voulez vous afficher les " + pair.getValue() + " (question " + pair.getKey() + ") ? [y/n]");
    		String answer = reader.next();
    		if (answer.equalsIgnoreCase("y")) {
    			it.remove();
    			Question.launch("question" + pair.getKey(), map);
    		}
    	}
    }

}
