package Default;

import java.util.ArrayList;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 1.1
 */
public class Instance extends Calculator{
	
	boolean[] key = new boolean[numPlayers];
	double weight;
	
	/**
	 * @param encodedKey
	 * constructor for Instance
	 */
	public Instance(String encodedKey) {
		for(int i = 0; i < encodedKey.length(); i++) {
			if((int)(encodedKey.charAt(i)-'0') == 0)
				key[i] = false;
			else
				key[i] = true;
		}
		updateWeight();
	}
	
	/**
	 * @return agents
	 * returns an arraylist of all the agents
	 */
	public ArrayList<Integer> getAgents(){
		ArrayList<Integer> agents = new ArrayList<Integer>();
		for(int i = 0; i < key.length; i++)
			if(!key[i])
				agents.add(i);
		return agents;
	}
	
	/**
	 * @return hackers
	 * returns an arraylist of all the hackers
	 */
	public ArrayList<Integer> getHackers(){
		ArrayList<Integer> hackers = new ArrayList<Integer>();
		for(int i = 0; i < key.length; i++)
			if(key[i])
				hackers.add(i);
		return hackers;
	}
	
	/**
	 * Updates the instances weight based on some equation I came up with
	 */
	public void updateWeight() {
		weight = 1;
		double temp;
		for(int i = 0; i < key.length; i++) {
			temp = key[i] ? 1 : 0;
			weight *= Math.abs(Players.get(i).getBias() + temp - 1d);
		}
	}
	
	/**
	 * @return weight
	 * gets the instance's 'truth' weight
	 */
	public double getWeight() { return weight; }
	
	/**
	 * @param weight
	 * sets the instance's 'truth' weight
	 */
	public void setWeight(double weight) { this.weight = weight; }
	
	/**
	 * @param player
	 * @return weight
	 * returns 0 if player in this instance isn't a hacker, and 'player' if they are one 
	 */
	public double getPlayerWeight(int player) {
		double temp = key[player] ? 1 : 0;
		return temp * weight;
	}
}
