package me.allevato.graph.nav;
import me.allevato.graph.ExtraInfo;
import me.allevato.graph.Node;


/**
 * A type of node that has an (x,y) position.
 * @author adam
 *
 */
public class NavNode extends Node<ExtraInfo> {
	int x;
	int y;
	
	public NavNode(int _x, int _y) {
		super();
		//System.out.println("id: " + id);
		x = _x;
		y = _y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
