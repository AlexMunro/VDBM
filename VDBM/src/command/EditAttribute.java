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

package command;

import model.diagram.Attribute;

/** Command for editing the properties
 *  of an attribute.
 * @author Alex Munro
 *
 */

public class EditAttribute implements Command
{
	private Attribute attribute;
	private int attributeColumn;
	private Object oldValue, newValue;
	
	public EditAttribute(Attribute a, int type, Object value)
	{
		this.attribute = a;
		this.attributeColumn = type;
		this.newValue = value;
		switch(attributeColumn)
		{
			case Attribute.NAME:
				oldValue = attribute.getName();
				break;
			case Attribute.TYPE:
				oldValue = attribute.getType();
				break;
			case Attribute.UNIQUE:
				oldValue = attribute.isUnique();
				break;
			case Attribute.NOT_NULL:
				oldValue = attribute.isNotNull();
				break;
			case Attribute.FIXED:
				oldValue = attribute.isFixed();
				break;
			case Attribute.MULTI:
				oldValue = attribute.isMultiValued();
				break;
			default:
				System.err.println("Column index outwidth bounds!");
				break;
		}
	}

	@Override
	public void execute()
	{
		switch (attributeColumn)
		{
			case Attribute.NAME:
				attribute.setName((String)newValue);
				break;
			case Attribute.TYPE:
				attribute.setType((String)newValue);
				break;
			case Attribute.UNIQUE:
				attribute.setUnique((Boolean)newValue);
				break;
			case Attribute.NOT_NULL:
				attribute.setNotNull((Boolean)newValue);
				break;
			case Attribute.FIXED:
				attribute.setFixed((Boolean)newValue);
				break;
			case Attribute.MULTI:
				attribute.setMultiValued((Boolean) newValue);
				break;
			default:
				System.err.println("Column index outwidth bounds!");
				break;
		}
		
	}

	@Override
	public void undo()
	{
		switch (attributeColumn)
		{
			case Attribute.NAME:
				attribute.setName((String)oldValue);
				break;
			case Attribute.TYPE:
				attribute.setType((String)oldValue);
				break;
			case Attribute.UNIQUE:
				attribute.setUnique((Boolean)oldValue);
				break;
			case Attribute.NOT_NULL:
				attribute.setNotNull((Boolean)oldValue);
				break;
			case Attribute.FIXED:
				attribute.setFixed((Boolean)oldValue);
				break;
			case Attribute.MULTI:
				attribute.setMultiValued((Boolean)oldValue);
				break;
			default:
				System.err.println("Column index outwidth bounds!");
		}	
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public String getDescription()
	{
		return "edit attribute";
	}

	
	
}
