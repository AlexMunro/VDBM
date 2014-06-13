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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import application.Relationship;

/**
 * Paints an inheritance arrow from one table to another.
 * 
 * @author Alex Munro
 *
 */

public class InheritanceLine extends RelationshipLine
{

	public InheritanceLine(Relationship r)
	{
		super(r, 3);
	}

	@Override
	public void paint(Graphics2D g2d)
	{
		super.paintLine(g2d);
		if (intersection != null)
		{	

			
			double angle = Math.atan2((connection.getP2().getY() - connection.getP1().getY()), 
					(connection.getP2().getX() - connection.getP1().getX()));;
			
			Polygon arrow = new Polygon();
			Point p1 = new Point(50, 50);
			Point p2 = new Point(40, 60);
			Point p3 = new Point(40, 40);
			
			rotatePoint(p1, angle);
			rotatePoint(p2, angle);
			rotatePoint(p3, angle);
			
			int dx = (int) (intersection.x - p1.getX());
			int dy = (int) (intersection.y - p1.getY());
			
			
			
			p1.translate(dx, dy);
			p2.translate(dx, dy);
			p3.translate(dx, dy);
			arrow.addPoint((int) p1.getX(), (int) p1.getY());
			arrow.addPoint((int)p2.getX(), (int)p2.getY());
			arrow.addPoint((int)p3.getX(), (int)p3.getY());
			g2d.setColor(Color.WHITE);
			g2d.fill(arrow);
			g2d.setColor(Color.BLACK);
			g2d.draw(arrow);
		}
	}
	
	@Override
	public String toString()
	{
		return "Inheritance line from " + eb1.getName() + " to " + eb2.getName();
	}

}
