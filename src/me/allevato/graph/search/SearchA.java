package me.allevato.graph.search;

import java.util.Iterator;
import java.util.Vector;

import me.allevato.graph.nav.NavEdge;
import me.allevato.graph.nav.NavGraph;
import me.allevato.graph.nav.NavNode;
import me.allevato.graph.util.IndexMinPQ;

public class SearchA<GraphType extends NavGraph> {
	int start;
	int goal;
	GraphType graph;
	Vector<NavEdge> shortestPath;
	public Vector<NavEdge> searchFrontier;
	Vector<Double> bestCostToNode;
	IndexMinPQ<Double> pq;
	
	public SearchA(GraphType g, int s, int go) {
		graph = g;
		goal = go;
		start = s;
		shortestPath = new Vector<NavEdge>(g.getNumNodes());
		searchFrontier = new Vector<NavEdge>(g.getNumNodes());
		pq = new IndexMinPQ<Double>(graph.getNumNodes());
		bestCostToNode = new Vector<Double>(graph.getNumNodes());
		
		for(int i = 0; i < graph.getNumNodes(); i++) {
			searchFrontier.add(null);
			bestCostToNode.add(0.d);
			shortestPath.add(null);
		}
	}
	
	public boolean search() {
		int nextClosestNode;

		pq.insert(start, 0.d);
		
		while(!pq.isEmpty()) {
			nextClosestNode = pq.minIndex();
			System.out.println("looking at node " + nextClosestNode);
			pq.delMin();
			
			if(searchFrontier.size() >= nextClosestNode) {
				shortestPath.set(nextClosestNode, searchFrontier.get(nextClosestNode));
			}
			if(nextClosestNode == goal) return true;
			
			//edge relaxation
			NavEdge next;
			for(Iterator<NavEdge> edgeIT = graph.ne(nextClosestNode).iterator(); edgeIT.hasNext(); ) {
				next = edgeIT.next();
				double costTo = bestCostToNode.elementAt(next.getSource());
				
				System.out.println("heuristic cost at dest:" + hCost(next.getDest()));
				System.out.println("cost to source:" + costTo);
				System.out.println("cost of edge:" + next.getCost());
				double newCost = costTo+next.getCost()+hCost(next.getDest());
				System.out.println("SUM: " + newCost);
				System.out.println("================================");
				
				if(searchFrontier.elementAt(next.getDest()) == null) {
					bestCostToNode.set(next.getDest(), newCost);
					pq.insert(next.getDest(), newCost);
					searchFrontier.set(next.getDest(), next);
					if(next.getDest() == goal) {
						shortestPath.set(goal, next);
						return true;
					}
				}
				else if(newCost < bestCostToNode.elementAt(next.getDest()) &&
						shortestPath.elementAt(next.getDest()) == null) {
					bestCostToNode.set(next.getDest(), newCost);
					pq.changeKey(next.getDest(), newCost);
					searchFrontier.set(next.getDest(), next);
				}
			}
		}
		
		return false;
	}
	
	double hCost(int nIndex) {
		return graph.distBetweenNodes(nIndex, goal);
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

	public Vector<NavEdge> getPath() {
		return shortestPath;
	}

	public void setPath(Vector<NavEdge> path) {
		this.shortestPath = path;
	}
}
