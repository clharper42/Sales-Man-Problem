package salesman;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class SalesmanFunct
{
	static Random ran = new Random();
    public static void addToPop(int num_pop, ArrayList<Integer> chromo, ArrayList<ArrayList<Integer>> population)
	{
		for(int i = 0; i < num_pop-12; i++)//12 chromos already in population from last generation
		{
			ArrayList<Integer> copy = new ArrayList<Integer>(chromo);
			Collections.shuffle(copy, new Random(System.nanoTime()));
			copy.add(0,0);
			copy.add(0);
			population.add(copy);
		}
    }
    
    public static void sortPop(ArrayList<SortedChromo> sorted_chromo, ArrayList<ArrayList<Integer>> population)
	{
			int x = 0;
			int total = 0;
			
			for(ArrayList<Integer> temp_chromo: population)
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
    
    public static void cutChromo(ArrayList<Integer> crossover_chromo_one_copy, int crossover_point, int cco_size)
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
	
	public static void missingChromo(ArrayList<Integer> missing_genes, ArrayList<Integer> comb_crossover_chromo)
	{
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
		Collections.shuffle(missing_genes);
		for(int i = 1; i <= comb_crossover_chromo.size()-2; i++)
		{
			if(comb_crossover_chromo.indexOf(i) != comb_crossover_chromo.lastIndexOf(i))
			{
				if(ran.nextInt(2) == 0)
				{
					comb_crossover_chromo.set(comb_crossover_chromo.indexOf(i),missing_genes.remove(0));
				}
				else
				{
					comb_crossover_chromo.set(comb_crossover_chromo.lastIndexOf(i),missing_genes.remove(0));
				}
			}
		}

	}
}
