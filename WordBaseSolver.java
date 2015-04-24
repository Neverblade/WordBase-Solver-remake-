/*
Given the board of letters, 
WordBaseSolver generates all words
that can be made from each starting letter.
It outputs a 2D Piece array, with each 
piece representing a square and containing
a list of its possible words.
*/

import java.util.*;
import java.io.*;

public class WordBaseSolver
{
	private char[][] letterBoard;
	private int[][] scoreBoard;
	private ArrayList<String> dict;
	private Piece[][] board;
	
	public WordBaseSolver()
	{
	}
	
	public WordBaseSolver(char[][] letterBoard, int[][] scoreBoard) throws Exception
	{
		this.letterBoard = letterBoard;
		this.scoreBoard = scoreBoard;
		
		/* Load the Dictionary */
		System.out.println("Loading dictionary...");
		dict = new ArrayList<String>();
		Scanner dictionaryScanner = new Scanner(new File("dictionary.txt"));
		while (dictionaryScanner.hasNext()) dict.add(dictionaryScanner.next());
	
		/* Run recursion for each of the starting letters to fill in the words */
		System.out.println("Running word finding algorithm...");
		board = new Piece[letterBoard.length][letterBoard[0].length];
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				//System.out.print(letterBoard[i][j]);
				board[i][j] = new Piece(j, i);
				generateWords(board[i][j]);
				
			}
			//System.out.println();
		}
	}
	
	//initializes / sets up variables for recursion
	public void generateWords(Piece p)
	{
		String currWord = "" + letterBoard[p.getY()][p.getX()]; //create the starting word
		
		int[][] used = new int[letterBoard.length][letterBoard[0].length]; //create array for used letters
		for (int i = 0; i < used.length; i++)
		{
			for (int j = 0; j < used[i].length; j++)
			{
				used[i][j] = -1;
			}
		}
		used[p.getY()][p.getX()] = currWord.length() - 1;
		
		ArrayList<String> dic = new ArrayList<String>(); //create the list of usable words
		for (int i = 0; i < dict.size(); i++)
		{
			if (dict.get(i).startsWith(currWord)) dic.add(dict.get(i));
		}
		
		generateWords(p, used, currWord, dic, p.getX(), p.getY());
	}
	
	public void generateWords(Piece p, int[][] used, String currWord, ArrayList<String> dic, int x, int y)
	{
		/* Do this process for all tiles surrounding the current one */
		for (int i = y-1; i <= y+1; i++)
		{
			for (int j = x-1; j <= x+1; j++)
			{
				/* Process only applies to tiles inside the board and not yet used */
				if (i >= 0 && i < letterBoard.length && j >= 0 && j < letterBoard[i].length && used[i][j] == -1)
				{
					/* Create new variables unique to the new word combinations we're making */
					String newWord = currWord + letterBoard[i][j];
				
					int[][] newUsed = new int[used.length][used[0].length];
					for (int k = 0; k < newUsed.length; k++)
					{
						newUsed[k] = used[k].clone();
					}
					newUsed[i][j] = newWord.length() - 1;
					
					/* Go Through the Current List, adding in words that still work */
					ArrayList<String> nDic = new ArrayList<String>();
					for (int k = 0; k < dic.size(); k++)
					{
						if (dic.get(k).startsWith(newWord))
						{
							if (dic.get(k).equals(newWord)) p.getWords().add(new Word(newUsed, newWord));
							nDic.add(dic.get(k));
						}
					}
					
					/* If there are still words possible, run recursion */
					if (nDic.size() > 0) generateWords(p, newUsed, newWord, nDic, j, i);
				}
			}
		}
	}
	
	public Piece[][] getBoard()
	{
		return board;
	}
	
	public int[][] getScoreBoard()
	{
		return scoreBoard;
	}
	
	public void printPiece(int x, int y)
	{
		System.out.println("Possible words for piece: (" + y + ", " + x + ")");
		for (int i = 0; i < board[y][x].getWords().size(); i++)
		{
			System.out.println(board[y][x].getWords().get(i));
		}
	}
	
	public void printWord(int x, int y, int index)
	{
		System.out.println("Word " + index + " for piece: (" + y + ", " + x + ")");
		System.out.println(board[y][x].getWords().get(index));
	}
}