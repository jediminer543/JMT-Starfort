package org.jmt.starfort.renderer.rules.item;

import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;

public class ItemRenderer implements IRendererRule {

	@Override
	public Class<? extends IComponent>[] getRenderableComponents() {
		return null;
	}

	@Override
	public void init(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean disabled(Renderer r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Renderer r, Coord offset, IComponent comp, Coord compLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
