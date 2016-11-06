package org.jmt.starfort;

import static org.lwjgl.opengl.GL11.*;
/*
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
*/
import static org.lwjgl.glfw.GLFW.*;
import static org.jmt.starfort.renderer.JMTGl.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.game.components.fluid.ComponentPipe;
import org.jmt.starfort.game.entity.EntityDrone;
import org.jmt.starfort.game.registra.RenderRegistra;
import org.jmt.starfort.game.renderer.DirectionBasedRenderer;
import org.jmt.starfort.game.renderer.GenericRenderer;
import org.jmt.starfort.game.renderer.WallRenderer;
import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.renderer.Colour;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NativePathModifier;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.IMaterialType;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.reflections.vfs.CommonsVfs2UrlType.Dir;

public class Starfort {

	public static long window;
	public static long worldNvgCtx;
	public static long guiNvgCtx;
	public static NkContext guiNukCtx = NkContext.create();
	public static ArrayList<IRendererRule> renderRules = new ArrayList<>();
	static Renderer r;
	
	static World w1;
	
	public static void main(String[] args) throws Exception {
		
		//TODO do stuff
		
		NativePathModifier.modLibraryPath("lib/native");
		
		preInit();
		
		init();
		
		IMaterialType stony = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Stone";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				// TODO Auto-generated method stub
				return "A stonelike material";
			}
		};
		
		IMaterialType metal = new IMaterialType() {
			
			@Override
			public String getMaterialTypeName() {
				return "Metal";
			}
			
			@Override
			public String getMaterialTypeDescriptor() {
				// TODO Auto-generated method stub
				return "A metalic material";
			}
		};
		
		MaterialRegistry.registerMaterialType(metal);
		MaterialRegistry.registerMaterialType(stony);
		
		//float[] colour = new float[] {0.5f, 0.2f, 0.5f, 1f};
		IMaterial mat = new IMaterial() {
			
			@Override
			public IMaterialType getMaterialType() {
				return MaterialRegistry.getMaterialType("Metal");
			}
			
			@Override
			public String getMaterialName() {
				return "jmt.starfort.mattmp.1";
			}
			
			@Override
			public float getMaterialHardness() {
				return 1;
			}
		};
		
		World w = w1;
		
		int matID = MaterialRegistry.registerMaterial(mat);	
		r.materialRenderReg.put(matID, new Colour(0.5f, 0.2f, 0.5f, 1f));
		
		w.getBlock(new Coord(0, 0, 0)).addComponent(new ComponentStairs(mat, true, false));
		w.getBlock(new Coord(0, 1, 0)).addComponent(new ComponentStairs(mat, false, true));
		
		w.getBlock(new Coord(9, 0, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat));
		
		w.getBlock(new Coord(0, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC), mat));
		w.getBlock(new Coord(1, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(2, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(-1, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(0, 0, 1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(0, 0, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(0, 0, 3)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC, Direction.ZINC), mat));
		
		w.getBlock(new Coord(5, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(5, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(6, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZDEC), mat));
		w.getBlock(new Coord(6, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZINC), mat));
		
		w.getBlock(new Coord(7, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC), mat));
		w.getBlock(new Coord(4, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC), mat));
		w.getBlock(new Coord(5, 0, 4)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(6, 0, 7)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC), mat));
		
		w.getBlock(new Coord(-1, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.SELFFULL), mat));
		
		w.getBlock(new Coord(-2, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(-1, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZDEC), mat));
		
		w.getBlock(new Coord(-2, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZDEC, Direction.ZINC, Direction.XINC), mat));
		w.getBlock(new Coord(-1, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZINC, Direction.ZDEC, Direction.XDEC), mat));
		
		w.getBlock(new Coord(-2, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(-1, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(0, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZINC), mat));
		
		w.getBlock(new Coord(0, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(1, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(2, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(3, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(4, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(5, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.XINC), mat));
		
		w.getBlock(new Coord(5, 1, 1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(5, 1, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(5, 1, 3)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(5, 1, 4)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w.getBlock(new Coord(5, 1, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC, Direction.ZINC), mat));		
		
		w.getBlock(new Coord(5, 0, 5)).addComponent(new ComponentStairs(mat, true, false));
		w.getBlock(new Coord(5, 1, 5)).addComponent(new ComponentStairs(mat, false, true));
		
		w.getBlock(new Coord(6, 0, 6)).addComponent(new EntityDrone());
		w.getBlock(new Coord(0, 0, 0)).addComponent(new EntityDrone());
		w.getBlock(new Coord(0, 0, 1)).addComponent(new EntityDrone());
		w.getBlock(new Coord(5, 1, 0)).addComponent(new EntityDrone());
		
		makeRoom(w1, mat, new Coord(0,0,5), new Coord(3,1,8));
		
		/*
		IMaterial mat2 = new IMaterial() {
			
			@Override
			public IMaterialType getMaterialType() {
				return MaterialRegistry.getMaterialType("Metal");
			}
			
			@Override
			public String getMaterialName() {
				return "jmt.starfort.mattmp.2";
			}
			
			@Override
			public float getMaterialHardness() {
				return 1;
			}
		};
		
		int mat2ID = MaterialRegistry.registerMaterial(mat2);	
		System.out.println("Drawing wall with matId:" + mat2ID);
		r.materialRenderReg.put(mat2ID, new Colour(0.5f, 0.7f, 0.5f, 0.5f));
		
		Coord src = new Coord(6, 0, 6);
		
		Path p = BruteforcePather.pathBetween(src, new Coord(0, 0, 3), w, new IPassageCallback() {
			
			@Override
			public boolean canPass(World w, Coord src, Direction dir) {
				if (dir != Direction.YINC && dir != Direction.YDEC) {
					System.out.println(w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(dir)  + " " + w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(Direction.SELFFULL) 
					+ " " + w.getBlock(src).getBlockedDirs(NavContext.Physical).contains(dir.inverse()));
					if (w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(dir.inverse()) || w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(Direction.SELFFULL) 
							|| w.getBlock(src.get()).getBlockedDirs(NavContext.Physical).contains(dir)) {
							return false;
					}
					return true;
				} else {
					List<IComponent> UDCL = null;
					if (!(UDCL = w.getBlock(src).getCompInstances(IComponentUpDown.class)).isEmpty()) {
						for (IComponent c : UDCL) {
							IComponentUpDown UDC = (IComponentUpDown) c;
							if (UDC.canUp() && dir == Direction.YINC)
								return true;
							if (UDC.canDown() && dir == Direction.YDEC)
								return true;
						}
					}
				}
				return false;
			}
		});
		
		while (p.remaining() > 0) {
			src.addRM(p.pop().getDir());
			//w.getBlock(new Coord(src.x, src.y, src.z)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat2));
			w.getBlock(src).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat2));
			System.out.println("Path to " + src + "");
		}
		*/
		
		
		final Coord displayOffset = new Coord(5, 0, 5);
		
		GLFW.glfwSetKeyCallback(window, new GLFWKeyCallbackI() {
			
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_PRESS) {
				switch (key) {
				case (GLFW.GLFW_KEY_UP):
					displayOffset.z += 1;
					break;
				case (GLFW.GLFW_KEY_DOWN):
					displayOffset.z -= 1;
					break;
				case (GLFW.GLFW_KEY_LEFT):
					displayOffset.x += 1;
					break;
				case (GLFW.GLFW_KEY_RIGHT):
					displayOffset.x -= 1;
					break;
				case (GLFW.GLFW_KEY_LEFT_BRACKET):
					displayOffset.y += 1;
					break;
				case (GLFW.GLFW_KEY_RIGHT_BRACKET):
					displayOffset.y -= 1;
					break;
				}
				}
			}
		});
		
		Processor.addRequest(new TickRequest(w));
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();
			GLFW.glfwSwapBuffers(window);
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			//NanoVG.nvgBeginFrame(worldNvgCtx, 1600, 900, 1);
			//NanoVG.nvgStrokeWidth(worldNvgCtx, 1f);
			r.draw(w, displayOffset);
			//NanoVG.nvgEndFrame(worldNvgCtx);
		}
		
		glfwTerminate();
		Processor.down();
	}
	
	public static void preInit() throws IOException {
		
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		//glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		//glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		//glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);
		//glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
	    glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
	    
	    //glfwWindowHint(GLFW_SAMPLES, 8);
	    //glfwWindowHint(GLFW_RED_BITS, 8);
	    //glfwWindowHint(GLFW_GREEN_BITS, 8);
	    //glfwWindowHint(GLFW_BLUE_BITS, 8);
	    //glfwWindowHint(GLFW_ALPHA_BITS, 8);
		//glfwWindowHint(GLFW_DEPTH_BITS, 0);
	    //glfwWindowHint(GLFW_STENCIL_BITS, 8);
		
		window = GLFW.glfwCreateWindow(1600, 900, "STARFORT - TEST", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1600, 900, 0, 0.000001, 100);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		jglMatrixMode(GL_PROJECTION);
		jglLoadIdentity();
		jglOrtho(0, 1600, 900, 0, 0.000001, 100);
		
		jglMatrixMode(GL_MODELVIEW);
		jglLoadIdentity();
		
		//jglLoadShader("".getClass().getResourceAsStream("/org/jmt/starfort/shader/ComponentShader.GLSL13.vert"), 
		//		"".getClass().getResourceAsStream("/org/jmt/starfort/shader/ComponentShader.GLSL13.frag"));
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_BLEND);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnableClientState(GL_VERTEX_ARRAY);
    	glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		//glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		
		worldNvgCtx = 0;//NanoVGGL3.nvgCreateGL3(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_DEBUG);
		//guiNukCtx = nk_gl
		r = new Renderer();
		preInitGenRenderRules();
		w1 = new World(); 
	}
	
	public static void makeRoom(World w, IMaterial m, Coord minCorner, Coord maxCorner) {
		for (int x = minCorner.x; x <= maxCorner.x; x++) {
			boolean XDEC = x == minCorner.x;
			boolean XINC = x == maxCorner.x;
			for (int z = minCorner.z; z <= maxCorner.z; z++) {
				boolean ZDEC = z == minCorner.z;
				boolean ZINC = z == maxCorner.z;
				for (int y = minCorner.y; y <= maxCorner.y; y++) {
					boolean YDEC = y == minCorner.y;
					boolean YINC = y == maxCorner.y;
					
					Direction[] wallConf = makeDirArray(XINC, XDEC, ZINC, ZDEC, YINC, YDEC);
					w.getBlock(new Coord(x, y, z)).addComponent(new ComponentWall(wallConf, m));
				}
			}
		}
	}
	
	public static Direction[] makeDirArray(boolean XINC, boolean XDEC, boolean ZINC, boolean ZDEC, boolean YINC, boolean YDEC) {
		ArrayList<Direction> dirArr = new ArrayList<>();
		if (XINC) { dirArr.add(Direction.XINC); }
		if (XDEC) { dirArr.add(Direction.XDEC); }
		if (YINC) { dirArr.add(Direction.YINC); }
		if (YDEC) { dirArr.add(Direction.YDEC); }
		if (ZINC) { dirArr.add(Direction.ZINC); }
		if (ZDEC) { dirArr.add(Direction.ZDEC); }
		return dirArr.toArray(new Direction[dirArr.size()]);
	}
	
	public static void preInitGenRenderRules() {
		//renderRules.add(new WallRenderer());

		RenderRegistra.register(renderRules);
		
		
	}
	
	public static void init() {
		Processor.init();
		r.init(renderRules);
	}
	
}
