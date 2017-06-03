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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import command.DeleteTable;
import application.gui.dialog.EditEntityDialog;
import model.diagram.Component;
import model.diagram.Entity;

/**
 * The Swing component that represents an entity.
 * It is bound to an Entity object from the model.
 * 
 * @author Alex Munro
 *
 */

public class EntityBox extends GUIComponent{
	
	private static final long serialVersionUID = 1099971071003807485L;
	private JLabel label = new JLabel();
	private JTable attributeTable;
	private Entity entity;
	
	/**
	 * Populates the tree with attributes and constraints
	 */
	
	public Entity getEntity()
	{
		return entity;
	}
	
	@Override
	public Component getComponent()
	{
		return getEntity();
	}

	/**
	 * This class passes mouse events on an EntityBox's table to
	 * the GUIComponent class so that mouse clicks on the table can
	 * be used to drag and open the edit entity dialog.
	 * 
	 * @author Alex Munro
	 */
	private class TableMouseHandler extends MouseInputAdapter
	{
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			EntityBox.this.getMouseAdapter().mouseDragged(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			EntityBox.this.getMouseAdapter().mouseReleased(e);
		}
		
		@Override
		public void mousePressed(final MouseEvent e)
		{
			EntityBox.this.getMouseAdapter().mousePressed(e);
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			EntityBox.this.getMouseAdapter().mouseClicked(e);
		}
	}

	public EntityBox(Entity entity, View w, Point p)
	{
		super(w, p);
		this.entity = entity;
		this.setLayout(new BorderLayout());
		
		
		this.add(label, BorderLayout.NORTH);
		label.setText(entity.getName());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.attributeTable = new JTable(w.getModel().getTs().getSystem().entityTable(entity)){
		
			private static final long serialVersionUID = 7027417737228700444L;

			@Override
			public java.awt.Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				java.awt.Component c = super.prepareRenderer(renderer, row, column);
				int rendererWidth = c.getPreferredSize().width;
				TableColumn tc = getColumnModel().getColumn(column);
				tc.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tc.getPreferredWidth()));
				return c;
			}
			
			@Override
			public void paint(Graphics g)
			{
				super.paint(g);
				for (int i = 0; i < getColumnCount(); i++)
				{
					getColumnModel().getColumn(i).setWidth(20);
					this.setSize(this.getPreferredSize());
					getColumnModel().getColumn(i).setWidth(getColumnModel().getColumn(i).getMaxWidth());
				}
				this.validate();
				this.setSize(this.getPreferredSize());
			}
		};
		attributeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.add(attributeTable, BorderLayout.CENTER);
		TableMouseHandler tableHandler = new TableMouseHandler();
		attributeTable.addMouseListener(tableHandler);
		attributeTable.addMouseMotionListener(tableHandler);
		attributeTable.setVisible(true);
		attributeTable.setEnabled(false);
		attributeTable.setRowSelectionAllowed(false);
		
		this.revalidate();
		repaint();
		getView().updatePanel();
		//TableUtility.setAutoResizable(attributeTable);
	}
	
	public Point getCentre()
	{
		int x = (int) (this.getLocation().getX() + (this.getWidth() / 2));
		int y = (int) (this.getLocation().getY() + (this.getHeight() / 2));
		return new Point(x,y);
	}
	
	public String getName()
	{
		return this.label.getText();
	}
	
	public void setName(String name)
	{
		this.label.setText(name);
		this.getEntity().rename(name);
	}
	
	@Override
	public void update(Graphics g)
	{
		super.update();
		this.paint(g);
		validate();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Resize table columns
		attributeTable.repaint();
		this.setSize(this.getPreferredSize());
	}
	
	@Override
	public void repaint()
	{
		
		this.validate();
		this.setSize(this.getPreferredSize());
		super.repaint();
	}

	@Override
	public void onDoubleClick() {
		this.repaint();
		new EditEntityDialog(this);
	}


	@Override
	protected void onDelete()
	{
		this.getView().getController().acceptCommand(new DeleteTable(this));
	}
	
	public String toString()
	{
		return entity.toString();
	}
	
	/**
	 * Adds a listener to this entitybox's tablemodel,
	 * relaying changes to the underlying entity
	 */
	public void addTableModelListener(TableModelListener tml)
	{
		this.attributeTable.getModel().addTableModelListener(tml);
	}

}
