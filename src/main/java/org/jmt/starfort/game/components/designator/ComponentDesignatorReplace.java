package org.jmt.starfort.game.components.designator;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.world.component.designator.IComponentDesignatorWorked;
import org.jmt.starfort.world.material.IMaterial;

/**
 * 
 * @author jediminer543
 *
 */
public class ComponentDesignatorReplace implements IComponentDesignatorWorked{

	@Override
	public void designatorTask(Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDesignatorComplete() {
		return false;
	}

	@Override
	public String getComponentName() {
		return "DesignatorReplace";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	public void tick(Object... args) {
		
	}
	
	@Override
	public ComplexRunnable getTick() {
		return this::tick;
	}

	@Override
	public int getDesignatorWorkNeeded() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDesignatorWorkDone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void designatorDoWork(float amount) {
		// TODO Auto-generated method stub
		
	}

}
