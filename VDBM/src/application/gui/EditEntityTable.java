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

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.diagram.Attribute;

/**
 * The JTable that represents an entity's attributes in the
 * edit entity dialogue.
 * 
 * @author Alex Munro
 *
 */
public class EditEntityTable extends JTable
{
	
	private static final long serialVersionUID = 4559133085551542549L;

	public EditEntityTable(EditEntityTableModel entityTableModel)
	{
		super(entityTableModel);
		this.getColumnModel().getColumn(Attribute.TYPE).setCellEditor(new DefaultCellEditor(new JComboBox()));
		this.setRowSelectionAllowed(false);
	}
	
	private class JComboBoxRenderer implements TableCellRenderer
	{

		private JComboBox comboBox;
		
		public JComboBoxRenderer(JComboBox comboBox)
		{
			this.comboBox = comboBox;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			// TODO Auto-generated method stub
			return comboBox;
		}
		
	}
	
	@Override 
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if (column == Attribute.COLUMN_COUNT)
		{
			return (DeleteAttributeButton) this.getValueAt(row, column);
		}
		else if (column == Attribute.TYPE)
		{
			return new JComboBoxRenderer((JComboBox) this.getValueAt(row, column));
		}
		else
			return super.getCellRenderer(row, column);
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		if(column == Attribute.COLUMN_COUNT)
			return (DeleteAttributeButton) this.getValueAt(row, column);
		Object o = super.getValueAt(row, column);
		if (o instanceof JComboBox)
		{
			return new DefaultCellEditor((JComboBox) o);
		}
		return super.getCellEditor(row, column);
	}

}
