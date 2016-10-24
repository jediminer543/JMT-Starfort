package org.jmt.starfort.game.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glClientActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.jmt.starfort.renderer.JMTGl.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;

import org.jmt.starfort.renderer.Colour;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.renderer.Texture;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.joml.Vector2f;

public class DirectionBasedRenderer implements IRendererRule {

	Class<? extends IComponent>[] comps;
	Map<List<Direction>, int[]> mapping;
	InputStream is;
	float atlasWidth, atlasHeight;
	boolean multiSelfull;
	int priority;
	
	Map<List<Direction>, Integer> vaoCache = new HashMap<List<Direction>, Integer>();
	
	static final boolean GL30 = true;
	
	public DirectionBasedRenderer(Class<? extends IComponent>[] comps, InputStream atlas, int atlasWidth, int atlasHeight, Map<Direction[], int[]> mapping, int priority, boolean multiSelfFull) {
		this.comps = comps;
		this.is = atlas;
		Map<List<Direction>, int[]> newMap = new HashMap<List<Direction>, int[]>();
		for (Entry<Direction[], int[]> e : mapping.entrySet()) {
			Arrays.sort(e.getKey());
			newMap.put(Arrays.asList(e.getKey()), e.getValue());
		}
		this.mapping = newMap;
		this.atlasWidth = atlasWidth;
		this.atlasHeight = atlasHeight;
		this.multiSelfull = multiSelfFull;
		this.priority = priority;
	}
	
	@Override
	public Class<? extends IComponent>[] getRenderableComponents() {
		return comps;
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

	@Override
	public void draw(Renderer r, Coord offset, IComponent comp, Coord compLoc) {
		if (!GL30) {
			glPushMatrix();
			Vector2f drawSrc = r.worldToRenderSpatialConvert(compLoc, offset);
			glTranslatef(drawSrc.x, drawSrc.y, 0);
			t.bind();
			Colour c;
			if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
				c.apply();
			} else {
				glColor4f(1f, 1f, 1f, 1f);
			}
			IComponentDirectioned dirComp = (IComponentDirectioned) comp;
			Arrays.sort(dirComp.getComponentDirections());
			int[] target = null;
			if ((target = mapping.get(Arrays.asList(dirComp.getComponentDirections()))) != null) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(target[0]/atlasWidth, target[1]/atlasHeight);
				glVertex2f(0, 0);
				glTexCoord2f(target[0]/atlasWidth, (target[1]+1)/atlasHeight);
				glVertex2f(0, r.worldToRenderLengthConvert(1));
				glTexCoord2f((target[0]+1)/atlasWidth, target[1]/atlasHeight);
				glVertex2f(r.worldToRenderLengthConvert(1), 0);
			
				glTexCoord2f((target[0]+1)/atlasWidth, (target[1]+1)/atlasHeight);
				glVertex2f(r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1));
				glTexCoord2f(target[0]/atlasWidth, (target[1]+1)/atlasHeight);
				glVertex2f(0, r.worldToRenderLengthConvert(1));
				glTexCoord2f((target[0]+1)/atlasWidth, target[1]/atlasHeight);
				glVertex2f(r.worldToRenderLengthConvert(1), 0);
				glEnd();
			
			
			} //else if (dirComp.getComponentDirections() && (target = mapping.get(dirComp.getComponentDirections())) != null) {
			glPopMatrix();
		} else {
			jglPushMatrix();
			Vector2f drawSrc = r.worldToRenderSpatialConvert(compLoc, offset);
			jglTranslatef(drawSrc.x, drawSrc.y, 0);
			t.bind();
			IComponentDirectioned dirComp = (IComponentDirectioned) comp;
			Arrays.sort(dirComp.getComponentDirections());
			Colour c;
			if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
				c.apply();
			} else {
				jglColor4f(1f, 1f, 1f, 1f);
			}
			jglUseProgram(r.program);
			int[] target = null;
			Integer vao = null;
			if ((target = mapping.get(Arrays.asList(dirComp.getComponentDirections()))) != null 
					&& (vao = vaoCache.get(Arrays.asList(dirComp.getComponentDirections()))) != null) {
				glBindVertexArray(vao);
	        	glDrawArrays(GL_TRIANGLES, 0, 6);
	        	glBindVertexArray(0);
			} else if (target != null) {
				glClientActiveTexture(GL_TEXTURE0);
		        glEnableClientState(GL_VERTEX_ARRAY);
		        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				
				int vboId = glGenBuffers();
				int vaoId = glGenVertexArrays();
				
				float[] vaodata = new float[] {
						target[0]/atlasWidth, target[1]/atlasHeight, 0, 0,
						target[0]/atlasWidth, (target[1]+1)/atlasHeight, 0, r.worldToRenderLengthConvert(1),
						(target[0]+1)/atlasWidth, target[1]/atlasHeight, r.worldToRenderLengthConvert(1), 0,
						
						(target[0]+1)/atlasWidth, (target[1]+1)/atlasHeight, r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1),
						target[0]/atlasWidth, (target[1]+1)/atlasHeight, 0, r.worldToRenderLengthConvert(1),
						(target[0]+1)/atlasWidth, target[1]/atlasHeight, r.worldToRenderLengthConvert(1), 0
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
				
				
				glBindVertexArray(vaoId);
	        	glDrawArrays(GL_TRIANGLES, 0, 6);
	        	glBindVertexArray(0);
				
				vaoCache.put(Arrays.asList(dirComp.getComponentDirections()), vaoId);
				System.out.println("VAO GENERATED");
			}
			jglUseProgram(0);
			jglPopMatrix();
		}
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
