/*
WordBasePlayer compiles the information,
sorts it, and picks out the best moves.
The main method is also contained here.
*/

import java.util.*;
import java.io.*;

public class WordBasePlayer
{
	private Piece[][] board;
	private int[][] scoreBoard;
	private int team;
	private ArrayList<Word> words;
	
	public WordBasePlayer(Piece[][] board, int[][] scoreBoard, int team)
	{
		this.board = board;
		this.scoreBoard = scoreBoard;
		this.team = team;
		words = new ArrayList<Word>();
		
		/* Compile possible words and Sort */
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if (scoreBoard[i][j] == team)
				{
					board[i][j].scoreWords(scoreBoard, team);
					for (int k = 0; k < board[i][j].getWords().size(); k++)
					{
						words.add(board[i][j].getWords().get(k));
					}
				}
			}
		}
		Collections.sort(words);
		Collections.reverse(words);
		
		/* Print out the best words */
		System.out.println("Top Ten Best Words");
		for (int i = 0; i < 10; i++)
		{
			System.out.println(words.get(i));
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		/* User Input for Basic Data */
		Scanner sc = new Scanner(System.in);
		System.out.print("Input File Name: ");
		String f = sc.next();
		System.out.print("Input Team Name (O/B): ");
		int team = -1;
		String s = sc.next().toUpperCase();
		if (s.equals("O") || s.equals("ORANGE")) team = 1;
		else if (s.equals("B") || s.equals("BLUE")) team = 2;
		else System.out.println("Bad team.");
		
		/* Initiate future variables */
		WordBaseScanner scanner;
		WordBaseSolver solver = new WordBaseSolver();
		WordBasePlayer player;
		
		if (f.equals("saved.txt")) //if they ask to load a game, use the input file rather than read an image
		{
			Scanner sc1 = new Scanner(new File("saved.txt"));
			char[][] letterBoard = new char[13][10];
			int[][] scoreBoard = new int[13][10];
			for (int i = 0; i < letterBoard.length; i++)
			{
				String line = sc1.next();
				for (int j = 0; j < letterBoard[i].length; j++)
				{
					letterBoard[i][j] = line.charAt(j);
				}
			}
			for (int i = 0; i < scoreBoard.length; i++)
			{
				String line = sc1.next();
				for (int j = 0; j < scoreBoard[i].length; j++)
				{
					scoreBoard[i][j] = Character.getNumericValue(line.charAt(j));
				}
			}
			solver = new WordBaseSolver(letterBoard, scoreBoard);
		} else //run the scanning and solving procedures
		{
			scanner = new WordBaseScanner(f);
			solver = new WordBaseSolver(scanner.getLetterBoard(), scanner.getScoreBoard());
		}
		
		player = new WordBasePlayer(solver.getBoard(), solver.getScoreBoard(), team);
	}
}