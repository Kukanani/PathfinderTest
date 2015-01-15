package me.allevato.graph.nav;

import me.allevato.graph.Edge;
import me.allevato.graph.ExtraInfo;

/**
 * A special type of edge for nav graphs. Currently, simply allows the cost to be specified in the constructor.
 * @author adam
 *
 */
public class NavEdge extends Edge<ExtraInfo> {
	public NavEdge(int s, int d, double c) {
		super(s, d);
		this.setCost(c);
	}
}
