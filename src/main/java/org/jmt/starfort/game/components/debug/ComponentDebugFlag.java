package org.jmt.starfort.game.components.debug;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTasked;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.aiold.IComponentTaskedOld;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

/**
 * A flag for debugging
 * 
 * Used for testing path-finding and AI task collection
 * 
 * As is most of the stuff around here, this is TODO
 * 
 * @author jediminer543
 *
 */
public class ComponentDebugFlag implements IComponent, IComponentTasked {

	public ComponentDebugFlag() {
		
	}

	@Override
	public String getComponentName() {
		return "Debug Flag";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return MaterialRegistry.getMaterial("Debug");
	}

	@Override
	public boolean isTaskGeneratorCompletable(IEntityAI entity) {
		return true;
	}

	@Override
	public int avaliableTaskGeneratorTasks() {
		return 0;
	}

	@Override
	public ITask getTaskGeneratorTask(IEntityAI entity) {
		return null;
	}

}
