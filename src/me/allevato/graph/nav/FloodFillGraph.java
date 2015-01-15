package me.allevato.graph.nav;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import me.allevato.graph.util.Geometry;

public class FloodFillGraph extends NavGraph {
	public Vector<Line> walls;
	Random r = new Random();
	Vector<Point> lookedAt;
	public Stack<Point> frontier;
	Point begin;
	int space;
	boolean diag;
	
	public FloodFillGraph(int w, int h, int spacing, boolean diagonals, Vector<Line> wall, boolean immediate) {
		super();
		width = w;
		height = h;
		walls = wall;
		space = spacing;
		diag = diagonals;
		
		lookedAt = new Vector<Point>();
		frontier = new Stack<Point>();

		begin = new Point(w/2/spacing*spacing, h/2/spacing*spacing);
		frontier.add(begin);
		
		if(immediate) {
			long startTime = System.currentTimeMillis();
			while(!frontier.isEmpty()) {
				step();
			}
			long endTime = System.currentTimeMillis();
			System.out.println("flood fill took " + (endTime-startTime) + "ms");
		}
		
		else {
			step(); //one step to kick things off
		}
	}
	
	public void step() {
		if(frontier.isEmpty()) return;
		//System.out.println("frontier size is " + frontier.size());
		begin = frontier.pop();
		addFrontierPoints();
	}
	
	void addFrontierPoints() {
		//System.out.println("exploring node at " + begin);
		if(lookedAt.contains(begin)) {
			//System.out.println("already been looked at. Moving on.");
			return;	
		}
		
		Point end;
		//if(r.nextBoolean()) {
			end = new Point(begin.x+space, begin.y);
			if(checkFrontierPoint(end)) {
				addPoint(end);
			}
		//}
		//if(r.nextBoolean()) {
			end = new Point(begin.x-space, begin.y);
			if(checkFrontierPoint(end)) {
				addPoint(end);
			}
		//}
		//if(r.nextBoolean()) {
			end = new Point(begin.x, begin.y+space);
			if(checkFrontierPoint(end)) {
				addPoint(end);
			}
		//}
		//if(r.nextBoolean()) {
			end = new Point(begin.x, begin.y-space);
			if(checkFrontierPoint(end)) {
				addPoint(end);
			}
		//}
		if(diag) {
			//if(r.nextBoolean()) {
				end = new Point(begin.x+space, begin.y-space);
				if(checkFrontierPoint(end)) {
					addPoint(end);
				}
			//}
			//if(r.nextBoolean()) {
				end = new Point(begin.x-space, begin.y+space);
				if(checkFrontierPoint(end)) {
					addPoint(end);
				}
			//}
			//if(r.nextBoolean()) {
				end = new Point(begin.x+space, begin.y+space);
				if(checkFrontierPoint(end)) {
					addPoint(end);
				}
			//}
			//if(r.nextBoolean()) {
				end = new Point(begin.x-space, begin.y-space);
				if(checkFrontierPoint(end)) {
					addPoint(end);
				}
			//}
		}
		lookedAt.add(begin);
	}
	
	boolean checkFrontierPoint(Point end) {
		if(begin.equals(end)) return false;
		if(lookedAt.contains(end)) return false;
		//System.out.println(begin + " to " + end);
		if(end.x < 0 || end.x > width || end.y < 0 || end.y > height) return false;
		Line connecting = new Line(begin, end);
		Line wall;
		for(Iterator<Line> it = walls.iterator(); it.hasNext(); ) {
			wall = it.next();
			if(Geometry.LineIntersection2D(wall.p1, wall.p2, connecting.p1, connecting.p2)) {
				return false;
			}
		}
		return true;
	}
	
	void addPoint(Point end) {
		//System.out.println("Going from " + nodeAt(begin).getId() + " to " + nodeAt(end).getId());
		NavNode endNode = nodeAt(end);
		if(!frontier.contains(end)) {
			//System.out.println("adding to the next frontier");
			frontier.add(0, end);
		}
		addEdge(new NavEdge(endNode.getId(), nodeAt(begin).getId(), distBetweenNodes(nodeAt(begin).getId(), endNode.getId())));
		addEdge(new NavEdge(nodeAt(begin).getId(), endNode.getId(), distBetweenNodes(nodeAt(begin).getId(), endNode.getId())));
	}
	
	/*public void addEdge(NavEdge ne) {
		if(r.nextInt(4) == 2) {
			ne.setCost(ne.getCost()*3);
		}
		super.addEdge(ne);
	}*/
	
	public boolean nodeExistsAt(Point p) {
		NavNode step;
		for(Iterator<NavNode> n = getNodeIterator(); n.hasNext(); ) {
			step = n.next();
			if(step.x == p.x && step.y == step.y) {
				return true;
			}
		}
		return false;
	}
	public NavNode nodeAt(Point p) {
		NavNode step;
		for(Iterator<NavNode> n = getNodeIterator(); n.hasNext(); ) {
			step = n.next();
			if(step.x == p.x && step.y == p.y) {
				return step;
			}
		}
		step = new NavNode(p.x, p.y);
		addNode(step);
		return step;
	}
}