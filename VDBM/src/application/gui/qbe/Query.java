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

package application.gui.qbe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.diagram.Attribute;
import model.diagram.Entity;

/**
 * Objects that represent queries formed in a query by example table.
 * @author Alex Munro
 *
 */
public class Query
{

	private Entity mainEntity;

	private HashMap<Attribute, List<String>> attributeQueries;
	private List<Attribute> printedColumns;
	
	public Query(Entity e, List<Attribute> printedColumns, HashMap<Attribute, List<String>> aq)
	{
		mainEntity = e;
		this.printedColumns = printedColumns;
		attributeQueries = aq;
	}

	public HashMap<Attribute, List<String>> getAttributeQueries()
	{
		return attributeQueries;
	}
	
	public Entity getEntity()
	{
		return this.mainEntity;
	}
	
	public List<Attribute> printedAttributes()
	{
		return printedColumns;
	}
	
	public HashMap<Attribute, List<String>> getConditions()
	{
		HashMap<Attribute, List<String>> conditions = new HashMap<Attribute, List<String>>();
		for (Attribute a : attributeQueries.keySet())
		{
			List<String> c = new ArrayList<String>();
			for (String s : attributeQueries.get(a))
			{
				if ((s != null) && (!s.equals("")) && (! s.equals("print")))
					c.add(s);
			}
			if(c.size() > 0)
				conditions.put(a, c);
		}
		return conditions;
	}
	
}
