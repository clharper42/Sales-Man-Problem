package salesman;

public class CityDistanceTo
{
	private String name;
	private int distance;
	private int gene;
	
	public CityDistanceTo(String name, int distance, int gene)
	{
		this.name = name;
		this.distance = distance;
		this.gene = gene;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public String getName()
	{
		return name;
	}
	public int getGene()
	{
		return gene;
	}
	
}