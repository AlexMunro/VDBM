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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import model.systems.SystemEnum;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.GridLayout;

import javax.swing.JTextField;

import application.gui.View;
/**
 * Dialog that is displayed when creating a new model.
 * Allows the user to choose a name and a target system.
 * @author Alex Munro
 *
 */
public class NewModel extends JDialog
{

	private static final long serialVersionUID = 7986303879287768236L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<SystemEnum> comboBox;
	private JTextField textField;
	private View window;

	/**
	 * Creates the new model dialog
	 * 
	 * @param window
	 *            - The GUI window that observes this
	 */
	public NewModel(View window)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			System.err.println("Could not set look and feel according to local environment");
			System.err.print(e.toString());
		}
		this.window = window;
		setTitle("New Model");
		setAlwaysOnTop(true);
		setBounds(100, 100, 300, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		JLabel lblNewLabel = new JLabel("Target system");
		contentPanel.add(lblNewLabel);
		comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<>(SystemEnum.values()));
		comboBox.setSelectedIndex(SystemEnum.XFDM.ordinal());
		contentPanel.add(comboBox);
		JLabel lblNewLabel_1 = new JLabel("Model name");
		contentPanel.add(lblNewLabel_1);
		textField = new JTextField();
		contentPanel.add(textField);
		textField.setColumns(10);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(arg0 -> {
            if (!textField.getText().equals(null))
            {
                NewModel.this.window.newModel(
                        (SystemEnum) comboBox.getSelectedItem(),
                        textField.getText());
                NewModel.this.setVisible(false);
                NewModel.this.dispatchEvent(new WindowEvent(
                        NewModel.this, WindowEvent.WINDOW_CLOSING));
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "You need to name the model.");
            }
        });
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
            NewModel.this.setVisible(false);
            NewModel.this.dispatchEvent(new WindowEvent(
                    NewModel.this, WindowEvent.WINDOW_CLOSING));
        });
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		this.setVisible(true);
	}

}
