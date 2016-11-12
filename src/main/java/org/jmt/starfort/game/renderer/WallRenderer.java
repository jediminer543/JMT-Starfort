package org.jmt.starfort.game.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.renderer.IRendererRule;
import org.jmt.starfort.renderer.RenderUtil;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.renderer.Texture;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.joml.Vector2f;


public class WallRenderer implements IRendererRule {

	//int vboId, vaoId;
	Texture floored, nofloored;
	//float[] vertexArr;
	
	@Override
	public void init(Renderer r) {
		/* !!VAO ATTEMPT!! - BORKED 
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		vaoId = glGenVertexArrays();
		vboId = glGenBuffers();
		vertexArr = new float[] {
				0, 0,
				0, r.worldToRenderLengthConvert(1),
				r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1),
				
				0, 0,
				r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1),
				r.worldToRenderLengthConvert(1), 0
		};
		
		RenderUtil.singleBufCreate((2*6) + (2*6));
		RenderUtil.singleBufMap(vertexArr, 2, 2, 0);
		RenderUtil.singleBufMap(RenderUtil.getAtlasCoord(4, 4, 1, 3), 2, 2, 2);
		FloatBuffer data = RenderUtil.singleBufGen();
		
		glBindVertexArray(vaoId);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 2*4, 2);
		glTexCoordPointer(2, GL_FLOAT, 2*Float.BYTES, 0);
		
		
		//glBindBuffer(GL_ARRAY_BUFFER, vboId);
		//glBufferData(GL_ARRAY_BUFFER, RenderUtil.getAtlasCoord(4, 4, 1, 3), GL_STATIC_DRAW);
		//glTexCoordPointer(2, GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		*/
		try {
			floored = new Texture(getClass().getClassLoader().getResourceAsStream("org/jmt/starfort/texture/component/wall/WallFloor.png"));
			nofloored = new Texture(getClass().getClassLoader().getResourceAsStream("org/jmt/starfort/texture/component/wall/WallnoFloor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Nanovg image loaded to " + image);
	}

	@Override
	public void draw(Renderer r, Coord offset, IComponent comp, Coord compLoc) {
		Vector2f drawSrc = r.wtrCoord(compLoc, offset);
		glTranslatef(drawSrc.x, drawSrc.y, 0);
		glEnable(GL_TEXTURE_2D);
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		r.getMaterialColor(((IComponent) comp).getComponentMaterial()).apply();
		if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).contains(Direction.YDEC)) {
			floored.bind();
			
			if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC))) {
				
				/* !!VAO ATTEMPT!! - BORKED 
				// Bind to the VAO that has all the information about the quad vertices
				glBindVertexArray(vaoId);
				glEnableVertexAttribArray(0);
				
				glEnableClientState(GL_VERTEX_ARRAY);
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				
				glVertexAttribPointer(0, 2, GL_FLOAT, false, 2*4, 0);
				glTexCoordPointer(2, GL_FLOAT, 2*Float.BYTES, 2);
				
				// Draw the vertices
				glDrawArrays(GL_TRIANGLES, 0, 100000);
				 
				glDisableClientState(GL_TEXTURE_COORD_ARRAY);
				glDisableClientState(GL_VERTEX_ARRAY);
				
				// Put everything back to default (deselect)
				glDisableVertexAttribArray(0);
				glBindVertexArray(0);
				
				//glDrawArrays(GL_TRIANGLES, 0, 2);
				*/
				
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
				
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			}else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC, Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC, Direction.XINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);	
				glEnd();
			}
		} else {
			
			nofloored.bind();
			
			if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZINC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 1f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 1f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			}else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC, Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC, Direction.XINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.75f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.75f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(1f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(1f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC, Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.75f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.75f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.XINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZDEC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0f, 0f);
				glVertex2f(0, 0);
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0f, 0.25f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.25f, 0f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} else if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
					InlineFunctions.inlineArrayList(Direction.ZINC))) {
				glBegin(GL_TRIANGLES);
				glTexCoord2f(0.25f, 0.25f);
				glVertex2f(0, 0);
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);
				
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(r.wtrLen(1), r.wtrLen(1));
				glTexCoord2f(0.25f, 0.5f);
				glVertex2f(0, r.wtrLen(1));
				glTexCoord2f(0.5f, 0.25f);
				glVertex2f(r.wtrLen(1), 0);				
				glEnd();
			} 
		} 
		if (Arrays.asList(((ComponentWall) comp).getComponentBlockedDirs().get(NavContext.Physical)).containsAll(
				InlineFunctions.inlineArrayList(Direction.SELFFULL))){
			nofloored.bind();
			glBegin(GL_TRIANGLES);
			glTexCoord2f(0f, 0.75f);
			glVertex2f(0, 0);
			glTexCoord2f(0f, 1f);
			glVertex2f(0, r.wtrLen(1));
			glTexCoord2f(0.25f, 0.75f);
			glVertex2f(r.wtrLen(1), 0);
			
			glTexCoord2f(0.25f, 1f);
			glVertex2f(r.wtrLen(1), r.wtrLen(1));
			glTexCoord2f(0f, 1f);
			glVertex2f(0, r.wtrLen(1));
			glTexCoord2f(0.25f, 0.75f);
			glVertex2f(r.wtrLen(1), 0);	
			glEnd();
		}
		glTranslatef(-drawSrc.x, -drawSrc.y, 0);
	}
	
	/*
	 * OLD RENDERCODE
	 * 
	Vector2f drawSrc = r.worldToRenderSpatialConvert(compLoc, offset);
	if (Arrays.asList(((ComponentWall) comp).getBlockedDirs().get(NavContext.Physical)).contains(Direction.YDEC)) {
		nvgBeginPath(nvgCtx);
		nvgRect(nvgCtx, drawSrc.x, drawSrc.y, r.worldToRenderLengthConvert(1), r.worldToRenderLengthConvert(1));
		NVGColor color = NVGColor.create();
		NVGPaint paint = NVGPaint.create();
		nvgRGBA((byte) 255, (byte) 64, (byte) 0, (byte) 255, color);
		//nvgLinearGradient(nvgCtx, 0, 0, 16, 16, color, color, paint);
		nvgImagePattern(nvgCtx, 0, 0, 16, 16, 0, image, 0.5f, paint);
		nvgFillPaint(nvgCtx, paint);
		nvgFill(nvgCtx);
	}
	if (Arrays.asList(((ComponentWall) comp).getBlockedDirs().get(NavContext.Physical)).contains(Direction.XDEC)) {
		nvgBeginPath(nvgCtx);
		nvgMoveTo(nvgCtx, drawSrc.x, drawSrc.y);
		nvgLineTo(nvgCtx, drawSrc.x, drawSrc.y + r.worldToRenderLengthConvert(1));
		NVGColor color = NVGColor.create();
		nvgRGBA((byte) 128, (byte) 128, (byte) 0, (byte) 255, color);
		nvgStrokeColor(nvgCtx, color);
		nvgStroke(nvgCtx);
	}
	if (Arrays.asList(((ComponentWall) comp).getBlockedDirs().get(NavContext.Physical)).contains(Direction.XINC)) {
		nvgBeginPath(nvgCtx);
		nvgMoveTo(nvgCtx, drawSrc.x + r.worldToRenderLengthConvert(1)+1, drawSrc.y);
		nvgLineTo(nvgCtx, drawSrc.x + r.worldToRenderLengthConvert(1)+1, drawSrc.y + r.worldToRenderLengthConvert(1));
		NVGColor color = NVGColor.create();
		nvgRGBA((byte) 128, (byte) 128, (byte) 0, (byte) 255, color);
		nvgStrokeColor(nvgCtx, color);
		nvgStroke(nvgCtx);
	}
	
	if (Arrays.asList(((ComponentWall) comp).getBlockedDirs().get(NavContext.Physical)).contains(Direction.ZDEC)) {
		nvgBeginPath(nvgCtx);
		nvgMoveTo(nvgCtx, drawSrc.x, drawSrc.y + r.worldToRenderLengthConvert(1));
		nvgLineTo(nvgCtx, drawSrc.x + r.worldToRenderLengthConvert(1), drawSrc.y + r.worldToRenderLengthConvert(1));
		NVGColor color = NVGColor.create();
		nvgRGBA((byte) 128, (byte) 128, (byte) 0, (byte) 255, color);
		nvgStrokeColor(nvgCtx, color);
		nvgStroke(nvgCtx);
	}
	if (Arrays.asList(((ComponentWall) comp).getBlockedDirs().get(NavContext.Physical)).contains(Direction.ZINC)) {
		nvgBeginPath(nvgCtx);
		nvgMoveTo(nvgCtx, drawSrc.x, drawSrc.y);
		nvgLineTo(nvgCtx, drawSrc.x + r.worldToRenderLengthConvert(1), drawSrc.y);
		NVGColor color = NVGColor.create();
		nvgRGBA((byte) 128, (byte) 128, (byte) 0, (byte) 255, color);
		nvgStrokeColor(nvgCtx, color);
		nvgStroke(nvgCtx);
	}
	*/

	@Override
	public Class<? extends IComponent>[] getRenderableComponents() {
		return InlineFunctions.inlineArray(ComponentWall.class);
	}

	@Override
	public int getPriority() {
		return -10;
	}

}
