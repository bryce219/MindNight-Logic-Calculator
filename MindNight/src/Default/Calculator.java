package Default;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 2.0
 */
public class Calculator {
	
	static int numPlayers, numHackers, longestNameLength = 0, nodeNum = 1, hackedNodes = 0, securedNodes = 0;
	static ArrayList<Player> Players;
	static ArrayList<Instance> Instances;
	static ArrayList<Double> Chances = new ArrayList<Double>(); // arraylist of double, which are the chances each player being hacker
	static ArrayList<Node> History = new ArrayList<Node>();
	
	static boolean secureNodeHeuristic = false;
	static double n1 = 0.0, n2 = 0.08, n3 = 0.12, n4 = 0.15;
	
	/**
	 * @param num
	 * @return totalHackerInstances
	 * Returns the number of instances in Instances that have player number 'num' as a hacker
	 */
	public static int GetTotalHackerInstances(int num) {
		int totalHackerInstances = 0;
		for(int i = 0; i < Instances.size(); i++) // run through all current instances
			if(Instances.get(i).getHackers().contains(num)) // if one of the hackers is 'num', add one to totalHackerInstances
				totalHackerInstances++;
		return totalHackerInstances;
	}
	
	/**
	 * @param numHackers
	 * @param numbers
	 * Removes instances which are impossible (if there's not at least 'numHackers' hackers between the players in 'numbers')
	 */
	public static void RemoveInstances(int numHackers, ArrayList<Integer> numbers) {
		int hackers;
		for(int i = 0; i < Instances.size(); i++) { // run through all instances
			hackers = 0;
			for(int j = 0; j < numbers.size(); j++) // run through all player numbers which must contain at lease 'numHackers' hackers
				if(Instances.get(i).getHackers().contains(numbers.get(j))) // if one of the players is a hacker, add one to 'hackers'
					hackers++;
			if(hackers < numHackers) { // if the total number of hackers found in the sample in that instance is less than what is required, that instance cannot exist
				Instances.remove(i);
				i--;
			}
		}
		UpdateChances();
	}
	
	/**
	 * Updates 'Chances' to be used when displaying probabilities
	 */
	public static void UpdateChances() {
		for(int i = 0; i < Instances.size(); i++) // updates the weight with respect to bias
			Instances.get(i).updateWeight();
		
		double totalSum = 0;
		for(int i = 0; i < Instances.size(); i++) // this is part 1/2 to normalize values, for percentages
			totalSum += Instances.get(i).getWeight();
		
		totalSum = 1/totalSum;
		for(int i = 0; i < Instances.size(); i++) // this is part 2/2 to normalize values, for percentages
			Instances.get(i).setWeight(Instances.get(i).getWeight() * totalSum);

		for(int i = 0; i < Chances.size(); i++) { // finally updates 'Chances' to be percent-compatable
			totalSum = 0;
			for(int j = 0; j < Instances.size(); j++) {
				totalSum += Instances.get(j).getPlayerWeight(i);
			}
			Chances.set(i, 100*totalSum);
		}
	}

