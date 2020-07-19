package Default;

import java.util.ArrayList;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 1.0
 */
public class Instance extends Calculator{
	
	boolean[] key = new boolean[numPlayers];
	double weight;
	
	public Instance(String encodedKey) {
		for(int i = 0; i < encodedKey.length(); i++) {
			if((int)(encodedKey.charAt(i)-'0') == 0)
				key[i] = false;
			else
				key[i] = true;
		}
		updateWeight();
	}
	
	public ArrayList<Integer> getAgents(){
		ArrayList<Integer> agents = new ArrayList<Integer>();
		for(int i = 0; i < key.length; i++)
			if(!key[i])
				agents.add(i);
		return agents;
	}
	
	public ArrayList<Integer> getHackers(){
		ArrayList<Integer> hackers = new ArrayList<Integer>();
		for(int i = 0; i < key.length; i++)
			if(key[i])
				hackers.add(i);
		return hackers;
	}
	
	public void updateWeight() {
		weight = 1;
		double temp;
		for(int i = 0; i < key.length; i++) {
			temp = key[i] ? 1 : 0;
			weight *= Math.abs(Players.get(i).getBias() + temp - 1d);
		}
	}
	
	public double getWeight() { return weight; }
	public void setWeight(double weight) { this.weight = weight; }
	
	public double getPlayerWeight(int player) {
		double temp = key[player] ? 1 : 0;
		return temp * weight;
	}
}
