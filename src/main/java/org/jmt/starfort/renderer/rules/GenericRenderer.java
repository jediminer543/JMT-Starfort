package org.jmt.starfort.renderer.rules;

import static org.jmt.starfort.renderer.JMTGl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glClientActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jmt.starfort.renderer.Colour;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.renderer.Texture;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;
import org.joml.Vector2f;

public class GenericRenderer implements IRendererRule {

	Class<? extends IComponent>[] toRender;
	InputStream is;
	int priority;
	
	public GenericRenderer(Class<? extends IComponent>[] compClass, InputStream tex, int priority) {
		toRender = compClass;
		is = tex;
		this.priority = priority;
	}

	public GenericRenderer(Class<? extends IComponent>[] compClass, String fileName, int priority, boolean ext) throws FileNotFoundException {
		this(compClass, ext ? new FileInputStream(fileName) : fileName.getClass().getResourceAsStream(fileName), priority);
	}

	@Override
	public Class<? extends IComponent>[] getRenderableComponents() {
		return toRender;
	}

	Texture t;
	
	@Override
	public void init(Renderer r) {
		try {
			t = new Texture(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			is = null;
		}
	}
	
	int vaoId;
	int vboId;
	
	/* GL11 Impl
	glBegin(GL_TRIANGLES);
	glTexCoord2f(0, 0);
	glVertex2f(0, 0);
	glTexCoord2f(0, 1);
	glVertex2f(0, r.wtrLen(1));
	glTexCoord2f(1, 0);
	glVertex2f(r.wtrLen(1), 0);
	
	glTexCoord2f(1, 1);
	glVertex2f(r.wtrLen(1), r.wtrLen(1));
	glTexCoord2f(0, 1);
	glVertex2f(0, r.wtrLen(1));
	glTexCoord2f(1, 0);
	glVertex2f(r.wtrLen(1), 0);
	glEnd();
	glPopMatrix();
	*/
	
	@Override
	public void draw(Renderer r, Coord offset, IComponent comp,
			Coord compLoc) {
		jglPushMatrix();
		Vector2f drawSrc = r.wtrCoord(compLoc, offset);
		jglTranslatef(drawSrc.x, drawSrc.y, 0);
		t.bind();
		Colour c;
		if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
			c.apply();
		} else {
			jglColor4f(1f, 1f, 1f, 1f);
		}
		jglUseProgram(r.program);
		if (vaoId == 0) {
			glClientActiveTexture(GL_TEXTURE0);
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

	@Override
	public boolean disabled(Renderer r) {
		return false;
	}
}