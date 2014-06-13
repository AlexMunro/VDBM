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

package application;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Point of entry for the application. Responsible for requesting
 * a new window on the event-dispatching thread.
 * @author Alex Munro
 *
 */

public class RunTool {

	public static final String APPLICATION_NAME = "VDBM: The Visual Database Modeller";
	public static final String APPLICATION_VERSION = "1.2.1";
	public static final String MOST_RECENT_UPDATE = "14.05.2014";
	public static final String EMAIL = "vdbmproject@gmail.com";
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
			// Scheduling the window on the event-dispatching thread for thread safety
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e)
				{
					System.err.println("Could not set look and feel");
				}
				new application.gui.View();
			}
		});
	}
	
}
