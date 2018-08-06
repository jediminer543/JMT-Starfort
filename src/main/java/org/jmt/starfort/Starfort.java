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

import java.io.IOException;
import java.util.ArrayList;

import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.ui.EventKey;
import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.game.components.conduit.ComponentConduit;
import org.jmt.starfort.game.entity.EntityDrone;
import org.jmt.starfort.game.entity.human.EntityHuman;
import org.jmt.starfort.game.registra.MaterialRegistra;
import org.jmt.starfort.game.registra.RenderRegistra;
import org.jmt.starfort.logging.LogLevel;
import org.jmt.starfort.logging.Logger;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.ui.UserInterfacing;
import org.jmt.starfort.ui.gui.GUI;
import org.jmt.starfort.ui.gui.window.WindowContext;
import org.jmt.starfort.ui.gui.window.WindowPause;
import org.jmt.starfort.ui.gui.window.debug.WindowInspectDebug;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NativePathModifier;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.controller.conduit.ControlerConduit;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.opengl.GL;

/**
 * THIS SHOULD LIVE IN TEST, BUT LIVES HERE BECAUSE IT CAN
 * 
 * ALSO I'M TIRED SO I'M YELLING
 * 
 * HELLO FUTURE ME
 * 
 * PAST ME INFORMS YOU YOU HAVE OTHER MORE IMPORTANT WORK TO BE DOING
 * 
 * JUST A FRIENDLY HINT
 * 
 * @author Jediminer543
 *
 */
public class Starfort {

	public static long window;
	public static long worldNvgCtx;
	public static long guiNvgCtx;
	public static NkContext guiNukCtx = NkContext.create();
	
	public static int winWidth, winHeight;
	
	public static ArrayList<IRendererRule> renderRules = new ArrayList<>();
	static Renderer r;
	
	static World w1;
	
	static int target = 0;
	
	static WindowPause winopt;
	static WindowInspectDebug winidbg;
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		
		//TODO do stuff
		
		// Initialise the library path
		NativePathModifier.modLibraryPath("lib/native");
		
		//ENABLE DEBUG
		//org.lwjgl.system.Configuration.DEBUG_STREAM.set(System.out);
		//org.lwjgl.system.Configuration.DEBUG_STACK.set(true);
		//org.lwjgl.system.Configuration.DEBUG.set(true);
		//org.lwjgl.system.Configuration.DEBUG_FUNCTIONS.set(true);
		//org.lwjgl.system.Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
		
		preInit();
		
		init();
		
		//float[] colour = new float[] {0.5f, 0.2f, 0.5f, 1f};
		
		World w = w1;
		
		IMaterial mat = MaterialRegistry.getMaterial("Debug");
		MaterialRegistra.registerRenderer(r);
		
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
		
		/*
		w.getBlock(new Coord(-2, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(-1, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZDEC), mat));
		
		w.getBlock(new Coord(-2, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZDEC, Direction.ZINC, Direction.XINC), mat));
		w.getBlock(new Coord(-1, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat));
		w.getBlock(new Coord(0, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZINC, Direction.ZDEC, Direction.XDEC), mat));
		
		w.getBlock(new Coord(-2, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(-1, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC), mat));
		w.getBlock(new Coord(0, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZINC), mat));
		*/
		
		
		w.getBlock(new Coord(-2, 0, -2)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(-1, 0, -2)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(0, 0, -2)).addComponent(new ComponentConduit(mat));
		
		w.getBlock(new Coord(-2, 0, -3)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(-1, 0, -3)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(0, 0, -3)).addComponent(new ComponentConduit(mat));
		
		w.getBlock(new Coord(-2, 0, -4)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(-1, 0, -4)).addComponent(new ComponentConduit(mat));
		w.getBlock(new Coord(0, 0, -4)).addComponent(new ComponentConduit(mat));
		
		w.getController(ControlerConduit.class);
		
		
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
		
		//w.getController(ControllerTask.class);
		w.getBlock(new Coord()).addComponent(new EntityHuman("BOB"));
		
		DevUtil.makeRoom(w1, mat, new Coord(0,0,5), new Coord(3,1,8));
		
		
		
