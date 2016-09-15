package org.jmt.starfort.game.components.fluid;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentPipe implements IComponent {

	@Override
	public String getComponentName() {
		return "Pipe";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

}
