package salesman;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class SalesmanDriver
{
	public static void main(String[] args) throws IOException
	{
		/*
			Information - 
				Chromo is a ind. path
				Gene is the index of the city in the orginal array/file
				Population is a group of chromos
				Generation is how many times the process repeats
				A distance of 7810 is the best route
		*/
		String path_cities = args[0];
		String path_distance = args[1];
		int num_gen = Integer.parseInt(args[2]);
		int num_pop = Integer.parseInt(args[3]);
		if(num_pop < 20 || num_pop%2 != 0)
		{
			System.out.println("20 is the minimum number in the population and must be even");
			System.exit(0);
		}
		else if(num_gen < 2)
		{
			System.out.println("2 is the minimum number of generations");
			System.exit(0);
		}

		String line;
		ArrayList<String> city_names = new ArrayList<String>();
		//create list of cities and their genes(index)
		BufferedReader cities_lines = new BufferedReader(new FileReader(path_cities));
		while((line = cities_lines.readLine()) != null)
		{
			city_names.add(line);
		}
		cities_lines.close();
		
		//Set up the distance matrix in the city class
		City.numofcities = city_names.size();
		City.citygenes = city_names;
		City.distances = new int[city_names.size()][city_names.size()];

		//Fill in the city distance matrix
		Scanner sc = new Scanner(new File(path_distance));
		City cityfrom;
		City cityto;
		while(sc.hasNext())
		{
			String city_start = sc.next();
			String city_end = sc.next();
			int distance = sc.nextInt();
			System.out.println(city_start + " to " + city_end + " is a distance of " + distance);

			City.distances[city_names.indexOf(city_start)][city_names.indexOf(city_end)] = distance;
		}


		//Set up chromo (which holds ints which symbolize the gene of the cities) and population 
		ArrayList<Integer> chromo = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> population = new ArrayList<ArrayList<Integer>>();
		for(int i = 1; i < city_names.size(); i++)
		{
			chromo.add(i);
		}
		//randomize chromo and add them to the population
		for(int i = 0; i < num_pop; i++)
		{
			ArrayList<Integer> copy = new ArrayList<Integer>(chromo);
			Collections.shuffle(copy, new Random(System.nanoTime()));
			copy.add(0,0);
			copy.add(0);
			population.add(copy);
		}
		
		//Fitness
		//calculate total distance for all  the chromo of the population
		ArrayList<SortedChromo> sorted_chromo = new ArrayList<SortedChromo>();
		SalesmanFunct.sortPop(sorted_chromo, population);


		//Selection
		//Take best 10 from sorted_chromo and move them to the next generation
		ArrayList<SortedChromo> best_chromo = new ArrayList<SortedChromo>();
		for (int i = 1; i <= 10; i++)
		{
			best_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
		}
		




		ArrayList<SortedChromo> ran_ten_chromo = new ArrayList<SortedChromo>();
		ArrayList<SortedChromo> final_four_chromo = new ArrayList<SortedChromo>();
		ArrayList<SortedChromo> final_two_chromo = new ArrayList<SortedChromo>();
		ArrayList<Integer> crossover_chromo_one = new ArrayList<Integer>();
		ArrayList<Integer> crossover_chromo_two = new ArrayList<Integer>();
		ArrayList<Integer> missing_genes_one = new ArrayList<Integer>();
		ArrayList<Integer> missing_genes_two = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> next_gen_chromos = new ArrayList<ArrayList<Integer>>();
		int crossover_point  = 0;
		int cco_size = 0;
		int cco_size2 = 0;
		//Move Best 10 into next gen
		for(SortedChromo test: best_chromo)
		{
			next_gen_chromos.add(population.get(test.getChromoNum()));
		}

		//fill rest of next gen
		for(int j = 0; j < num_pop-10; j = j + 2) //NUM_POP MUST BE EVEN
		{
			//Shuffle remaining sorted_chromo and take 10
			Collections.shuffle(sorted_chromo, new Random(System.nanoTime()));
			//System.out.println(sorted_chromo.size());
			ran_ten_chromo.clear();
			for (int i = 0; i < 10; i++)
			{
				//ran_ten_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
				ran_ten_chromo.add(sorted_chromo.get(i));
			}

			/*
			System.out.println("Ran ten");
			for(SortedChromo chro : ran_ten_chromo)
			{
				System.out.println(chro.getChromoNum() + " " + chro.getDistance());
			}
			*/

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
			for (int i =0; i < 4; i++)
			{
				//final_four_chromo.add(ran_ten_chromo.remove(ran_ten_chromo.size()-1));
				final_four_chromo.add(ran_ten_chromo.get(ran_ten_chromo.size()-(i+1)));
			}

			/*
			System.out.println("Best 4");
			for(SortedChromo chro : final_four_chromo)
			{
				System.out.println(chro.getChromoNum() + " " + chro.getDistance());
			}
			*/

			//Shuffle final four and take two
			Collections.shuffle(final_four_chromo, new Random(System.nanoTime()));
			final_two_chromo.clear();
			for (int i = 0; i < 2; i++)
			{
				final_two_chromo.add(final_four_chromo.get(i));
			}

			/*
			System.out.println("Final 2");
			for(SortedChromo chro : final_two_chromo)
			{
				System.out.println(chro.getChromoNum() + " " + chro.getDistance());
			}
			*/

			/*
			//Take next 2 best for crossover
			ArrayList<SortedChromo> final_two_chromo = new ArrayList<SortedChromo>();
			for (int i = 1; i <= 2; i++)
			{
				final_two_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
			}
			*/

			
			//Crossover
			/*Random r = new Random();
			int crossover_point = r.nextInt((((chromo.size()+ 2) - 3)- 2) + 1) + 2;*/
			crossover_point = (int)(Math.random() * ((chromo.size()-1) - 1 + 1) + 1);

			//crossover_chromo_one.clear();
			///crossover_chromo_two.clear();
			crossover_chromo_one = population.get(final_two_chromo.get(0).getChromoNum());
			crossover_chromo_two = population.get(final_two_chromo.get(1).getChromoNum());
			ArrayList<Integer> crossover_chromo_one_copy = new ArrayList<Integer>(crossover_chromo_one);
			ArrayList<Integer> crossover_chromo_two_copy = new ArrayList<Integer>(crossover_chromo_two);
			
			/*
			System.out.println("crossover one " + crossover_chromo_one_copy);
			System.out.println("Crossover two " + crossover_chromo_two_copy);
			*/

			//cut chromo one at point
			cco_size = crossover_chromo_one_copy.size();
			SalesmanFunct.cutChromo(crossover_chromo_one_copy,crossover_point,cco_size);

			//combine chromo one with chromo two at point
			SalesmanFunct.combChromo(crossover_chromo_one_copy, crossover_chromo_two, cco_size, crossover_point);

			//crossover finding missing genes of one
			missing_genes_one.clear();
			SalesmanFunct.missingChromo(missing_genes_one, crossover_chromo_one_copy);

			//Mutation
			//crossover replace dups with missing genes one
			SalesmanFunct.replaceDups(crossover_chromo_one_copy, missing_genes_one);
			

			//Start of crossover chromo two
			cco_size2 = crossover_chromo_two_copy.size();
			SalesmanFunct.cutChromo(crossover_chromo_two_copy,crossover_point,cco_size2);
			SalesmanFunct.combChromo(crossover_chromo_two_copy, crossover_chromo_one, cco_size2, crossover_point);


			//crossover finding missing genes of two
			missing_genes_two.clear();
			SalesmanFunct.missingChromo(missing_genes_two, crossover_chromo_two_copy);
			
			//Mutation
			//crossover replace dups with missing genes two
			SalesmanFunct.replaceDups(crossover_chromo_two_copy, missing_genes_two);
			
			//The chromos that go to the next generation		
			next_gen_chromos.add(crossover_chromo_one_copy);
			next_gen_chromos.add(crossover_chromo_two_copy);

			/*
			System.out.println("crossover one new " + crossover_chromo_one_copy);
			System.out.println("Crossover two new" + crossover_chromo_two_copy);
			System.out.println("\n");
			*/
		}














		//start of second generation loop
		for(int i = 1; i < num_gen; i++)
		{
			population.clear();
			
			
			for(ArrayList<Integer> next_gen: next_gen_chromos)
			{
				population.add(next_gen);
			}
			/*
			//randomize chromo and add them to the population
			SalesmanFunct.addToPop(num_pop, chromo, population);
			*/

			//Fitness
			//sort chromo
			sorted_chromo.clear();
			SalesmanFunct.sortPop(sorted_chromo, population);

			//Selection
			//Take best 10 from sorted_chromo
			best_chromo.clear();
		
			for (int j = 1; j <= 10; j++)
			{
				best_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
			}

			//Put best 10 into next gen
			next_gen_chromos.clear();
			for(SortedChromo test : best_chromo)
			{
				//ArrayList<Integer> next_gen = population.get(test.getChromoNum());
				next_gen_chromos.add(population.get(test.getChromoNum()));	
			}
				
			/*if(i == num_gen - 1)
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
			}*/
			for(int t = 0; t < num_pop-10; t = t + 2)
			{
				//Shuffle remaining sorted_chromo and take 10
				Collections.shuffle(sorted_chromo, new Random(System.nanoTime()));
			
				ran_ten_chromo.clear();
			
				for (int j = 0; j < 10; j++)
				{
					ran_ten_chromo.add(sorted_chromo.get(j));
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
				for (int j = 0; j < 4; j++)
				{
					final_four_chromo.add(ran_ten_chromo.get(ran_ten_chromo.size()-(j+1)));
				}
				
				//Shuffle final four and take two
				Collections.shuffle(final_four_chromo, new Random(System.nanoTime()));
				final_two_chromo.clear();
				for (int j = 0; j < 2; j++)
				{
					final_two_chromo.add(final_four_chromo.get(j));
				}

				/*
				//Take next two best for crossover
				final_two_chromo.clear();
				for (int j = 1; j <= 2; j++)
				{
					final_two_chromo.add(sorted_chromo.remove(sorted_chromo.size()-1));
				}
				*/

				//Crossover
				//crossover_point = r.nextInt((((chromo.size()+ 2) - 3)- 2) + 1) + 2;
				crossover_point = (int)(Math.random() * ((chromo.size()-1) - 1 + 1) + 1);

				//crossover_chromo_one.clear();
				//crossover_chromo_two.clear();
				crossover_chromo_one = population.get(final_two_chromo.get(0).getChromoNum());
				crossover_chromo_two = population.get(final_two_chromo.get(1).getChromoNum());
				ArrayList<Integer> corossover_chromo_one_copy_loop = new ArrayList<>(crossover_chromo_one);
				ArrayList<Integer> corossover_chromo_two_copy_loop = new ArrayList<>(crossover_chromo_two);
				
				//Crossover one
				//cut chromo one at crossover point
				cco_size = corossover_chromo_one_copy_loop.size();
				SalesmanFunct.cutChromo(corossover_chromo_one_copy_loop, crossover_point, cco_size); //crossover_chromo_one_copy
				
				//combine first crossover chromo
				SalesmanFunct.combChromo(corossover_chromo_one_copy_loop, crossover_chromo_two, cco_size, crossover_point);

				//Find missing genes of first crossover chromo
				missing_genes_one.clear();
				SalesmanFunct.missingChromo(missing_genes_one, corossover_chromo_one_copy_loop);

				//Mutaion
				//replace dups with missing
				SalesmanFunct.replaceDups(corossover_chromo_one_copy_loop, missing_genes_one);
				
				//Crossover two
				cco_size2 = corossover_chromo_two_copy_loop.size();
				SalesmanFunct.cutChromo(corossover_chromo_two_copy_loop, crossover_point, cco_size2);

				SalesmanFunct.combChromo(corossover_chromo_two_copy_loop, crossover_chromo_one, cco_size2, crossover_point);

				missing_genes_two.clear();
				SalesmanFunct.missingChromo(missing_genes_two, corossover_chromo_two_copy_loop);

				//Mutation
				SalesmanFunct.replaceDups(corossover_chromo_two_copy_loop, missing_genes_two);

				//The chromos that go to the next generation
				next_gen_chromos.add(corossover_chromo_one_copy_loop);
				next_gen_chromos.add(corossover_chromo_two_copy_loop);
			}
		}

		//printing best route
		sorted_chromo.clear();
		int x= 0;
		int total = 0;
		for(ArrayList<Integer> temp_chromo: next_gen_chromos)
			{
				total = 0;
				for (int j = 0; j < temp_chromo.size()-1; j++)
				{
					total = total + City.distances[temp_chromo.get(j)][temp_chromo.get(j+1)];
				}
				SortedChromo new_sort = new SortedChromo(x, total);
				sorted_chromo.add(new_sort);
				x++;
			}

		//sorts the best chromos from last generation 
		Collections.sort(sorted_chromo, new Comparator<SortedChromo>()
		{
			// descending sort
			public int compare(SortedChromo sc1, SortedChromo sc2)
			{
				return Integer.valueOf(sc2.getDistance()).compareTo(sc1.getDistance());
			}
		});

		//display best route
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");
		
		SortedChromo final_chromo = sorted_chromo.get(sorted_chromo.size()-1);
		ArrayList<Integer> final_route = next_gen_chromos.get(final_chromo.getChromoNum());
		for(int route: final_route)
		{
			//System.out.println(route);
			System.out.println(city_names.get(route) + " " + route);
		}
		System.out.println("with a distance of " + final_chromo.getDistance());
    }

}
