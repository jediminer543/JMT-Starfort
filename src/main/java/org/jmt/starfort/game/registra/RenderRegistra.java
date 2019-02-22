package org.jmt.starfort.game.registra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.game.components.conduit.ComponentConduit;
import org.jmt.starfort.game.components.debug.ComponentDebugFlag;
import org.jmt.starfort.game.entity.EntityDrone;
import org.jmt.starfort.game.renderer.HumanRenderer;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.rules.DirectionBasedRenderer;
import org.jmt.starfort.renderer.rules.GenericRenderer;
import org.jmt.starfort.renderer.rules.MaskedRenderer;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.world.component.designator.DesignatorConstruct;
import org.jmt.starfort.world.component.designator.DesignatorReplace;
import org.jmt.starfort.world.component.designator.IComponentDesignator;

public class RenderRegistra {

	public static void register(ArrayList<IRendererRule> ruleSet) {
		registerBlocks(ruleSet);
	}
	
	public static void registerBlocks(ArrayList<IRendererRule> ruleSet) {
		
		Map<Direction[], int[]> mapping = new HashMap<>();
		/*
		//Register Pipe Renderer
		mapping = new HashMap<>();
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
		*/
		
		
		mapping = new HashMap<>();
		mapping.put(new Direction[] {Direction.XINC}, new int[] {1, 3});
		mapping.put(new Direction[] {Direction.XDEC}, new int[] {3, 3});
		mapping.put(new Direction[] {Direction.ZINC}, new int[] {3, 2});
		mapping.put(new Direction[] {Direction.ZDEC}, new int[] {2, 3});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.XDEC}, new int[] {1, 0});
		mapping.put(new Direction[] {Direction.ZINC, Direction.ZDEC}, new int[] {0, 0});
		
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZINC}, new int[] {2, 1});
		mapping.put(new Direction[] {Direction.XDEC, Direction.ZDEC}, new int[] {2, 0});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC}, new int[] {3, 1});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC}, new int[] {3, 0});
		
		mapping.put(new Direction[] {Direction.XINC, Direction.ZINC, Direction.XDEC}, new int[] {1, 2});
		mapping.put(new Direction[] {Direction.XINC, Direction.ZDEC, Direction.XDEC}, new int[] {1, 1});
		mapping.put(new Direction[] {Direction.ZINC, Direction.XINC, Direction.ZDEC}, new int[] {0, 1});
		mapping.put(new Direction[] {Direction.ZDEC, Direction.XDEC, Direction.ZINC}, new int[] {0, 2});
		
		mapping.put(new Direction[] {Direction.ZDEC, Direction.XDEC, Direction.ZINC , Direction.XINC}, new int[] {2, 2});
		
		
		Map<Direction[], int[]> tmpmapping = new HashMap<>();
		for (Entry<Direction[], int[]> p : mapping.entrySet()) {
			List<Direction> l = InlineFunctions.inlineArrayList(p.getKey());
			l.add(Direction.YINC);
			tmpmapping.put(l.toArray(new Direction[l.size()]), p.getValue());
		}
		mapping.putAll(tmpmapping);
		
		tmpmapping = new HashMap<>();
		for (Entry<Direction[], int[]> p : mapping.entrySet()) {
			List<Direction> l = InlineFunctions.inlineArrayList(p.getKey());
			l.add(Direction.YDEC);
			tmpmapping.put(l.toArray(new Direction[l.size()]), p.getValue());
		}
		mapping.putAll(tmpmapping);
		
		ruleSet.add(new DirectionBasedRenderer(InlineFunctions.inlineArray(ComponentConduit.class), 
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
		
		ruleSet.add(new GenericRenderer(InlineFunctions.inlineArray(IComponentDesignator.class, DesignatorConstruct.class, DesignatorReplace.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/designator/designator.png"),
				150));
		
		ruleSet.add(new MaskedRenderer(InlineFunctions.inlineArray(ComponentDebugFlag.class), 
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/debug/flag.png"),
				"".getClass().getResourceAsStream("/org/jmt/starfort/texture/component/debug/flagmask.png"),
				120));
		
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
