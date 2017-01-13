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

package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import application.exception.ModelLoadException;

/**
 * Used for saving and loading @Model instances to and from XML files.
 * 
 * @author Alex Munro
 *
 */

public class ModelXMLFileHandler {
	
	/**
	 * 
	 * 
	 * @param file The file containing the model loaded
	 * @return The model loaded from file
	 * @throws ModelLoadException Thrown if problems occur when constructing the model from file
	 */
	public static Model loadModel(File file) throws ModelLoadException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		return null;
	}
	
	/**
	 * Saves a model to an XML file.
	 * 
	 * @param file The file to which the model should be saved
	 * @param model The model being saved
	 * @throws FileNotFoundException Thrown if the file passed by the view does not exist
	 */
	public static void saveModel(File file, Model model) throws FileNotFoundException
	{
		FileOutputStream fos = new FileOutputStream(file);
		
	}

}
