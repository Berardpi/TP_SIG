package geoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main JFrame of the application: basically only contains a map panel.
 * @author Sylvain B.
 * @version 1.0
 */
public class GeoMainFrame extends JFrame {
    private MapPanel mapPanel;
    
    /**
     * Builds and initializes the main window.
     * @param name the window name (displayed in the window title bar)
     * @param m the map panel. Needs to be specified so as to be able to draw on it outside the GeoMainFrame class.
     */
    public GeoMainFrame(String name, MapPanel m) {
        super(name);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GeoMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.mapPanel = m;
        
        Container container = this.getContentPane();
        
        container.setLayout(new BorderLayout());
        container.add(mapPanel, BorderLayout.CENTER);
                        
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        mapPanel.repaint();
        setVisible(true);        
    }                
}
