package org.jmt.starfort.world.component;

import java.util.Map;

import org.jmt.starfort.world.material.IMaterial;

public interface IComponentMaterials extends IComponent {

	public Map<IMaterial, Integer> getMaterials();
}
