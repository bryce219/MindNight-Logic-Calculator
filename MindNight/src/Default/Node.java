package Default;

import java.util.ArrayList;

public class Node {

	static int[][] nodeCounts = {
			{2,3,2,3,3},
			{2,3,4,3,4},
			{2,3,3,4,4}, //is this right?
			{3,4,5,4,5}  //is this right?
			},

			requiredHacks = {
			{1,1,1,1,1},
			{1,1,1,1,1},
			{1,1,1,2,1},
			{1,1,1,2,1},
			};
	
	ArrayList<Player> players;
	int hackers;
	
	public Node(ArrayList<Player> players, int hackers) {
		this.players = players;
		this.hackers = hackers;
	}
	
	public int getNumHackers() { return hackers; }
	
	public ArrayList<Player> getPlayers() { return players; }
}
