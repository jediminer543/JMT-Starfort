package org.jmt.starfort.world.controller.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.CoordRange;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentTasked;
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
	 * The in-progress version of the taskgenerator list
	 */
	transient List<ITaskGenerator> pregen = new ArrayList<>();
	
	/**
	 * The list of task generators registered
	 */
	transient List<ITaskGenerator> registered = new ArrayList<>();
	
	
	public ITask getTask(IEntity ie) {
		if (tasks != null) {
			for (ITaskGenerator tg : tasks) {
				if (tg.avaliableTaskGeneratorTasks() > 0 && tg.isTaskGeneratorCompletable(ie)) {
					return tg.getTaskGeneratorTask(ie);
				}
			}
		}
		return null;
	}
	
	private int countdown = 0;
	private int countdownMax = 2;
	
	//private CoordRange witter = null;

	private void tick(Object... args) {
		World w = (World) args[0];
		if (countdown <= 0) {
			pregen.clear();
			for (Coord i : new CoordRange(w.getBounds(false))) {
				Block b = w.getBlockNoAdd(i);
				if (b != null) {
					pregen.addAll(b.getCompInstances(IComponentTasked.class));
				}
			}
			List<ITaskGenerator> newFinal = new ArrayList<>();
			newFinal.addAll(pregen);
			newFinal.addAll(registered);
			newFinal.sort(Comparator.comparingInt(ITaskGenerator::getTaskGeneratorHighestPriority).reversed());
			tasks.clear();
			tasks.addAll(newFinal);
			countdown = countdownMax;
		} else {
			countdown--;
		}
	}
	
	transient ComplexRunnable tick = this::tick;
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
	}

}