		// TESTING WORLD SAVING WITH JAVA SERIALSER HERE
		//File f = new File("saveTest.serial");
		//f.createNewFile();
		/*
		//Kryo k = new Kryo();
		//k.setAsmEnabled(true);
		//Output o = new Output(new FileOutputStream(f));
		//KryoObjectOutput oos = new KryoObjectOutput(k, o);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(w);
		oos.close();
		if (f.exists() && true) {
			//Input i = new Input(new FileInputStream(f));
			//KryoObjectInput ois = new KryoObjectInput(k, i);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			w = (World) ois.readObject();
			ois.close();
		}*/
		//WorldSaver.saveWorld(f, w);
		//w = WorldSaver.loadWorld(f);
		
		IMaterial testmat = w.getBlock(new Coord(0, 0, 0)).getCompInstance(ComponentStairs.class).getComponentMaterial();
		System.out.println(testmat == mat);
		
		final Coord displayOffset = new Coord(5, 0, 5);
		
		EventBus.registerEventCallback(new EventBus.EventCallback() {
				
			@Override
			public void handleEvent(IEvent ev) {
				if (!ev.getEventConsumed()) {
				if (ev instanceof EventKey) {
					EventKey kev = (EventKey)(ev);
					if (kev.getEventAction() == GLFW.GLFW_PRESS) {
						switch (kev.getEventKey()) {
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
							displayOffset.y -= 1;
						break;
						case (GLFW.GLFW_KEY_RIGHT_BRACKET):
							displayOffset.y += 1;
						break;
						case (GLFW.GLFW_KEY_0):
							target = 0;
						break;
						case (GLFW.GLFW_KEY_1):
							target = 1;
						break;
						}
					}
				} 
				}	
			}
			
			@SuppressWarnings("unchecked") // Cant fix because reasons
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventKey.class};
			}

