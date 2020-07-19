package Default;

/**
 * @author Justin Parker
 * Created On: 18/07/20
 * V. 1.0
 */
public class Player extends Calculator{

	int playerNumber;
	String name;
	double bias;
	
	public Player(int num, String name) {
		playerNumber = num;
		this.name = name;
		bias = 0.5;
	}
	
	public String getName() { return name; }
	public double getBias() { return bias; }
	
	public void setBias(double bias) { this.bias = bias; }
}
