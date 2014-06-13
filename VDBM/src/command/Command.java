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

package command;

/**
 * Interface for commands that perform changes on a model.
 * Implementing classes are reified commands that can be used
 * as according to the command pattern.
 * 
 * These commands are used in the private CommandManager class
 * of application.Controller.
 * 
 * @author Alex Munro
 *
 */

public interface Command {
	/**
	 * Function called when a command is first introduced to the CommandManager.
	 */
	public void execute();
	
	/**
	 * Function called when a command is undone.
	 */
	public void undo();
	
	/**
	 * Function called when a command is done after being undone. In most cases, simply a call to execute().
	 */
	public void redo();
	
	/**
	 * Returns a string that describes the command
	 * @return Descriptor of the action represented by the command
	 */
	public String getDescription();

}
