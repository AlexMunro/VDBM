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
import application.gui.EntityBox;

/**
 * Command for deleting an attribute from a table.
 * 
 * @author Alex Munro
 *
 */

public class DeleteAttribute implements Command{

	EntityBox entityBox;
	Attribute attribute;
	
	public DeleteAttribute(EntityBox entityBox, Attribute attribute)
	{
		this.entityBox = entityBox;
		this.attribute = attribute;
	}
	
	@Override
	public void execute()
	{
		entityBox.getEntity().removeAttribute(attribute);
		entityBox.update();
	}

	@Override
	public void undo()
	{
		entityBox.getEntity().addAttribute(attribute);
		entityBox.update();
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public String getDescription()
	{
		return "delete attribute";
	}
	
	

}
