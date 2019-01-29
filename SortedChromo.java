package salesman;

public class SortedChromo
{
	private int chromo_num;
	private int distance;
	
	public SortedChromo(int chromo_num, int distance)
	{
		this.chromo_num = chromo_num;
		this.distance = distance;
	}
	
	public int getChromoNum()
	{
		// which index they are in the population
		return chromo_num;
	}
	
	public int getDistance()
	{
		return distance;
	}
}