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

import java.awt.Point;

import application.gui.GUIComponent;

/**
 * Action for repositioning a component in the model.
 * 
 * @author Alex Munro
 * 
 */

public class RepositionComponent implements Command
{

	GUIComponent gc;
	Point startPoint, endPoint;

	/**
	 * Constructor for RepositionComponent. Start point is taken from the
	 * un-updated model component and the end point is taken from the updated
	 * GUI component.
	 * 
	 * @param gc
	 *            The GUI component that is being repositioned.
	 */
	public RepositionComponent(GUIComponent gc)
	{
		this.gc = gc;
		this.startPoint = (Point) gc.getModelPoint().clone();
		this.endPoint = (Point) gc.getLocation().clone();
	}

	/**
	 * The repositioning of the graphical component is redundant upon first
	 * executing as the user drags the component. However, this will not already
	 * be done upon subsequent executions (i.e. on redoing) so this has to be
	 * included.
	 */

	@Override
	public void execute()
	{
		gc.moveModelComponent(endPoint);
		gc.getView().updateMaxXAndY();
	}

	@Override
	public void undo()
	{
		gc.setLocation(startPoint);
		gc.moveModelComponent(startPoint);
		gc.update();
		gc.getView().updateMaxXAndY();
	}

	@Override
	public void redo()
	{
		gc.setLocation(endPoint);
		execute();
	}

	@Override
	public String getDescription()
	{
		return "reposition component";
	}

}
