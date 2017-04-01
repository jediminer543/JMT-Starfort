Tick Design and you
====

Ticks are part of a world collection that are run every, you guessed it,
tick. Their goal is to update things that need updating.

##Usage cases of ticks

Ticks should be used whenever you have an object that needs regular per update
processing. Ticks are executed across threads, hence should be vaguely thread
safe (the tick processor will end a tick that throws a Concurrent Mod. Exception). 
However, in general terms, the renderer seems to be the one to take the brunt
of the access problems, which is also easily solved, since it is unlikley that a
human will notice a partially missing tile for a frame.

###When should you use ticks
* To create per update processing jobs
* To queue up none per update jobs

###When shouldn't you use ticks
* To do things at occasional, random intervals
* To do non-regular processing 