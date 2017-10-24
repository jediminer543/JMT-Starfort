package org.jmt.starfort.world.component.conduits;

import java.util.List;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;

/**
 * Merely marks that this is a conduit piece, so that
 * the world Conduit Controller can generate networks.
 * 
 * @author jediminer543
 *
 */
public interface IComponentConduit extends IComponent {
	
	/**
	 * If implemented, the conduit will be updated with directions in which it is connected.
	 * 
	 * Usefull for rendering, but you should also implement IComponentDirectioned
	 * 
	 * @return List of directions the conduit is connected in
	 * @see IComponentDirectioned
	 */
	public List<Direction> getConduitConnectedDirections();
}
