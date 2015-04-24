/*
Contains RGB values of each image
pixel and it's best guess as to 
what color it's supposed to be.
*/

import java.awt.*;

public class Pixel
{
	public int red;
	public int green;
	public int blue;
	public String color;
	
	public Pixel(Color c)
	{
		this.red = c.getRed();
		this.green = c.getGreen();
		this.blue = c.getBlue();
		this.color = determineColor();
	}
	
	public int getRed()
	{
		return this.red;
	}
	
	public int getGreen()
	{
		return this.green;
	}
	
	public int getBlue()
	{
		return this.blue;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	/* Selects the color that's closest to the RGB values given */
	public String determineColor()
	{
		String[] colorNames = {"WHITE", "BLACK", "BLUE", "BLUE", "ORANGE", "ORANGE"};
		int[][] colorRGB = {{251, 251, 251}, {0, 0, 0}, {0, 201, 232}, {78, 212, 235}, {254, 136, 0}, {252, 159, 79}};
		
		int min = 800;
		int index = -1;
		for (int i = 0; i < colorRGB.length; i++)
		{
			int sum = 0;
			sum += Math.abs(colorRGB[i][0] - getRed());
			sum += Math.abs(colorRGB[i][1] - getGreen());
			sum += Math.abs(colorRGB[i][2] - getBlue());
			
			if (sum < min)
			{
				min = sum;
				index = i;
			}
		}
		
		return colorNames[index];
	}
	
	public String toString()
	{
		return this.color;
	}
}