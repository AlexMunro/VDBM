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

import application.gui.EntityBox;
import model.diagram.Entity;

/**
 * Changes which other entity an entity inherits from
 * @author Alex Munro
 *
 */
public class EditInheritance implements Command
{
	private EntityBox entityBox;
	private Entity entity, superEntity, oldSuperEntity;

	public EditInheritance(EntityBox eb, Entity superEntity)
	{
		this.entityBox = eb;
		this.entity = entityBox.getEntity();
		this.superEntity = superEntity;
		this.oldSuperEntity = entity.inheritsFrom();
	}

	@Override
	public void execute()
	{
		entity.setInheritsFrom(superEntity);
		//TODO inheritance arrow
	}

	@Override
	public void undo()
	{
		entity.setInheritsFrom(oldSuperEntity);
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public String getDescription()
	{
		return "edit inheritance";
	}

}
