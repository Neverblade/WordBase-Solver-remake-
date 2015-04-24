/*
A subset of the overall image,
it contains one of the letters.
Here we do most of the detailed
processing and final recognition
of a letter.
*/

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;

public class Tile
{
	public BufferedImage image;
	public char[][][] alphabet;
	public Pixel[][] pixels;
	public String color;
	public char letter;
	public int inlay;
	public int size = 32;
	
	public Tile(BufferedImage image, char[][][] alphabet) throws Exception
	{
		this.image = image;
		this.alphabet = alphabet;
		setPixels();
		this.color = determineColor();
		crop();
		resize();
		this.letter = identify();
	}
	
	/* Determine what the background color is by testing iteratively shrinking borders*/
	public String determineColor()
	{
		int z = 0; //how far in we are into the image borders
		while (true)
		{
			boolean c = true;
			String color = pixels[z][z].getColor();
			for (int i = z; i < pixels.length - z; i++)
			{
				if (!pixels[i][z].getColor().equals(color) || !pixels[i][pixels[i].length-z-1].getColor().equals(color))
				{
					c = false;
					break;
				}
			}
			if (c)
			{
				for (int j = z; j < pixels[0].length - z; j++)
				{
					if (!pixels[z][j].getColor().equals(color) || !pixels[pixels.length-z-1][j].getColor().equals(color))
					{
						c = false;
						break;
					}
				}
			}
			if (c)
			{
				inlay = z;
				return color;
			}
			else z++;
		}
	}
	
	/* Crop the image so that the borders are cut out */
	public void crop()
	{
		/* Find the boundaries you want to cut at */
		
		String z = "BLACK"; //target color
		if (this.color.equals("BLACK")) z = "WHITE";
		int y1 = inlay, x1 = inlay, y2 = pixels.length - 1 - inlay, x2 = pixels[0].length - 1 - inlay;
		
		cy1: //y1 (top)
		while (true)
		{
			for (int j = inlay; j < pixels[0].length - inlay; j++)
			{
				if (pixels[y1][j].getColor().equals(z)) break cy1;
			}
			y1++;
		}	
		cy2: //y2 (bottom)
		while (true)
		{
			for (int j = inlay; j < pixels[0].length - inlay; j++)
			{
				if (pixels[y2][j].getColor().equals(z)) break cy2;
			}
			y2--;
		}
		y2++; //the end points are exclusive
		cx1: //x1 (left)
		while (true)
		{
			for (int i = inlay; i < pixels.length - inlay; i++)
			{
				if (pixels[i][x1].getColor().equals(z)) 
				{
					break cx1;
				}
			}
			x1++;
		}
		cx2:
		while (true)
		{
			for (int i = inlay; i < pixels.length - inlay; i++)
			{
				if (pixels[i][x2].getColor().equals(z)) break cx2;
			}
			x2--;
		}
		x2++; //the end points are exclusive
		
		/* Reshape the image */
		BufferedImage medImage = new BufferedImage(x2-x1, y2-y1, image.getType());
		Graphics g = medImage.createGraphics();
		g.drawImage(image, 0, 0, x2-x1, y2-y1, x1, y1, x2, y2, null);
		g.dispose();
		
		this.image = medImage;
		setPixels();
	}
	
	/* Resizes the image to a uniform size */
	public void resize()
	{
		BufferedImage medImage = new BufferedImage(size, size, image.getType());
		Graphics g = medImage.createGraphics();
		g.drawImage(image, 0, 0, size, size, null);
		g.dispose();
		this.image = medImage;
		setPixels();
	}
	
	/* Identifies the letter by comparing it to the alphabet array */
	public char identify()
	{
		double match = 0.0;
		char letter = '#';
				
		for (int i = 0; i < alphabet.length; i++)
		{
			int met = 0;
			int total = 0;
			
			for (int j = 0; j < alphabet[i].length; j++)
			{
				for (int k = 0; k < alphabet[i][j].length; k++)
				{
					total++;
					if (alphabet[i][j][k] == 'X') //if we're dealing with a letter space
					{
						if (!getColor().equals("BLACK") && pixels[j][k].getColor().equals("BLACK")) met++;
						else if (getColor().equals("BLACK") && pixels[j][k].getColor().equals("WHITE")) met++;
					} else //if we're dealing with a background space
					{
						if (getColor().equals(pixels[j][k].getColor())) met++;
					}
				}
			}
			
			if (1.0 * met / total > 1) System.out.println("It went over one");
			//System.out.println((char) (i+65) + ": " + (1.0 * met / total));
			if (1.0 * met / total > match)
			{
				match = 1.0 * met / total;
				letter = (char) (i + 65);
			}
		}
		
		return letter;
	}
	
	/* Resets the pixel array based off of the given image */
	public void setPixels()
	{
		Pixel[][] pixels = new Pixel[image.getHeight()][image.getWidth()];
		
		for (int i = 0; i < image.getHeight(); i++)
		{
			for (int j = 0; j < image.getWidth(); j++)
			{
				pixels[i][j] = new Pixel(new Color(image.getRGB(j, i)));
			}
		}
		
		this.pixels = pixels;
	}
	
	public BufferedImage getImage()
	{
		return this.image;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
	
	public void print() throws Exception
	{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("testout.out")));
		for (int i = 0; i < pixels.length; i++)
		{
			for (int j = 0; j < pixels[i].length; j++)
			{
				String z = pixels[i][j].getColor();
				if (z.equals("WHITE")) out.print("W");
				else if (z.equals("ORANGE")) out.print("O");
				else if (z.equals("BLUE")) out.print("B");
				else if (z.equals("BLACK")) out.print("X");
				else System.out.println("Mistakes were made.");
			}
			out.println();
		}
		out.close();
	}
}