package salesman;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class SalesmanDriver
{
	public static void main(String[] args) throws IOException
	{
		String path_cities = args[0];
		String path_distance = args[1];
		int num_gen = Integer.parseInt(args[2]);
		int num_pop = Integer.parseInt(args[3]);
		if(num_pop < 20)
		{
			System.out.println("20 is the minimum number in the population");
			System.exit(0);
		}
		else if(num_gen < 2)
		{
			System.out.println("2 is the minimum number of generations");
			System.exit(0);
		}
		String line;
		
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<String> city_names = new ArrayList<String>();
		
		//create list of cities and their genes(index)
		BufferedReader cities_lines = new BufferedReader(new FileReader(path_cities));
		int temp_gene = 0;
		while((line = cities_lines.readLine()) != null)
		{
			City temp_city = new City(line, temp_gene);
			cities.add(temp_city);
			city_names.add(line);
			temp_gene++;
		}
		cities_lines.close();
		
		//Give each city the abilty to know where each city lies in the orginal array and initialize each citites citydistanceto array
		City.numofcities = city_names.size();
		City.citygenes = city_names;
		for(City temp : cities)
		{
			temp.SetCityDistanceToArrayListSize();
		}



		//allow each city to know what cities they connect to and distance and gene of each of those citie
		Scanner sc = new Scanner(new File(path_distance));
		City cityfrom;
		City cityto;
		while(sc.hasNext())
		{
			String city_start = sc.next();
			String city_end = sc.next();
			int distance = sc.nextInt();
			cityfrom = cities.get(city_names.indexOf(city_start));
			cityto = cities.get(city_names.indexOf(city_end));
			System.out.println(city_start + " to " + city_end + " is a distance of " + distance);
			cityfrom.addCityDistanceTo(city_end, distance, cityto.getGene());
		}

		//Set up chromo (which holds ints which symbolize the gene of the cities) and population 
		ArrayList<Integer> chromo = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> population = new ArrayList<ArrayList<Integer>>();
		for(int i = 1; i < cities.size(); i++)
		{
			chromo.add(i);
		}
		//randomize chromo and add them to the population
		for(int i = 0; i < num_pop; i++)
		{
			ArrayList<Integer> copy = new ArrayList<Integer>(chromo);
			Collections.shuffle(copy);
			copy.add(0,0);
			copy.add(0);
			population.add(copy);
		}
		
		//calculate total distance for all  the chromo of the population
		//sorted_chromo is used to get best distance of the chromos
		ArrayList<SortedChromo> sorted_chromo = new ArrayList<SortedChromo>();
		
		SalesmanFunct.sortPop(sorted_chromo, population, cities);
		//Take best 10 from sorted_chromo
		ArrayList<SortedChromo> best_chromo = new ArrayList<SortedChromo>();
		for (int i = 1; i <= 10; i++)
		{
			best_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
		}
		
		
		//Shuffle remaining sorted_chromo and take 10
		Collections.shuffle(sorted_chromo);

		ArrayList<SortedChromo> ran_ten_chromo = new ArrayList<SortedChromo>();
		for (int i = 1; i <= 10; i++)
		{
			ran_ten_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
		}

		//Sort those 10
		Collections.sort(ran_ten_chromo, new Comparator<SortedChromo>()
		{
			// descending sort
			public int compare(SortedChromo sc1, SortedChromo sc2)
			{
				return Integer.valueOf(sc2.getDistance()).compareTo(sc1.getDistance());
			}
		});
		
		//Select best four from ran_ten_chromo
		ArrayList<SortedChromo> final_four_chromo = new ArrayList<SortedChromo>();
		for (int i = 1; i <= 4; i++)
		{
			final_four_chromo.add(ran_ten_chromo.remove(ran_ten_chromo.size()-1));
		}
		
		
		//Shuffle final four and take two
		Collections.shuffle(final_four_chromo);
		ArrayList<SortedChromo> final_two_chromo = new ArrayList<SortedChromo>();
		for (int i = 1; i <= 2; i++)
		{
			final_two_chromo.add(final_four_chromo.remove(final_four_chromo.size()-1));
		}
		
		
		//Crossover
		Random r = new Random();
		int crossover_point = r.nextInt((((chromo.size()+ 2) - 3)- 2) + 1) + 2;

		ArrayList<Integer> crossover_chromo_one = population.get(final_two_chromo.get(0).getChromoNum());
		ArrayList<Integer> crossover_chromo_two = population.get(final_two_chromo.get(1).getChromoNum());
		ArrayList<Integer> crossover_chromo_one_copy = new ArrayList<Integer>(crossover_chromo_one);
		ArrayList<Integer> crossover_chromo_two_copy = new ArrayList<Integer>(crossover_chromo_two);
		
		//cut chromo one at point
		int cco_size = crossover_chromo_one_copy.size();
		SalesmanFunct.cutChromo(crossover_chromo_one_copy,crossover_point,1,cco_size);

		//combine chromo one with chromo two at point
		SalesmanFunct.combChromo(crossover_chromo_one_copy, crossover_chromo_two, cco_size, crossover_point);

		//crossover finding missing genes of one
		ArrayList<Integer> missing_genes_one = new ArrayList<Integer>();
		SalesmanFunct.missingChromo(missing_genes_one, crossover_chromo_one_copy, 1);


		//crossover replace dups with missing genes one
		SalesmanFunct.replaceDups(crossover_chromo_one_copy, missing_genes_one);
		

		//Start of crossover chromo two
		int cco_size2 = crossover_chromo_two_copy.size();
		SalesmanFunct.cutChromo(crossover_chromo_two_copy,crossover_point,2,cco_size2);
		SalesmanFunct.combChromo(crossover_chromo_two_copy, crossover_chromo_one, cco_size2, crossover_point);


		//crossover finding missing genes of two
		ArrayList<Integer> missing_genes_two = new ArrayList<Integer>();
		SalesmanFunct.missingChromo(missing_genes_two, crossover_chromo_two_copy, 2);
		
		//crossover replace dups with missing genes two
		SalesmanFunct.replaceDups(crossover_chromo_two_copy, missing_genes_two);
		
		//The chromos that go to the next generation
		ArrayList<ArrayList<Integer>> next_gen_chromos = new ArrayList<ArrayList<Integer>>();
		for(SortedChromo test: best_chromo)
		{
			next_gen_chromos.add(population.get(test.getChromoNum()));
		}
		next_gen_chromos.add(crossover_chromo_one_copy);
		next_gen_chromos.add(crossover_chromo_two_copy);
		
		//start of second generation loop
		for(int i = 0; i < num_gen; i++)
		{
			
			population.clear();
			
			
			for(ArrayList<Integer> next_gen: next_gen_chromos)
			{
				population.add(next_gen);
			}
			//randomize chromo and add them to the population
			SalesmanFunct.addToPop(num_pop, chromo, population);
			
			//sort chromo
			sorted_chromo.clear();
			SalesmanFunct.sortPop(sorted_chromo, population, cities);
			
			//Take best 10 from sorted_chromo
			best_chromo.clear();
		
			for (int j = 1; j <= 10; j++)
			{
				best_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
			}
				
			if(i == num_gen - 2)
			{
				//display best route
				System.out.println("\n");
				System.out.println("\n");
				System.out.println("\n");
				SortedChromo final_chromo = best_chromo.get(0);
				ArrayList<Integer> final_route = population.get(final_chromo.getChromoNum());
				for(int route: final_route)
				{
					//System.out.println(route);
					System.out.println(city_names.get(route) + " " + route);
				}
				System.out.println("with a distance of " + final_chromo.getDistance());
				break;
			}
			
			//Shuffle remaining sorted_chromo and take 10
			Collections.shuffle(sorted_chromo);
		
			ran_ten_chromo.clear();
		
			for (int j = 1; j <= 10; j++)
			{
				ran_ten_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
			}
			
			//Sort those 10
			Collections.sort(ran_ten_chromo, new Comparator<SortedChromo>()
			{
				// descending sort
				public int compare(SortedChromo sc1, SortedChromo sc2)
				{
					return Integer.valueOf(sc2.getDistance()).compareTo(sc1.getDistance());
				}
			});
		
			
			//Select best four from ran_ten_chromo
			final_four_chromo.clear();
			for (int j = 1; j <= 4; j++)
			{
				final_four_chromo.add(ran_ten_chromo.remove(ran_ten_chromo.size()-1));
			}
		
			
			//Shuffle final four and take two
			Collections.shuffle(final_four_chromo);
			final_two_chromo.clear();
			for (int j = 1; j <= 2; j++)
			{
				final_two_chromo.add(final_four_chromo.remove(final_four_chromo.size()-1));
			}
		
			
			//Crossover
			crossover_point = r.nextInt((((chromo.size()+ 2) - 3)- 2) + 1) + 2;
			
			crossover_chromo_one = population.get(final_two_chromo.get(0).getChromoNum());
			crossover_chromo_two = population.get(final_two_chromo.get(1).getChromoNum());
			ArrayList<Integer> corossover_chromo_one_copy_loop = new ArrayList<>(crossover_chromo_one);
			ArrayList<Integer> corossover_chromo_two_copy_loop = new ArrayList<>(crossover_chromo_two);
			
			//Crossover one
			//cut chromo one at crossover point
			cco_size = crossover_chromo_one.size();
			SalesmanFunct.cutChromo(corossover_chromo_one_copy_loop, crossover_point, 1, cco_size); //crossover_chromo_one_copy
			
			//combine first crossover chromo
			SalesmanFunct.combChromo(corossover_chromo_one_copy_loop, crossover_chromo_two, cco_size, crossover_point);

			//Find missing genes of first crossover chromo
			missing_genes_one.clear();
			SalesmanFunct.missingChromo(missing_genes_one, corossover_chromo_one_copy_loop, 1);

			//replace dups with missing
			SalesmanFunct.replaceDups(corossover_chromo_one_copy_loop, missing_genes_one);
			
			//Crossover one two
			SalesmanFunct.cutChromo(corossover_chromo_two_copy_loop, crossover_point, 2, cco_size);

			SalesmanFunct.combChromo(corossover_chromo_two_copy_loop, crossover_chromo_two, cco_size, crossover_point);

			missing_genes_two.clear();
			SalesmanFunct.missingChromo(missing_genes_two, corossover_chromo_two_copy_loop, 2);

			SalesmanFunct.replaceDups(corossover_chromo_two_copy_loop, missing_genes_two);

			//The chromos that go to the next generation	
			next_gen_chromos.clear();
			for(SortedChromo test : best_chromo)
			{
				ArrayList<Integer> next_gen = population.get(test.getChromoNum());
				next_gen_chromos.add(next_gen);	
			}
			
			next_gen_chromos.add(corossover_chromo_one_copy_loop);
			next_gen_chromos.add(corossover_chromo_two_copy_loop);
	
		}
	}
}
