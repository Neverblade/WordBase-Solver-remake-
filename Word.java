import java.util.*;

public class Word implements Comparable<Word>
{
	private String word;
	private ArrayList<Integer> xPath;
	private ArrayList<Integer> yPath;
	
	private int score;
	
	
	private static final double MULTIPLIER = 0.5;
	
	public Word(String word, ArrayList<Integer> x, ArrayList<Integer> y)
	{
		this.word = word;
		xPath = x;
		yPath = y;
	}
	
	public Word(int[][] used, String currWord)
	{
		word = currWord;
		xPath = new ArrayList<Integer>();
		yPath = new ArrayList<Integer>();
		for (int i = 0; i < currWord.length(); i++)
		{
			xPath.add(-1);
			yPath.add(-1);
		}
		
		for (int i = 0; i < used.length; i++)
		{
			for (int j = 0; j < used[i].length; j++)
			{
				if (used[i][j] != -1)
				{
					xPath.set(used[i][j], j);
					yPath.set(used[i][j], i);
				}
			}
		}
	}
	
	/* Given a board of current states, give this word a score */
	public void scoreWord(int[][] scoreBoard, int team)
	{
		if (scoreBoard[yPath.get(0)][xPath.get(0)] != team)
		{
			System.out.println("Supposed team: " + team + " Actual Start: " + scoreBoard[yPath.get(0)][xPath.get(0)]);
			return;
		}
		
		/* Copy the scoreBoard so you can manipulate it freely */
		int[][] newBoard = new int[scoreBoard.length][scoreBoard[0].length];
		for (int i = 0; i < newBoard.length; i++)
		{
			newBoard[i] = scoreBoard[i].clone();
		}
		
		int score = 0;
		
		/* Get the gains */
		
		for (int i = 0; i < xPath.size(); i++)
		{
			if (newBoard[yPath.get(i)][xPath.get(i)] != team)
			{
				//if bombs, adjacent 
				if (newBoard[yPath.get(i)][xPath.get(i)] == 3)
				{
					if (yPath.get(i) - 1 >= 0)
					{
						newBoard[yPath.get(i) - 1][xPath.get(i)] = team;
						if (team == 1) score += yPath.get(i) - 1;
						else if (team == 2) score += 12 - (yPath.get(i) - 1);
					} else if (yPath.get(i) + 1 < newBoard.length)
					{
						newBoard[yPath.get(i) + 1][xPath.get(i)] = team;
						if (team == 1) score += yPath.get(i) + 1;
						else if (team == 2) score += 12 - (yPath.get(i) + 1);
					} else if (xPath.get(i) - 1 >= 0)
					{
						newBoard[yPath.get(i)][xPath.get(i) - 1] = team;
						if (team == 1) score += yPath.get(i);
						else if (team == 2) score += 12 - yPath.get(i);
					} else if (xPath.get(i) + 1 < newBoard[0].length)
					{
						newBoard[yPath.get(i)][xPath.get(i) + 1] = team;
						if (team == 1) score += yPath.get(i);
						else if (team == 2) score += 12 - yPath.get(i);
					}
				}
				
				//actual tile
				int temp = 0;
				newBoard[yPath.get(0)][xPath.get(0)] = team;
				if (team == 1) temp = yPath.get(i);
				else if (team == 2) temp = 12 - yPath.get(i);
				else System.out.println("Wrong team.");
				
				if (temp == 12) score += 999999;
				else score += temp;
			}
		}
		
		/* Go through and what tiles you cut off from the opponent */
		if (team == 1) markCutOff(newBoard, 2, 9, 12);
		else if (team == 2) markCutOff(newBoard, 1, 0, 0);
		
		/* Take the cut off ones and add them to the score */
		int target = -1;
		if (team == 1) target = 2;
		else if (team == 2) target = 1;
		for (int i = 0; i < newBoard.length; i++)
		{
			for (int j = 0; j < newBoard[i].length; j++)
			{
				if (newBoard[i][j] == target)
				{
					if (team == 1) score += 12 - i;
					else if (team == 2) score += i;
				}
			}
		}		
		this.score = score;
	}
	
	public void markCutOff(int[][] newBoard, int team, int x, int y)
	{
		newBoard[y][x] = 5;
		
		for (int i = y-1; i <= y+1; i++)
		{
			for (int j = x-1; j <= x+1; j++)
			{
				if (i >= 0 && i < newBoard.length && j >= 0 && j < newBoard[i].length  && newBoard[i][j] == team)
				{
					markCutOff(newBoard, team, j, i);
				}
			}
		}
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getX(int index)
	{
		return xPath.get(index);
	}
	
	public int getY(int index)
	{
		return yPath.get(index);
	}
	
	public void print(int[][] scoreBoard, int[][] newBoard, int score)
	{
		System.out.println(word);
		System.out.println("Original: ");
		for (int i = 0; i < scoreBoard.length; i++)
		{
			for (int j = 0; j < scoreBoard[i].length; j++)
			{
				System.out.print(scoreBoard[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("New: ");
		for (int i = 0; i < newBoard.length; i++)
		{
			for (int j = 0; j < newBoard[i].length; j++)
			{
				System.out.print(newBoard[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("Score: " + score);
	}
	
	public String toString()
	{
		return word + "  (" + yPath.get(0) + ", " + xPath.get(0) + ")  Score: " + score;
	}
	
	public int compareTo(Word other)
	{
		if (score < other.getScore()) return -1;
		else if (score > other.getScore()) return 1;
		else return 0;
	}
}