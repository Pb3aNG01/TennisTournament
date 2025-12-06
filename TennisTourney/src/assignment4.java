import java.io.IOException;
import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

class Player
{
	
	@Override
	public String toString() {
		return name;
	}

	private String name;
	private int points = 0;
	
	public String getName() // returns the player's name
	{
		return this.name;
	}
	
	public void setName(String n) // sets the player's name
	{
		this.name = n;
	}
	
	public int getPoints() // returns the player's score
	{
		return this.points;
	}
	
	public void addPoints() // adds points to the player's score
	{
		this.points++;
	}
	
	public Player() // default constructor
	{
		
	}
	
	public Player(String n, int p) // constructor with parameters
	{
		this.name = n;
		this.points = p;
	}
}

class Tournament
{
	
	private int playercount;
	private ArrayList<Player> playerlist = new ArrayList<Player>(); // holds all the players in the tournament
	private ArrayList<Player> playersemi = new ArrayList<Player>(); // holds all the players that won in the first round
	private ArrayList<Player> playerfinal = new ArrayList<Player>(); // holds all the players that won in the second round
	Random rand = new Random();
	
	public void getplayers() // adds the players into the arraylist
	{
		playerlist.clear();
		
		for(int x = 0; x < playercount; x++)
		{
			playerlist.add(new Player());
		}
	}
	
	public void getplayernames(Scanner scan) // user input the names of the players
	{
		String playername = "";
		
		for(int b = 0; b < playerlist.size(); b++)
		{
			System.out.print("Player " + (b+1) + " name: ");
			playername = scan.nextLine();
			playerlist.get(b).setName(playername);
		}
	}
	
	public void setupbracket() // shuffles and then pairs up players for the tournament at the beginning of the game
	{
		System.out.println(playerlist.toString());
		Collections.shuffle(playerlist,rand);
		System.out.println(playerlist.toString());
	}
	
	public void playround(ArrayList<Player> arr) throws IOException // plays rounds of tennis
	{
		int x = 0, score = 0;
		
		while(x < arr.size()-1) // while loop goes through the arraylists until every player has played in the rounds
		{
			whoserver(arr,x,x+1);
			
			while(arr.get(x).getPoints() != 4 && arr.get(x+1).getPoints() != 4) // game is played until someone scores 4 points and wins
			{
				score = rand.nextInt(2)+1;
				
				if(score == 1)
				{
					arr.get(x).addPoints();
					System.out.println("Player " + (x+1) + " " + arr.get(x).getName() + " is now the server");
				}
				else if(score == 2)
				{
					arr.get(x+1).addPoints();
					System.out.println("Player " + (x+2) + " " + arr.get(x+1).getName() + " is now the server");
				}

				if(arr.get(x).getPoints() == 3 && arr.get(x+1).getPoints() == 3) // checks if the score is 40-40 or a deuce
				{
					int tiebreaker = 0;
					int p1adv = 0, p2adv = 0;
					
					System.out.println("DEUCE");
					
					while(p1adv != 2 && p2adv != 2)
					{
						tiebreaker = rand.nextInt(2)+1;
						
						if(tiebreaker == 1)
						{			
							if(p2adv >= 1)
							{
								System.out.println("Player " + (x+1) + " " + arr.get(x).getName() + " advantage");
								p1adv++;
								p2adv--;
							}
							else if(p1adv == 2)
							{
								arr.get(x).addPoints();
							}
							else
							{
								System.out.println("Player " + (x+1) + " " + arr.get(x).getName() + " advantage");
								p1adv++;
							}
						}
						else if(tiebreaker == 2)
						{
							if(p1adv >= 1)
							{
								System.out.println("Player " + (x+2) + " " + arr.get(x+1).getName() + " advantage");
								p2adv++;
								p1adv--;
							}
							else if(p2adv == 2)
							{
								arr.get(x+1).addPoints();
							}
							else
							{
								System.out.println("Player " + (x+2) + " " + arr.get(x+1).getName() + " advantage");
								p2adv++;
							}
						}
					}
				}
				else
				{
					System.out.println(displayscore(arr,x) + "/" + displayscore(arr,x+1));
					cont();
				}
			}
			
			if(arr.get(x).getPoints() == 4)
			{
				System.out.println(arr.get(x).getName() + " is the winner!");
			}
			else if (arr.get(x+1).getPoints() == 4)
			{
				System.out.println(arr.get(x+1).getName() + " is the winner!");
			}
			x+=2;
		}
	}
	
	public void whoserver(ArrayList<Player> arr, int m, int n) // decides who will be first serving in the game
	{
		int cointoss = rand.nextInt(2)+1;
		
		if(cointoss == 1)
		{
			System.out.println("Player " + (m+1) + " " + arr.get(m).getName() + " is the starting server");
		}
		else
		{
			System.out.println("Player " + (n+1) + " " + arr.get(n).getName() + " is the starting server");
		}
	}
	
	public String displayscore(ArrayList<Player> arr, int n) // displays the points of each player after one has scored
	{
		switch(arr.get(n).getPoints())
		{
		case 0:
			return "0";
		case 1:
			return "15";
		case 2:
			return "30";
		case 3:
			return "40";
		}
		return "GAME";
		
	}
	
	public void advance(ArrayList<Player> arr, ArrayList<Player> arr1) // the winners of a round advances to the next one(players moved from one arraylist to another)
	{
		for(int x = 0; x < arr.size(); x++)
		{
			if(arr.get(x).getPoints() == 4)
			{
				arr1.add(new Player(arr.get(x).getName(),0));
			}
		}
	}
	
	public void elimround() throws IOException // first round of the tennis tournament
	{
		System.out.println("First up, the elimination round!");
		playround(playerlist);
		advance(playerlist, playersemi);
	}

	public void semiround() throws IOException // second round of the tennis tournament
	{
		System.out.println("Next up, the semi finals round!");
		playround(playersemi);
		advance(playersemi, playerfinal);
	}

	public void finalround() throws IOException // final round of the tennis tournament
	{
		System.out.println("Last up, the finals round!");
		playround(playerfinal);
		
		if(playerfinal.get(0).getPoints() == 4)
		{
			System.out.println(playerfinal.get(0).getName() + " is the winner of the tournament!");
			System.out.println(playerfinal.get(1).getName() + " is second place");
		}
		else if(playerfinal.get(1).getPoints() == 4)
		{
			System.out.println(playerfinal.get(1).getName() + " is the winner of the tournament!");
			System.out.println(playerfinal.get(0).getName() + " is second place");
		}
	}
	
	public static void cont() throws IOException // will prompt the user to press Enter to continue the match
	{
		System.out.println("Press Enter to continue");
		System.in.read();
	}
	
	public Tournament(int players) // constructor
	{
		this.playercount = players;
	}
}

public class assignment4
{
	static void game() throws IOException
	{
		Scanner scan = new Scanner(System.in);
		Tournament tourney = new Tournament(8);
		
		tourney.getplayers();
		tourney.getplayernames(scan);
		
		tourney.setupbracket();
		
		tourney.elimround();
		tourney.semiround();
		tourney.finalround();
	}
	
	public static void main(String[] args) throws IOException
	{
		game();
	}
}
