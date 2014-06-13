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
/**
 * Command that removes a component.
 * Restores the component on undo.
 * 
 * @author Alex Munro
 *
 */

public class DeleteTable implements Command{
	
	private EntityBox eb;
	

	/**
	 * 
	 * @param c Component being deleted
	 * @param view View from which the component is being deleted
	 * @param model
	 */
	public DeleteTable(EntityBox eb)
	{
		this.eb = eb;
	}

	@Override
	public void execute()
	{
		eb.setVisible(false);
		eb.getView().getModel().removeEntity(eb.getEntity());
		eb.getView().removeGUIComponent(eb);
		eb.getView().repaint();
		eb.getView().revalidate();
	}

	@Override
	public void undo()
	{
		eb.getView().getModel().addEntity(eb.getEntity());
		eb.getView().addEntityBox(eb.getEntity(), eb.getEntity().getPoint());
		eb.setVisible(true);
		eb.getView().repaint();
		eb.getView().revalidate();
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public String getDescription()
	{
		return "delete table";
	}

}
