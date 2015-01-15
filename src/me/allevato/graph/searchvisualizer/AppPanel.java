package me.allevato.graph.searchvisualizer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import me.allevato.graph.nav.FloodFillGraph;
import me.allevato.graph.nav.Line;
import me.allevato.graph.nav.NavEdge;
import me.allevato.graph.nav.NavNode;
import me.allevato.graph.search.SearchA;
import me.allevato.graph.search.SearchDFS;
import me.allevato.graph.search.SearchDjk;

@SuppressWarnings("serial")
/**
 * Stores a graph and creates and runs pathfinding algorithms.
 * @author adam
 *
 */
public class AppPanel extends JPanel {
	NavNode start; //starting node for search
	NavNode goal; //end node for search
	SearchDFS<FloodFillGraph> dfs; //depth-first search
	SearchDjk<FloodFillGraph> djk; //Dijkstra's (circular) search
	SearchA<FloodFillGraph> astar; //(incomplete) A* search (improved speed Dijkstra's)
	boolean found = false; //true if our last search algorithm succeeeded
	
	public FloodFillGraph graph; //the graph to search in
    int spacing = 30; //should be even. Distance between nodes in the grid graph
    int width = 600; //total panel width
    int height = 600; //total panel height
    Vector<Line> walls = new Vector<Line>(); //2D line walls to be avoided while creating a graph and pathfinding
    Random r = new Random(); //for generating random numbers
    
    //if true, then the flood fill algorithm will happen automatically.
    //set to false to use the space bar to see how the flood fill works.
    boolean immediateFill = true; 
    
    //if true, then the flood fill will create diagonal connections between grid nodes.
    //the cost of the edges will be proportional to their length.
    boolean diagonals = false;
    
