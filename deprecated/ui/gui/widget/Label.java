package org.jmt.starfort.ui.gui.widget;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;

import java.util.HashMap;
import java.util.Map;

import org.jmt.starfort.ui.gui.util.GUIColour;
import org.jmt.starfort.ui.gui.util.GUIText;

public class Label extends Widget implements IWidgetBordered,IWidgetBackgrounded, IWidgetColoured {

	GUIText text;
	boolean border = false, background = false;
	Map<String, GUIColour> colours = new HashMap<>();
	
	public Label(String text, String font, float xPos, float yPos) {
		this.text = new GUIText(font, text).setAlign(NVG_ALIGN_TOP | NVG_ALIGN_LEFT);
		this.setPos(xPos, yPos);
	}
	
	@Override
	public GUIColour getColour(String colourID) {
		return colours.get(colourID);
	}

	@Override
	public void setColour(String colourID, GUIColour colour) {
		colours.put(colourID, colour);
	}

	@Override
	public void setBackgroundColour(GUIColour c) {
		setColour("background", c);
	}

	@Override
	public GUIColour getBackgroundColour() {
		return getColour("background");
	}

	@Override
	public void setBackgroundShow(boolean show) {
		background = show;
	}

	@Override
	public boolean getBackgroundShow() {
		return background;
	}
	
	@Override
	public void setBorderColour(GUIColour c) {
		setColour("border", c);
	}

	@Override
	public GUIColour getBorderColour() {
		return getColour("border");
	}

	@Override
	public void setBorderShow(boolean show) {
		border = show;
	}

	@Override
	public boolean getBorderShow() {
		return border;
	}

	@Override
	public void draw(long ctx) {
		nvgSave(ctx);
		float[] bounds = text.getBounds(ctx, x, y);
		width = (int) Math.ceil(bounds[2] - bounds[0]);
		height = (int) Math.ceil(bounds[3] - bounds[1]);
		nvgBeginPath(ctx);
		if (background || border) {
			nvgRect(ctx, x, y, width, height);
			if (background) {
				nvgFillColor(ctx, getBackgroundColour().getNativeColour());
				nvgFill(ctx);
			}
			if (border) {
				nvgStrokeColor(ctx, getBackgroundColour().getNativeColour());
				nvgStroke(ctx);
			}
		}
		if (colours.containsKey("text")); {
			nvgFillColor(ctx, getColour("text").getNativeColour());
		}
		text.drawText(ctx, x, y);
		
	}

}
