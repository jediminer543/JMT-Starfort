package org.jmt.starfort.game.registra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.StyledEditorKit.StyledTextAction;

import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.fluid.ComponentPipe;
import org.jmt.starfort.game.entity.EntityDrone;
import org.jmt.starfort.game.renderer.DirectionBasedRenderer;
import org.jmt.starfort.game.renderer.GenericRenderer;
import org.jmt.starfort.game.renderer.WallRenderer;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;

public class RenderRegistra {

	public static void register(ArrayList<IRendererRule> ruleSet) {
		registerBlocks(ruleSet);
	}
	
	public static void registerBlocks(ArrayList<IRendererRule> ruleSet) {
		//Register Wall Renderer
		ruleSet.add(new WallRenderer());
		
		//Register Pipe Renderer
		Map<Direction[], int[]> mapping = new HashMap<>();
		mapping.put(new Direction[] {Direction.XINC}, new int[] {1, 3});
		mapping.put(new Direction[] {Direction.XDEC}, new int[] {3, 3});
		mapping.put(new Direction[] {Direction.ZINC}, new int[] {3, 2});
		mapping.put(new Direction[] {Direction.ZDEC}, new int[] {2, 3});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC}, new int[] {1, 0});
		mapping.put(new Direction[] {Direction.ZINC, Direction.ZDEC}, new int[] {0, 0});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC}, new int[] {2, 1});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZDEC}, new int[] {3, 0});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC, Direction.XDEC}, new int[] {1, 2});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC, Direction.XDEC}, new int[] {1, 1});
		mapping.put(new Direction[] {Direction.ZINC, Direction.XINC, Direction.ZDEC}, new int[] {0, 1});
		mapping.put(new Direction[] {Direction.ZDEC, Direction.XDEC, Direction.ZINC}, new int[] {0, 2});
		
		mapping.put(new Direction[] {Direction.ZDEC, Direction.XDEC, Direction.ZINC , Direction.XINC}, new int[] {2, 2});
		
		ruleSet.add(new DirectionBasedRenderer(InlineFunctions.inlineArray(ComponentPipe.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/fluid/pipe/Pipe.png"), 
				4, 4, 
				mapping,
				10, 
				false));
		
		mapping = new HashMap<>();
		mapping.put(new Direction[] {Direction.YINC}, new int[] {1, 0});
		mapping.put(new Direction[] {Direction.YDEC}, new int[] {0, 1});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC}, new int[] {0, 0});
		
		ruleSet.add(new DirectionBasedRenderer(InlineFunctions.inlineArray(ComponentStairs.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/stair/Stairs.png"), 
				2, 2, 
				mapping, 
				100,
				false));
		
		ruleSet.add(new GenericRenderer(InlineFunctions.inlineArray(EntityDrone.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/entity/Drone.png"),
				150));
		
	}
	
}
