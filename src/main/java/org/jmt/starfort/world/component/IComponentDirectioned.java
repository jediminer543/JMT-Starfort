package org.jmt.starfort.world.component;

import org.jmt.starfort.util.Direction;

public interface IComponentDirectioned extends IComponent {

	public Direction[] getComponentDirections();
}
