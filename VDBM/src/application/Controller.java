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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.Model;
import model.diagram.Attribute;
import model.diagram.Entity;
import application.gui.View;
import command.*;

/**
 * Controller that mediates between the view and the model.
 * This class should be called whenever the view (window)
 * wants to change an existing model.
 * 
 * Created when a new model is created or loaded.
 * 
 * @author Alex Munro
 *
 */

public class Controller implements TableModelListener
{
	private Model model;
	private View view;
	private CommandManager commandManager;
	private List<Relationship> associations;
	private List<Relationship> inheritances;
	
	public List<Relationship> getAssociations()
	{
		return associations;
	}
	
	public List<Relationship> getInheritances()
	{
		return inheritances;
	}
	
	public View getView()
	{
		return this.view;
	}
	
	public Model getModel()
	{
		return this.model;
	}
	
	public void acceptCommand(Command c)
	{
		commandManager.addCommand(c);
		view.setUndoable(true, c.getDescription());
		view.setRedoable(false, null);
	}
	
	public void undo()
	{
		commandManager.redoStack.add(commandManager.undoStack.peek());
		commandManager.undo();
		if (commandManager.undoStack.isEmpty())
		{
			view.setUndoable(false, null);
		}
		else
		{
			view.setUndoable(true, commandManager.undoStack.peek().getDescription());
		}
		view.setRedoable(true, commandManager.redoStack.peek().getDescription());
	}
	
	public void redo()
	{
		commandManager.undoStack.add(commandManager.redoStack.peek());
		commandManager.redo();
		if (commandManager.redoStack.isEmpty())
		{
			view.setRedoable(false, null);
		}
		view.setUndoable(true, commandManager.undoStack.peek().getDescription());
	}
	
	public Controller(Model model, View view)
	{
		this.model = model;
		this.view = view;
		commandManager = new CommandManager();
		this.associations = new ArrayList<>();
		this.inheritances = new ArrayList<>();
	}

	
	/**
	 * Make sure this method is invoked every time attributes are changed.
	 * Removal/addition of attributes and entities
	 * 
	 * Do not invoke this before the view has finished loading!
	 */
	public void updateRelationships()
	{
		inheritances = new ArrayList<>();
		associations = new ArrayList<>();
		for (Entity e : model.getEntities())
		{
			if (e.isSubclass())
			{
				inheritances.add(new Relationship(view.getEntityBox(e), view.getEntityBox(e.inheritsFrom()), null));
			}
			for(Attribute a : e.getAttributes())
			{
				if (!(model.getTs().getSystem().returnTypes().contains(a.getType()))) // i.e. if the return type is not standard
				{
					associations.add(new Relationship(view.getEntityBox(e), view.getEntityBox(model.getEntity(a.getType())), a));
				}
			}
		}
		view.updateRelationships(inheritances, associations);
	}

	private class CommandManager {

		Stack<Command> undoStack = new Stack<>();
		Stack<Command> redoStack = new Stack<>();
		
		public void addCommand(Command c)
		{
			c.execute();
			undoStack.add(c);
			// The timeline has been altered
			// Undone commands no longer hold
			redoStack = new Stack<>();
			updateRelationships();
		}
		
		public void undo()
		{
			Command c = undoStack.pop();
			c.undo();
			redoStack.add(c);
			updateRelationships();
		}
		
		public void redo()
		{
			Command c = redoStack.pop();
			c.redo();
			undoStack.add(c);
			updateRelationships();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		
	}

	
}
