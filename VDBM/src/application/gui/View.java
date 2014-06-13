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

package application.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;

import command.CreateTable;
import command.RepositionComponent;
import application.Controller;
import application.Relationship;
import application.RunTool;
import application.error.CircularInheritanceError;
import application.error.DependencyError;
import application.gui.dialog.AboutPage;
import application.gui.dialog.NewModel;
import application.gui.dialog.QueryByExampleDialog;
import model.Model;
import model.diagram.Entity;
import model.systems.SystemEnum;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The view component of the application. Directly responsible for the main
 * application window holding the major interface components.
 * 
 * @author Alex Munro
 */

// TODO: refactor some of these functions to the controller

public class View extends JFrame
{
	
	private static final long serialVersionUID = 7075773129667996584L;

	private Model model;

	private Controller controller;

	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JFileChooser fileChooser = new JFileChooser(".");

	private JMenuItem mntmSaveModel, mntmNewModel, mntmExportImage, mntmGenerateSchema, mntmUndo, mntmRedo, mntmQBE;

	private JScrollPane contentScroller;

	private JMenuItem mntmAboutErdTool;

	private ArrayList<EntityBox> entities = new ArrayList<EntityBox>();

	private int maxX, maxY = 0;
	
	private List<RelationshipLine> relationshipLines;
	
	private boolean loaded = false;
	
	public void updateRelationships(List<Relationship> inheritances, List<Relationship> associations)
	{
		relationshipLines = new ArrayList<RelationshipLine>();
		for (Relationship i: inheritances)
		{
			relationshipLines.add(new InheritanceLine(i));
		}
		for (Relationship a : associations)
		{
			relationshipLines.add(new AssociationLine(a));
		}
		this.repaint();
	}
	

