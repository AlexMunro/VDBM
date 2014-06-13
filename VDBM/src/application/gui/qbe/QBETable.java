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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.diagram.Entity;

/**
 * Table representing a query. Columns correspond to entity attributes.
 * First row is a series of checkboxes indicating whether a certain
 * column should be printed, remainder indicate query conditions.
 * @author Alex Munro
 *
 */

public class QBETable extends JTable
{

	private static final long serialVersionUID = -3896274684469320109L;
	private Entity entity;
	private boolean[] printConditions;
	private List<String[]> conditions;

	public Entity getEntity()
	{
		return entity;
	}
	
	public boolean[] getPrintConditions()
	{
		return printConditions;
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		if (row == 0)
		{
			JCheckBox cb = new JCheckBox();
			cb.setSelected((boolean) this.getValueAt(row, column));
			return new DefaultCellEditor(new JCheckBox());
		}
		else
			return super.getCellEditor(row, column);
	}

	/**
	 * Refactor out into a utility library, applicable to all Components?
	 */
	
	private class CheckBoxRenderer implements TableCellRenderer
	{
		
		private JCheckBox checkBox;
		
		public CheckBoxRenderer(JCheckBox checkBox)
		{
			this.checkBox = checkBox;
		}

		@Override
		public Component getTableCellRendererComponent(JTable arg0,
				Object arg1, boolean arg2, boolean arg3, int arg4, int arg5)
		{
			return checkBox;
		}
		
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if (row == 0)
		{
			JCheckBox cb = new JCheckBox();
			cb.setSelected((boolean) this.getValueAt(row, column));
			return new CheckBoxRenderer(cb);
		}
		else
			return super.getCellRenderer(row, column);
	}
	
	public QBETable(Entity e)
	{
		this.entity = e;
		this.conditions = new ArrayList<String[]>();
		printConditions = new boolean[e.getAllAttributes().size()];
		for (int i = 0; i < printConditions.length; i++)
		{
			printConditions[i] = false;
		}
		String[] firstEntry = new String[e.getAllAttributes().size()];
		conditions.add(firstEntry);
		this.setVisible(true);
		this.setEnabled(true);
		this.setModel(new QBETableModel());
		this.revalidate();
		this.repaint();
		this.setFillsViewportHeight(true);
		this.setFocusable(false);
		this.setRowSelectionAllowed(false);
		
	}
	
	private class QBETableModel extends AbstractTableModel
	{
		
		private static final long serialVersionUID = -8044681048126794695L;
		List<TableModelListener> listeners = new ArrayList<TableModelListener>();
		
		@Override
		public void addTableModelListener(TableModelListener tableModelListener)
		{
			this.listeners.add(tableModelListener);
		}

		@Override
		public Class<?> getColumnClass(int column)
		{
			return String.class;
		}

		@Override
		public int getColumnCount()
		{
			return entity.getAllAttributes().size();
		}

		@Override
		public String getColumnName(int column)
		{
			return entity.getAllAttributes().get(column).getName();
		}

		@Override
		public int getRowCount()
		{
			return conditions.size() + 1;
		}

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return ((row == 0)  || entity.getModel().getTs().getSystem().returnTypes().contains(entity.getAllAttributes().get(column).getType()));
		}

		@Override
		public void removeTableModelListener(TableModelListener tableListener)
		{
			listeners.remove(tableListener);
		}

		@Override
		public void setValueAt(Object o, int row, int column)
		{
			if (row > 0)
			{
				conditions.get(row-1)[column] = (String) o;
				if (row == conditions.size())
				{
					conditions.add(new String[entity.getAllAttributes().size()]);
				}
			}
			else
			{
				printConditions[column] = (boolean) o;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			if (rowIndex == 0)
			{
				return printConditions[columnIndex];
			}
			if (conditions.get(rowIndex-1)[columnIndex] == null)
			{
				return "";
			}
			return conditions.get(rowIndex-1)[columnIndex];
		}
	}
}
