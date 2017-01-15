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

package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.diagram.Attribute;
import model.diagram.Entity;
import model.systems.SystemEnum;

/**
 * Represents the conceptual model of a diagram. This is the object that is
 * saved and so contains everything necessary to reproduce a diagram.
 * <p>
 * When making changes to the members of this class, remember to update
 * @ModelXMLFileHandler to ensure that file handling still works.
 * 
 * @author Alex Munro
 *
 */

public class Model {

	private SystemEnum ts;
	private String name;
	private List<Entity> entities;
	
	/**
	 * Constructor for creating a new model
	 * @param ts The target system of the new model
	 * @param name The name of the new model
	 */
	public Model(SystemEnum ts, String name)
	{
		this.ts = ts;
		this.name = name;
		this.entities = new ArrayList<>();
	}
	
	public SystemEnum getTs() {
		return ts;
	}


	public String getName() {
		return name;
	}

	/**
	 * Gets a list of all the entities in the model
	 * @return A shallow clone of the entity list
	 */
	public List<Entity> getEntities(){
		return new ArrayList<>(entities);
	}


	/**
	 * Combines standard return types with user-defined types
	 * @return List of valid return types
	 */
	public List<String> getReturnTypes()
	{
		List<String> returnTypes = new ArrayList<>(ts.getSystem().returnTypes());
		returnTypes.addAll(getEntityNames());
		return returnTypes;
	}

	/**
	 * Does a linear search on nodes in the graph to find one with a certain name.
	 * @param name The name of the entity to be found.
	 * @return The entity object with the correct name or null if search is unsuccessful.
	 */
	public Entity getEntity(String name)
	{
		for (Entity e : entities)
		{
			if (e.getName().equals(name))
				return e;
		}
		return null;
	}

	/**
	 * Gets a list of entity set names
	 * @return An ArrayList containing the name of every entity set in the model
	 */
	public ArrayList<String> getEntityNames()
	{
		ArrayList<String> entityNames = new ArrayList<>();
		for (Entity e : entities)
		{
			entityNames.add(e.getName());
		}
		return entityNames;
	}

	/**
	 * Removes an entity from the model and removes any
	 * relationships that depend on it
	 * 
	 * @param oldEntity Entity to be removed from the model
	 */
	public void removeEntity(Entity oldEntity)
	{
		this.entities.remove(oldEntity);
		for (Entity entity : this.entities)
		{
			if (entity.inheritsFrom() == oldEntity)
				entity.setInheritsFrom(null);
			List<Attribute> oldAttributes = new ArrayList<>();
			for (Attribute a : entity.getAttributes())
			{
				if (a.getType() == oldEntity.getName())
					oldAttributes.add(a);
			}
			for (Attribute a : oldAttributes)
			{
				entity.removeAttribute(a);
			}
		}
	}
	
	/**
	 * Adds a blank entity to the table
	 * @param p The upper-left most point of the new table
	 * @return The newly created entity
	 */
	public Entity newEntity(Point p)
	{
		Entity newEntity = new Entity(this, "Table" + (entities.size() + 1), p);
		return newEntity;
	}

	/**
	 * Add an entity to the list of entities
	 * @param e The entity to be added
	 */
	public void addEntity(Entity e)
	{
		this.entities.add(e);
	}


	/**
	 * Used to keep foreign table references up to date
	 * @param oldName Previous name of a table
	 * @param newName New name of a table
	 */
	public void renameAttributes(String oldName, String newName)
	{
		for (Entity e : entities)
		{
			for (Attribute a : e.getAttributes())
			{
				if (a.getType().equals(oldName))
					a.setType(newName);
			}
		}
	}


}
