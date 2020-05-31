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
	
	public static void addToPop(int num_pop, ArrayList<Integer> chromo, ArrayList<ArrayList<Integer>> population)
	{
		for(int i = 0; i < num_pop-2; i++)//12
		{
			ArrayList<Integer> copy = new ArrayList<Integer>(chromo);
			Collections.shuffle(copy);
			copy.add(0,0);
			copy.add(0);
			population.add(copy);
		}
	}
	
	public static void sortPop(ArrayList<SortedChromo> sorted_chromo, ArrayList<ArrayList<Integer>> population, ArrayList<City> cities)
	{
			int x = 0;
			int total = 0;
			for(ArrayList<Integer> temp_chromo: population)
			{
				total = 0;
				for (int j = 0; j < temp_chromo.size()-1; j++)
				{
					City temp_city = cities.get(temp_chromo.get(j));
					total = total + temp_city.getDistanceTo(temp_chromo.get(j+1));
				}
				SortedChromo new_sort = new SortedChromo(x, total);
				sorted_chromo.add(new_sort);
				x++;
			}

		
		//sorts the chromos in the population by 
		Collections.sort(sorted_chromo, new Comparator<SortedChromo>()
		{
			// descending sort
			public int compare(SortedChromo sc1, SortedChromo sc2)
			{
				return Integer.valueOf(sc2.getDistance()).compareTo(sc1.getDistance());
			}
		});
	}
	
	public static void cutChromo(ArrayList<Integer> crossover_chromo_one_copy, int crossover_point, int chromo, int cco_size)
	{
		//add +1 to crossover_point to make count start at zero over 1
		for(int i = crossover_point; i < cco_size; i++)
		{
			crossover_chromo_one_copy.remove(crossover_point);
		}
		
	}
	
	public static void combChromo(ArrayList<Integer> crossover_chromo_copy, ArrayList<Integer> crossover_chromo_from, int cco_size, int crossover_point)
	{
		
		for(int i = crossover_point; i < cco_size; i++)
		{
			crossover_chromo_copy.add(crossover_chromo_from.get(i));
		}

	}
	
	public static void missingChromo(ArrayList<Integer> missing_genes, ArrayList<Integer> comb_crossover_chromo, int chromo)
	{
		//could have done comb_crossover_chromo.indexOf() == -1
		/*for(int i = 1; i <= comb_crossover_chromo.size()-2; i++)
		{
			boolean appears = false;
			for(int test: comb_crossover_chromo)
			{
				if( i == test)
				{
					appears = true;
				}
				
			}
			if(appears == false)
			{
				missing_genes.add(i);
			}
		}
		
		return missing_genes;*/

		for(int i = 1; i <= comb_crossover_chromo.size()-2; i++)
		{
			if(comb_crossover_chromo.indexOf(i) == - 1)
			{
				missing_genes.add(i);
			}
		}
	}
	
	public static void replaceDups(ArrayList<Integer> comb_crossover_chromo, ArrayList<Integer> missing_genes)
	{
		//could have done comb_crossover_chromo.indexOf() != comb_crossover_chromo.lastIndexOf() to replace
		/*for(int i = 1; i <= comb_crossover_chromo.size()-2; i++)
		{
			int num_appears = 0;
			for(int test: comb_crossover_chromo)
			{
				//System.out.println("int test " + in_test);
				if(i == test)
				{
				//	System.out.println(i + " equals " + test);
					num_appears++;
				}
				if(num_appears == 2)
				{
					comb_crossover_chromo.set(comb_crossover_chromo.indexOf(test),missing_genes.remove(0));
					num_appears = 0;
				}
				
			}
		}
		
		return comb_crossover_chromo;*/

		for(int i = 1; i <= comb_crossover_chromo.size()-2; i++)
		{
			if(comb_crossover_chromo.indexOf(i) != comb_crossover_chromo.lastIndexOf(i))
			{
				comb_crossover_chromo.set(comb_crossover_chromo.indexOf(i),missing_genes.remove(0));
			}
		}

	}
	
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
		
		//create list of cities and their genes(index)
		BufferedReader cities_lines = new BufferedReader(new FileReader(path_cities));
		int temp_gene = 0;
		while((line = cities_lines.readLine()) != null)
		{
			City temp_city = new City(line, temp_gene);
			cities.add(temp_city);
			temp_gene++;
		}
		cities_lines.close();
		
		//allow each city to know what cities they connect to and distance and gene of each of those cities
		Scanner sc = new Scanner(new File(path_distance));
		while(sc.hasNext())
		{
			String comp_city = sc.next();
			
			for(City city_en : cities)
			{
				if(city_en.getName().equals(comp_city))
				{
					String city_to = sc.next();
					int distance_to = sc.nextInt();
					int gene_to = 0;
					//gets gene for connecting city so each city knows what it connects to, distance to said city, and what gene that city is
					for(City city_dis : cities)
					{
						if(city_dis.getName().equals(city_to))
						{
							gene_to = city_dis.getGene();
						}
					}
					System.out.println(city_en.getName() + " Matches " + comp_city + " so add " + city_to + " which has a gene of " + gene_to + " and distance between of " + distance_to);
					city_en.addCityDistanceTo(city_to, distance_to, gene_to);
				}
			}
			
		}
		//store desired starting and ending city
		//City dest_city = cities.get(0);
		
		
		
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
		
		sortPop(sorted_chromo, population, cities);
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
		cutChromo(crossover_chromo_one_copy,crossover_point,1,cco_size);

		//combine chromo one with chromo two at point
		combChromo(crossover_chromo_one_copy, crossover_chromo_two, cco_size, crossover_point);

		//crossover finding missing genes of one
		ArrayList<Integer> missing_genes_one = new ArrayList<Integer>();
		missingChromo(missing_genes_one, crossover_chromo_one_copy, 1);


		//crossover replace dups with missing genes one
		replaceDups(crossover_chromo_one_copy, missing_genes_one);
		

		//Start of crossover chromo two
		int cco_size2 = crossover_chromo_two_copy.size();
		cutChromo(crossover_chromo_two_copy,crossover_point,2,cco_size2);
		combChromo(crossover_chromo_two_copy, crossover_chromo_one, cco_size2, crossover_point);


		//crossover finding missing genes of two
		ArrayList<Integer> missing_genes_two = new ArrayList<Integer>();
		missingChromo(missing_genes_two, crossover_chromo_two_copy, 2);
		
		//crossover replace dups with missing genes two
		replaceDups(crossover_chromo_two_copy, missing_genes_two);
		
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
			addToPop(num_pop, chromo, population);
			
			//sort chromo
			sorted_chromo.clear();
			sortPop(sorted_chromo, population, cities);
			
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
					System.out.println(route);
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
			cutChromo(corossover_chromo_one_copy_loop, crossover_point, 1, cco_size); //crossover_chromo_one_copy
			
			//combine first crossover chromo
			combChromo(corossover_chromo_one_copy_loop, crossover_chromo_two, cco_size, crossover_point);

			//Find missing genes of first crossover chromo
			missing_genes_one.clear();
			missingChromo(missing_genes_one, corossover_chromo_one_copy_loop, 1);

			//replace dups with missing
			replaceDups(corossover_chromo_one_copy_loop, missing_genes_one);
			
			//Crossover one two
			cutChromo(corossover_chromo_two_copy_loop, crossover_point, 2, cco_size);

			combChromo(corossover_chromo_two_copy_loop, crossover_chromo_two, cco_size, crossover_point);

			missing_genes_two.clear();
			missingChromo(missing_genes_two, corossover_chromo_two_copy_loop, 2);

			replaceDups(corossover_chromo_two_copy_loop, missing_genes_two);

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
