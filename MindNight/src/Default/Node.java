package Default;

import java.util.ArrayList;

public class Node {

	ArrayList<Player> players;
	int hackers;
	
	public Node(ArrayList<Player> players, int hackers) {
		this.players = players;
		this.hackers = hackers;
	}
	
	public int getNumHackers() { return hackers; }
	
	public ArrayList<Player> getPlayers() { return players; }
}
