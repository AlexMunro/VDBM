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

import javax.swing.JComboBox;

import application.gui.dialog.EditEntityDialog;
import model.diagram.Attribute;

/**
 * An extension of EntityTableModel that includes a
 * delete attribute button. Changes the attribute
 * type to a combobox so that it can be correctly edited.
 * @author Alex Munro
 *
 */

public class EditEntityTableModel extends EntityTableModel
{
	private EditEntityDialog eed;
	
	public EditEntityTableModel(EditEntityDialog eed, EntityBox eb)
	{
		super(eb);
		this.eed = eed;
	}
	
	@Override
	public int getColumnCount()
	{
		return super.getColumnCount() + 1;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == super.getColumnCount())
			return DeleteAttributeButton.class;
		else if (columnIndex == Attribute.TYPE)
			return JComboBox.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Attribute a = getAttributeAt(rowIndex);
		if (columnIndex == super.getColumnCount())
			return new DeleteAttributeButton(entityBox, a, eed);
		else if (columnIndex == (Attribute.TYPE))
		{
			JComboBox<String> typeComboBox = new JComboBox(entityBox.getEntity().getModel().getReturnTypes().toArray());
			if (a.getType() != null)
			{
				int j = -1;
				for (int i = 0; i < entityBox.getEntity().getModel().getReturnTypes().size(); i++)
				{
					if (entityBox.getEntity().getModel().getReturnTypes().get(i).equals(a.getType()))
						j = i;
				}
				typeComboBox.setSelectedIndex(j);
			}
			return typeComboBox;
		}
		return super.getValueAt(rowIndex, columnIndex);
	}
	
}
