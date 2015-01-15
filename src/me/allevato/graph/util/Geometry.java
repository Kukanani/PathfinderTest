package me.allevato.graph.util;

import java.awt.Point;

/**
 * Geometry utilities
 * @author adam
 *
 */
public class Geometry {
	public static boolean LineIntersection2D(Point A,
			Point B,
			Point C, 
			Point D)
	{
		double rTop = (A.y-C.y)*(D.x-C.x)-(A.x-C.x)*(D.y-C.y);
		double sTop = (A.y-C.y)*(B.x-A.x)-(A.x-C.x)*(B.y-A.y);

		double Bot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

		if (Bot == 0)//parallel
		{
			return false;
		}

		double invBot = 1.0/Bot;
		double r = rTop * invBot;
		double s = sTop * invBot;

		if( (r > 0) && (r < 1) && (s > 0) && (s < 1) )
		{
			//lines intersect
			return true;
		}

		//lines do not intersect
		return false;
	}
}
