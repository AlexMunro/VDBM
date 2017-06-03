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

import application.exception.CircularInheritanceException;
import application.gui.qbe.Query;
import model.Model;
import model.diagram.Attribute;
import model.diagram.Entity;
import model.systems.SystemStrategy;

/**
 * System strategy providing support for XFDM.
 * 
 * @author Alex Munro
 *
 */

public class XFDM implements SystemStrategy {
	
	private int declaredConstraints;
	private List<Attribute> undeclaredAttributes; // Contains undeclared attributes and their constraints
	private List<Entity> dumpedEntities;
	private HashMap<Attribute, String> attributeStrings; // stores the attribute(entity) style for declaration
	private List<String> returnTypes;
	
	/** If an attribute cannot be immediately declared it is put in
	 * this list and dumped at the end.
	 */
	
	private boolean checkInheritanceDependency(Entity e)
	{
		if (!e.isSubclass())
			return true;
		if (dumpedEntities.contains(e.inheritsFrom()))
			return true;
		return false;
	}
	
	private boolean canDeclareAttribute(Attribute a)
	{
		if (returnTypes().contains(a.getType()))
			return true;
		for (Entity e : dumpedEntities)
		{
			if (e.getName().equals(a.getName()))
				return true;
		}
		return false;
	}

	/**
	 * String that declares a new entity set
	 * @param e The newly declared entity
	 * @return XFDM declaration line
	 */
	private String declareEntity(Entity e)
	{
		if (e.isSubclass())
			return "declare " + e.getName() + "() ->> " + e.inheritsFrom() + ";";
		else
			return "declare " + e.getName() + "() ->> entity;";
	}
	
	private String attributeArrow(Attribute a)
	{
		if (a.isMultiValued())
			return " ->> ";
		else
			return " -> ";
	}
	
	private String declareAttribute(Attribute a)
	{
		return "declare " + attributeStrings.get(a)  + attributeArrow(a) + a.getType() + ";";
	}
	
	private List<String> declareAttributes(Entity e)
	{
		List<String> attributeDeclarations = new ArrayList<>();
		List<String> constraints = new ArrayList<>();
		for (Attribute a : e.getAttributes())
		{
			String attributeDeclaration = declareAttribute(a);
			if (canDeclareAttribute(a))
			{
				attributeDeclarations.add(attributeDeclaration);
				constraints.addAll(declareConstraints(a));
			}
			else
				undeclaredAttributes.add(a);
		}
		attributeDeclarations.addAll(constraints);
		return attributeDeclarations;
	}
	
	private List<String> declareConstraints(Attribute a)
	{
		List<String> constraintDeclarations = new ArrayList<>();
		if (a.isUnique())
		{
			constraintDeclarations.add("constraint cu" + declaredConstraints + " on " + attributeStrings.get(a) + " -> unique;");
			declaredConstraints++;
		}
		if (a.isNotNull())
		{
			constraintDeclarations.add("constraint cu" + declaredConstraints + " on " + attributeStrings.get(a) + " -> total;");
			declaredConstraints++;
		}
		if (a.isFixed())
		{
			constraintDeclarations.add("constraint cu" + declaredConstraints + " on " + attributeStrings.get(a) + " -> fixed;");
			declaredConstraints++;
		}
		return constraintDeclarations;
	}
	
	/**
	 * Returns the XFDM definition of a single entity set
	 * @param e Entity to be defined and declared
	 */
	private List<String> generateEntityCode(Entity e, List<Entity> dumpedEntities)
	{
		List<String> entityCode = new ArrayList<>();
		
		entityCode.add(declareEntity(e));
		
		// Then declare all attributes (where their types are already defined)
		// and their constraints.
		
		entityCode.addAll(declareAttributes(e));
		
		entityCode.add("");
		
		return entityCode;
	}

	@Override
	public List<String> generateSchema(Model model) throws CircularInheritanceException
	{

		declaredConstraints = 0;
		undeclaredAttributes = new ArrayList<>();
		dumpedEntities = new ArrayList<>();
		List<Entity> undumpedEntities = new ArrayList<>();
		declaredConstraints = 0;
		attributeStrings = new HashMap<>();
		undumpedEntities.addAll(model.getEntities());
		
		for(Entity e : model.getEntities())
		{
			for (Attribute a : e.getAttributes())
			{
				attributeStrings.put(a, a.getName() + "(" + e.getName() + ")");
			}
		}
		
		List<String> generatedCode = new ArrayList<>();
	
		// Iterate until all entities are dumped or circular inheritance dependency
		// is apparent
		while(undumpedEntities.size() != 0)
		{
			int newEntitiesDumped = 0;
			List<Entity> justDumped = new ArrayList<>();
			for (Entity e: undumpedEntities)
			{
				if (checkInheritanceDependency(e))
				{
					generatedCode.addAll(generateEntityCode(e, dumpedEntities));
					justDumped.add(e);
					newEntitiesDumped++;
				}
			}
			undumpedEntities.removeAll(justDumped);
			dumpedEntities.addAll(justDumped);
			// If an entire pass of entities can be done without making progress
			// there is a circular inheritance error and code generation should
			// be aborted.
			if (newEntitiesDumped == 0)
			{
				throw new CircularInheritanceException(undumpedEntities);
			}
		}
		
		for (Attribute a : undeclaredAttributes)
		{
			generatedCode.add(declareAttribute(a));
			generatedCode.addAll(declareConstraints(a));
			generatedCode.add("");
		}
		//clearGenerationFields();
		return generatedCode;
	}
	@Override
	public List<String> returnTypes()
	{
		if (returnTypes != null)
			return returnTypes;
		returnTypes = new ArrayList<>();
		String[] types = {"Integer", "String", "Real", "Boolean", "Entity", "Char"};
		returnTypes.addAll(Arrays.asList(types));
		return returnTypes;
	}
	
	private class xfdmTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = -4452704591401705502L;

		private Entity entity;
		
		public xfdmTableModel(Entity entity)
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
	public TableModel entityTable(Entity entity)
	{
		return new xfdmTableModel(entity);
	}
	
	private String attributeCall(Attribute a)
	{
		return a.getName() + "(e)";
	}

	@Override
	public List<String> generateQuery(Query q)
	{
		List<String> query = new ArrayList<>();
		String line = "for each e in " + q.getEntity().getName() + " such that ";
		ArrayList<String> conds = new ArrayList<>();
		for (Attribute a : q.getConditions().keySet())
		{
			for(String c : q.getConditions().get(a))
			{
				conds.add(attributeCall(a) + " " + c);
			}
		}
		for (int i = 0; i < conds.size(); i++)
		{
			line += conds.get(i);
			if (i < conds.size() - 1)
				line += " and ";
		}
		query.add(line);
		String printLine = "print ";
		if (q.printedAttributes().size() == 0)
		{
			printLine += "e";
		}
		else
		{
			for (int i = 0; i < q.printedAttributes().size(); i++)
			{
				printLine += q.printedAttributes().get(i).getName();
				if (i < (q.printedAttributes().size() -1))
					printLine += ", ";
			}
		}
		printLine += ";";
		query.add(printLine);
		return query;
	}

}
