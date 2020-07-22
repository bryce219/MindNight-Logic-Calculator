package Default;

import java.util.ArrayList;

/**
 * @author Justin Parker
 * Created On: 22/07/20
 * V. 1.0
 */
public class Misc extends Calculator{
	
	/**
	 * @return Instances
	 * Runs at the beginning of program, generates all possible Hacker-Agent configurations and puts them in 'Instances'
	 */
	public static ArrayList<Instance> GenerateInstances() {
		ArrayList<Instance> Instances = new ArrayList<Instance>();
		int totalOnes;
		String encodedKey;
		for(int i = 0; i < Math.pow(2, numPlayers); i++) { // generate all combos of 1|0 of length 'numPlayers'
			totalOnes = 0;
			encodedKey = PadZeros(Integer.toBinaryString(i),numPlayers); // converts the number to binary
			
			for(int j = 0; j < numPlayers; j++) // adds up the ones
				if(encodedKey.charAt(j)-'0' == 1)
					totalOnes++;
			
			if(totalOnes == numHackers) // if total ones equals total number of hackers, it can be used as a valid instance
				Instances.add(new Instance(encodedKey));
		}
		return Instances;
	}
	
	/**
	 * @param string
	 * @param length
	 * @return string
	 * Adds '0' on the left of a string repeatedly until its 'length' long
	 */
	public static String PadZeros(String string, int length) {
		while(string.length() < length)
			string = "0" + string;
		return string;
	}
	
	/**
	 * Run at the start of the program, adds 'numPlayers' elements to 'Chances'
	 */
	public static void InitializeChances() {
		while(Chances.size() < numPlayers)
			Chances.add(-1d);
		UpdateChances();
	}
	
	/**
	 * @param name
	 * @return Player Number
	 * Returns the player number within 'Players' whos name matches 'name'
	 */
	public static int NameToInt(String name) {
		for(int i = 0; i < Players.size(); i++)
			if(Players.get(i).getName().equals(name))
				return i;
		return -1;
	}

	/**
	 * @param name
	 * @return Player
	 * Returns the Player within 'Players' whos name matches 'name'
	 */
	public static Player NameToPlayer(String name) {
		for(int i = 0; i < Players.size(); i++)
			if(Players.get(i).getName().equals(name))
				return Players.get(i);
		return new Player(-1,"-1");
	}
	
	/**
	 * Prints 'Chances' in a visually readable way
	 */
	public static void PrintChances() {
		for(int i = 0; i < Players.size(); i++)
			System.out.println("Player "+(i+1)+" :  "+String.format("%"+(-longestNameLength)+"s", Players.get(i).getName())+" | "+String.format("%.2f", Chances.get(i))+"%");
		System.out.print("\nNode #"+nodeNum);
	}
	
	/**
	 * Prints the node history of the game
	 */
	public static void PrintInfo() {
		System.out.println();
		for(int i = 0; i < History.size(); i++) {
			System.out.print("Node #"+(i+1)+": ");
			if(History.get(i).getNumHackers() >= 1)
				System.out.print("Hacked - ");
			else if(History.get(i).getNumHackers() == 0)
				System.out.print("Secured - ");
			System.out.println(History.get(i).getNumHackers()+" Hacker(s)");
			for(int j = 0; j < History.get(i).getPlayers().size(); j++)
				System.out.println("	- "+History.get(i).getPlayers().get(j).getName());
		}
		if(History.size() == 0)
			System.out.println("\nNo game history to display...");
		System.out.println();
	}
	
	/**
	 * @return relationFactor
	 * Returns the incentive factor for hackers to hack
	 */
	public static double CalculateRelationFactor() {
		if(hackedNodes >= 2 || securedNodes >= 2)
			return 1;
		else
			switch(nodeNum) {
			case 1:
				return n1;
			case 2:
				return n2;
			case 3:
				return n3;
			case 4:
				return n4;
			}
		return 0;
		
	}
	
	public static void UseRelationFactor(double relationFactor) {
		
	}
}
