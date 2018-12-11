package org.jmt.starfort.renderer.rules;

import static org.jmt.starfort.renderer.JMTGl.jglColor4f;
import static org.jmt.starfort.renderer.JMTGl.jglGetAttribLocation;
import static org.jmt.starfort.renderer.JMTGl.jglPopMatrix;
import static org.jmt.starfort.renderer.JMTGl.jglPushMatrix;
import static org.jmt.starfort.renderer.JMTGl.jglTranslatef;
import static org.jmt.starfort.renderer.JMTGl.jglUseProgram;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.IOException;
import java.io.InputStream;

import org.jmt.starfort.renderer.Colour;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.renderer.Texture;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;
import org.joml.Vector2f;

public class MaskedRenderer implements IRendererRule {

	Class<? extends IComponent>[] toRender;
	InputStream isTex, isMask;
	int priority;
	
	/**
	 * Initialises a generic renderer with a texture stream
	 * 
	 * @param compClass List of Component Classes to render
	 * @param tex Texture that the components will be rendered with as an input stream
	 * @param mask Texture Mask that the components will be rendered with as an input stream
	 * @param priority Z priority of this render rule
	 */
	public MaskedRenderer(Class<? extends IComponent>[] compClass, InputStream tex, InputStream mask, int priority) {
		toRender = compClass;
		isTex = tex;
		isMask = mask;
		this.priority = priority;
	}
	
	@Override
	public Class<? extends IComponent>[] getRenderableComponents() {
		return toRender;
	}
	
	Texture t, m;
	
	@Override
	public void init(Renderer r) {
		try {
			t = new Texture(isTex);
			m = new Texture(isMask);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				isTex.close();
				isMask.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isTex = isMask = null;
		}
	}

	@Override
	public boolean disabled(Renderer r) {
		return false;
	}

	int vaoId;
	int vboId;
	
	@Override
	public void draw(Renderer r, Coord offset, IComponent comp, Coord compLoc) {
		jglPushMatrix();
		Vector2f drawSrc = r.wtrCoord(compLoc, offset);
		jglTranslatef(drawSrc.x, drawSrc.y, 0);
		glActiveTexture(GL_TEXTURE0);
		t.bind();
		glActiveTexture(GL_TEXTURE0+1);
		m.bind();
		glActiveTexture(GL_TEXTURE0);
		Colour c;
		if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
			c.apply();
		} else {
			jglColor4f(1f, 1f, 1f, 1f);
		}
		jglUseProgram(r.program);
		if (vaoId == 0) {
        	glEnableClientState(GL_VERTEX_ARRAY);
        	glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
			vboId = glGenBuffers();
			vaoId = glGenVertexArrays();
			
			float[] vaodata = new float[] {
				0, 0, 0, 0,
				0, 1, 0, r.wtrLen(1),
				1, 0, r.wtrLen(1), 0,
				
				1, 1, r.wtrLen(1), r.wtrLen(1),
				0, 1, 0, r.wtrLen(1),
				1, 0, r.wtrLen(1), 0
			};
			glBindVertexArray(vaoId);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
		
			glBufferData(GL_ARRAY_BUFFER, vaodata, GL_STATIC_DRAW);
			int v_ver = jglGetAttribLocation("v_ver");
			glEnableVertexAttribArray(v_ver);
			glVertexAttribPointer(v_ver, 2, GL_FLOAT, false, 4*Float.BYTES, 2*Float.BYTES);
		
			int v_tex = jglGetAttribLocation("v_tex");
			glEnableVertexAttribArray(v_tex);
			glVertexAttribPointer(v_tex, 2, GL_FLOAT, false, 4*Float.BYTES, 0);
			glBindVertexArray(0);
		}
		
		glBindVertexArray(vaoId);
    	glDrawArrays(GL_TRIANGLES, 0, 6);
    	glBindVertexArray(0);
    	jglUseProgram(0);
    	jglPopMatrix();
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
