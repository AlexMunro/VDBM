/*
Copyright 2014 Alexander Munro

This file is part of VDBM.

VDBM is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

VDBM is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with VDBM.  If not, see <http://www.gnu.org/licenses/>.
*/

package application.gui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import application.Relationship;

/**
 * Abstract class for lines painted between tables.
 * 
 * @author Alex Munro
 *
 */

public abstract class RelationshipLine
{
	public abstract void paint(Graphics2D g2d);
	
	protected EntityBox eb1, eb2;
	
	protected Line2D connection;
	protected Point intersection, outgoingIntersection;
	protected Point startPoint, endPoint;

	private int thickness;
	
	protected enum IntersectionDirection
	{
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
	
	protected IntersectionDirection direction;
	
	public RelationshipLine(Relationship r, int thickness)
	{
		eb1 = r.getStartBox();
		eb2 = r.getEndBox();
		this.thickness = thickness;
	}
	
	/**
	 * Rotates a point around the origin by the given angle
	 * @param p point to be rotated
	 * @param angle angle of rotation
	 * @return rotated point
	 */
	public void rotatePoint(Point p, double angle)
	{
		double x = (Math.cos(angle) * p.x) - (Math.sin(angle) * p.y);
		double y = (Math.sin(angle) * p.x) + (Math.cos(angle) * p.y);
		p.setLocation(x, y);;
	}
	
	private Point getPointAtX(Line2D l, double x)
	{
		
	
		/* Choose point (a,b) from some point on l
		 * Calculate gradient from two points on l
		 * y - b = m(x -a)
		 * y = mx - ma + b
		 */
		
		double a = l.getP1().getX();
		double b = l.getP1().getY();
		double m = ((l.getP2().getY() - l.getP1().getY()) / (l.getP2().getX() - l.getP1().getX()));
		double y = (m * x) - (m * a) + b;
		return new Point((int) x, (int) y);
	}
	
	private Point getPointAtY(Line2D l, int y)
	{
		// If line is vertical, simply take (x,y) at the constant y point
		
		if (l.getX2() == l.getX1())
		{
			return new Point((int) l.getX2(), y);
		}	
		
		/* Choose point (a,b)
		 * Calculate gradient
		 * m(x - a) = y - b
		 * x - a = (y-b)/m
		 * x = (y-b)/m + a
		 */
		double a = l.getP1().getX();
		double b = l.getP1().getY();
		double m = ((l.getP2().getY() - l.getP1().getY()) / (l.getP2().getX() - l.getP1().getX()));
		double x = ((y - b)/m) + a;
		return new Point((int) x, (int) y);
	}

	public void paintLine(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		startPoint = eb1.getMidPoint();
		endPoint = eb2.getMidPoint();
		
		connection = new Line2D.Float(startPoint, endPoint);
		Line2D top, bottom, left, right;
		
		//TODO: use bounds for x/y as ints instead of points
		
		//Point eb1TopLeft = eb1.getLocation();
		Point eb1TopRight = new Point(eb1.getX() + eb1.getWidth(), eb1.getY());
		Point eb1BottomLeft = new Point(eb1.getX(), eb1.getY() + eb1.getHeight());
		Point eb1BottomRight = new Point(eb1.getX() + eb1.getWidth(), eb1.getY() + eb1.getHeight());
		
		Point eb2TopLeft = eb2.getLocation();
		Point eb2TopRight = new Point(eb2.getX() + eb2.getWidth(), eb2.getY());
		Point eb2BottomLeft = new Point(eb2.getX(), eb2.getY() + eb2.getHeight());
		Point eb2BottomRight = new Point(eb2.getX() + eb2.getWidth(), eb2.getY() + eb2.getHeight());
		
		top = new Line2D.Float(eb2TopLeft, eb2TopRight);
		bottom = new Line2D.Float(eb2BottomLeft, eb2BottomRight);
		left = new Line2D.Float(eb2TopLeft, eb2BottomLeft);
		right = new Line2D.Float(eb2TopRight, eb2BottomRight);
		
		intersection = null;
		outgoingIntersection = null;
		
		if (connection.intersectsLine(top))
		{
			direction = IntersectionDirection.TOP;
			intersection = getPointAtY(connection, eb2TopLeft.y);
			outgoingIntersection = getPointAtY(connection, eb1BottomRight.y);
		}
		else if(connection.intersectsLine(left))
		{
			direction = IntersectionDirection.LEFT;
			intersection = getPointAtX(connection, eb2TopLeft.x);
			outgoingIntersection = getPointAtX(connection, eb1BottomRight.x);
		}
		else if(connection.intersectsLine(right))
		{
			direction = IntersectionDirection.RIGHT;
			intersection = getPointAtX(connection, eb2TopRight.x);
			outgoingIntersection = getPointAtX(connection, eb1BottomLeft.x);
		}
		else if(connection.intersectsLine(bottom))
		{
			direction = IntersectionDirection.BOTTOM;
			intersection = getPointAtY(connection, eb2BottomLeft.y);
			outgoingIntersection = getPointAtY(connection, eb1TopRight.y);
		}
		
		
		g2d.setStroke(new BasicStroke(thickness));
		g2d.draw(connection);
	}
}
