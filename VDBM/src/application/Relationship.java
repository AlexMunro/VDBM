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

package application;

import application.gui.EntityBox;
import model.diagram.Attribute;

/**
 * Class recording the existence of a relationship between
 * two tables.
 * 
 * @author Alex Munro
 *
 */

public class Relationship
{

	private EntityBox startBox, endBox;
	private Attribute attribute;

	public Relationship(EntityBox startBox, EntityBox endBox, Attribute attribute)
	{
		this.startBox = startBox;
		this.endBox = endBox;
		this.attribute = attribute;
	}

	public EntityBox getStartBox()
	{
		return startBox;
	}

	public EntityBox getEndBox()
	{
		return endBox;
	}

	public Attribute getAttribute()
	{
		return attribute;
	}
	
}
