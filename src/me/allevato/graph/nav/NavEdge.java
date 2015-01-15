package me.allevato.graph.nav;

import me.allevato.graph.Edge;
import me.allevato.graph.ExtraInfo;

public class NavEdge extends Edge<ExtraInfo> {
	public NavEdge(int s, int d, double c) {
		super(s, d);
		this.setCost(c);
	}
}
