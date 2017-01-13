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

package application.exception;

/**
 * Exception thrown when a name is used more than once 
 * 
 * @author Alex
 *
 */

public class DuplicateNameException extends Exception{
	
	private static final long serialVersionUID = -1639357518137080977L;

	/**
	 * Constructor to be used when a duplicate name is used for a new
	 * attribute or table. 
	 * 
	 * @param name The duplicated name.
	 */
	public DuplicateNameException(String name)
	{
		super("The name " + name + " is already in use.");
	}

}
