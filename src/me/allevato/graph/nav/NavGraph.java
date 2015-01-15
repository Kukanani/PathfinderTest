package me.allevato.graph.nav;

import me.allevato.graph.ExtraInfo;
import me.allevato.graph.Graph;

/**
 * A special type of graph consisting of NavEdges and NavNodes, which have XY positions. Useful for pathfinding algorithms.
 * @author adam
 *
 */
public class NavGraph extends Graph<NavNode, NavEdge, ExtraInfo, ExtraInfo> {
	int width = 0;
	int height = 0;
	
	public NavGraph() {
		
	}
	
	public NavGraph(int w, int h, int spacing, boolean diagonals) {
		width = w;
		height = h;
		int index = 0;
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				index = j*width+i;
				
				addNode(new NavNode((j+1)*spacing, (i+1)*spacing));
			}
		}
		for(int j=0; j<height; j++) {
			for(int i=0; i<width; i++) {
				index = j*width+i;
				//add the eight edges
				if(i > 0) { //left
					addEdge(new NavEdge(index-1, index, distBetweenNodes(index-1, index)));
					addEdge(new NavEdge(index, index-1, distBetweenNodes(index, index-1)));
				}
				if(i < width-1 && i > 0 && j > 0 && diagonals) { //up and left
					addEdge(new NavEdge(index-1-width, index, distBetweenNodes(index, index-1-width)));
					addEdge(new NavEdge(index, index-1-width, distBetweenNodes(index, index-1-width)));
				}
				if(j > 0) { //up
					addEdge(new NavEdge(index-width, index, distBetweenNodes(index-width, index)));
					addEdge(new NavEdge(index, index-width, distBetweenNodes(index-width, index)));
				}
				if(i < width-1 && j < height-1 && j > 0 && diagonals) { //up and right
					addEdge(new NavEdge(index+1-width, index, distBetweenNodes(index, index+1-width)));
					addEdge(new NavEdge(index, index+1-width, distBetweenNodes(index, index+1-width)));
					
				}
				if(i < width-1) { //right
					addEdge(new NavEdge(index+1, index, distBetweenNodes(index, index+1)));
					addEdge(new NavEdge(index, index+1, distBetweenNodes(index, index+1)));
				}
				if(i < width-1 && j < height-1 && i > 0 && diagonals) { //down and right
					addEdge(new NavEdge(index+1+width, index, distBetweenNodes(index, index+1+width)));
					addEdge(new NavEdge(index, index+1+width, distBetweenNodes(index, index+1+width)));
				}
				if(j < height-1) { //down
					addEdge(new NavEdge(index+width, index, distBetweenNodes(index, index+width)));
					addEdge(new NavEdge(index, index+width, distBetweenNodes(index, index+width)));
				}
				if(j < height-1 && i > 0 && j > 0 && diagonals) { //down and left
					addEdge(new NavEdge(index-1+width, index, distBetweenNodes(index, index-1+width)));
					addEdge(new NavEdge(index, index-1+width, distBetweenNodes(index, index-1+width)));
				}
			}
		}
	}
	
	public double distBetweenNodes(int one, int two) {
		return Math.sqrt(Math.pow(n(two).getX()-n(one).getX(), 2)+Math.pow(n(two).getY()-n(one).getY(), 2));
	}

	NavNode n2(int x, int y) {
		return n(y*width+x);
	}
}