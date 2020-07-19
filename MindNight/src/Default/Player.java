package Default;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 1.1
 */
public class Player extends Calculator{

	int playerNumber;
	String name;
	double bias;
	
	/**
	 * @param num
	 * @param name
	 * constructor for Player
	 */
	public Player(int num, String name) {
		playerNumber = num;
		this.name = name;
		bias = 0.5;
	}
	
	/**
	 * @return name
	 * returns the name of the player
	 */
	public String getName() { return name; }
	
	/**
	 * @return bias
	 * returns the bias of the player
	 */
	public double getBias() { return bias; }
	
	/**
	 * @param bias
	 * sets the bias of the player
	 */
	public void setBias(double bias) { this.bias = bias; }
}
