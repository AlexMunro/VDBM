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
import application.gui.View;
import model.Model;
import model.diagram.Entity;

public class CreateTable implements Command
{
	private View view;
	private Model model;
	private Entity entity;
	private EntityBox entityBox;

	public CreateTable(View view, Model model, Entity e)
	{
		this.view = view;
		this.model = model;
		this.entity = e;
	}

	@Override
	public void execute()
	{
		this.entityBox = view.addEntityBox(entity, entity.getPoint());
		model.addEntity(entity);
		view.updatePanel();
	}

	@Override
	public void undo()
	{
		model.removeEntity(entity);
		view.removeEntityBox(entityBox);
		entityBox.setVisible(false);
		view.updatePanel();
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public String getDescription()
	{
		return "create table";
	}

}
