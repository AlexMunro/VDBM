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

package application.gui;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import command.EditAttribute;
import model.diagram.Attribute;

/**
 * Wraps an entity from the model in a table model
 * 
 * @author Alex Munro
 *
 */

public class EntityTableModel implements TableModel
{
	
	protected EntityBox entityBox;
	
	private ArrayList<TableModelListener> listeners = new ArrayList<>();

	
	public EntityTableModel(EntityBox eb)
	{
		this.entityBox = eb;
	}
	
	@Override
	public int getRowCount()
	{
		return entityBox.getEntity().getAttributes().size();
	}

	@Override
	public int getColumnCount()
	{
		return Attribute.COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex){
			case Attribute.NAME:
				return "Name";
			case Attribute.TYPE:
				return "Type";
			case Attribute.UNIQUE:
				return "Unique";
			case Attribute.NOT_NULL:
				return "Not null";
			case Attribute.FIXED:
				return "Fixed";
			case Attribute.MULTI:
				return "Multi-valued";
			default:
				return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch(columnIndex){
			case Attribute.NAME:
				return String.class;
			case Attribute.TYPE:
				return String.class;
			case Attribute.UNIQUE:
				return Boolean.class;
			case Attribute.NOT_NULL:
				return Boolean.class;
			case Attribute.FIXED:
				return Boolean.class;
			case Attribute.MULTI:
				return Boolean.class;
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	
	public Attribute getAttributeAt(int row)
	{
		return entityBox.getEntity().getAttributes().get(row);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Attribute a = getAttributeAt(rowIndex);

		switch (columnIndex)
		{
			case Attribute.NAME:
				return a.getName();
			case Attribute.TYPE:
				return a.getType();
			case Attribute.UNIQUE:
				return a.isUnique();
			case Attribute.NOT_NULL:
				return a.isNotNull();
			case Attribute.FIXED:
				return a.isFixed();
			case Attribute.MULTI:
				return a.isMultiValued();
			default:
				return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		Attribute a = entityBox.getEntity().getAttributes().get(rowIndex);
		entityBox.getView().getController().acceptCommand(new EditAttribute(a, columnIndex, aValue));
	}

	@Override
	public void addTableModelListener(TableModelListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l)
	{
		listeners.remove(l);
	}	

}
