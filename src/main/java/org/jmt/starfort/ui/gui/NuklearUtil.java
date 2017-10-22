package org.jmt.starfort.ui.gui;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBTruetype.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.EventBus.EventCallback;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.IEventUI;
import org.jmt.starfort.event.events.ui.EventChar;
import org.jmt.starfort.event.events.ui.EventCursorPos;
import org.jmt.starfort.event.events.ui.EventKey;
import org.jmt.starfort.event.events.ui.EventMouseButton;
import org.jmt.starfort.event.events.ui.EventScroll;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkConvertConfig;
import org.lwjgl.nuklear.NkDrawCommand;
import org.lwjgl.nuklear.NkDrawVertexLayoutElement;
import org.lwjgl.nuklear.NkMouse;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.NkUserFontGlyph;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * 
 * THis code is mainly lifted from the LWJGL Demo library; thus:
 * 
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * 
 * I just modified it to work slightly more with my code. All code made by me
 * is obvious by the massive change in coding style. And the fact it contains
 * jmt in the name generally.
 * 
 * @author LWJGL, jediminer543
 *
 */
public class NuklearUtil {

	private static final int NK_BUFFER_DEFAULT_INITIAL_SIZE = 4 * 1024;
	
	private static final int MAX_VERTEX_BUFFER  = 512 * 1024;
	private static final int MAX_ELEMENT_BUFFER = 128 * 1024;

	private static final NkAllocator ALLOCATOR;

	private static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;

	static {
		ALLOCATOR = NkAllocator.create();
		ALLOCATOR.alloc((handle, old, size) -> {
			long mem = nmemAlloc(size);
			if ( mem == NULL )
				throw new OutOfMemoryError();

			return mem;

		});
		ALLOCATOR.mfree((handle, ptr) -> nmemFree(ptr));

		VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
			.position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
			.position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
			.position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
			.position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
			.flip();
	}

	/**
	 * Sets up the passed context; called in nk_glfw3_init (dont call twice)
	 * 
	 * Use instead when you want to customise the glfw3 interface code; just set the window so it can get data from it.
	 * 
	 * @param glfw3ctx
	 */
	public static void nk_glfw3_device_create(NkCtxGLFW3 glfw3ctx) {
		String NK_SHADER_VERSION = "#version 130\n";
		String vertex_shader =
			NK_SHADER_VERSION +
				"uniform mat4 ProjMtx;\n" +
				"in vec2 Position;\n" +
				"in vec2 TexCoord;\n" +
				"in vec4 Color;\n" +
				"out vec2 Frag_UV;\n" +
				"out vec4 Frag_Color;\n" +
				"void main() {\n" +
				"   Frag_UV = TexCoord;\n" +
				"   Frag_Color = Color;\n" +
				"   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
				"}\n";
		String fragment_shader =
			NK_SHADER_VERSION +
				"precision mediump float;\n" +
				"uniform sampler2D Texture;\n" +
				"in vec2 Frag_UV;\n" +
				"in vec4 Frag_Color;\n" +
				"out vec4 Out_Color;\n" +
				"void main(){\n" +
				"   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
				"}\n";

		nk_buffer_init(glfw3ctx.cmds, ALLOCATOR, NK_BUFFER_DEFAULT_INITIAL_SIZE);
		glfw3ctx.prog = glCreateProgram();
		glfw3ctx.vert_shdr = glCreateShader(GL_VERTEX_SHADER);
		glfw3ctx.frag_shdr = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(glfw3ctx.vert_shdr, vertex_shader);
		glShaderSource(glfw3ctx.frag_shdr, fragment_shader);
		glCompileShader(glfw3ctx.vert_shdr);
		glCompileShader(glfw3ctx.frag_shdr);
		if ( glGetShaderi(glfw3ctx.vert_shdr, GL_COMPILE_STATUS) != GL_TRUE )
			throw new IllegalStateException();
		if ( glGetShaderi(glfw3ctx.frag_shdr, GL_COMPILE_STATUS) != GL_TRUE )
			throw new IllegalStateException();
		glAttachShader(glfw3ctx.prog, glfw3ctx.vert_shdr);
		glAttachShader(glfw3ctx.prog, glfw3ctx.frag_shdr);
		glLinkProgram(glfw3ctx.prog);
		if ( glGetProgrami(glfw3ctx.prog, GL_LINK_STATUS) != GL_TRUE )
			throw new IllegalStateException();

		glfw3ctx.uniform_tex = glGetUniformLocation(glfw3ctx.prog, "Texture");
		glfw3ctx.uniform_proj = glGetUniformLocation(glfw3ctx.prog, "ProjMtx");
		int attrib_pos = glGetAttribLocation(glfw3ctx.prog, "Position");
		int attrib_uv = glGetAttribLocation(glfw3ctx.prog, "TexCoord");
		int attrib_col = glGetAttribLocation(glfw3ctx.prog, "Color");

		{
			// buffer setup
			glfw3ctx.vbo = glGenBuffers();
			glfw3ctx.ebo = glGenBuffers();
			glfw3ctx.vao = glGenVertexArrays();

			glBindVertexArray(glfw3ctx.vao);
			glBindBuffer(GL_ARRAY_BUFFER, glfw3ctx.vbo);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glfw3ctx.ebo);

			glEnableVertexAttribArray(attrib_pos);
			glEnableVertexAttribArray(attrib_uv);
			glEnableVertexAttribArray(attrib_col);

			glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
			glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
			glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
		}