	private JPanel contentPanel = new JPanel()
	{	
		private static final long serialVersionUID = -4160537509518820491L;

		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(View.this.maxX + 20, View.this.maxY + 20);
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			if(loaded)
			{
				if(relationshipLines == null)
					updateRelationships(controller.getInheritances(), controller.getAssociations());
				for (RelationshipLine rl : relationshipLines)
				{
					rl.paint(g2d);
				}
			}
		}
	};

	private JToolBar toolbar;

	private JToggleButton newTableBtn, selecterBtn, deleteTableBtn;

	private ButtonGroup toolbarBtns;

	private JMenuItem mntmCloseModel;
	

	/**
	 * forgive me for i have sinned
	 */
	public View()
	{
		this.setTitle(RunTool.APPLICATION_NAME);
		this.setBounds(100, 100, 650, 500);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//TODO: investigate this
		createBufferStrategy(2);
	
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
	
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
	
		mntmNewModel = new JMenuItem("New model");
		mntmNewModel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						new NewModel(View.this);
					}
				});
			}
		});
		mnFile.add(mntmNewModel);
	
		JMenuItem mntmLoadModel = new JMenuItem("Load model");
		mnFile.add(mntmLoadModel);
		mntmLoadModel.addActionListener(new ActionListener()
		{
	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
	
				View.this.loadModelFromFile();
	
			}
	
		});
	
		mntmGenerateSchema = new JMenuItem("Generate schema");
		mntmGenerateSchema.addActionListener(new ActionListener()
		{
	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				View.this.generateSchema();
			}
	
		});
		mntmGenerateSchema.setEnabled(false);
	
		mntmSaveModel = new JMenuItem("Save model");
		mntmSaveModel.setEnabled(false);
		mnFile.add(mntmSaveModel);
		mnFile.add(mntmGenerateSchema);
		mntmSaveModel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				View.this.saveModelToFile();
			}
		});
		
		mntmCloseModel = new JMenuItem("Close model");
		mntmCloseModel.setEnabled(false);
		mnFile.add(mntmCloseModel);
		mntmCloseModel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				View.this.closeModel();
			}
			
		});
		
		mntmQBE = new JMenuItem("Build a query by example");
		mntmQBE.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				QueryByExampleDialog qbeDialog = new QueryByExampleDialog(model);
				qbeDialog.setVisible(true);
			}
			
		});
		mnFile.add(mntmQBE);
		mntmQBE.setEnabled(false);
		
		mntmExportImage = new JMenuItem("Export as image");
		mntmExportImage.setEnabled(false);
		mntmExportImage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				exportToImage();
			}
		});
		mnFile.add(mntmExportImage);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		mnFile.add(mntmQuit);
	
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
	
		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mnEdit.add(mntmUndo);
		
		mntmUndo.addActionListener(new ActionListener()
		{
	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.undo();
			}
			
		});
	
		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setEnabled(false);
		mnEdit.add(mntmRedo);
		
		mntmRedo.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					controller.redo();
				}			
			}
		);
	
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	
		mntmAboutErdTool = new JMenuItem("About VDBM");
		mntmAboutErdTool.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				AboutPage ap = new AboutPage();
				ap.setVisible(true);
			}
		});
		mnHelp.add(mntmAboutErdTool);
		
		
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Database diagram models (*.dbdm)", "dbdm");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
	
		
		// Configure layouts and main UI components - content panel and toolbar
		contentPanel.setLayout(null); // This prevents the entity boxes from
										// being repositioned upon window
										// resizing
	
		toolbar = new JToolBar(null, JToolBar.VERTICAL);
		toolbar.setFloatable(false);
	
		// Adding buttons to toolbar - insert future buttons here!
	
		toolbarBtns = new ButtonGroup();
	
		ImageIcon cursorIcon = getCorrectlySizedIcon("icons/Cursor.png", 25, 25);
		selecterBtn = new JToggleButton(cursorIcon);
		selecterBtn.setSize(25, 25);
		selecterBtn.setPreferredSize(new Dimension(25,25));
		selecterBtn.setToolTipText("Create and reposition diagram components");
		selecterBtn.setFocusable(false);
		toolbarBtns.add(selecterBtn);
		toolbar.add(selecterBtn);
	
		Icon newTableIcon = getCorrectlySizedIcon("icons/NewTable.png", 25, 25);
		newTableBtn = new JToggleButton(newTableIcon);
		newTableBtn.setToolTipText("Create a new table");
		newTableBtn.setFocusable(false);
		newTableBtn.setSize(25, 25);
		toolbarBtns.add(newTableBtn);
		toolbar.add(newTableBtn);
	
		Icon deleteTableIcon = getCorrectlySizedIcon("icons/DeleteTable.png", 25, 25);
		deleteTableBtn = new JToggleButton(deleteTableIcon);
		deleteTableBtn.setToolTipText("Delete a table");
		deleteTableBtn.setFocusable(false);
		deleteTableBtn.setSize(25, 25);
		toolbarBtns.add(deleteTableBtn);
		toolbar.add(deleteTableBtn);
	
		// End buttons
	
		this.add(toolbar, BorderLayout.WEST);
	
		contentPanel.setSize(500, 500);
	
		contentPanel.addMouseListener(
	
		new MouseInputAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (model == null || model.equals(null))
				{
					return;
				}
				if (newTableBtn.isSelected())
				{
					controller.acceptCommand(new CreateTable(View.this, View.this.getModel(), View.this.getModel().newEntity(e.getPoint())));
					toolbarBtns.clearSelection();
					selecterBtn.setSelected(true);
					newTableBtn.setFocusPainted(false);
					contentPanel.revalidate();
					contentPanel.repaint();
				}
			}
		}
	
		);
		contentScroller = new JScrollPane(contentPanel);
		this.add(contentScroller, BorderLayout.CENTER);
	}

	protected void closeModel()
	{
		mntmSaveModel.setEnabled(false);
		mntmGenerateSchema.setEnabled(false);
		mntmExportImage.setEnabled(false);
		mntmCloseModel.setEnabled(false);
		mntmQBE.setEnabled(false);
		this.model = null;
		this.controller = null;
		for (EntityBox e : entities)
		{
			e.setVisible(false);
			e.setEnabled(false);
		}
		this.entities = new ArrayList<EntityBox>();
		contentPanel.setBackground(new Color(240,240,240));
		contentPanel.repaint();
		this.relationshipLines = new ArrayList<RelationshipLine>();
		this.setTitle(RunTool.APPLICATION_NAME);
	}

	public Model getModel()
	{
		return model;
	}

	public ArrayList<EntityBox> getEntities()
	{
		return entities;
	}

	public ButtonGroup getButtonGroup()
	{
		return buttonGroup;
	}

	/**
	 * Create a new model with information passed from the new model dialog
	 * 
	 * @param ts
	 *            - the target system for schema generation
	 * @param name
	 *            - the name of the new model
	 */
	public void newModel(SystemEnum ts, String name)
	{
		loadModel(new Model(ts, name));
	}

	/**
	 * Loads a model passed in from within the application and removes the old
	 * model.
	 * 
	 * @param model
	 *            - the model to be loaded
	 */
	public void loadModel(Model model)
	{
		// Remove any old entity boxes

		this.loaded = false;
		
		for (EntityBox eb : entities)
		{
			contentPanel.remove(eb);
		}

		// Reset and repopulate the entity box list

		entities = new ArrayList<EntityBox>();
		this.model = model;
		this.setTitle(RunTool.APPLICATION_NAME + " - " + model.getTs().toString() + " - "
				+ model.getName());

		contentPanel.setBackground(Color.WHITE);
		
		for (Entity e: model.getEntities())
		{
			addEntityBox(e, e.getPoint());
		}
		
		contentPanel.repaint();
		contentPanel.revalidate();
		mntmSaveModel.setEnabled(true);
		mntmGenerateSchema.setEnabled(true);
		mntmExportImage.setEnabled(true);
		mntmCloseModel.setEnabled(true);
		mntmQBE.setEnabled(true);
		this.controller = new Controller(model, this);
		updateMaxXAndY();

		for (EntityBox eb : entities)
		{
			eb.update();
		}
		controller.updateRelationships();
		loaded = true;
	}

	/**
	 * Loads a model from file as selected by the user in a file chooser prompt
	 */
	public void loadModelFromFile()
	{
		int loadResult = fileChooser.showOpenDialog(this);
		if (loadResult == JFileChooser.APPROVE_OPTION)
		{
			Model model = null;
			try
			{
				FileInputStream fis = new FileInputStream(
						fileChooser.getSelectedFile());
				ObjectInputStream ois = new ObjectInputStream(fis);
				model = (Model) ois.readObject();
				ois.close();
				fis.close();
			} catch (IOException e)
			{

			} catch (ClassNotFoundException e)
			{

			}

			if (model != null)
			{
				loadModel(model);
			}
			else
			{
				System.err.println("No model!");
			}
		}
	}

	/**
	 * Saves the model currently loaded in the application.
	 * @return boolean indicating success of save operation
	 */
	public boolean saveModelToFile()
	{
		fileChooser.setSelectedFile(new File(getModel().getName() + ".dbdm"));
		
		int saveResult = fileChooser.showSaveDialog(this);
		if (saveResult == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				FileOutputStream fos = new FileOutputStream(
						fileChooser.getSelectedFile());
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(model);
				oos.close();
				fos.close();
			} catch (FileNotFoundException e)
			{
				System.err.println("Could not access file");
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			} catch (IOException e)
			{
				System.err.println("IO error");
				System.err.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true; // Successfully cancelled
	}

	protected void exportToImage()
	{
		JFileChooser imageChooser = new JFileChooser();
		imageChooser.setDialogTitle("Save to image");
		for (String filter : ImageIO.getReaderFileSuffixes())
		{
			imageChooser.addChoosableFileFilter(new FileNameExtensionFilter(filter.toUpperCase() + " files", filter));
		}
		
		int saveResult = imageChooser.showSaveDialog(this);
		if (saveResult == JFileChooser.APPROVE_OPTION)
		{
			BufferedImage image = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2D = image.createGraphics();
			contentPanel.paint(g2D);
			
			String format;
			if (imageChooser.getFileFilter() instanceof FileNameExtensionFilter)
			{
				format = ((FileNameExtensionFilter) imageChooser.getFileFilter()).getExtensions()[0];
			}
			else
			{
				format = "bmp";
			}
			try
			{
				ImageIO.write(image, format, new File(imageChooser.getSelectedFile().toString() + "." + format));
			} catch (IOException e)
			{
				System.err.println("IO error");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		
		}
	}

	/**
	 * Using the currently loaded model, generates a schema and saves it to a text file.
	 */
	private void generateSchema()
	{
		fileChooser.setSelectedFile(new File(getModel().getName() + ".txt"));
		
		int saveResult = fileChooser.showSaveDialog(this);
		if (saveResult == JFileChooser.APPROVE_OPTION)
		{
			try{
				List<String> outputSchema = model.getTs().getSystem().generateSchema(model);
				PrintWriter out = new PrintWriter(fileChooser.getSelectedFile());
				for (String line : outputSchema)
				{
					out.println(line);
				}
				out.flush();
				out.close();
				JOptionPane.showMessageDialog(this, "Schema successfully saved to " + fileChooser.getSelectedFile().toString());
			}
			catch (FileNotFoundException e)
			{
				JOptionPane.showMessageDialog(this, "Could not save schema due to IO problems.");
				System.err.println("Could not access file");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} 
			catch(DependencyError de)
			{
				if (de instanceof CircularInheritanceError)
					JOptionPane.showMessageDialog(this, "Schema could not be generated due to circular inheritance dependencies.");
				System.err.println("Dependency error found when generating schema");
				System.err.println(de.getMessage());
			}
		}
	}

	public void updatePanel()
	{
		this.contentPanel.repaint();
		this.contentPanel.revalidate();
		for (EntityBox e : entities)
		{
			e.repaint();
			e.revalidate();
		}
	}

	public EntityBox addEntityBox(Entity e, Point p)
	{
		EntityBox eb = new EntityBox(e, this, p);

		eb.setSize(eb.getPreferredSize());
		entities.add(eb);
		contentPanel.add(eb);
		
		return eb;
	}
	
	/**
	 * Updates the maximum X and Y points according to components on the content panel.
	 * This is a somewhat inefficient method, being linear in the number of EntityBoxes.
	 * This could be improved by storing max X and Y co-ords in the commands and updating
	 * from these. A linear search will still have to be done upon loading a model.
	 */
	public void updateMaxXAndY()
	{
		maxX = 0;
		maxY = 0;
		for (EntityBox eb : entities)
		{
			int ebMaxX = eb.getLocation().x + eb.getSize().width;
			if (ebMaxX > maxX)
			{
				maxX = ebMaxX;
			}
			int ebMaxY = eb.getLocation().y + eb.getSize().height;
			if (ebMaxY > maxY)
			{
				maxY = ebMaxY;
			}
		}
	}
	
	public void moveComponent(GUIComponent gc)
	{
		this.controller.acceptCommand(new RepositionComponent(gc));
	}

	/**
	 * To get the icon at the correct size, the 25x25 png is read in as an ImageIcon.
	 * For some reason, when using the getClass() form of URI, this is automatically
	 * scaled up to 512x512. To correct its size, the icon is converted into an
	 * Image, resized and recreated using that Image.
	 * 
	 * For some reason, you don't have to do this when using the non-JAR friendly form
     * of URI.
	 * 
	 * @param uri The URI of the image being read in, relative to application.gui.Window
	 * @param width Desired width of the ImageIcon
	 * @param height Desired height of the ImageIcon
	 * @return An image scaled to width x height
	 */
	public ImageIcon getCorrectlySizedIcon(String uri, int width, int height)
	{
		ImageIcon imgIcon = new ImageIcon(getClass().getResource(uri));
		return new ImageIcon(imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}
	
	public void removeEntityBox(EntityBox entityBox)
	{
		entities.remove(entityBox);
		contentPanel.remove(entityBox);
	}
	
	/**
	 * Enables or disables the undo button and sets its
	 * text accordingly.
	 * @param b Whether another action can be undone.
	 * @param action The next action that can be undone if applicable.
	 */
	public void setUndoable(boolean b, String action)
	{
		mntmUndo.setEnabled(b);
		if (b)
			mntmUndo.setText("Undo " + action);
		else
			mntmUndo.setText("Undo");
	}
	
	/**
	 * Enables or disables the redo button and sets its
	 * text accordingly.
	 * @param b Whether another action can be redone.
	 * @param action The next action that can be redone if applicable.
	 */
	public void setRedoable(boolean b, String action)
	{
		mntmRedo.setEnabled(b);
		if (b)
			mntmRedo.setText("Redo " + action);
		else
			mntmRedo.setText("Redo");
	}
	
	public boolean isDeleteBtnPressed()
	{
		return deleteTableBtn.isSelected();
	}
	
	public Controller getController()
	{
		return this.controller;
	}

	public void repaintContentPanel()
	{
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	
	public void resetBtnSelection()
	{
		toolbarBtns.clearSelection();
		selecterBtn.setSelected(true);
	}

	public void removeGUIComponent(GUIComponent gc)
	{
		this.contentPanel.remove(gc);
	}
	
	public EntityBox getEntityBox(Entity e)
	{
		for (EntityBox eb : entities)
		{
			if (eb.getEntity().equals(e))
				return eb;
		} 
		return null;
	}

}
