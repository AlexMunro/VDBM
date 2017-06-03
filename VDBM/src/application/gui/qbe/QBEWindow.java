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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Model;
import model.diagram.Entity;

/**
 * The window that pops up after the QueryByExample dialog.
 * Contains the QBE tables and is responsible for passing
 * a query object to the system strategy.
 * <p>
 * This feature has so far only been implemented as a 
 * "proof of concept" - much more work should be done
 * to make this feature properly usable.
 * 
 * @author Alex Munro
 *
 */
public class QBEWindow extends JFrame
{

	private static final long serialVersionUID = 1410509712694289229L;

	private JDesktopPane desktopPane;
	private JPanel btnPane;
	private List<QBEBox> boxes = new ArrayList<>();
	
	private Model model;
	
	private Entity firstEntity; // Hacky, sorry!
	
	public QBEWindow(Model model, Entity firstEntity)
	{
		this.setTitle("Query by example builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		getContentPane().setLayout(new BorderLayout());
		
		desktopPane = new JDesktopPane();
		desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		desktopPane.setBackground(Color.WHITE);
		
		getContentPane().add(desktopPane, BorderLayout.CENTER);
		btnPane = new JPanel();
		btnPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(arg0 -> {
            setVisible(false);
            dispose();
        });
		
		getContentPane().add(btnPane, BorderLayout.SOUTH);
		
		JButton btnBuildQuery = new JButton("Build query");
		btnBuildQuery.addActionListener(e -> buildQuery());
		btnPane.add(btnBuildQuery);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(arg0 -> {
            QBEWindow.this.setEnabled(false);
            QBEWindow.this.setVisible(false);
            QBEWindow.this.dispose();
        });
		btnPane.add(btnCancel);
		
		this.model = model;
		this.firstEntity = firstEntity;
		
		boxes.add(new QBEBox(firstEntity, 10, 10));
		desktopPane.add(boxes.get(0));
		desktopPane.revalidate();
		
		this.setVisible(true);
		this.setEnabled(true);
		
	}
	
	public void addNewBox(QBEBox newBox)
	{
		this.boxes.add(newBox);
		//TODO: add to panel?
	}
	
	public List<QBEBox> getQBEBoxes()
	{
		return this.boxes;
	}
	
	private Query prepareQuery()
	{
		Query q = new Query(firstEntity, boxes.get(0).getPrintedAttributes(), boxes.get(0).getMap());
		return q;
	}
	
	public void buildQuery()
	{
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setSelectedFile(new File(model.getName() + " query.txt"));
		int saveResult = fileChooser.showSaveDialog(this);
		if (saveResult == JFileChooser.APPROVE_OPTION)
		{
			try{
				List<String> outputQuery = model.getTs().getSystem().generateQuery(prepareQuery());
				PrintWriter out = new PrintWriter(fileChooser.getSelectedFile());
				for (String line : outputQuery)
				{
					out.println(line);
				}
				out.flush();
				out.close();
				JOptionPane.showMessageDialog(this, "Query successfully saved to " + fileChooser.getSelectedFile().toString());
			}
			catch (FileNotFoundException e)
			{
				JOptionPane.showMessageDialog(this, "Could not save query due to IO problems.");
				System.err.println("Could not access file");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} 
		}
	}

}
