Background Processing
======
The game is designed to process many worlds at once. Unfortunatly, CPUs will stuggle when running many worlds.

#The Problem
The game is likley going to have to perform updates against several worlds
symaltaniously. Worlds, use large amounts of processing power to run, so
running several will cause nom all of the CPU. Portions of the world will also
probably have to be dumped to disk, while it is unloaded (probably unloading 
to a memory file store in an interrim period).

#The Solution
##Processing
The easiest way to sum up how to perform background processing would be to use
an idea from tabletop RPGs; buying hits. When all the worlds are ticked, unloaded
worlds have a counter incremented. Then, by pulling out averages, the key ones being:
* Average Path Multiplier (the average difference between a paths direct length, and actual length)
* Average JOB_TYPE time (the average time between jobs)
* Average RESOURCE_TYPE consumption (the average rate at which resources, be it food or energy are consumed)
* Average RESOURCE_TYPE production (the average rate at which resources, be it food or energy are produced)
* Etc.

Once the world is unloaded, these values are used to calculate resource deltas, as well as to perfrom idle jobs.

##Memory

TODO
