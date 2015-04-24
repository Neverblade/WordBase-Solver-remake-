/*
WordBaseScanner contains the OCR.
Given an image file, it'll read
the individual letters and output
two arrays: letterBoard for letters,
scoreBoard for who owns what.
*/

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class WordBaseScanner
{
	private BufferedImage image;
	private Pixel[][] pixels;
	private int tileHeight;
	private int tileWidth;
	private int y1;
	private int y2;
	private Tile[][] tiles;
	private char[][][] alphabet;
	private int size = 32;
	
	/* This is what the program outputs */
	private char[][] letterBoard;
	private int[][] scoreBoard;
	
	public WordBaseScanner(String imageName) throws Exception
	{
		File imgFile = new File(imageName);
		BufferedImage iniImage = ImageIO.read(imgFile);
		BufferedImage image = new BufferedImage(iniImage.getWidth(), iniImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.drawImage(iniImage, 0, 0, null);
		g.dispose();
		
		/* Read in the Alphabet letters */
		System.out.println("Reading in alphabet fonts...");
		alphabet = new char[26][size][size];
				
		Scanner alpha = new Scanner(new File("letters.txt"));
		for (int i = 0; i < alphabet.length; i++)
		{
			alpha.next();
			for (int j = 0; j < alphabet[i].length; j++)
			{
				String s = alpha.next();
				for (int k = 0; k < alphabet[i][j].length; k++)
				{
					alphabet[i][j][k] = s.charAt(k);
				}
			}
		}

		
		/* Convert the image into pixel colors */
		System.out.println("Converting into pixel array...");
		pixels = new Pixel[image.getHeight()][image.getWidth()];
		
		for (int i = 0; i < image.getHeight(); i++)
		{
			for (int j = 0; j < image.getWidth(); j++)
			{
				pixels[i][j] = new Pixel(new Color(image.getRGB(j, i)));
			}
		}
		
		
		/*Retrieve Tile Height, y1, and y2, and tile width*/
		System.out.println("Retrieving image info...");
		
		y2 = pixels.length-1; //start from the bottom
		while (true)
		{
			if (pixels[y2][0].getColor().equals("BLUE")) break;
			else y2--;
		}
		y2++;
		
		y1 = 0 ;
		while (true)
		{
			boolean c = true;
			for (int j = 0; j < pixels[y1].length; j++)
			{
				if (!pixels[y1][j].getColor().equals("ORANGE"))
				{
					c = false;
					break;
				}
			}
			if (c) break;
			else y1++;
		}
		
		tileHeight = (int) Math.round(1.0 * (y2 - y1) / 13);
		tileWidth = (int) Math.round(1.0 * (pixels[0].length) / 10);
				
		/* Split the main picture into tile pictures */
		System.out.println("Splitting image into tiles...");
		tiles = new Tile[13][10];
		
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				
				BufferedImage medImage = new BufferedImage(tileWidth, tileHeight, image.getType());
				Graphics g1 = medImage.createGraphics();
				g1.drawImage(image, 0, 0, tileWidth, tileHeight, j*tileWidth, i*tileHeight + y1, (j+1)*tileWidth, (i+1)*tileHeight + y1, null);
				g1.dispose();
				
				tiles[i][j] = new Tile(medImage, alphabet);
			}
		}
		
		/* Load the Tile Info into the Arrays */
		letterBoard = new char[13][10];
		scoreBoard = new int[letterBoard.length][letterBoard[0].length];
		
		for (int i = 0; i < letterBoard.length; i++)
		{
			for (int j = 0; j < letterBoard[i].length; j++)
			{
				letterBoard[i][j] = tiles[i][j].getLetter();
				String z = tiles[i][j].getColor();
				if (z.equals("WHITE")) scoreBoard[i][j] = 0;
				else if (z.equals("ORANGE")) scoreBoard[i][j] = 1;
				else if (z.equals("BLUE")) scoreBoard[i][j] = 2;
				else if (z.equals("BLACK")) scoreBoard[i][j] = 3;
				else System.out.println("Mistakes were made.");
			}
		}
		
		print();
	}
	
	/* Prints information to the save file */
	public void print() throws Exception
	{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("saved.txt")));
		
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				out.print(letterBoard[i][j]);
			}
			out.println();
		}
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				out.print(scoreBoard[i][j]);
			}
			out.println();
		}
		out.close();
	}
	
	public char[][] getLetterBoard()
	{
		return letterBoard;
	}
	
	public int[][] getScoreBoard()
	{
		return scoreBoard;
	}
}