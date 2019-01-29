package salesman;

public class BestChromo
{
	private int chromo_num;
	private int distance;
	
	public BestChromo(int chromo_num, int distance)
	{
		this.chromo_num = chromo_num;
		this.distance = distance;
	}
	
	public int getChromoNum()
	{
		return chromo_num;
	}
	
	public int getDistance()
	{
		return distance;
	}
}