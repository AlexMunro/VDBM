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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import model.diagram.Attribute;
import model.diagram.Entity;

/**
 * A frame representing one table in a QBEWindow. Contains a QBETable.
 * @author Alex Munro
 *
 */
public class QBEBox extends JInternalFrame
{

	private static final long serialVersionUID = -8312419989412489231L;
	private JScrollPane scrollPane;
	private QBETable table;
	
	/**
	 * Generates a QBEBox for an entity
	 * @param e Entity being modelled
	 */
	public QBEBox(Entity e, int initX, int initY)
	{
		this.setTitle(e.getName());
		this.setResizable(true);
		this.table = new QBETable(e);
		scrollPane = new JScrollPane();
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
		this.setLocation(initX, initY);
		this.validate();
		this.pack();
		this.setSize(this.getPreferredSize());
		scrollPane.setViewportView(table);
		this.setSize(200,200);
	}
	
	public List<Attribute> getPrintedAttributes()
	{
		List<Attribute> printList = new ArrayList<>();
		for (int i = 0; i < table.getEntity().getAllAttributes().size(); i++)
		{
			if (table.getPrintConditions()[i])
				printList.add(table.getEntity().getAllAttributes().get(i));
		}
		return printList;
	}

	public HashMap<Attribute, List<String>> getMap()
	{
		HashMap<Attribute, List<String>> map = new HashMap<>();
		for (int col = 0; col < table.getColumnCount(); col++)
		{
			List<String> queries = new ArrayList<>();
			for (int row = 1; row < table.getRowCount(); row++)
			{
				if ((table.getValueAt(row, col) != null) && (table.getValueAt(row, col).toString().length() != 0))
				{
					queries.add(table.getValueAt(row, col).toString());
				}
			}
			if (queries.size() != 0)
				map.put(table.getEntity().getAllAttributes().get(col), queries);
		}
		return map;
	}

	
}
