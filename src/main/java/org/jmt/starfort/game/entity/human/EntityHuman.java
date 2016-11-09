package org.jmt.starfort.game.entity.human;

import org.jmt.starfort.game.entity.EntityHumanoid;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityHuman extends EntityHumanoid {

	String name;
	
	public EntityHuman(String name) {
		
	}
	
	@Override
	public String getEntityName() {
		return name;
	}

	@Override
	public String getComponentName() {
		return "Human";
	}

	@Override
	public ComplexRunnable getTick() {
		return null;
	}

	@Override
	public IOrgan getEntityOrganBody() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	@Override
	public ITask[] getEntityTaskList() {
		// TODO Auto-generated method stub
		return null;
	}

}
