package Default;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 1.1
 */
public class Calculator {
	
	static int numPlayers, numHackers, longestNameLength = 0;
	static ArrayList<Player> Players;
	static ArrayList<Instance> Instances;
	static ArrayList<Double> Chances = new ArrayList<Double>();
	
	public static int GetTotalHackerInstances(int num) {
		int totalHackerInstances = 0;
		for(int i = 0; i < Instances.size(); i++)
			if(Instances.get(i).getHackers().contains(num))
				totalHackerInstances++;
		return totalHackerInstances;
	}
	
	public static void RemoveInstances(int numHackers, ArrayList<Integer> numbers) {
		int hackers;
		for(int i = 0; i < Instances.size(); i++) {
			hackers = 0;
			for(int j = 0; j < numbers.size(); j++)
				if(Instances.get(i).getHackers().contains(numbers.get(j)))
					hackers++;
			if(hackers < numHackers) {
				Instances.remove(i);
				i--;
			}
		}
		UpdateChances();
	}
	
	public static void UpdateChances() {
		for(int i = 0; i < Instances.size(); i++) {
			Instances.get(i).updateWeight();
		}
		double totalSum = 0;
		for(int i = 0; i < Instances.size(); i++) {
			totalSum += Instances.get(i).getWeight();
		}
		totalSum = 1/totalSum;
		for(int i = 0; i < Instances.size(); i++) {
			Instances.get(i).setWeight(Instances.get(i).getWeight() * totalSum);
		}

		for(int i = 0; i < Chances.size(); i++) {
			totalSum = 0;
			for(int j = 0; j < Instances.size(); j++) {
				totalSum += Instances.get(j).getPlayerWeight(i);
			}
			Chances.set(i, 100*totalSum);
		}
	}
	
	public static ArrayList<Instance> GenerateInstances() {
		ArrayList<Instance> Instances = new ArrayList<Instance>();
		int totalOnes;
		String encodedKey;
		for(int i = 0; i < Math.pow(2, numPlayers); i++) { //generate all combos of 1|0 of length 'numPlayers'
			totalOnes = 0;
			encodedKey = PadZeros(Integer.toBinaryString(i),numPlayers);
			
			for(int j = 0; j < numPlayers; j++)
				if(encodedKey.charAt(j)-'0' == 1)
					totalOnes++;
			
			if(totalOnes == numHackers)
				Instances.add(new Instance(encodedKey));
		}
		return Instances;
	}
	
	public static String PadZeros(String string, int length) {
		while(string.length() < length)
			string = "0" + string;
		return string;
	}
	
	public static void InitializeChances() {
		while(Chances.size() < numPlayers)
			Chances.add(-1d);
		UpdateChances();
	}
	
	public static int NameToInt(String name) {
		for(int i = 0; i < Players.size(); i++)
			if(Players.get(i).getName().equals(name))
				return i;
		return -1;
	}

	public static void PrintChances() {
		UpdateChances();
		for(int i = 0; i < Players.size(); i++)
			System.out.println("Player "+i+" :  "+String.format("%"+(-longestNameLength)+"s", Players.get(i).getName())+" | "+String.format("%.2f", Chances.get(i))+"%");
	}
	
	public static ArrayList<String> getNames(){
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < Players.size(); i++)
			names.add(Players.get(i).getName());
		return names;
	}
	
	public static void LogicHandler(Scanner scan) {
		int inNode,hackers;
		try {
			System.out.print("\n\nHow many Players were in the node?\n>> ");
			inNode = scan.nextInt();
			System.out.print("\nHow many Hackers were in the node?\n>> ");
			hackers = scan.nextInt();
			System.out.println("\nPlease type the names of the "+inNode+" Players that were the node...");
			ArrayList<Integer> players = new ArrayList<Integer>();
			String temp;
			for(int i = 0; i < inNode; i++) {
				while(true) {
					System.out.print("\n"+(i+1)+") >> ");
					temp = scan.next();
					if(NameToInt(temp) == -1)
						System.out.println("\nInvalid name... Valid names are: "+getNames());
					else
						break;
				}
				players.add(NameToInt(temp));
			}
			
			System.out.print("\n\nWould you like to proceed with this information? (Yes / No)\n\n>> ");
			boolean exit = false;
			while(!exit)
				switch(scan.next()) {
				case "Yes":
					RemoveInstances(hackers,players);
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
	
	public static void AssumeHandler(Scanner scan) {
		try {
			boolean exit = false;
			String input;
			while(!exit) {
				System.out.print("\nEnter the name of the Player you would like to assume information about ('Reset' to reset everyone)\n\n>> ");
				input = scan.next();
				if(input.equals("Reset")) {
					System.out.print("\n\nWould you like to proceed with this information? (Yes / No)\n\n>> ");
					switch(scan.next()) {
					case "Yes":
						for(int i = 0; i < Players.size(); i++)
							Players.get(i).setBias(0.5);
						exit = true;
						break;
					case "No":
						exit = true;
						break;
					default:
						System.out.print("\nNot a valid selection...\n\n");
						break;
					}
				}
				else if(getNames().contains(input)) {
					System.out.print("\n\nHow much do you trust this person? (0 - 1, with 0 being Agent and 1 being Hacker)\n\n>> ");
					double newBias = scan.nextDouble();
					System.out.print("\n\nWould you like to proceed with this information? (Yes / No)\n\n>> ");
					switch(scan.next()) {
					case "Yes":
							Players.get(NameToInt(input)).setBias(newBias);
						exit = true;
						break;
					case "No":
						exit = true;
						break;
					default:
						System.out.print("\nNot a valid selection...\n\n");
						break;
					}
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
	
	public static void Menu(Scanner scan) {
		boolean exit = false;
		while(!exit) {
			scan = new Scanner(System.in);
			System.out.print("\n===================================\n"
					+ "<Logic> - Use when a hacker was detected within a node.\n"
					+ "<Assume> - Use to set your trust with a player\n"
					+ "<Exit> - Exit the program.\n\n>> ");
			switch(scan.next()) {
			case "Logic":
				LogicHandler(scan);
				PrintChances();
				break;
			case "Assume":
				AssumeHandler(scan);
				PrintChances();
				break;
			case "Exit":
				exit = true;
				System.out.print("\n\nThanks for using me!");
				break;
			}
		}
		scan.close();
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Welcome to Justin Parker's MINDNIGHT Logic Helper!\n\nPlease enter the number of players (5-8)\n>> ");
		
		boolean exit = false;
		while(!exit) {
			try {
				numPlayers = scan.nextInt();
			}catch(Exception e) {
				System.out.print("\nVery Funny...");
				System.exit(0);
			}
			
			if(numPlayers < 5 || numPlayers > 8)
				System.out.print("\nInvalid number of players...\n>> ");
			else
				exit = true;
		}
		
		numHackers = (int)Math.floor((double)(numPlayers-1)/2);
		Players = new ArrayList<Player>();
		System.out.println("\nThere are "+numHackers+" Hackers and "+(numPlayers-numHackers)+" Agents.  Please enter...");
		
		for(int i = 0; i < numPlayers; i++) {
			System.out.print("\nPlayer "+(i+1)+"'s name\n>> ");
			Players.add(new Player(i,scan.next()));
		}
		
		for(int i = 0; i < Players.size(); i++)
			if(longestNameLength < Players.get(i).getName().length())
				longestNameLength = Players.get(i).getName().length();
		
		Instances = GenerateInstances();
		InitializeChances();
		System.out.println();
		PrintChances();
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
}