		{
			// null texture setup
			int nullTexID = glGenTextures();

			glfw3ctx.null_texture.texture().id(nullTexID);
			glfw3ctx.null_texture.uv().set(0.5f, 0.5f);

			glBindTexture(GL_TEXTURE_2D, nullTexID);
			try ( MemoryStack stack = stackPush() ) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, stack.ints(0xFFFFFFFF));
			}
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		}

		glBindTexture(GL_TEXTURE_2D, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	//TODO Implement 
	public static ByteBuffer StreamToByteBuffer(InputStream istream) throws IOException {
		ByteBuffer outbuf = BufferUtils.createByteBuffer(istream.available());
	    ReadableByteChannel rbc = Channels.newChannel(istream);
	    while ( true ) {
	    	int bytes = rbc.read(outbuf);
	    	if (bytes == -1)
	    		break;
	    	if (outbuf.remaining() == 0) {
	    		//Changed to use istream.available() as it should read currently remaining bytes
	    		//ByteBuffer newBuffer = BufferUtils.createByteBuffer(outbuf.capacity() + istream.available());
	    		ByteBuffer newBuffer = BufferUtils.createByteBuffer(outbuf.capacity() + 256);
	    		outbuf.flip();
	    		newBuffer.put(outbuf);
	    		outbuf = newBuffer;
	    	}
	    }
	    outbuf.flip();
	    return outbuf;
	}
	
	// Stop stuff from being GC'D TODO FIX MEMORY LEAKS THAT WILL INEVITABLY OCCUR
	static ArrayList<Object> list = new ArrayList<Object>();
	
	public static NkUserFont nk_jmt_loadfont(ByteBuffer ttf) {
		int BITMAP_W = 1024;
		int BITMAP_H = 1024;

		int FONT_HEIGHT = 18;
		int fontTexID = glGenTextures();
		
		STBTTFontinfo fontInfo = STBTTFontinfo.create();
		STBTTPackedchar.Buffer cdata = STBTTPackedchar.create(120);
		
		list.add(ttf);
		
		float scale;
		float descent;
		
		try ( MemoryStack stack = stackPush() ) {

			stbtt_InitFont(fontInfo, ttf);
			scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

			IntBuffer d = MemoryUtil.memAllocInt(1);
			stbtt_GetFontVMetrics(fontInfo, null, d, null);
			descent = d.get(0) * scale;

			ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

			STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
			stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, 0);
			stbtt_PackSetOversampling(pc, 4, 4);
			stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
			stbtt_PackEnd(pc);

			// Convert R8 to RGBA8
			ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
			for ( int i = 0; i < bitmap.capacity(); i++ )
				texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
			texture.flip();

			glBindTexture(GL_TEXTURE_2D, fontTexID);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

			MemoryUtil.memFree(texture);
			MemoryUtil.memFree(bitmap);

		}

		
		NkUserFont font = NkUserFont.create();
		font
		.width((handle, h, text, len) -> {
			float text_width = 0;
			//try ( MemoryStack substack = stackPush() ) {
				//MemoryStack substack = MemoryStack.stackGet();
				IntBuffer unicode = memCallocInt(1);

				int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
				int text_len = glyph_len;

				if ( glyph_len == 0 )
					return 0;

				IntBuffer advance = memCallocInt(1);
				while ( text_len <= len && glyph_len != 0 ) {
					if ( unicode.get(0) == NK_UTF_INVALID )
						break;

                    /* query currently drawn glyph information */
					stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
					text_width += advance.get(0) * scale;

					/* offset next glyph */
					glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
					text_len += glyph_len;
				
				//}
				
			}
			return text_width;
		})
		.height(FONT_HEIGHT)
		.query((handle, font_height, glyph, codepoint, next_codepoint) -> {
			//try ( MemoryStack substack = stackPush() ) {
				//MemoryStack substack = MemoryStack.stackGet();
				FloatBuffer x = BufferUtils.createFloatBuffer(1);
				FloatBuffer y = BufferUtils.createFloatBuffer(1);

				STBTTAlignedQuad q = STBTTAlignedQuad.create();
				IntBuffer advance = BufferUtils.createIntBuffer(1);

				stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
				stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

				NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

				ufg.width(q.x1() - q.x0());
				ufg.height(q.y1() - q.y0());
				ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
				ufg.xadvance(advance.get(0) * scale);
				ufg.uv(0).set(q.s0(), q.t0());
				ufg.uv(1).set(q.s1(), q.t1());
				
				//q.free();
				//MemoryUtil.memFree(advance);
			//}
		})
		.texture().id(fontTexID);
		return font;
	}

	/**
	 * Initialises and binds to window
	 * 
	 * @param glfw3ctx
	 * @param win
	 * @return
	 */
	public static NkContext nk_glfw3_init(NkCtxGLFW3 glfw3ctx, long win) {
		glfw3ctx.win = win;
		glfwSetScrollCallback(win, (window, xoffset, yoffset) -> {
			NkVec2 vec = NkVec2.create();
			nk_vec2v(new float[] {(float) xoffset, (float)yoffset}, vec);
			nk_input_scroll(glfw3ctx.ctx, vec);
		});
		glfwSetCharCallback(win, (window, codepoint) -> nk_input_unicode(glfw3ctx.ctx, codepoint));
		glfwSetKeyCallback(win, (window, key, scancode, action, mods) -> {
			boolean press = action == GLFW_PRESS;
			switch ( key ) {
				case GLFW_KEY_ESCAPE:
					glfwSetWindowShouldClose(window, true);
					break;
				case GLFW_KEY_DELETE:
					nk_input_key(glfw3ctx.ctx, NK_KEY_DEL, press);
					break;
				case GLFW_KEY_ENTER:
					nk_input_key(glfw3ctx.ctx, NK_KEY_ENTER, press);
					break;
				case GLFW_KEY_TAB:
					nk_input_key(glfw3ctx.ctx, NK_KEY_TAB, press);
					break;
				case GLFW_KEY_BACKSPACE:
					nk_input_key(glfw3ctx.ctx, NK_KEY_BACKSPACE, press);
					break;
				case GLFW_KEY_UP:
					nk_input_key(glfw3ctx.ctx, NK_KEY_UP, press);
					break;
				case GLFW_KEY_DOWN:
					nk_input_key(glfw3ctx.ctx, NK_KEY_DOWN, press);
					break;
				case GLFW_KEY_HOME:
					nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_START, press);
					nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_START, press);
					break;
				case GLFW_KEY_END:
					nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_END, press);
					nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_END, press);
					break;
				case GLFW_KEY_PAGE_DOWN:
					nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_DOWN, press);
					break;
				case GLFW_KEY_PAGE_UP:
					nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_UP, press);
					break;
				case GLFW_KEY_LEFT_SHIFT:
				case GLFW_KEY_RIGHT_SHIFT:
					nk_input_key(glfw3ctx.ctx, NK_KEY_SHIFT, press);
					break;
				case GLFW_KEY_LEFT_CONTROL:
				case GLFW_KEY_RIGHT_CONTROL:
					if ( press ) {
						nk_input_key(glfw3ctx.ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
					} else {
						nk_input_key(glfw3ctx.ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(glfw3ctx.ctx, NK_KEY_COPY, false);
						nk_input_key(glfw3ctx.ctx, NK_KEY_PASTE, false);
						nk_input_key(glfw3ctx.ctx, NK_KEY_CUT, false);
						nk_input_key(glfw3ctx.ctx, NK_KEY_SHIFT, false);
					}
					break;
			}
		});
		glfwSetCursorPosCallback(win, (window, xpos, ypos) -> nk_input_motion(glfw3ctx.ctx, (int)xpos, (int)ypos));
		glfwSetMouseButtonCallback(win, (window, button, action, mods) -> {
				DoubleBuffer cx = memCallocDouble(1);
				DoubleBuffer cy = memCallocDouble(1);

				glfwGetCursorPos(window, cx, cy);

				int x = (int)cx.get(0);
				int y = (int)cy.get(0);

				int nkButton;
				switch ( button ) {
					case GLFW_MOUSE_BUTTON_RIGHT:
						nkButton = NK_BUTTON_RIGHT;
						break;
					case GLFW_MOUSE_BUTTON_MIDDLE:
						nkButton = NK_BUTTON_MIDDLE;
						break;
					default:
						nkButton = NK_BUTTON_LEFT;
				}
				nk_input_button(glfw3ctx.ctx, nkButton, x, y, action == GLFW_PRESS);
		});

		nk_init(glfw3ctx.ctx, ALLOCATOR, null);
		glfw3ctx.ctx.clip().copy((handle, text, len) -> {
			if ( len == 0 )
				return;

			try ( MemoryStack stack = stackPush() ) {
				ByteBuffer str = stack.malloc(len + 1);
				memCopy(text, memAddress(str), len);
				str.put(len, (byte)0);

				glfwSetClipboardString(win, str);
			}
		});
		glfw3ctx.ctx.clip().paste((handle, edit) -> {
			long text = nglfwGetClipboardString(win);
			if ( text != NULL )
				nnk_textedit_paste(edit, text, nnk_strlen(text));
		});
		nk_glfw3_device_create(glfw3ctx);
		return glfw3ctx.ctx;
	}

	/**
	 * Initialises and binds to window
	 *  
	 * @param glfw3ctx
	 * @param win
	 * @return
	 */
	public static NkContext nk_jmt_bus_init(NkCtxGLFW3 glfw3ctx, long win) {
		glfw3ctx.win = win;
		EventBus.registerEventCallback(new EventCallback() {
			
			@Override
			public void handleEvent(IEvent ev) {
				if (ev instanceof IEventUI && ((IEventUI)ev).getEventWindow() == glfw3ctx.win && !ev.getEventConsumed()) {
				if (ev instanceof EventScroll) {
					EventScroll cev = (EventScroll) ev;
					NkVec2 vec = NkVec2.create();
					nk_vec2v(new float[] {(float) cev.getEventXoffset(), (float) cev.getEventYoffset()}, vec);
					nk_input_scroll(glfw3ctx.ctx, vec);
				} else if (ev instanceof EventChar) {
					EventChar cev = (EventChar) ev;
					nk_input_unicode(glfw3ctx.ctx, cev.getEventCodepoint());
				} else if (ev instanceof EventCursorPos) {
					EventCursorPos cev = (EventCursorPos) ev;
					nk_input_motion(glfw3ctx.ctx, (int)cev.getEventXPos(), (int)cev.getEventYPos());
				} else if (ev instanceof EventMouseButton) {
					EventMouseButton cev = (EventMouseButton) ev;
					DoubleBuffer cx = memCallocDouble(1);
					DoubleBuffer cy = memCallocDouble(1);

					glfwGetCursorPos(cev.getEventWindow(), cx, cy);

					int x = (int)cx.get(0);
					int y = (int)cy.get(0);

					int nkButton;
					switch ( cev.getEventButton() ) {
						case GLFW_MOUSE_BUTTON_RIGHT:
							nkButton = NK_BUTTON_RIGHT;
							break;
						case GLFW_MOUSE_BUTTON_MIDDLE:
							nkButton = NK_BUTTON_MIDDLE;
							break;
						default:
							nkButton = NK_BUTTON_LEFT;
					}
					nk_input_button(glfw3ctx.ctx, nkButton, x, y, cev.getEventAction() == GLFW_PRESS);
				} else if (ev instanceof EventKey) {
					EventKey cev = (EventKey) ev;
					long window = cev.getEventWindow();
					boolean press = cev.getEventAction() == GLFW_PRESS;
					switch ( cev.getEventKey() ) {
						case GLFW_KEY_DELETE:
							nk_input_key(glfw3ctx.ctx, NK_KEY_DEL, press);
							break;
						case GLFW_KEY_ENTER:
							nk_input_key(glfw3ctx.ctx, NK_KEY_ENTER, press);
							break;
						case GLFW_KEY_TAB:
							nk_input_key(glfw3ctx.ctx, NK_KEY_TAB, press);
							break;
						case GLFW_KEY_BACKSPACE:
							nk_input_key(glfw3ctx.ctx, NK_KEY_BACKSPACE, press);
							break;
						case GLFW_KEY_UP:
							nk_input_key(glfw3ctx.ctx, NK_KEY_UP, press);
							break;
						case GLFW_KEY_DOWN:
							nk_input_key(glfw3ctx.ctx, NK_KEY_DOWN, press);
							break;
						case GLFW_KEY_HOME:
							nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_START, press);
							nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_START, press);
							break;
						case GLFW_KEY_END:
							nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_END, press);
							nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_END, press);
							break;
						case GLFW_KEY_PAGE_DOWN:
							nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_DOWN, press);
							break;
						case GLFW_KEY_PAGE_UP:
							nk_input_key(glfw3ctx.ctx, NK_KEY_SCROLL_UP, press);
							break;
						case GLFW_KEY_LEFT_SHIFT:
						case GLFW_KEY_RIGHT_SHIFT:
							nk_input_key(glfw3ctx.ctx, NK_KEY_SHIFT, press);
							break;
						case GLFW_KEY_LEFT_CONTROL:
						case GLFW_KEY_RIGHT_CONTROL:
							if ( press ) {
								nk_input_key(glfw3ctx.ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
							} else {
								nk_input_key(glfw3ctx.ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
								nk_input_key(glfw3ctx.ctx, NK_KEY_COPY, false);
								nk_input_key(glfw3ctx.ctx, NK_KEY_PASTE, false);
								nk_input_key(glfw3ctx.ctx, NK_KEY_CUT, false);
								nk_input_key(glfw3ctx.ctx, NK_KEY_SHIFT, false);
							}
							break;
					} 
				}
				}
				if (nk_item_is_any_active(glfw3ctx.ctx)) {
					//Flag the event as being consumed if inside the windos
					ev.consumeEvent();
				}
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventChar.class, EventCursorPos.class, EventKey.class, EventMouseButton.class, EventScroll.class};
			}

			@Override
			public int getPriority() {
				return 100;
			}
		});
		nk_init(glfw3ctx.ctx, ALLOCATOR, null);
		
		glfw3ctx.ctx.clip().copy((handle, text, len) -> {
			if ( len == 0 )
				return;

			try ( MemoryStack stack = stackPush() ) {
				ByteBuffer str = stack.malloc(len + 1);
				memCopy(text, memAddress(str), len);
				str.put(len, (byte)0);

				glfwSetClipboardString(win, str);
			}
		});
		glfw3ctx.ctx.clip().paste((handle, edit) -> {
			long text = nglfwGetClipboardString(win);
			if ( text != NULL )
				nnk_textedit_paste(edit, text, nnk_strlen(text));
		});
		nk_glfw3_device_create(glfw3ctx);
		return glfw3ctx.ctx;
	}
	
	public static void nk_glfw3_new_frame(NkCtxGLFW3 glfw3ctx) {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);

			glfwGetWindowSize(glfw3ctx.win, w, h);
			glfw3ctx.width = w.get(0);
			glfw3ctx.height = h.get(0);

			glfwGetFramebufferSize(glfw3ctx.win, w, h);
			glfw3ctx.display_width = w.get(0);
			glfw3ctx.display_height = h.get(0);
		}

		nk_input_begin(glfw3ctx.ctx);
		glfwPollEvents();

		NkMouse mouse = glfw3ctx.ctx.input().mouse();
		if ( mouse.grab() )
			glfwSetInputMode(glfw3ctx.win, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		else if ( mouse.grabbed() ) {
			float prevX = mouse.prev().x();
			float prevY = mouse.prev().y();
			glfwSetCursorPos(glfw3ctx.win, prevX, prevY);
			mouse.pos().x(prevX);
			mouse.pos().y(prevY);
		} else if ( mouse.ungrab() )
			glfwSetInputMode(glfw3ctx.win, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

		nk_input_end(glfw3ctx.ctx);
	}

	public static void nk_glfw3_render(NkCtxGLFW3 glfw3ctx, int AA) {
			try (MemoryStack stack = stackPush()) {
			// setup global state
			glBlendEquation(GL_FUNC_ADD);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_DEPTH_TEST);

			// setup program
			glUseProgram(glfw3ctx.prog);
			glUniform1i(glfw3ctx.uniform_tex, 0);
			FloatBuffer buf = stack.floats(
			//buf.put(new float[] {
					2.0f / glfw3ctx.width, 0.0f, 0.0f, 0.0f,
					0.0f, -2.0f / glfw3ctx.height, 0.0f, 0.0f,
					0.0f, 0.0f, -1.0f, 0.0f,
					-1.0f, 1.0f, 0.0f, 1.0f
			//});
					);
			glUniformMatrix4fv(glfw3ctx.uniform_proj, false, buf);
			glViewport(0, 0, glfw3ctx.display_width, glfw3ctx.display_height);
			}
			
			

		{
			// convert from command queue into draw list and draw to screen

			// allocate vertex and element buffer
			glBindVertexArray(glfw3ctx.vao);
			glBindBuffer(GL_ARRAY_BUFFER, glfw3ctx.vbo);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glfw3ctx.ebo);

			glBufferData(GL_ARRAY_BUFFER, MAX_VERTEX_BUFFER, GL_STREAM_DRAW);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, MAX_ELEMENT_BUFFER, GL_STREAM_DRAW);

			// load draw vertices & elements directly into vertex + element buffer
			ByteBuffer vertices = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_VERTEX_BUFFER, null);
			ByteBuffer elements = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_ELEMENT_BUFFER, null);
			try ( MemoryStack stack = stackPush() ) {
				// fill convert configuration
				NkConvertConfig config = NkConvertConfig.mallocStack(stack)
					.vertex_layout(VERTEX_LAYOUT)
					.vertex_size(20)
					.vertex_alignment(4)
					.null_texture(glfw3ctx.null_texture)
					.circle_segment_count(22)
					.curve_segment_count(22)
					.arc_segment_count(22)
					.global_alpha(1.0f)
					.shape_AA(AA)
					.line_AA(AA);

				// setup buffers to load vertices and elements
				NkBuffer vbuf = NkBuffer.mallocStack(stack);
				NkBuffer ebuf = NkBuffer.mallocStack(stack);

				nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
				nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
				nk_convert(glfw3ctx.ctx, glfw3ctx.cmds, vbuf, ebuf, config);
			}
			glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
			glUnmapBuffer(GL_ARRAY_BUFFER);

			// iterate over and execute each draw command
			float fb_scale_x = (float)glfw3ctx.display_width / (float)glfw3ctx.width;
			float fb_scale_y = (float)glfw3ctx.display_height / (float)glfw3ctx.height;

			long offset = NULL;
			for ( NkDrawCommand cmd = nk__draw_begin(glfw3ctx.ctx, glfw3ctx.cmds); cmd != null; cmd = nk__draw_next(cmd, glfw3ctx.cmds, glfw3ctx.ctx) ) {
				if ( cmd.elem_count() == 0 ) continue;
				glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
				glScissor(
					(int)(cmd.clip_rect().x() * fb_scale_x),
					(int)((glfw3ctx.height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
					(int)(cmd.clip_rect().w() * fb_scale_x),
					(int)(cmd.clip_rect().h() * fb_scale_y)
				);
				glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
				offset += cmd.elem_count() * 2;
			}
			nk_clear(glfw3ctx.ctx);
		}

		// default OpenGL state
		glUseProgram(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glEnable(GL_DEPTH_TEST);
	}

	private static void nk_glfw3_device_destroy(NkCtxGLFW3 glfw3ctx) {
		glDetachShader(glfw3ctx.prog, glfw3ctx.vert_shdr);
		glDetachShader(glfw3ctx.prog, glfw3ctx.frag_shdr);
		glDeleteShader(glfw3ctx.vert_shdr);
		glDeleteShader(glfw3ctx.frag_shdr);
		glDeleteProgram(glfw3ctx.prog);
		glDeleteTextures(glfw3ctx.default_font.texture().id());
		glDeleteTextures(glfw3ctx.null_texture.texture().id());
		glDeleteBuffers(glfw3ctx.vbo);
		glDeleteBuffers(glfw3ctx.ebo);
		nk_buffer_free(glfw3ctx.cmds);
	}

	public static void nk_glfw3_shutdown(NkCtxGLFW3 glfw3ctx) {
		glfw3ctx.ctx.clip().copy().free();
		glfw3ctx.ctx.clip().paste().free();
		nk_free(glfw3ctx.ctx);
		nk_glfw3_device_destroy(glfw3ctx);
		glfw3ctx.default_font.query().free();
		glfw3ctx.default_font.width().free();

		ALLOCATOR.alloc().free();
		ALLOCATOR.mfree().free();
	}
}
