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

package model.systems;

import java.util.List;

import javax.swing.table.TableModel;

import application.error.DependencyError;
import application.gui.qbe.Query;
import model.Model;
import model.diagram.Entity;

/**
 * The interface that must be implemented to add support for
 * database systems to VDBM.
 * 
 * @author Alex Munro
 *
 */

public interface SystemStrategy {

	/**
	 * @param model - The model created by the user, including the graph and the target system
	 *                identifier
	 * @param file The file location to dump the new schema
	 * @return The generated schema as a text file
	 */
	public List<String> generateSchema(Model model) throws DependencyError;
	
	/**
	 * To implement this method, create your type and create an ArrayList<String>
	 * with the names of each type as you would like them to appear.
	 * 
	 * For an example, refer to XFDM.java in this package.
	 */
	public List<String> returnTypes();

	/**
	 * A table that displays how attributes and constraints should appears
	 * @param e - Entity to be displayed
	 * @return Table as correctly displayed
	 */
	public TableModel entityTable(Entity e);
	
	public List<String> generateQuery(Query q);
	
}
