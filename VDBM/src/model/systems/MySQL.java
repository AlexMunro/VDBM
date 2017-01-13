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
 * System strategy implementing support for the MySQL database system.
 * @author Alex Munro
 *
 */

public class MySQL implements SystemStrategy
{
	
	private List<String> returnTypes;
	private HashMap<Entity, List<Attribute>> junctionTables;
	
	public String getID(String s)
	{
		return s.toLowerCase() + "_id_vdbm";
	}
	
	/**
	 * Creates a junction table for many-to-many relationships
	 * @param m diagram model
	 * @param schema code list being built
	 * @param e first entity
	 * @param a attribute of type corresponding to the second entity
	 */
	private void generateJunctionTable(Model m, List<String> schema, Entity e, Attribute a)
	{
		schema.add("CREATE TABLE junction_" + e.getName() + "_" + a.getType() + "(");
		schema.add("\t" + getID(e.getName()) + " INT REFERENCES " + e.getName() + " (" + getID(e.getName()) + "),");
		schema.add("\t" + getID(a.getType()) + " INT REFERENCES " + a.getType() + " (" + getID(a.getType()) + "),");
		schema.add("\tPRIMARY KEY (" + getID(e.getName()) + ", " + getID(a.getType() + ")"));
		schema.add(")");
	}

	/**
	 * 
	 * @param schema Schema in progress
	 * @param e The entity being created
	 */
	public void generateEntity(Model m, List<String> schema, Entity e)
	{
		schema.add("CREATE TABLE " + e.getName() + "(");
		schema.add("\t" + getID(e.getName()) + " INT NOT NULL AUTO_INCREMENT,");
		List<String> foreignKeys = new ArrayList<String>();
		for (Attribute a : e.getAllAttributes())
		{	
			String attributeString = "";
			if(m.getTs().getSystem().returnTypes().contains(a.getType()))
			{
				attributeString = "\t"  + a.getName() + " " + a.getType();
			}
			else if(a.isMultiValued())
			{
				if (junctionTables.containsKey(e))
				{
					junctionTables.get(e).add(a);
				}
				else
				{
					ArrayList<Attribute> al = new ArrayList<Attribute>();
					al.add(a);
					junctionTables.put(e, al);
				}
				continue;
			}
			else
			{
				attributeString = "\t" + a.getName() + " " + " INT";
				foreignKeys.add("\t" + a.getName() + " REFERENCES " + a.getType() + "(" + getID(a.getType()) + "),");
			}
			if(a.isNotNull())
				attributeString += " NOT NULL";
			if(a.isUnique())
				attributeString += " UNIQUE";
			attributeString += ",";
			schema.add(attributeString);
		}
		schema.addAll(foreignKeys);
		schema.add("\tPRIMARY KEY(" + getID(e.getName()) + ")");
		schema.add(");");
		schema.add("");
	}
	
	private boolean dependencySatisfied(Model m, Entity entity, List<String> dumpedEntityNames)
	{
		System.out.println("Checking if " +  entity.getName() + " can be dumped.");
		for(Attribute a : entity.getAllAttributes())
		{
			if (!m.getTs().getSystem().returnTypes().contains(a.getType()))
			{
				System.out.println(a.getType() + " is not a default return type");
				if (!dumpedEntityNames.contains(a.getType()))
				{
					System.out.println(a.getType() + " not already dumped");
					if (!a.getType().equals(entity.getName()))
					{
						System.out.println("Not currently dumping " + a.getType());
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public List<String> generateSchema(Model model) throws CircularInheritanceException
	{
		// TODO Auto-generated method stub
		List<String> schema = new ArrayList<String>();
		junctionTables = new HashMap<Entity, List<Attribute>>();
		schema.add("CREATE DATABASE " + model.getName() +";");
		schema.add("USE " + model.getName() +";");
		schema.add("");
		List<String> dumpedEntityNames = new ArrayList<String>();
		while (dumpedEntityNames.size() < model.getEntities().size())
		{
			for (Entity e : model.getEntities())
			{
				System.out.println("Dumped entities: " + dumpedEntityNames.size());
				System.out.println("Model entities: " + model.getEntities().size());
				if ((!dumpedEntityNames.contains(e.getName())) && (dependencySatisfied(model, e, dumpedEntityNames)))
				{
					generateEntity(model, schema, e);
					dumpedEntityNames.add(e.getName());
				}
			}
		}
		
		schema.add("");
		System.out.println("Doing junction tables");
		System.out.println("There ams " + junctionTables.keySet().size() + " junction tables");
		for (Entity e : junctionTables.keySet())
		{
			for (Attribute a : junctionTables.get(e))
			{
				System.out.println("YOLO");
				generateJunctionTable(model, schema, e, a);
			}
		}
		System.out.println("Done!");
		return schema;
	}

	@Override
	public List<String> returnTypes()
	{
		if (returnTypes != null)
			return returnTypes;
		returnTypes = new ArrayList<String>();
		String[] types = {"INT", "VARCHAR(20)", "REAL", "BOOLEAN", "DATE"};
		returnTypes.addAll(Arrays.asList(types));
		return returnTypes;
	}

	@Override
	public TableModel entityTable(Entity e)
	{
		// TODO Auto-generated method stub
		return new mySQLTableModel(e);
	}
	
	private class mySQLTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = -1362407284986663036L;
		private Entity entity;

		@Override
		public int getColumnCount()
		{
			return 3;
		}

		@Override
		public int getRowCount()
		{
			return entity.getAttributes().size();
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
		
		public String constraintString(Attribute a)
		{
			String result = "";
			List<String> constraints = new ArrayList<String>();
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
		
		public mySQLTableModel(Entity e)
		{
			this.entity = e;
		}
		
	}

	@Override
	public List<String> generateQuery(Query q)
	{
		List<String> query = new ArrayList<String>();
		if (q.printedAttributes().size() == 0)
			query.add("SELECT * FROM " + q.getEntity().getName());
		else
		{
			String selectionLine = "SELECT ";
			for (int i = 0; i < q.printedAttributes().size(); i++)
			{
				selectionLine += q.printedAttributes().get(i).getName();
				if (i < q.printedAttributes().size() - 1)
					selectionLine += ", ";
			}
			selectionLine += " FROM " + q.getEntity().getName();
			query.add(selectionLine);
		}
		if (q.getConditions().keySet().size() > 0)
		{
			String line = "WHERE ";
			ArrayList<String> conds = new ArrayList<String>();
			for (Attribute a : q.getConditions().keySet())
			{
				for(String c : q.getConditions().get(a))
				{
					conds.add(a.getName() + " " + c);
				}
			}
			for (int i = 0; i < conds.size(); i++)
			{
				line += conds.get(i);
				if (i < conds.size() - 1)
					line += " AND ";
			}
			query.add(line);
		}
		query.add(";");
		return query;
	}

}
