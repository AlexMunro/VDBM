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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import model.diagram.Attribute;
import application.Relationship;
import application.gui.RelationshipLine;
/**
 * Class responsible for painting an association
 * between two tables.
 * 
 * @author Alex Munro
 *
 */
public class AssociationLine extends RelationshipLine
{
	private Attribute association;
	
	public AssociationLine(Relationship r)
	{
		super(r, 1);
		this.association = r.getAttribute();
	}

	@Override
	public void paint(Graphics2D g2d)
	{
		super.paintLine(g2d);
		
		String cardinality1 = "1";
		String cardinality2 = "1";

		String lowerCard = "0";
		String upperCard = "1";
		
		if(association.isMultiValued())
			upperCard = "*";
		if(association.isNotNull())
			lowerCard = "1";
		
		if (lowerCard.equals(upperCard))
			cardinality2 = lowerCard;
		else
			cardinality2 = lowerCard + ".." + upperCard;
		
		Point midPoint = new Point(eb1.getMidPoint().x + ((eb2.getMidPoint().x - eb1.getMidPoint().x)/2), eb1.getMidPoint().y + ((eb2.getMidPoint().y - eb1.getMidPoint().y)/2));

		
		
		Point cardPoint1 = (Point) outgoingIntersection.clone();
		Point cardPoint2 = (Point) intersection.clone();
		
		switch(direction)
		{
			case TOP: 
				cardPoint1.translate(0, 20);
				cardPoint2.translate(0, -20);
				midPoint.translate(20, 0);
				break;
			case LEFT:
				cardPoint1.translate(20, 0);
				cardPoint2.translate(-40, 0);
				midPoint.translate(0, 20);
				break;
			case RIGHT:
				cardPoint1.translate(-40, 0);
				cardPoint2.translate(20, 0);
				midPoint.translate(0, -20);
				break;
			case BOTTOM:
				cardPoint1.translate(0, -20);
				cardPoint2.translate(0, 20);
				midPoint.translate(-20, 0);
				break;
		}
		Font textFont = new Font("Dialog", Font.BOLD, 14);
		g2d.setFont(textFont);
		g2d.drawString(cardinality1, (float)cardPoint1.x, (float)cardPoint1.y);
		g2d.drawString(cardinality2, (float)cardPoint2.x, (float)cardPoint2.y);
		
		textFont = new Font("Dialog", Font.PLAIN, 14);
		g2d.setFont(textFont);
		g2d.drawString(association.getName(), (float) midPoint.getX(), (float) midPoint.getY());
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Line2D connection = new Line2D.Float(eb1.getMidPoint(), eb2.getMidPoint());
		g2d.draw(connection);

	}
	
	@Override
	public String toString()
	{
		return "Association line between " + eb1.getName() + " to " + eb2.getName() + " on attribute " + association.getName();
	}

}
