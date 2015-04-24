/*
The logic version of the Tile.
Contains a list of all the words
that can be made from this starting
letter.
*/

import java.util.*;

public class Piece
{
	private ArrayList<Word> words;
	private int y;
	private int x;
	
	public Piece(int x, int y)
	{
		words = new ArrayList<Word>();
		this.x = x;
		this.y = y;
	}
	
	public ArrayList<Word> getWords()
	{
		return words;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void scoreWords(int[][] scoreBoard, int team)
	{
		for (int i = 0; i < words.size(); i++)
		{
			words.get(i).scoreWord(scoreBoard, team);
		}
	}
}