			@Override
			public int getPriority() {
				return 0;
			}
			
			
		});
		
		World w2 = new World();
		
		
		w2.getBlock(new Coord(0, 0, 0)).addComponent(new ComponentStairs(mat, true, false));
		w2.getBlock(new Coord(0, 1, 0)).addComponent(new ComponentStairs(mat, false, true));
		
		w2.getBlock(new Coord(9, 0, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat));
		
		w2.getBlock(new Coord(0, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		w2.getBlock(new Coord(0, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC), mat));
		w2.getBlock(new Coord(1, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(2, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(-1, 0, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(0, 0, 1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(0, 0, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(0, 0, 3)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC, Direction.ZINC), mat));
		
		w2.getBlock(new Coord(5, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC), mat));
		w2.getBlock(new Coord(5, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(6, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZDEC), mat));
		w2.getBlock(new Coord(6, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC, Direction.ZINC), mat));
		
		w2.getBlock(new Coord(7, 0, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC), mat));
		w2.getBlock(new Coord(4, 0, 6)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(5, 0, 4)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(6, 0, 7)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC), mat));
		
		w2.getBlock(new Coord(-1, 0, -1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.SELFFULL), mat));
		
		//w2.getBlock(new Coord(-2, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZDEC), mat));
		//w2.getBlock(new Coord(-1, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZDEC), mat));
		//w2.getBlock(new Coord(0, 0, -2)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZDEC), mat));
		
		//w2.getBlock(new Coord(-2, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZDEC, Direction.ZINC, Direction.XINC), mat));
		//w2.getBlock(new Coord(-1, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC), mat));
		//w2.getBlock(new Coord(0, 0, -3)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.ZINC, Direction.ZDEC, Direction.XDEC), mat));
		
		//w2.getBlock(new Coord(-2, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XDEC, Direction.ZINC), mat));
		//w2.getBlock(new Coord(-1, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.XDEC, Direction.ZINC), mat));
		//w2.getBlock(new Coord(0, 0, -4)).addComponent(new ComponentPipe(InlineFunctions.inlineArray(Direction.XINC, Direction.ZINC), mat));
		
		w2.getBlock(new Coord(0, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(1, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(2, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(3, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(4, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.ZINC), mat));
		w2.getBlock(new Coord(5, 1, 0)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.ZDEC, Direction.XINC), mat));
		
		w2.getBlock(new Coord(5, 1, 1)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(5, 1, 2)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(5, 1, 3)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(5, 1, 4)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC), mat));
		w2.getBlock(new Coord(5, 1, 5)).addComponent(new ComponentWall(InlineFunctions.inlineArray(Direction.YDEC, Direction.XDEC, Direction.XINC, Direction.ZINC), mat));		
		
		w2.getBlock(new Coord(5, 0, 5)).addComponent(new ComponentStairs(mat, true, false));
		w2.getBlock(new Coord(5, 1, 5)).addComponent(new ComponentStairs(mat, false, true));
		
		w2.getBlock(new Coord(6, 0, 6)).addComponent(new EntityDrone());
		w2.getBlock(new Coord(0, 0, 0)).addComponent(new EntityDrone());
		w2.getBlock(new Coord(0, 0, 1)).addComponent(new EntityDrone());
		w2.getBlock(new Coord(5, 1, 0)).addComponent(new EntityDrone());
		
		
		Processor.addRequest(new TickRequest(w));
		Processor.addRequest(new TickRequest(w2));
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();
			GLFW.glfwSwapBuffers(window);
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			//NanoVG.nvgBeginFrame(worldNvgCtx, 1600, 900, 1);
			//NanoVG.nvgStrokeWidth(worldNvgCtx, 1f);
			if (target == 0) {
				r.draw(w, displayOffset);
			} else {
				r.draw(w2, displayOffset);
			}
			
			GUI.draw();
			//NanoVG.nvgEndFrame(worldNvgCtx);
		}
		
		glfwTerminate();
		Processor.down();
		System.out.println("Processor: TotalTicks="+Processor.getTotalTicks()+";IdleTicks="+Processor.getIdleTicks()+";%Idle="+((double)Processor.getIdleTicks()/Processor.getTotalTicks())*100);
		
	}
	
	public static void preInit() throws IOException {
		
		Logger.addLogOutput((LogLevel l, String mesg) -> {
			if (l.ordinal() > LogLevel.Info.ordinal()) {
				System.err.println(mesg);
			} else if (l.ordinal() >= LogLevel.Debug.ordinal()) {
				System.out.println(mesg);
			}
		});
		
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
		
	    winWidth = 1600;
	    winHeight = 900;
		window = GLFW.glfwCreateWindow(winWidth, winHeight, "STARFORT - TEST", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		
		GLFW.glfwSwapInterval(2);
		
		GL.createCapabilities();
		
		Logger.info("OpenGL version: " + glGetString(GL_VERSION));
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1600, 900, 0, 0.000001, 100000);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		jglMatrixMode(GL_PROJECTION);
		jglLoadIdentity();
		jglOrtho(0, 1600, 900, 0, 0.000001, 100000);
		
		jglMatrixMode(GL_MODELVIEW);
		jglLoadIdentity();
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_BLEND);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnableClientState(GL_VERTEX_ARRAY);
    	glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		//glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		
		worldNvgCtx = 0;//NanoVGGL3.nvgCreateGL3(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_DEBUG);
		r = new Renderer();
		preInitRegistas();
		w1 = new World(); 
	}
	
	/**
	 * Register Registras 
	 */
	public static void preInitRegistas() {
		RenderRegistra.register(renderRules);
		MaterialRegistra.register();
		
	}
	
	/**
	 * Initialise the game
	 */
	public static void init() {
		Processor.init();
		UserInterfacing.setupInterfacing(window);
		GUI.init(window);
		//IWidgetTree wwindow = new WidgetWindow("Test", 0, 0, 200, 500);
		//IWidgetTree wbutton = new WidgetButton("TEST BUTTON", (bool) -> {
		//	System.out.println("Button changed to:" + bool);
		//});
		//wwindow.getWidgetChildren().add(wbutton);
		//GUI.addWidget(wwindow);
		WindowContext winctx = new WindowContext();
		GUI.addWidget(winctx);
		winopt = new WindowPause();
		GUI.addWidget(winopt);
		winidbg = new WindowInspectDebug();
		GUI.addWidget(winidbg);
		r.init(renderRules);
		
	}
	
}
