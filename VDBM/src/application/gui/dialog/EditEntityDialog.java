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

package application.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import model.diagram.Entity;
import application.gui.EditEntityTable;
import application.gui.EditEntityTableModel;
import application.gui.EntityBox;
import application.gui.GUIComponent;
import application.gui.View;
import command.EditInheritance;
import command.RenameTable;

import java.awt.GridLayout;

import javax.swing.SwingConstants;

/**
 * Dialog for editing the attributes, name and parent of
 * an entity set.
 * @author Alex Munro
 *
 */

@SuppressWarnings("serial") // This is part of the view so doesn't need to be serialised.
public class EditEntityDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private EntityBox parent;
	private JTable table;
	private JTextField entityName;
	private JComboBox<Entity> inheritanceBox;


	public Entity getEntity()
	{
		return (Entity) this.getParent().getComponent();
	}
	
	public GUIComponent getParent()
	{
		return this.parent;
	}
	
	public void renameEntity()
	{
		//TODO: Add some kind of mechanism to prevent duplicate entity set names
		getView().getController().acceptCommand(new RenameTable(parent.getView().getModel(), parent, entityName.getText()));
		this.setTitle("Edit attributes of " + parent.getEntity().getName());
	}
	
	public View getView()
	{
		return parent.getView();
	}
	
	/**
	 * Dialog constructor
	 * @param parent Reference to parent entityBox for access to model information
	 */
	public EditEntityDialog(EntityBox parent) {
		this.parent = parent;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		setBounds(100, 100, 500, 200);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setTitle("Edit attributes of " + parent.getEntity().getName());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel nameLbl = new JLabel("Name:");
		nameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		entityName = new JTextField(parent.getEntity().getName(), 15);
		entityName.setHorizontalAlignment(SwingConstants.LEFT);
		
		ActionListener renameListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				renameEntity();
			}
		};
		
		entityName.addActionListener(renameListener);
		JButton renameBtn = new JButton("Rename");
		renameBtn.addActionListener(renameListener);
		JPanel northLeftPane = new JPanel();
		northLeftPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		northLeftPane.add(nameLbl);
		northLeftPane.add(entityName);
		northLeftPane.add(renameBtn);
		
		inheritanceBox = new JComboBox<Entity>();
	
		List<Entity> inheritableEntities = this.getView().getModel().getEntities();
		inheritableEntities.remove(parent.getEntity());
		inheritanceBox.setModel(new DefaultComboBoxModel<Entity>(inheritableEntities.toArray(new Entity[inheritableEntities.size()])));
		
		inheritanceBox.insertItemAt(null, 0);
		
		if(getEntity().isSubclass())
		{
			inheritanceBox.setSelectedItem(parent.getEntity().inheritsFrom());
		}
		else
			inheritanceBox.setSelectedItem(null);
		
		
		inheritanceBox.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				EditEntityDialog.this.getView().getController().acceptCommand(new EditInheritance(EditEntityDialog.this.parent, inheritanceBox.getItemAt(inheritanceBox.getSelectedIndex())));
			}
			
		}
				);
		JPanel northPane = new JPanel();
		northPane.setLayout(new GridLayout(0, 2, 0, 0));
		
		northPane.add(northLeftPane);
		
		getContentPane().add(northPane, BorderLayout.NORTH);
		
		JPanel northRightPane = new JPanel();
		northPane.add(northRightPane);
		northRightPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Inherits from");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		northRightPane.add(lblNewLabel);
		
		northRightPane.add(inheritanceBox);

		table = new EditEntityTable(new EditEntityTableModel(this, parent));
		//AFDS
		JPanel buttonPanes = new JPanel();
		getContentPane().add(buttonPanes, BorderLayout.SOUTH);
		buttonPanes.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel buttonPaneLeft = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPaneLeft.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		buttonPanes.add(buttonPaneLeft);
		
		JButton newAttributeBtn = new JButton("Add new attribute");
		newAttributeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NewAttributeDialog(EditEntityDialog.this);
			}
		});
		buttonPaneLeft.add(newAttributeBtn);
		
		JPanel buttonPaneRight = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) buttonPaneRight.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		buttonPanes.add(buttonPaneRight);
		JButton okButton = new JButton("OK");
		okButton.setHorizontalAlignment(SwingConstants.RIGHT);
		buttonPaneRight.add(okButton);
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					EditEntityDialog.this.parent.repaint();
					getView().repaint();
					setVisible(false);
					dispatchEvent(new WindowEvent(EditEntityDialog.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		);
		JScrollPane scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane);
		table.setFillsViewportHeight(true);
		table.setVisible(true);
		
	}
	

	public void refreshTable()
	{
		table.repaint();
		table.revalidate();
	}

}
