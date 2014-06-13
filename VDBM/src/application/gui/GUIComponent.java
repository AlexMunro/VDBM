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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import model.diagram.Component;

/**
 * A superclass for all implemented GUI panel-style components that
 * implements common functionality (dragging and dropping and
 * double-click detection)
 * @author Alex Munro
 *
 */

@SuppressWarnings("serial") // This is part of the view and so doesn't need to be serialised.
public abstract class GUIComponent extends JPanel{


	protected abstract void onDoubleClick();
	protected abstract void onDelete();
	
	private Point clickPoint;
	private View view;
	private MouseHandler mouseAdapter;
	
	
	public abstract Component getComponent();
	
	public Point getModelPoint()
	{
		return this.getComponent().getPoint();
	}
	
	public View getView()
	{
		return this.view;
	}
	
	protected MouseInputAdapter getMouseAdapter()
	{
		return this.mouseAdapter;
	}
	
	/**
	 * Centre of the component
	 * @return Centre point
	 */
	public Point getMidPoint()
	{
		Point midPoint = new Point();
		midPoint.x = this.getX() + (this.getWidth() / 2);
		midPoint.y = this.getY() + (this.getHeight() / 2);
		return midPoint;
	}
	
	public GUIComponent(View view, Point p)
	{
		this.view = view;
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLocation(p);
		mouseAdapter = new MouseHandler();
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
	}
	
	private class MouseHandler extends MouseInputAdapter
	{
		private static final int DELAY = 500;
		private long lastClick = 0;
		private boolean justDragged = false;
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			Point pointDragged = e.getPoint();
			Point loc = getLocation();
			loc.translate(pointDragged.x - clickPoint.x, pointDragged.y - clickPoint.y);
			setLocation(loc);
			justDragged = true;
		}
		
		
		/**
		 * Instead of updating the component's location information in the
		 * model as the component is being dragged, it is better to wait until
		 * a dragging action has been completed and then sending.
		 * 
		 * This allows for the movement to be seen as a single action which
		 * allows movements to be undone/redone.
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (justDragged)
			{
				view.moveComponent(GUIComponent.this);
				justDragged = false;
			}
		}
		
		@Override
		public void mousePressed(final MouseEvent e)
		{
			clickPoint = e.getPoint();
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(view.isDeleteBtnPressed())
			{
				onDelete();
				view.resetBtnSelection();
			}
			else if (e.getWhen() - lastClick < DELAY)
			{
				onDoubleClick();
			}
			else
				lastClick = e.getWhen();
		}
	}
	public void update()
	{
		repaint();
		revalidate();
		this.getParent().repaint();
		this.getParent().revalidate();
	}
	
	public void moveModelComponent(Point endPoint)
	{
		this.setLocation(endPoint);
		getComponent().setPos(endPoint);
		update();
	}
	
}
