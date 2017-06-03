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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import application.gui.dialog.EditEntityDialog;
import command.DeleteAttribute;
import model.diagram.Attribute;

/**
 * Extension of a JButton that can be placed in the EditEntityTable.
 * JButtons don't just work with JTables. Go figure.
 * 
 * @author Alex Munro
 *
 */
public class DeleteAttributeButton extends JButton implements TableCellRenderer, TableCellEditor
{
	private static final long serialVersionUID = -8253090083454815217L;
	public EntityBox entityBox;
	private Attribute a;
	private EditEntityDialog eed;
	
	public DeleteAttributeButton(EntityBox eb, Attribute a, EditEntityDialog eed)
	{
		super();
		this.entityBox = eb;
		this.a = a;
		this.eed = eed;
		this.addActionListener(e -> {
            deleteAttribute();
            DeleteAttributeButton.this.setEnabled(false);
            DeleteAttributeButton.this.setVisible(false);
        });
		this.setText("Delete");
	}
	
	public void deleteAttribute()
	{
		entityBox.getView().getController().acceptCommand(new DeleteAttribute(entityBox, a));
		eed.repaint();
	}

	@Override
	public Object getCellEditorValue()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean stopCellEditing()
	{
		return true;
	}

	@Override
	public void cancelCellEditing()
	{
	}

	@Override
	public void addCellEditorListener(CellEditorListener l){}

	@Override
	public void removeCellEditorListener(CellEditorListener l){}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		return (DeleteAttributeButton) value;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		return (DeleteAttributeButton) value;
	}


	
}