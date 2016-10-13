package org.jmt.starfort.game.renderer;

import org.jmt.starfort.world.component.IComponent;

public class LogicBasedRenderer {

	static interface LBRLogic {
		
		public int logic(IComponent comp);
		
	}
	
}
