Pathfinding
=====

#The Problem
Pathfinding is dificult enough whn you are trying to operate in 2D; 3D is
normaly exponentialy more difficult, and thats before accounting for the 
fact that most being cannot fly, so need to route through stairs. Bruteforce
solutions normally eat memory over distances, but would also nom processing,
as each movement operation must be validated (because directional walls). 

#The Solution
The solution to this problem is to biaficate it, one part being a bruteforced 
graph traversal, and the other being the actual pathfinding algorithm.

##The Graph
The idea here is to generate a graph of possible ways to a navigational paths. 
Once navigation is requested, the location of the target and source are used,
and then a directed bruteforce (read: no backtracking) method is used to find 
a route between nodes; some may be defined as precalculated (such as ramps or
lifts), and others will define the need for the use of the World Pathfinder.
This also allows for sites for interworld travel to be added, and then to 
traverse worlds.

