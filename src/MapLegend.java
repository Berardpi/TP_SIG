
import java.awt.Color;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alexandre
 */
public class MapLegend {
    private static HashMap<String, Color> colors = null;
    private static HashMap<String, Integer> lineStrengths = null;
    
    public static HashMap<String, Color> getColors(){
        if(colors == null){
            colors = new HashMap();
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
        }
        
        return colors;
    }
       
    public static HashMap<String, Integer> getLineStrengths(){
        if(lineStrengths == null){
            lineStrengths = new HashMap();
            lineStrengths.put("motorway", 2);
            lineStrengths.put("motorway_link", 2);
            lineStrengths.put("tunk", 2);
            lineStrengths.put("trunk_link", 2); 
            lineStrengths.put("primary", 2);
            lineStrengths.put("primary_link", 1);
            lineStrengths.put("secondary", 2);
            lineStrengths.put("secondary_link", 1);
            lineStrengths.put("cycleway", 1);
        }
        return lineStrengths;
    }
    
    public static int getLineStrength(String wayType){
        Integer lineStrength = getLineStrengths().get(wayType);
        return lineStrength != null ? lineStrength : 1;
    }
    
    public static Color getColor(String wayType){
        Color color = getColors().get(wayType);
        return color != null ? color : Color.BLACK;
    }
}
