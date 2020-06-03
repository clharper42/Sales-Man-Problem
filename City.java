package salesman;
import java.util.ArrayList;
import java.util.Iterator;

public class City
{

	private int gene;
	private String name;
	private ArrayList<CityDistanceTo> cities;
	public static int numofcities;
	public static ArrayList<String> citygenes;
	
	public City(String name, int gene)
	{
		this.name = name;
		this.gene = gene; //index in initial array
		cities = new ArrayList<CityDistanceTo>();
	}

	public void SetCityDistanceToArrayListSize()
	{
		for(int i = 0; i < numofcities; i++)
		{
			cities.add(new CityDistanceTo(name, 0, citygenes.indexOf(name)));
		}
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
		//cities.add(city);
		cities.set(citygenes.indexOf(city_name), city);
	}
	
	public int getDistanceTo(String city_name)
	{
		/*Iterator<CityDistanceTo> city_iterator = cities.iterator();
		while(city_iterator.hasNext())
		{
			CityDistanceTo temp_city = city_iterator.next();
			if(city_name.equals(temp_city.getName()))
			{
				return temp_city.getDistance();
			}
		}*/
		return cities.get(citygenes.indexOf(city_name)).getDistance();
		//return 0;
	}
	
	public int getDistanceTo(int gene_to)
	{
		/*Iterator<CityDistanceTo> city_iterator = cities.iterator();
		while(city_iterator.hasNext())
		{
			CityDistanceTo temp_city = city_iterator.next();
			if(gene_to == temp_city.getGene())
			{
				return temp_city.getDistance();
			}
		}
		return 0;*/
		return cities.get(gene_to).getDistance();
	}
	
}
