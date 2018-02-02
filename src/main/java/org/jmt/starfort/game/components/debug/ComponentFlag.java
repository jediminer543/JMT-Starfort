package org.jmt.starfort.game.components.debug;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTasked;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.ai.Task;
import org.jmt.starfort.world.entity.aiold.IComponentTaskedOld;
import org.jmt.starfort.world.material.IMaterial;

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
public class ComponentFlag implements IComponent, IComponentTasked {

	public ComponentFlag() {
		
	}

	@Override
	public String getComponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMaterial getComponentMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTaskGeneratorCompletable(IEntityAI entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int avaliableTaskGeneratorTasks() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Task getTaskGeneratorTask(IEntityAI entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
