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

import java.util.List;

import model.diagram.Entity;

/**
 * An error raised at schema generation time when there is an
 * inheritance loop. Ideally, these loops should be caught as
 * the user is creating them - long before schema generation.
 * 
 * @author Alex Munro
 *
 */
public class CircularInheritanceException extends Exception
{
	private static final long serialVersionUID = -149551925406436809L;
	private List<Entity> affectedEntities;
	
	@Override
	public String getMessage()
	{
		String msg = "Circular inheritance error concerning: ";
		for (int i = 0; i < affectedEntities.size(); i++)
		{
			msg += affectedEntities.get(i);
			if (i + 1 < affectedEntities.size())
			{
				msg += ", ";
			}
			else
			{
				msg += ".";
			}
		}
		return msg;
	}
	
	public CircularInheritanceException(List<Entity> affectedEntities)
	{
		this.affectedEntities = affectedEntities;
	}

}
