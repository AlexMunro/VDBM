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

package model.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.Model;
import model.diagram.Attribute;
import model.diagram.Entity;
import application.exception.CircularInheritanceException;
import application.gui.qbe.Query;

/**
 * System strategy implementing support for Oracle9G's
 * object-relation features. Currently incomplete.
 * 
 * @author Alex Munro
 *
 */
public class Oracle9G implements SystemStrategy
{
	private List<String> returnTypes;

	private HashMap<Entity, Attribute> undeclaredAttributes;
	List<String> validReturnTypes;

	private List<String> generatedCode;
	
	
	@Override
	public List<String> generateSchema(Model model) throws CircularInheritanceException
	{
		generatedCode = new ArrayList<>();
		List<Entity> undumpedEntities = new ArrayList<>(model.getEntities());
		validReturnTypes = new ArrayList<>(returnTypes());
		while (!undumpedEntities.isEmpty())
		{
			for (Entity e : undumpedEntities)
			{
				if (!e.isSubclass() || (!undumpedEntities.contains(e.inheritsFrom())))
				{
					dumpEntity(e);
					undumpedEntities.remove(e);
				}
			}
		}
		for (Entity e : undeclaredAttributes.keySet())
		{
			//unfinished
		}
		return generatedCode;
	}
	
	private void dumpEntity(Entity e)
	{
		validReturnTypes.add(e.getName());
		if (e.isSubclass())
			generatedCode.add("CREATE TYPE " + e.getName() + " UNDER " + e.inheritsFrom().getName());
		else
			generatedCode.add("CREATE TYPE " + e.getName() + " AS OBJECT");
		generatedCode.add("(");
		List<Attribute> declaredAttributes = new ArrayList<>();
		for (Attribute a : e.getAttributes())
		{
			if (validReturnTypes.contains(a.getType()))
			{
				if(returnTypes().contains(a.getType()))
				{
					if (a.isMultiValued())
						undeclaredAttributes.put(e,a);
					else
					{
						generatedCode.add(a.getName() + " " + a.getType() + ",");
						declaredAttributes.add(a);
					}
				}
				else
				{
					if (a.isMultiValued())
						undeclaredAttributes.put(e,a); // TODO: handle multi-valued
					else
					{
						generatedCode.add(a.getName() + " REF " + a.getType() + ",");
						declaredAttributes.add(a);
					}
				}
			}
			else
				undeclaredAttributes.put(e, a);
		}
		
		// Remove the final, unnecessary comma!
		String last = generatedCode.remove(generatedCode.size() - 1);
		if(last.charAt(last.length() - 1) == ',')
			last = last.substring(0, last.length() - 1);
		generatedCode.add(last);
		
		generatedCode.add(");");
		generatedCode.add("/");
		generatedCode.add("");
	}

	@Override
	public List<String> returnTypes()
	{
		if (returnTypes != null)
			return returnTypes;
		String[] types = {"CHAR", "VARCHAR2", "NUMBER", "DATE"};
		returnTypes = new ArrayList<>(Arrays.asList(types));
		return returnTypes;
	}
	
	private class oracleTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = -3221238283417466204L;
		private Entity entity;
		
		public oracleTableModel(Entity entity)
		{
			this.entity = entity;
		}
		
		@Override
		public int getRowCount()
		{
			return entity.getAttributes().size();
		}

		@Override
		public int getColumnCount()
		{
			// Function name, function type, constraints
			return 3;
		}
		
		/**
		 * Groups a list of strings into a single string, separated by commas
		 * @param strings A list of the strings to be grouped
		 */
		public String constraintString(Attribute a)
		{
			//TODO: investigate these
			String result = "";
			List<String> constraints = new ArrayList<>();
			if (a.isUnique())
				constraints.add("unique");
			if (a.isNotNull())
				constraints.add("not null");
			if (a.isFixed())
				constraints.add("fixed");
			for(int i = 0; i < constraints.size(); i++)
			{
				result += constraints.get(i);
				if (i < (constraints.size() - 1))
					result += ", ";
			}
			
			return result;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Attribute a = entity.getAttributes().get(rowIndex);
			switch(columnIndex)
			{
				case 0: // function name
					if(a.isUnique() && a.isNotNull())
						return ("<html><b><u>" + a.getName() + "</u></b></html>");
					else
						return a.getName();
				case 1:
					return a.getType();
				case 2:
					return constraintString(a);
			}
			return null;
		}
	}

	@Override
	public TableModel entityTable(Entity e)
	{
		return new oracleTableModel(e);
	}

	@Override
	public List<String> generateQuery(Query q)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