    /*
     * set up variables, listeners and perform initial search
     */
    public AppPanel() {
    	//clamp to multiples of the grid spacing
    	width -= width % spacing;
    	height -= height % spacing;
        setBorder(BorderFactory.createLineBorder(Color.black));
        //walls.add(new Line(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height)));
        rebuild();
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	if(e.getButton() == MouseEvent.BUTTON1) {
            		setGoal(e.getX(), e.getY()); //move goal node
            	}
            	else if(e.getButton() == MouseEvent.BUTTON3) {
            		setStart(e.getX(), e.getY()); //move start node 
            	}
            	doSearch();
            	repaint();
            }
         });
        setFocusable(true); //allow keyboard events
        this.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent k) {
        		switch(k.getKeyCode()) {
        		case KeyEvent.VK_SPACE:
            		//step the incremental flood fill
	        		if(!immediateFill) {
		        		graph.step();
		        		repaint();
	        		}
	        		break;
        		case KeyEvent.VK_RIGHT:
        			if(goal != null) { //move goal
	            		setGoal(goal.getX()+spacing,goal.getY());
	                	doSearch();
	                	repaint();
        			}
	        		break;
        		case KeyEvent.VK_LEFT:
        			if(goal != null) { //move goal
	            		setGoal(goal.getX()-spacing,goal.getY());
	                	doSearch();
	                	repaint();
        			}
	        		break;
        		case KeyEvent.VK_UP:
        			if(goal != null) { //move goal
	            		setGoal(goal.getX(),goal.getY()-spacing);
	                	doSearch();
	                	repaint();
        			}
	        		break;
        		case KeyEvent.VK_DOWN:
        			if(goal != null) { //move goal
	            		setGoal(goal.getX(),goal.getY()+spacing);
	                	doSearch();
	                	repaint();
        			}
	        		break;
        		case KeyEvent.VK_ENTER: //reset everything
        			//System.out.println("rebuilding");
        			rebuild();
        			break;
	        	default:
	        		break;
        		}
        	}
         });
        this.setBounds(10,10,width,height); //don't draw at the edge of the screen
    }
    
    /**
     * reset the variables and randomize the walls, start node, and end node.
     */
    void rebuild() {
		walls.clear();
        generateVHWalls(10);
        generateBorderWalls();
		graph = new FloodFillGraph(width, height, spacing, diagonals, walls, immediateFill); //set last to false for incremental flood fill
        goal = null;
        found = false;
        start = graph.n(r.nextInt(graph.getNumNodes()));
        goal = graph.n(r.nextInt(graph.getNumNodes()));
        if(immediateFill) doSearch();
        repaint();
    }

    /**
     * Creates a set of walls that bound the graph area (otherwise the flood fill will find cracks in the walls)
     */
    void generateBorderWalls() {
    	walls.add(new Line(spacing/2, spacing/2, width-spacing/2, spacing/2));
    	walls.add(new Line(spacing/2, spacing/2, spacing/2, height-spacing/2));
    	walls.add(new Line(spacing/2, height-spacing/2, width-spacing/2, height-spacing/2));
    	walls.add(new Line(width-spacing/2, spacing/2, width-spacing/2, height-spacing/2));
    	walls.add(new Line(0, spacing, spacing, 0));
    	walls.add(new Line(0, height-spacing, spacing, height));
    	walls.add(new Line(width-spacing, 0, width, spacing));
    	walls.add(new Line(width, height-spacing, width-spacing, height));
    }
    
    /**
     * Generate random vertical and horizontal walls
     * @param num the number of walls to create
     */
    void generateVHWalls(int num) {
        Random r = new Random();
        for(int i = 0; i < num; i++) {
        	int x = r.nextInt(width/spacing+2)*spacing+spacing/2-spacing;
        	int y = r.nextInt(height/spacing+2)*spacing+spacing/2-spacing;
        	int x2 = x;
        	int y2 = y;
        	if(r.nextBoolean()) { //horiz
        		x2 = r.nextInt(width/spacing+2)*spacing+spacing/2-spacing;
        	} else { //vertical
        		y2 = r.nextInt(width/spacing+2)*spacing+spacing/2-spacing; 
        	}
        	walls.add(new Line(x,y, x2, y2));
        }
    }
    
    /**
     * set the goal node to the nearest node to the given coordinates.
     * @param x
     * @param y
     */
    void setGoal(int x, int y) {
    	int gx = (x+spacing/2)/spacing*spacing; //System.out.println(gx);
    	int gy = (y+spacing/2)/spacing*spacing; //System.out.println(gy);
    	if(graph.nodeExistsAt(new Point(gx, gy))) {
    		goal = graph.nodeAt(new Point(gx, gy));
    	}
    	else {
    		
    	}
    }
    
    /**
     * set the search start node to the nearest node to the given coordinates.
     * @param x
     * @param y
     */
    void setStart(int x, int y) {
    	int gx = (x+spacing/2)/spacing*spacing; //System.out.println(gx);
    	int gy = (y+spacing/2)/spacing*spacing; //System.out.println(gy);
    	if(graph.nodeExistsAt(new Point(gx, gy))) {
    		start = graph.nodeAt(new Point(gx, gy));
    	}
    	else {
    		
    	}
    }
    /**
     * perform a search
     */
    public void doSearch() {
    	if(goal == null || start == null) return;
    	long timeStart = System.currentTimeMillis();
    	
    	dfs = new SearchDFS<FloodFillGraph>(graph, start.getId(), goal.getId());
    	djk = new SearchDjk<FloodFillGraph>(graph, start.getId(), goal.getId());
    	astar = new SearchA<FloodFillGraph>(graph, start.getId(), goal.getId());
    	//System.out.println(goal.getId());
    	dfs.search();
    	//if(djk.search()) {
    	if(djk.search()) {
    		found = true;
    	}
    	else found = false;
    	long timeEnd = System.currentTimeMillis();
    	System.out.println("Search took " + (timeEnd-timeStart) + "ms");
    }

    /**
     * Swing override
     */
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    /**
     * draw everything
     */
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
    	//for convenience
    	Graphics2D g = (Graphics2D)g1;
        
    	// START NODE /////////////////////////////////////////////////////////////////////////
        if(start != null) {
	        g.setColor(Color.BLUE);
	        g.fillRect(start.getX()-10, start.getY()-10, 20, 20);
        }

    	// GOAL NODE /////////////////////////////////////////////////////////////////////////
        if(goal != null) {
	        g.setColor(Color.GREEN);
	        g.fillRect(goal.getX()-10, goal.getY()-10, 20, 20);
        }
        
    	// GRAPH EDGES /////////////////////////////////////////////////////////////////////////
		for(Iterator<NavEdge> edgeIT = graph.getEdgeIterator(); edgeIT.hasNext(); ) {
			NavEdge e = edgeIT.next();
			g.setColor(new Color((int)e.getCost()*2, 0, 0));
			//System.out.println(e.getId());
			g.drawLine((int)graph.n(e.getSource()).getX(),
					(int)graph.n(e.getSource()).getY(),
					(int)graph.n(e.getDest()).getX(),
					(int)graph.n(e.getDest()).getY());
		}
		
    	// GRAPH NODES /////////////////////////////////////////////////////////////////////////
		g.setColor(Color.GRAY);
		for(Iterator<NavNode> nodeIT = graph.getNodeIterator(); nodeIT.hasNext(); ) {
			NavNode n = nodeIT.next();
			g.fillOval(
					(int)(n.getX()-3),
					(int)(n.getY()-3),
					6,
					6);
			g.setColor(new Color(200, 200, 200));
			//g.drawString("" + n.getId(),
			//		(int)(n.getX()-3),
			//		(int)(n.getY()-3));
		}

    	// START NODE /////////////////////////////////////////////////////////////////////////
		g.setColor(Color.MAGENTA);
		for(Iterator<Point> nodeIT = graph.frontier.iterator(); nodeIT.hasNext(); ) {
			Point n = nodeIT.next();
			g.drawOval(
					(int)n.getX()-10,
					(int)n.getY()-10,
					20,
					20);
		}

    	// NODES ON THE FRONTIER //////////////////////////////////////////////////////////////
		g.setColor(Color.ORANGE);
		/*for(Iterator<Integer> nodeIT = dfs.visited.iterator(); nodeIT.hasNext(); ) {
			NavNode n = graph.n(nodeIT.next());
			g.drawOval(
					(int)n.getX()-7,
					(int)n.getY()-7,
					14,
					14);
		}*/
		for(Iterator<NavEdge> nodeIT = djk.searchFrontier.iterator(); nodeIT.hasNext(); ) {
			NavEdge e = nodeIT.next();
			if(e == null) continue;
			NavNode n = graph.n(e.getDest());
			g.drawOval(
					(int)n.getX()-7,
					(int)n.getY()-7,
					14,
					14);
		}

    	// FINAL SEARCH PATH //////////////////////////////////////////////////////////////
		if(found) {
			g.setColor(Color.MAGENTA);
			g.setStroke(new BasicStroke(3));
			NavNode currNode = null;
			NavNode lastNode = null;
			int curr;
			for(Iterator<Integer> nodeIT = dfs.getPath().iterator(); nodeIT.hasNext(); ) {
				curr = nodeIT.next();
				lastNode = currNode;
				currNode = graph.n(curr);
				if(lastNode != null) {
					g.drawLine(lastNode.getX(), lastNode.getY(), currNode.getX(), currNode.getY());
				}
			}
			g.setColor(Color.BLACK);
			NavEdge currEdge = djk.getPath().elementAt(goal.getId());
			lastNode = goal;
			currNode = graph.n(currEdge.getSource());
			while(currNode != start && currNode != start) {
				//System.out.println("testing node " + currNode.getId());
				//System.out.println("start node " + start.getId());
				if(lastNode != null) {
					g.drawLine(lastNode.getX(), lastNode.getY(), currNode.getX(), currNode.getY());
				}
				lastNode = currNode;
				currNode = graph.n(currEdge.getSource());
				currEdge = djk.getPath().elementAt(currNode.getId());
			}
			g.drawLine(lastNode.getX(), lastNode.getY(), currNode.getX(), currNode.getY());
		}
		

    	// WALLS //////////////////////////////////////////////////////////////
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(4));
		Line w;
		for(Iterator<Line> wallIT = graph.walls.iterator(); wallIT.hasNext(); ) {
			w = wallIT.next();
			drawLine(g, w);
			g.drawLine(w.p1.x, w.p1.y, w.p2.x, w.p2.y);
		}
    }
    
    /**
     * convenience method for drawing a Line (connection between two points)
     * @param g
     * @param line
     */
    void drawLine(Graphics2D g, Line line) {
		g.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
    }
}