package me.allevato.graph;

public class Node<Extra> {
	int id;
	Extra attached;
	
	private static int nextID = 0;
	
	public Node() {
		//System.out.println("node ID = " + nextID);
		id = nextID++;
	}
	
	public static void resetNodes() {
		nextID = 0;
	}
	
	public Node(Extra e) {
		this();
		attached = e;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Extra getAttached() {
		return attached;
	}

	public void setAttached(Extra attached) {
		this.attached = attached;
	}
}
