package me.allevato.graph.nav;

import java.awt.Point;

public class Line {
	public Point p1;
	public Point p2;
	public Line(Point _p1, Point _p2) {
		p1 = _p1;
		p2 = _p2;
	}

	public Line(int x1, int y1, int x2, int y2) {
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);
	}
}