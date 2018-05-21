package org.jmt.starfort.test.world;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentBlocking;
import org.jmt.starfort.world.material.IMaterial;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testBasicComponentOperations() {
		Block b = new Block();
		assertEquals("Check initial length", b.getComponents().size(), 0);
		IComponent comp = new IComponent() {
			
			private static final long serialVersionUID = 4051712896629942910L;

			@Override
			public String getComponentName() {
				return null;
			}
			
			@Override
			public IMaterial getComponentMaterial() {
				return null;
			}
		};
		b.addComponent(comp);
		assertEquals("Check array length post add", b.getComponents().size(), 1);
		assertEquals("Check component was added", b.getComponent(0), comp);
		b.removeComponent(comp);
		assertEquals("Check array length post remove", b.getComponents().size(), 0);
	}
	
	@Test
	public void test_getBlockedDirs() {
		Block b = new Block();
		ArrayList<Direction> response = b.getBlockedDirs(NavContext.Physical);
		assertEquals("Check empty with new block", response.size(), 0);
		IComponent comp = new IComponent() {
			
			private static final long serialVersionUID = 4051712896629942910L;

			@Override
			public String getComponentName() {
				return null;
			}
			
			@Override
			public IMaterial getComponentMaterial() {
				return null;
			}
		};
		b.addComponent(comp);
		response = b.getBlockedDirs(NavContext.Physical);
		assertEquals("Check empty with non blocking component", response.size(), 0);
		Direction[] blockTest = new Direction[] {Direction.XDEC, Direction.ZINC};
		IComponent compblock = new IComponentBlocking() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Direction[] getComponentDirections() {
				return blockTest;
			}
			
			@Override
			public String getComponentName() {
				return null;
			}
			
			@Override
			public IMaterial getComponentMaterial() {
				return null;
			}
			
			@Override
			public Map<NavContext, Direction[]> getComponentBlockedDirs() {
				return InlineFunctions.inlineMap(InlineFunctions.inlineKey(NavContext.Physical, blockTest));
			}
		};
		b.addComponent(compblock);
		response = b.getBlockedDirs(NavContext.Physical);
		assertEquals("Check non empty with blocking component", response.size(), blockTest.length);
		assertArrayEquals("Check output", response.toArray(new Direction[response.size()]), blockTest);
	}
	
	@Test
	public void test_getCompInstances() {
		Block b = new Block();
		IComponent response = b.getCompInstance(IComponentBlocking.class);
		assertNull("Check null response", response);
		assertEquals("Check non empty with blocking component", b.getCompInstances(IComponentBlocking.class).size(), 0);
		Direction[] blockTest = new Direction[] {Direction.XDEC, Direction.ZINC};
		IComponent compblock = new IComponentBlocking() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Direction[] getComponentDirections() {
				return blockTest;
			}
			
			@Override
			public String getComponentName() {
				return null;
			}
			
			@Override
			public IMaterial getComponentMaterial() {
				return null;
			}
			
			@Override
			public Map<NavContext, Direction[]> getComponentBlockedDirs() {
				return InlineFunctions.inlineMap(InlineFunctions.inlineKey(NavContext.Physical, blockTest));
			}
		};
		b.addComponent(compblock);
		response = b.getCompInstance(IComponentBlocking.class);
		assertEquals("Check component returned", response, compblock);

		
		
	}

}
