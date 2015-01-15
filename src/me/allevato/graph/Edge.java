package me.allevato.graph;

public class Edge<E> {
	int id;
	
	int source;
	int dest;
	
	static int nextID = 0;
	
	E attached;
	
	double cost;
	
	public Edge(int s, int d) {
		id = nextID++;
		source = s;
		dest = d;
	}

	public static void resetEdges() {
		nextID = 0;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double c) {
		this.cost = c;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public E getAttached() {
		return attached;
	}

	public void setAttached(E attached) {
		this.attached = attached;
	}
	
	public Edge<E> reverse() {
		Edge<E> e = new Edge<E>(dest, source);
		e.setAttached(attached);
		return e;
	}
}
