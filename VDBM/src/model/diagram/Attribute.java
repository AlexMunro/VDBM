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

import java.io.Serializable;

/**
 * Class representing a single attribute of an entity component.
 * @author Alex Munro
 *
 */

public class Attribute implements Serializable{
	
	public static final int NAME = 0;
	public static final int TYPE = 1;
	public static final int UNIQUE = 2;
	public static final int NOT_NULL = 3;
	public static final int FIXED = 4;
	public static final int MULTI = 5;
	
	public static final int COLUMN_COUNT = 6; // Make sure this is one more than the highest preceding constant

	private static final long serialVersionUID = 6096881378243270650L;
	private String name, type;
	
	// Simple constraints
	private boolean unique, notNull, fixed, multiValued;

	public boolean isMultiValued()
	{
		return multiValued;
	}

	public void setMultiValued(boolean multiValued)
	{
		this.multiValued = multiValued;
	}

	public boolean isFixed()
	{
		return fixed;
	}

	public void setFixed(boolean fixed)
	{
		this.fixed = fixed;
	}
	
	public Attribute(String name, String type, boolean unique, boolean notNull, boolean fixed, boolean multiValued)
	{
		this.name = name.replaceAll(" ", "_");
		this.type = type;
		this.unique = unique;
		this.notNull = notNull;
		this.fixed = fixed;
		this.multiValued = multiValued;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name.replaceAll(" ", "_");
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public boolean isUnique()
	{
		return unique;
	}
	public void setUnique(boolean unique)
	{
		this.unique = unique;
	}
	public boolean isNotNull()
	{
		return notNull;
	}
	public void setNotNull(boolean notNull)
	{
		this.notNull = notNull;
	}
	
}
