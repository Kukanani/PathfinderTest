package me.allevato.graph.search;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import me.allevato.graph.nav.NavEdge;
import me.allevato.graph.nav.NavGraph;


/**
 * Depth-first graph search. Creates long winding paths, although they will eventually find the target.
 * @author adam
 *
 * @param <GraphType> the type of graph to search
 */
public class SearchDFS<GraphType extends NavGraph> {
	int start;
	int goal;
	GraphType graph;
	Vector<Integer> path;
	public ArrayList<Integer> visited = new ArrayList<Integer>(); 
	
	public SearchDFS(GraphType g, int s, int go) {
		graph = g;
		goal = go;
		start = s;
		path = new Vector<Integer>(g.getNumNodes());
	}
	
	public boolean search() {
		int current = start;
		path.add(current);
		NavEdge nextEdge;
		
		visited = new ArrayList<Integer>(); 
		Stack<NavEdge> frontier = new Stack<NavEdge>();
		frontier.add(new NavEdge(start, start, 0.d));
		
		while(!frontier.isEmpty()) {
			while(frontier.peek().getSource() != path.get(path.size()-1)) {
				path.remove(path.size()-1);
			}
			nextEdge = frontier.pop();
			if(visited.contains(nextEdge.getDest())) {
				continue;
			}
			current = nextEdge.getDest();
			visited.add(current);
			//System.out.println("edge " + nextEdge.getId() + " from node " + nextEdge.getSource() + " to " + nextEdge.getDest());
			path.add(current);
			if(current == goal) {
				return true;
			}
			frontier.addAll((graph.getIndexedEdges()).elementAt(current));
		}
		
		return false;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public Vector<Integer> getPath() {
		return path;
	}

	public void setPath(Vector<Integer> path) {
		this.path = path;
	}
}
