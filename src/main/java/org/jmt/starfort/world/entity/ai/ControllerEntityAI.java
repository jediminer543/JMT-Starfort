package org.jmt.starfort.world.entity.ai;

import java.util.List;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.world.controller.IController;
import org.jmt.starfort.world.entity.IEntity;

//TODO
public class ControllerEntityAI implements IController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3480513721105512020L;

	/**
	 * The latest full taskgenerator list, sorted by priority
	 */
	List<ITaskGenerator> tasks;
	
	/**
	 * The in-progress version of the taskgenerator list
	 */
	List<ITaskGenerator> pregen;
	
	public ITask getTask(IEntity ie) {
		if (tasks != null) {
			for (ITaskGenerator tg : tasks) {
				if (tg.isTaskGeneratorCompletable(ie)) {
					return tg.getTaskGeneratorTask(ie);
				}
			}
		}
		return null;
	}
	
	@Override
	public ComplexRunnable getTick() {
		return null;
	}

}
