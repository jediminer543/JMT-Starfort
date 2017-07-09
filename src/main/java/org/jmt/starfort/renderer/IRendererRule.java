package org.jmt.starfort.renderer;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;

public interface IRendererRule {
	
	/**
	 * Get components renderable by this rule
	 * 
	 * @return components renderable by this rule
	 */
	public Class<? extends IComponent>[] getRenderableComponents();
	
	/**
	 * Time to load images etc.
	 * @param nvgCtx
	 */
	public void init(Renderer r);
	
	/**
	 * Check if this rule is disabled; is run before world itteration
	 * so should be a purely world/renderer wise setting (if disabling on a
	 * component wise basis, just add an `if` statement to the draw function). 
	 * Allows UI based toggling of views.<br>
	 * <br>
	 * Named disabled instead of enabled, as IDE auto-complete usually defaults to false,
	 * and most rules will ignore this option (for now?).
	 * 
	 * @param r Renderer, which should be used to access Renderer settings
	 * 
	 * @return
	 */
	public boolean disabled(Renderer r);
	
	/**
	 * Time to draw; assume this is called between nvgBeginFrame operations;
	 * @param nvgCtx
	 * @param comp
	 * @param compLoc
	 */
	public void draw(Renderer r, Coord offset, IComponent comp, Coord compLoc);
	
	/**
	 * Get's the draw priority
	 * 
	 * @return The render rule's draw priority
	 */
	public int getPriority();

}
