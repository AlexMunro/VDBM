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

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Model;

/**
 * Represents a table in the conceptual model.
 * 
 * Implements TableModel methods to communicate with JTables from the view.
 * 
 * @author Alex
 *
 */

public class Entity extends Component implements Serializable{

	
	private static final long serialVersionUID = 7524813382347793342L;
	private String name;
	private ArrayList<Attribute> attributes = new ArrayList<>();
	private Entity inheritedEntity;
	
	private Model model; // reference back to overall model
	
	public Model getModel()
	{
		return this.model;
	}
	
	public Entity inheritsFrom()
	{
		return this.inheritedEntity;
	}
	
	public void setInheritsFrom(Entity e)
	{
		this.inheritedEntity = e;
	}
	
	public String getName()
	{
		return name.replaceAll(" ", "_");
	}
	
	public void rename(String name) {
		this.name = name;
	}
	
	public Entity(Model model, String defaultName, Point p)
	{
		super(p);
		this.model = model;
		this.name = defaultName;
	}

	public List<Attribute> getAttributes()
	{
		return attributes;
	}
	
	public void addAttribute(Attribute attribute)
	{
		attributes.add(attribute);
	}

	public void removeAttribute(Attribute attribute)
	{
		attributes.remove(attribute);
	}
	
	public boolean isSubclass()
	{
		return !(this.inheritedEntity == null);
	}
	
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Returns directly owned attributes as well as attributes obtained from inheritance
	 * @return list of all applicable attributes
	 */
	public List<Attribute> getAllAttributes()
	{
		if (!this.isSubclass())
			return this.getAttributes();
		else
		{
			Set<Attribute> fullAttributeSet = new LinkedHashSet<>(this.getAttributes());
			fullAttributeSet.addAll(this.inheritsFrom().getAllAttributes()); // Woo! Recursion!
			return new ArrayList<>(fullAttributeSet);
		}
	}
	
}
