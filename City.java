package salesman;
import java.util.ArrayList;
import java.util.Iterator;

public class City
{

	//private int gene;
	//private String name;
	public static int numofcities;
	public static ArrayList<String> citygenes;
	public static int[][] distances; // a matrix where the indexes refer to the cities postion in the orginal array and it stores the distance. EX: [4][3] contains the distance between city 3 and 4

	/*public City(String name, int gene)
	{
		this.name = name;
		this.gene = gene; //index in initial array
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getGene()
	{
		return gene;
	}
	
	public void addCityDistanceTo(int gene_from , int gene_to, int distance)
	{
		distances[gene_from][gene_to] = distance;
	}
	
	public int getDistanceTo(int gene_from, int gene_to)
	{
		return distances[gene_from][gene_to];
	}*/
	
}
