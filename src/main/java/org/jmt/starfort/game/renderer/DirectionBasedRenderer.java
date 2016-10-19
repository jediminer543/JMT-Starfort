package org.jmt.starfort.game.renderer;

import static org.lwjgl.opengl.GL11.*;

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
		glPushMatrix();
		Vector2f drawSrc = r.worldToRenderSpatialConvert(compLoc, offset);
		glTranslatef(drawSrc.x, drawSrc.y, 0);
		t.bind();
		Colour c;
		if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
			c.apply();
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
			//
		//}
		
		glPopMatrix();
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
