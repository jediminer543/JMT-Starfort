package org.jmt.starfort.ui.gui.widget;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;

import org.jmt.starfort.renderer.JMTGl;
import org.jmt.starfort.ui.UIEvent;
import org.jmt.starfort.ui.gui.IGUIWidget;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GUIWidgetPanel implements IGUIWidget {

	IGUIWidget parent;
	ArrayList<IGUIWidget> children = new ArrayList<>();
	Vector2f topLeft, bottomRight = null;
	
	public GUIWidgetPanel(IGUIWidget parent, Vector2f topLeft, Vector2f bottomRight) {
		this.parent = parent;
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	public GUIWidgetPanel(IGUIWidget parent, Vector2f topLeft, float width, float height) {
		this.parent = parent;
		this.topLeft = topLeft;
		this.bottomRight = new Vector2f(topLeft.x + width, topLeft.y + height);
	}
	
	@Override
	public IGUIWidget getWidgetPatent() {
		return parent;
	}

	@Override
	public void setWidgetPos(Vector3f newPos) {
		float width = topLeft.x - bottomRight.x;
		float height = topLeft.y - bottomRight.y;
		topLeft.x = newPos.x;
		topLeft.y = newPos.y;
		bottomRight.x = newPos.x + width ;
		bottomRight.y = newPos.y + height;
	}

	@Override
	public Vector3f getWidgetPos() {
		return new Vector3f(topLeft, 0f);
	}

	@Override
	public boolean select() {
		return false;
	}

	@Override
	public ArrayList<IGUIWidget> getWidgetChildren() {
		return children;
	}

	boolean GL30 = false;
	private int vaoId = 0, vboId = 0;
	
	private void drawSelf() {
		if (GL30) {
		if (vaoId != 0) {
			float width = topLeft.x - bottomRight.x;
			float height = topLeft.y - bottomRight.y;
			vboId = glGenBuffers();
			vaoId = glGenVertexArrays();
			
			float[] vaodata = new float[] {
					0, 0, 
					0, height,
					
					0, 0, 
					width, 0,
					
					width, 0, 
					width, height,
					
					0, height, 
					width, height
			};
			
			glBindVertexArray(vaoId);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			
			glBufferData(GL_ARRAY_BUFFER, vaodata, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 2*Float.BYTES, 0);
			
			glBindVertexArray(0);
		}
		glColor4f(1f, 1f, 1f, 1f);
		glBindVertexArray(vaoId);
    	glDrawArrays(GL_LINES, 0, 8);
    	glBindVertexArray(0);
		} else {
			float width = topLeft.x - bottomRight.x;
			float height = topLeft.y - bottomRight.y;
			glLineWidth(5f);
			
			glBegin(GL_LINES);
			glVertex2f(0, 0); 
			glVertex2f(0, -height);
			
			glVertex2f(0, 0);
			glVertex2f(-width, 0);
			
			glVertex2f(-width, 0) ;
			glVertex2f(-width, -height);
			
			glVertex2f(0, -height);
			glVertex2f(-width, -height);
			glEnd();
		}
	}
	
	@Override
	public void drawWidget() {
		glPushMatrix();
		JMTGl.jglPushMatrix();
		glTranslatef(topLeft.x, topLeft.y, 0);
		JMTGl.jglTranslatef(topLeft.x, topLeft.y, 0);
		drawSelf();
		for (IGUIWidget child : children) {
			child.drawWidget();
		}
		glPopMatrix();
		JMTGl.jglPopMatrix();
	}

	@Override
	public void handleEvent(UIEvent gev) {
		for (IGUIWidget child : children) {
			child.handleEvent(gev);
		}
	}

}
