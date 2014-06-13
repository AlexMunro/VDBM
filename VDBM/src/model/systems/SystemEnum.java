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

import model.systems.XFDM;

/**
 * An enumeration of all system strategies.
 * To see how more can be added, refer to the constructor and
 * to SystemStrategy.java or the extension documentation.
 * 
 * @author Alex Munro
 */
public enum SystemEnum {
	
	
	XFDM("XFDM 1.0.2", new XFDM()),
	MYSQL("MySQL 5.6.14", new MySQL());

	private final String s;
	private SystemStrategy system;
	
	@Override
	public String toString()
	{
		return this.s;
	}
	
	public SystemStrategy getSystem()
	{
		return this.system;
	}

	/**
	 * 
	 * @param displayString - how the system's name should be displayed
	 * @param system - the class that defines the system's behaviour
	 */
	private SystemEnum(String displayString, SystemStrategy system)
	{
		this.s = displayString;
		this.system = system; 
	}
}
