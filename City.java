package salesman;
import java.util.ArrayList;
import java.util.Iterator;

public class City
{

	private int gene;
	private String name;
	private ArrayList<CityDistanceTo> cities;
	
	public City(String name, int gene)
	{
		this.name = name;
		this.gene = gene; //index in initial array
		cities = new ArrayList<CityDistanceTo>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getGene()
	{
		return gene;
	}
	
	public void addCityDistanceTo(String city_name, int distance, int gene_to)
	{
		CityDistanceTo city = new CityDistanceTo(city_name, distance, gene_to);
		cities.add(city);
	}
	
	public int getDistanceTo(String city_name)
	{
		Iterator<CityDistanceTo> city_iterator = cities.iterator();
		while(city_iterator.hasNext())
		{
			CityDistanceTo temp_city = city_iterator.next();
			if(city_name.equals(temp_city.getName()))
			{
				return temp_city.getDistance();
			}
		}
		return 0;
	}
	
	public int getDistanceTo(int gene_to)
	{
		Iterator<CityDistanceTo> city_iterator = cities.iterator();
		while(city_iterator.hasNext())
		{
			CityDistanceTo temp_city = city_iterator.next();
			if(gene_to == temp_city.getGene())
			{
				return temp_city.getDistance();
			}
		}
		return 0;
	}
	
}