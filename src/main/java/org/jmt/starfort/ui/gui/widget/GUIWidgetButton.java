package org.jmt.starfort.ui.gui.widget;

import java.util.ArrayList;

import org.jmt.starfort.renderer.JMTGl;
import org.jmt.starfort.ui.UIEvent;
import org.jmt.starfort.ui.gui.IGUIWidget;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class GUIWidgetButton implements IGUIWidget {

	IGUIWidget parent;
	ArrayList<IGUIWidget> children = new ArrayList<>();
	public Vector2f topLeft, bottomRight = null;
	
	public GUIWidgetButton(IGUIWidget parent) {
		this.parent = parent;
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

	@Override
	public void drawWidget() {
		GL11.glPushMatrix();
		JMTGl.jglPushMatrix();
		GL11.glTranslatef(topLeft.x, topLeft.y, 0);
		JMTGl.jglTranslatef(topLeft.x, topLeft.y, 0);
		for (IGUIWidget child : children) {
			child.drawWidget();
		}
		GL11.glPopMatrix();
		JMTGl.jglPopMatrix();
	}

	@Override
	public void handleEvent(UIEvent gev) {
		// TODO Auto-generated method stub
		
	}

}
