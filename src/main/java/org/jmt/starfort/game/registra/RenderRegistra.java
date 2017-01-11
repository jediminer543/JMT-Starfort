package org.jmt.starfort.game.registra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.game.components.fluid.ComponentPipe;
import org.jmt.starfort.game.entity.EntityDrone;
import org.jmt.starfort.game.renderer.HumanRenderer;
import org.jmt.starfort.renderer.DirectionBasedRenderer;
import org.jmt.starfort.renderer.GenericRenderer;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;

public class RenderRegistra {

	public static void register(ArrayList<IRendererRule> ruleSet) {
		registerBlocks(ruleSet);
	}
	
	public static void registerBlocks(ArrayList<IRendererRule> ruleSet) {
		
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
		
		//Register Stairs Renderer		
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
		
		//Register Drone Renderer
		ruleSet.add(new GenericRenderer(InlineFunctions.inlineArray(EntityDrone.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/entity/Drone.png"),
				150));
		
		//Register Wall Renderer
		//ruleSet.add(new WallRenderer());
		mapping = new HashMap<>();
		mapping.put(new Direction[] {Direction.YDEC}, new int[] {0, 3});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1, 3});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC}, new int[] {2, 3});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 3});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.ZINC, Direction.ZDEC}, new int[] {3, 2});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {2, 2});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.XDEC}, new int[] {0, 2});
		mapping.put(new Direction[] {Direction.YDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1, 2});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 0});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC, Direction.ZINC}, new int[] {2, 1});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 0});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC, Direction.ZINC}, new int[] {2, 1});
		
		mapping.put(new Direction[] {Direction.YDEC, Direction.XINC}, new int[] {1, 0});
		mapping.put(new Direction[] {Direction.YDEC, Direction.XDEC}, new int[] {0, 1});
		mapping.put(new Direction[] {Direction.YDEC, Direction.ZINC}, new int[] {1, 1});
		mapping.put(new Direction[] {Direction.YDEC, Direction.ZDEC}, new int[] {0, 0});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1 + 4, 3});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 3});
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 3});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC, Direction.ZDEC}, new int[] {3 + 4, 2});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {2 + 4, 2});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC}, new int[] {0 + 4, 2});
		mapping.put(new Direction[] {Direction.ZINC, Direction.ZDEC}, new int[] {1 + 4, 2});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC}, new int[] {2 + 4, 0});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 0});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC}, new int[] {3 + 4, 1});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 1});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC}, new int[] {2 + 4, 0});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 0});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC}, new int[] {3 + 4, 1});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 1});
		
		mapping.put(new Direction[] {Direction.XINC}, new int[] {1 + 4, 0});
		mapping.put(new Direction[] {Direction.XDEC}, new int[] {0 + 4, 1});
		mapping.put(new Direction[] {Direction.ZINC}, new int[] {1 + 4, 1});
		mapping.put(new Direction[] {Direction.ZDEC}, new int[] {0 + 4, 0});
		
		mapping.put(new Direction[] {Direction.SELFFULL}, new int[] {0 + 4, 3});
		
		//////Wall Sections with Roof
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC}, new int[] {0, 3});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1, 3});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC}, new int[] {2, 3});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 3});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.ZINC, Direction.ZDEC}, new int[] {3, 2});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {2, 2});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.XDEC}, new int[] {0, 2});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1, 2});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC, Direction.ZINC}, new int[] {2, 1});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC, Direction.ZDEC}, new int[] {3, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC, Direction.ZINC}, new int[] {2, 1});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XINC}, new int[] {1, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.XDEC}, new int[] {0, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.ZINC}, new int[] {1, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.YDEC, Direction.ZDEC}, new int[] {0, 0});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {1 + 4, 3});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 3});
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 3});
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.ZINC, Direction.ZDEC}, new int[] {3 + 4, 2});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC}, new int[] {2 + 4, 2});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.XDEC}, new int[] {0 + 4, 2});
		mapping.put(new Direction[] {Direction.YINC, Direction.ZINC, Direction.ZDEC}, new int[] {1 + 4, 2});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.ZDEC}, new int[] {2 + 4, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.ZINC}, new int[] {3 + 4, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 1});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.ZDEC}, new int[] {2 + 4, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC, Direction.ZDEC}, new int[] {3 + 4, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC, Direction.ZINC}, new int[] {3 + 4, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC, Direction.ZINC}, new int[] {2 + 4, 1});
		
		mapping.put(new Direction[] {Direction.YINC, Direction.XINC}, new int[] {1 + 4, 0});
		mapping.put(new Direction[] {Direction.YINC, Direction.XDEC}, new int[] {0 + 4, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.ZINC}, new int[] {1 + 4, 1});
		mapping.put(new Direction[] {Direction.YINC, Direction.ZDEC}, new int[] {0 + 4, 0});
		
		ruleSet.add(new DirectionBasedRenderer(InlineFunctions.inlineArray(ComponentWall.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/wall/Wall.png"), 
				8, 4, 
				mapping, 
				0,
		false));
		
		ruleSet.add(new HumanRenderer());
	}
	
}
