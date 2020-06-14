The "traveling salesman" must start in a specified city, travel to each of several other cities exactly once, and return to the starting city following the path of least cost.
Using a genetic algorithm to create many generations one can determin the best route.

Given a list of cities and distance between them this program can determine the best route.
Ran in the cmd like this: java salesman.SalesmanDriver cities.txt distance.txt (num_gen here) (num_pop here)
where num_gen means how many generations and num_pop means how many populations in each generation. A population is a single route.

1000 is recommed for both numbers. 7810 is the best distance and it should get it in one or two tries
