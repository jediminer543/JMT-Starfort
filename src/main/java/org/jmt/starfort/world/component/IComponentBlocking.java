package org.jmt.starfort.world.component;

import java.util.Map;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;

/**
 * Defines a component that blocks things
 * 
 * @author Jediminer543
 *
 */
public interface IComponentBlocking extends IComponent {

	public Map<NavContext, Direction[]> getBlockedDirs();
	
	
	
}
