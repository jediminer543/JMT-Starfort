package org.jmt.starfort.game.renderer;

import static org.lwjgl.opengl.GL11.*;

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
	
	public GenericRenderer(Class<? extends IComponent>[] compClass, InputStream tex) {
		toRender = compClass;
		is = tex;
	}

	public GenericRenderer(Class<? extends IComponent>[] compClass, String fileName, boolean ext) throws FileNotFoundException {
		this(compClass, ext ? new FileInputStream(fileName) : fileName.getClass().getResourceAsStream(fileName));
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
		
	@Override
	public void draw(Renderer r, Coord offset, IComponent comp,
			Coord compLoc) {
		glPushMatrix();
		Vector2f drawSrc = r.worldToRenderSpatialConvert(compLoc, offset);
		glTranslatef(drawSrc.x, drawSrc.y, 0);
		t.bind();
		Colour c;
		if (comp.getComponentMaterial() != null && (c = r.getMaterialColor(comp.getComponentMaterial())) != null) {
			c.apply();
		}
		glBegin(GL_TRIANGLES);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(0, 1);
		glVertex2f(0, r.worldToRenderLengthConvert(1));
		glTexCoord2f(1, 0);
		glVertex2f(r.worldToRenderLengthConvert(1), 0);
		
		glTexCoord2f(1, 1);
		glVertex2f(r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1));
		glTexCoord2f(0, 1);
		glVertex2f(0, r.worldToRenderLengthConvert(1));
		glTexCoord2f(1, 0);
		glVertex2f(r.worldToRenderLengthConvert(1), 0);
		glEnd();
		glPopMatrix();
	}
}