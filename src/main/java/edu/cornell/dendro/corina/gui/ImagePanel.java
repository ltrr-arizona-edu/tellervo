/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final int TILED = 0;
	public static final int SCALED = 1;
	public static final int ACTUAL = 2;

	private BufferedImage image;
	private int style;
	private float alignmentX = 0.5f;
	private float alignmentY = 0.5f;

	public ImagePanel(BufferedImage image)
	{
		this(image, TILED);
	}

	public ImagePanel(BufferedImage image, int style)
	{
		this.image = image;
		this.style = style;
		setLayout( new BorderLayout() );
	}

	public void setImageAlignmentX(float alignmentX)
	{
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
	}

	public void setImageAlignmentY(float alignmentY)
	{
		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;

	}

	public void add(JComponent component)
	{
		add(component, null);
	}

	public void add(JComponent component, Object constraints)
	{
		component.setOpaque( false );

		if (component instanceof JScrollPane)
		{
			JScrollPane scrollPane = (JScrollPane)component;
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque( false );
			Component c = viewport.getView();

			if (c instanceof JComponent)
			{
				((JComponent)c).setOpaque( false );
			}
		}

		super.add(component, constraints);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (image == null ) return;

		switch (style)
		{
		case TILED :
			drawTiled(g);
			break;

		case SCALED :
			Dimension d = getSize();
			g.drawImage(image, 0, 0, d.width, d.height, null);
			break;

		case ACTUAL :
			drawActual(g);
			break;
		}
	}

	private void drawTiled(Graphics g)
	{
		Dimension d = getSize();
		int width = image.getWidth( null );
		int height = image.getHeight( null );

		for (int x = 0; x < d.width; x += width)
		{
			for (int y = 0; y < d.height; y += height)
			{
				g.drawImage( image, x, y, null, null );
			}
		}
	}

	private void drawActual(Graphics g)
	{
		Dimension d = getSize();
		float x = (d.width-image.getWidth()) * alignmentX;
		float y = (d.height-image.getHeight()) * alignmentY;
		g.drawImage(image, (int)x, (int)y, this);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
		this.setSize(d);
	}

	public static void main(String [] args)
	throws Exception
	{
		BufferedImage image = javax.imageio.ImageIO.read( new java.io.File("Images/splash2.png") );

		ImagePanel north = new ImagePanel(image, ImagePanel.ACTUAL);
		north.setImageAlignmentY(1.0f);
		JTextArea text = new JTextArea(5, 40);
		JScrollPane scrollPane = new JScrollPane( text );
		north.add( scrollPane );

		ImagePanel south = new ImagePanel(image, ImagePanel.SCALED);
		JPanel buttons = new JPanel();
		buttons.add( new JButton("One") );
		buttons.add( new JButton("Two") );
		JPanel boxes = new JPanel();
		boxes.add( new JCheckBox("One") );
		boxes.add( new JCheckBox("Two") );
		south.add(buttons, BorderLayout.NORTH);
		south.add(boxes, BorderLayout.SOUTH);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add( north, BorderLayout.NORTH );
		frame.getContentPane().add( south, BorderLayout.SOUTH );
		frame.pack();
		frame.setVisible(true);
	}
}
