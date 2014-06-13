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

package model.diagram;

import java.awt.Point;
import java.io.Serializable;

/**
 * Abstract class for displaying components of the graph. Only used for
 * entity tables at present. The responsibility of this class is to
 * record the position on the graph of the upper-left most point of the component.
 * @author Alex Munro
 *
 */
public abstract class Component implements Serializable{
	

	private Point position;
	
	public Component(Point p) {
		this.position = p;
	}
	
	private static final long serialVersionUID = 8917181532410480968L;

	public Point getPoint() {
		return position;
	}
	
	public void setPos(Point p)
	{
		position.setLocation(p);
		
	}
	
	

}
