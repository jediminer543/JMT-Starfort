Ai Task Logic
=========

This well highlights the purpose of these documents, to assist in designing be allowing the fleshing out of ideas,
without the need to find a human or a large volume of paper.

My current problem (sans GUI and the terribly structured code) is allocating tasks to people.

## TLDR
* Tasks are pulled not pushed
* 4 parts of system: Generator, Controller, EntityAi, and Task
* Entity should implement methods for "verbs" (interactions)

## Task structure

All tasks are to opperate on a pulled basis; that is, instead of a character being given a task, it will request one. Handling needs to be implemented for both times when there are no tasks (likley by allocating random meaningless tasks, like going for a walk, or whatever else people in the game universe decide to do) and when a task must change part way through (like whey you are in the middle of programming, realise you are starving to death, and go and get some food)

To that end, it is likley that there will be four main components:

### Task Generator

This checks wether an entity can complete a task and generates a task valid for that entity; I.e. a drone can just fly up to the top of a multi-layer room, whereas a human needs a grappling hook, a jetpack, or more convetionally, a ladder (but thats boring). Task constructors should be on a per task-set basis, and, if they are attached to a component, be the component itself, so as to minimise class creation. I.e. If a designator needs materials, then work, it should offer material collection tasks, then a construction task.

### Task Controller

Pools all task controllers, and is used by entity ai to pull tasks; I.e. Entity goes "get me task" -> task controller responds "here is task". It needs to be able to account for both External and Internal tasks, I.e. should tell entity to eat if hungry etc. (I need one of these BTW). Is going to be a world controller, and likley be a repurposing of the current task controller.

### Task (itself)

Generated by the task generator, and executed by the entity AI. Likley that this will call methods in Entity AI, such as go to mneh, pick up mneh, use item on mneh, etc.

### Task AI for entity

Likley going to be an internal representation of the entities brain; thus specific to an entity, hence also transferable between ~~shells~~ bodies. As previously mentioned, should include methods for doing basic tasks; Movement, component interaction, item interaction, entity interaction, fluid interaction, etc. Calls execute on task. 


##Other notes

All of this will be being threaded by the processor, hence all of these components should be thread tolerant (if not safe). 

##Conclusions:

This helps with design, and should be done more often.

FIN,
jediminer543



