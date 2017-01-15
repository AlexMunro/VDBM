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

import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;



import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import application.gui.qbe.QBEWindow;
import model.Model;
import model.diagram.Entity;

/**
 * The dialogue that precedes the query by example process.
 * Asks the user which table they would like to query and
 * starts a QBE window (see application.gui.qbe)
 * 
 * @author Alex Munro
 *
 */
public class QueryByExampleDialog extends JDialog
{

	private static final long serialVersionUID = 8438315690796728533L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<Entity> entitiesComboBox;
	private Model model;

	/**
	 * Create the dialog.
	 */
	public QueryByExampleDialog(Model model)
	{
		this.model = model;
		List<Entity> entityList = model.getEntities();
		if (entityList.size() == 0)
		{
			JOptionPane.showMessageDialog(this, "There must be at least one table to begin query by example.", "Could not start QBE", JOptionPane.WARNING_MESSAGE);
			setVisible(false);
			dispose();
		}
		Entity[] entities = new Entity[entityList.size()];
		entities = entityList.toArray(entities);
		setBounds(100, 100, 350, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblSelectATable = new JLabel("Select a table to query from:");
		contentPanel.add(lblSelectATable);
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
		}
		entitiesComboBox = new JComboBox<>(entities);
		contentPanel.add(entitiesComboBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton beginQBE = new JButton("Begin");
				beginQBE.setActionCommand("OK");
				beginQBE.addActionListener(arg0 -> {
                    new QBEWindow(QueryByExampleDialog.this.model, entitiesComboBox.getItemAt(entitiesComboBox.getSelectedIndex()));
                    setVisible(false);
                    dispose();
                });
				buttonPane.add(beginQBE);
				getRootPane().setDefaultButton(beginQBE);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(arg0 -> {
                    setVisible(false);
                    dispose();
                });
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
