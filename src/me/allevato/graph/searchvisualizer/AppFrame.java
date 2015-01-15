package me.allevato.graph.searchvisualizer;
import javax.swing.JFrame;

/**
 * Main app frame. Handles Swing creation.
 * @author adam
 *
 */
public class AppFrame {
	public static void main(String args[]) {
		new AppFrame();
	}
	
	public AppFrame() {
		//Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            createAndShowGUI();
	        }
	    });
	}
	
    void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("AWESOME PATHFINDER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayoutManager(new GridManager(1,2));
        
        frame.add(new AppPanel());
        
        //JButton button = new JButton("Calculate");
        //frame.add(button);
        
        frame.pack();
        frame.setVisible(true);
    }
}

