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

import model.Model;
import application.gui.EntityBox;

/**
 * Class that renames a table and renames references to
 * that table throughout the model.
 * 
 * @author Alex Munro
 *
 */

public class RenameTable implements Command {

	private EntityBox entityBox;
	private String oldName, newName;
	private Model model;
	
	public RenameTable(Model model, EntityBox entityBox, String newName)
	{
		this.model = model;
		this.entityBox = entityBox;
		this.oldName = entityBox.getEntity().getName();
		this.newName = newName;
	}

	@Override
	public void execute() {
		entityBox.setName(newName);
		model.renameAttributes(oldName, newName);
		entityBox.update();
	}

	@Override
	public void undo() {
		entityBox.getEntity().rename(oldName);
		entityBox.setName(oldName);
		model.renameAttributes(oldName, newName);
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
		return "rename table";
	}

}
