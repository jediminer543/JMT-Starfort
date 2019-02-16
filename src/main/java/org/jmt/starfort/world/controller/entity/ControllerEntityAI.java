package org.jmt.starfort.world.controller.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.CoordRange;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentTasked;
import org.jmt.starfort.world.component.designator.IComponentDesignator;
import org.jmt.starfort.world.controller.IController;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.ai.ITaskGenerator;

//TODO
public class ControllerEntityAI implements IController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3480513721105512020L;

	/**
	 * The latest full taskgenerator list, sorted by priority
	 */
	transient volatile List<ITaskGenerator> tasks = new CopyOnWriteArrayList<>();
	
	/**
	 * Lookup for the location of the taskgenerators, for finding closest
	 */
	transient volatile Map<ITaskGenerator, Coord> taskLocLookup = new ConcurrentHashMap<>();
	
	/**
	 * Lookup for the location of the taskgenerators, for finding closest
	 */
	transient volatile Map<ITaskGenerator, Coord> taskLocLookupPregen = new ConcurrentHashMap<>();
	
	/**
	 * The in-progress version of the taskgenerator list
	 */
	transient List<ITaskGenerator> pregen = new ArrayList<>();
	
	/**
	 * The list of task generators registered
	 */
	transient List<ITaskGenerator> registered = new ArrayList<>();
	
	
	public ITask getTask(IEntity ie, Coord eloc) {
		ITask out = null;
		int maxPri = Integer.MIN_VALUE;
		int distance = Integer.MAX_VALUE;
		if (tasks != null) {
			for (ITaskGenerator tg : tasks) {
				if (tg.avaliableTaskGeneratorTasks() > 0 && tg.isTaskGeneratorCompletable(ie)) {
					if (tg.getTaskGeneratorHighestPriority() > maxPri) {
						maxPri = tg.getTaskGeneratorHighestPriority();
						out = tg.getTaskGeneratorTask(ie);
						distance = taskLocLookup.get(tg).sub(eloc).abs().manhattan();
					} else if (tg.getTaskGeneratorHighestPriority() == maxPri) {
						int tdist = taskLocLookup.get(tg).sub(eloc).abs().manhattan();
						if (tdist < distance) {
							out = tg.getTaskGeneratorTask(ie);
							distance = tdist;
						}
					}
				}
			}
		}
		return out;
	}
	
	private int state = 0;
	private int countdown = 0;
	private int countdownMax = 10;
	
	//private CoordRange witter = null;

	
	
	private void tick(Object... args) {
		World w = (World) args[0];
		if (state == 0) {
			for (Coord i : new CoordRange(w.getBounds(false))) {
				Block b = w.getBlockNoAdd(i);
				if (b == null) continue;
				for (IComponentDesignator icd : b.getCompInstances(IComponentDesignator.class)) {
					if (icd.isDesignatorComplete()) {
						b.removeComponent(icd);
					}
				}
			}
			countdown = countdownMax;
			state++;
		} else if (state == 1) {
			pregen.clear();
			taskLocLookupPregen.clear();
			for (Coord i : new CoordRange(w.getBounds(false))) {
				Block b = w.getBlockNoAdd(i);
				if (b != null) {
					List<IComponentTasked> things = b.getCompInstances(IComponentTasked.class);
					pregen.addAll(things);
					for (IComponentTasked thing : things) {
						taskLocLookupPregen.put(thing, i);
					}
				}
			}
			List<ITaskGenerator> newFinal = new ArrayList<>();
			newFinal.addAll(pregen);
			newFinal.addAll(registered);
			newFinal.sort(Comparator.comparingInt(ITaskGenerator::getTaskGeneratorHighestPriority).reversed());
			tasks.clear();
			tasks.addAll(newFinal);
			taskLocLookup = taskLocLookupPregen;
			taskLocLookupPregen = new ConcurrentHashMap<>();
			countdown = countdownMax;
			state++;
		} else {
			if (countdown <= 0) {
				state = 0;
			} else {
				countdown--;
			}
		}
	}
	
	transient ComplexRunnable tick = this::tick;
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
	}

}
