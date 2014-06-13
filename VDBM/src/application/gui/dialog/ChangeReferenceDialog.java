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
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.diagram.Attribute;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import application.gui.EntityBox;
import application.gui.View;

/**
 * Allows the user to set a foreign key for a given column.
 * 
 * This dialog will only be used where the target system uses foreign key referencing.
 * 
 * @author Alex Munro
 *
 */
public class ChangeReferenceDialog extends JDialog
{

	private static final long serialVersionUID = 5247640330247543677L;

	private final JPanel contentPanel = new JPanel();
	
	private EditEntityDialog eed;
	
	JComboBox<EntityBox> refTblCombo;
	JComboBox<Attribute> refColCombo;

	public View getView()
	{
		return eed.getView();
	}
	
	public ChangeReferenceDialog(EditEntityDialog eed, Attribute a)
	{
		this.eed = eed;
		setTitle("Change reference for" + a.getName());
		setBounds(100, 100, 400, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblReferenceTable = new JLabel("Reference table:");
			contentPanel.add(lblReferenceTable);
		}
		{
			List<EntityBox> entities = getView().getEntities();
			DefaultComboBoxModel<EntityBox> tblModel = new DefaultComboBoxModel<EntityBox>(entities.toArray(new EntityBox[entities.size()]));
			refTblCombo = new JComboBox<EntityBox>(tblModel);
			contentPanel.revalidate();
			pack();
		}
		{
			JLabel lblReferenceColumn = new JLabel("Reference column:");
			contentPanel.add(lblReferenceColumn);
		}
		{
			refTblCombo.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					List<Attribute> attributes = refTblCombo.getItemAt(refTblCombo.getSelectedIndex()).getEntity().getAttributes();
					refColCombo.setModel(new DefaultComboBoxModel<Attribute>(attributes.toArray(new Attribute[attributes.size()])));
				}
				
			}
					
					);
			refColCombo = new JComboBox<Attribute>();
			refColCombo.setEnabled(false);
			contentPanel.add(refColCombo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						ChangeReferenceDialog.this.eed.refreshTable();
					}
					
				}
				);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						setVisible(false);
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
