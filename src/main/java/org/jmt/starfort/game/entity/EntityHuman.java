package org.jmt.starfort.game.entity;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityHuman extends EntityHumanoid {

	String name;
	
	public EntityHuman(String name) {
		
	}
	
	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBody() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStrength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAgility() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReaction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWillpower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCharisma() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLogic() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIntuition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getComponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComplexRunnable getTick() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrgan getOrganBody() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

}
