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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import application.gui.EntityBox;
import application.gui.View;
import command.AddAttribute;
import model.diagram.Attribute;
import java.awt.Component;

/**
 * Dialog that allows the user to add a new attribute
 * to a table.
 * @author Alex Munro
 *
 */
public class NewAttributeDialog extends JDialog
{

	private static final long serialVersionUID = -7089640919539492958L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JComboBox<String> typeComboBox;
	private EditEntityDialog eed;
	private JCheckBox uniqueCheckBox, notNullCheckBox, fixedCheckBox, multiCheckBox;

	public View getView()
	{
		return eed.getView();
	}
	
	public void close()
	{
		this.eed.refreshTable();
		this.setVisible(false);
		this.dispatchEvent(new WindowEvent(NewAttributeDialog.this, WindowEvent.WINDOW_CLOSING));
	}
	
	public NewAttributeDialog(EditEntityDialog eed)
	{
		this.eed = eed;
		setTitle("New attribute");
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.add(new JLabel("Name"));
		nameField = new JTextField();
		panel.add(nameField);
		nameField.setColumns(10);
	
		JPanel panel2 = new JPanel();
		contentPanel.add(panel2);
		panel2.setLayout(new GridLayout(0, 2, 0, 0));
			
		JLabel lblType = new JLabel("Type");
		panel2.add(lblType);
	
		List<String> returnTypes = this.getView().getModel().getReturnTypes();
		ComboBoxModel<String> typeModel = new DefaultComboBoxModel<>(returnTypes.toArray(new String[returnTypes.size()]));
		typeComboBox = new JComboBox<>(typeModel);
		panel2.add(typeComboBox);
	
		
		JPanel panel3 = new JPanel();
		panel3.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPanel.add(panel3);
		panel3.setLayout(new GridLayout(0, 3, 0, 0));
			
		JLabel lblNewLabel = new JLabel("Constraints:");
		panel3.add(lblNewLabel);
			
			
		uniqueCheckBox = new JCheckBox("Unique");
		panel3.add(uniqueCheckBox);
	
	
		notNullCheckBox = new JCheckBox("Not null");
		panel3.add(notNullCheckBox);
		
		JLabel label = new JLabel("");
		panel3.add(label);
			
		fixedCheckBox = new JCheckBox("Fixed");
		panel3.add(fixedCheckBox);
		
		multiCheckBox = new JCheckBox("Multi-valued");
		panel3.add(multiCheckBox);
		
		JLabel label_1 = new JLabel("");
		panel3.add(label_1);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(e -> {
            Attribute a = new Attribute(nameField.getText(),
                                        typeComboBox.getItemAt(typeComboBox.getSelectedIndex()),
                                        uniqueCheckBox.isSelected(),
                                        notNullCheckBox.isSelected(),
                                        fixedCheckBox.isSelected(),
                                        multiCheckBox.isSelected());

            NewAttributeDialog.this.getView().getController().acceptCommand(new AddAttribute((EntityBox)NewAttributeDialog.this.eed.getParent(), a));
            NewAttributeDialog.this.close();
        });
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> close());
		buttonPane.add(cancelButton);
		
		this.setVisible(true);
		
	}

}
