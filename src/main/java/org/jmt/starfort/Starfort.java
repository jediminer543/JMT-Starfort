package org.jmt.starfort;

import static org.lwjgl.nuklear.Nuklear.*;
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

import java.util.ArrayList;

import org.jmt.starfort.game.components.ComponentWall;
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
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.IMaterialType;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.opengl.GL;

public class Starfort {

	public static long window;
	public static long worldNvgCtx;
	public static long guiNvgCtx;
	public static NkContext guiNukCtx = NkContext.create();
	public static ArrayList<IRendererRule> renderRules = new ArrayList<>();
	static Renderer r;
	
	static World w;
	
	public static void main(String[] args) throws Exception {
		
		//TODO do stuff
		
		NativePathModifier.modLibraryPath("lib/native");
	
		preInit();
		
		init();
		
		//float[] colour = new float[] {0.5f, 0.2f, 0.5f, 1f};
		IMaterial mat = new IMaterial() {
			
			@Override
			public IMaterialType getMaterialType() {
				return null;
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
		
		int matID = MaterialRegistry.registerMaterial(mat);	
		r.materialRenderReg.put(matID, new Colour(0.5f, 0.2f, 0.5f, 1f));
		w.getBlock(new Coord(0, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC), mat));
		w.getBlock(new Coord(1, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZDEC, Direction.ZINC), mat));
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
		w.getBlock(new Coord(5, 0, 4)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(6, 0, 7)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZINC), mat));
		
		w.getBlock(new Coord(-1, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.SELFFULL), mat));
		
		/*
		colour = new float[] {0.5f, 0.7f, 0.5f, 0.5f};
		
		Coord src = new Coord(10, 0, 10);
		
		Path p = BruteforcePather.pathBetween(src,  src.addR(new Coord(5, 0, 5)), w, new IPassageCallback() {
			
			@Override
			public boolean canPass(World w, Coord src, Direction dir) {
				if (dir != Direction.YINC) {
					if (w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(dir) || w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(Direction.SELFFULL) 
							|| w.getBlock(src).getBlockedDirs(NavContext.Physical).contains(dir.inverse())) {
							return false;
					}
					return true;
				}
				return false;
			}
		});
		
		while (p.remaining() > 0) {
			w.getBlock(src).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.SELFFULL), colour));
			src.addR(p.pop().getDir());
		}
		*/
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();
			GLFW.glfwSwapBuffers(window);
			
			NanoVG.nvgBeginFrame(worldNvgCtx, 1600, 900, 1);
			NanoVG.nvgStrokeWidth(worldNvgCtx, 1f);
			r.draw(worldNvgCtx, w, new Coord(5, 0, 5));
			NanoVG.nvgEndFrame(worldNvgCtx);
		}
	}
	
	public static void preInit() {
		
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		window = GLFW.glfwCreateWindow(1600, 900, "STARFORT - TEST", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1600, 900, 0, 0.000001, 100);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		
		worldNvgCtx = NanoVGGL3.nvgCreateGL3(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_DEBUG);
		//guiNukCtx = nk_gl
		r = new Renderer();
		renderRules.add(new WallRenderer());
		w = new World(); 
	}
	
	public static void init() {
		Processor.init();
		r.init(worldNvgCtx, renderRules);
	}
	
}