	/**
	 * @return names
	 * Adds the names of all players within 'Players' to an arraylist and returns that
	 */
	public static ArrayList<String> getNames(){
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < Players.size(); i++)
			names.add(Players.get(i).getName());
		return names;
	}
	
	/**
	 * @param scan
	 * Handles the sub-menu when the user selects <Logic>
	 */
	public static void NodeHandler(Scanner scan) {
		int inNode, hackers;
		boolean exit = false;
		try {
			System.out.print("\n\nHow many Players were in the node?\n>> ");
			inNode = scan.nextInt();
			System.out.print("\nHow many Hackers were in the node?\n>> ");
			hackers = scan.nextInt();
			System.out.println("\nPlease type the names of the "+inNode+" Players that were the node...");
			ArrayList<Integer> playerNums = new ArrayList<Integer>();
			ArrayList<Player> players = new ArrayList<Player>();
			String temp = "";
			for(int i = 0; i < inNode; i++) {
				while(true) {
					System.out.print((i+1)+") >> ");
					temp = scan.next();
					players.add(Misc.NameToPlayer(temp));
					if(Misc.NameToInt(temp) == -1)
						System.out.println("\nInvalid name... Valid names are: "+getNames());
					else
						break;
				}
				playerNums.add(Misc.NameToInt(temp));
				players.add(Misc.NameToPlayer(temp));
			}
			System.out.print("\n\nWould you like to proceed with this information? (Yes / No)\n>> ");
			exit = false;
			while(!exit)
				switch(scan.next()) {
				case "Yes":
					if(hackers >= 1) {
						RemoveInstances(hackers,playerNums);
						/*if(hackedNodes >= 2)
							for(Player player: players) {
								System.out.println("A");
								player.addBias(Misc.CalculateRelationFactor());
								System.out.println("B");
								player.returnBiasToRange();
								System.out.println(player.getBias());
							}
						hackedNodes++;*/
					}
					else if (hackers == 0) {
						/*if(securedNodes >= 2)
							for(Player player: players) {
								player.addBias(-Misc.CalculateRelationFactor());
								player.returnBiasToRange();
								System.out.println(player.getBias());
							}
						securedNodes++;*/
					}
					History.add(new Node(players,hackers));
					nodeNum++;
					exit = true;
					break;
				case "No":
					exit = true;
					break;
				default:
					System.out.print("\nNot a valid selection...\n\n>> ");
					break;
			}
		}catch(Exception e) {
			System.out.println("\nThere was an error... Returning you to the main menu");
		}
		System.out.println("\n");
	}
	
	/**
	 * @param scan
	 * Handles the sub-menu when the user selects <Assume>
	 */
	public static void AssumeHandler(Scanner scan) {
		try {
			boolean exit = false;
			String input;
			while(!exit) {
				System.out.print("\nEnter the name of the Player you would like to assume information about ('Reset' to reset everyone)\n>> ");
				input = scan.next();
				if(input.equals("Reset")) {
					for(int i = 0; i < Players.size(); i++) // reset all players biases
						Players.get(i).setBias(0.5);
					exit = true;
					System.out.print("\nAll players bias was reset");
				}
				else if(getNames().contains(input)) {
					System.out.print("\n\nHow much do you trust this person? (decimal from 0 - 1, with 0 being Agent and 1 being Hacker)\n>> ");
					double newBias = -1;
					while(!exit) {
						newBias = scan.nextDouble();
						if(newBias >= 0 && newBias <= 1)
							exit = true;
						else
							System.out.print("\nPlease enter a decimal between 0 and 1\n>> ");
					}
					
					Players.get(Misc.NameToInt(input)).setBias(newBias);
					System.out.print("\n"+input+"'s bias was set to: "+newBias);
				}
				else {
					System.out.print("\nNot a valid selection...\n\n");
				}
			}
		}catch(Exception e) {
			System.out.println("\nThere was an error... Returning you to the main menu");
		}
		System.out.println("\n");
		}
	
	/**
	 * @param scan
	 * The main menu
	 */
	public static void Menu(Scanner scan) {
		boolean exit = false;
		while(!exit) {			
			scan = new Scanner(System.in);
			System.out.print("\n===================================\n"
					+ "<Node> - Use when a node has proceeded.\n"
					+ "<Assume> - Use to set your trust with a player\n"
					+ "<Info> - Use to see all game information\n"
					+ "<Exit> - Exit the program.\n\n>> ");
			switch(scan.next()) {
			case "Node":
				NodeHandler(scan);
				UpdateChances();
				Misc.PrintChances();
				break;
			case "Assume":
				AssumeHandler(scan);
				UpdateChances();
				Misc.PrintChances();
				break;
			case "Info":		
				Misc.PrintInfo();
				Misc.PrintChances();
				break;
			case "Exit":
				exit = true;
				System.out.print("\n\nThanks for using me!");
				break;
			}
		}
		scan.close();
	}
	
	/**
	 * @param scan
	 * Runs the code which is used before entering the menu
	 */
	public static void Start(Scanner scan) { 
		
		if(!secureNodeHeuristic) {
			Misc.n1 = Misc.n2 = Misc.n3 = Misc.n4 = 0;
		}
		
		System.out.print("Welcome to Justin Parker's MINDNIGHT Logic Helper!\n\nPlease enter the number of players (5-8)\n>> ");
		
		boolean exit = false;
		while(!exit) {
			try {
				numPlayers = scan.nextInt();
			}catch(Exception e) {
				System.out.print("\nVery Funny...");
				System.exit(0);
			}
			
			if(numPlayers < 5 || numPlayers > 8) // number of mindnight players must be between 5 and 8
				System.out.print("\nInvalid number of players...\n>> ");
			else
				exit = true;
		}
		
		numHackers = (int)Math.floor((double)(numPlayers-1)/2); //hackers-based-on-players equation
		Players = new ArrayList<Player>();
		System.out.println("\nThere are "+numHackers+" Hackers and "+(numPlayers-numHackers)+" Agents.  Please enter...");
		
		for(int i = 0; i < numPlayers; i++) {
			System.out.print("\nPlayer "+(i+1)+"'s name\n>> ");
			Players.add(new Player(i,scan.next()));
		}
		
		for(int i = 0; i < Players.size(); i++)
			if(longestNameLength < Players.get(i).getName().length())
				longestNameLength = Players.get(i).getName().length();
		
		Instances = Misc.GenerateInstances();
		Misc.InitializeChances();
		System.out.println();
		Misc.PrintChances();
		Menu(scan);
		
		/*
		Players.get(NameToInt("a")).setBias(0);
		
		UpdateChances();
		System.out.println(Chances);
		
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(NameToInt("a"));
		numbers.add(NameToInt("b"));
		numbers.add(NameToInt("c"));
		RemoveInstances(2,numbers);
		
		UpdateChances();
		System.out.println(Chances);
		*/
		
		scan.close();
	}
	
	/**
	 * @param args
	 * The runner and initializer code
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Start(scan);
	}
}
