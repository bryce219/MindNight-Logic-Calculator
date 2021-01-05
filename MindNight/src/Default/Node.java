package Default;

import java.util.ArrayList;

/**
 * @author Justin Parker
 * Created On: 22/07/20
 * V. 2.2
 */
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
	
	/**
	 * @param players
	 * @param hackers
	 * constructor for Node
	 */
	public Node(ArrayList<Player> players, int hackers) {
		this.players = players;
		this.hackers = hackers;
	}
	
	/**
	 * @return hackers
	 * returns the number of hackers the node
	 */
	public int getNumHackers() { return hackers; }
	
	/**
	 * @return players
	 * returns the number of players the node
	 */
	public ArrayList<Player> getPlayers() { return players; }
}
