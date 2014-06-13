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
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import application.RunTool;

/**
 * Dialog page showing version information and other information about
 * the application
 * @author Alex Munro
 *
 */
public class AboutPage extends JDialog {

	private static final long serialVersionUID = -4135138556772057914L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutPage() {
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		setTitle("About VDBM");
		setBounds(100, 100, 299, 158);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JTextArea aboutTxtArea = new JTextArea(RunTool.APPLICATION_NAME + " " + RunTool.APPLICATION_VERSION + " last updated on " + RunTool.MOST_RECENT_UPDATE + ". If you have any queries, please send an email to " + RunTool.EMAIL);
		aboutTxtArea.setBackground(new Color(240,240,240));
		aboutTxtArea.setWrapStyleWord(true);
		aboutTxtArea.setLineWrap(true);
		
		aboutTxtArea.setBounds(23, 12, 250, 112);
		contentPanel.add(aboutTxtArea);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

}
