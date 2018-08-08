package org.jmt.starfort.ui.gui.util;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Provides the config for text
 * <br><br>
 * All values are set to the defaults from nvgReset when class is initialised
 * <br><br>
 * Setters return instance for fluent interface; so that text can be configured
 * with only a font and text (there isn't a default for font or text)
 * 
 * @author Jediminer543
 *
 */
public class GUIText {

	String font, text;
	
	float size = 16.0f, blur = 0.0f, spacing = 0.0f, lineHeight = 1.0f;
	
	int align = NVG_ALIGN_LEFT | NVG_ALIGN_BASELINE;
	
	public GUIText(String font, String text) {
		this.font = font;
		this.text = text;
	}
	
	private void modContext(long ctx) {
		nvgFontFace(ctx, font);
		nvgFontSize(ctx, size);
		nvgFontBlur(ctx, blur);
		nvgTextLetterSpacing(ctx, spacing);
		nvgTextLineHeight(ctx, lineHeight);
		nvgTextAlign(ctx, align);
	}
	
	public void drawText(long ctx, float x, float y) {
		nvgSave(ctx);
		modContext(ctx);
		nvgText(ctx, x, y, text, 0);
		nvgRestore(ctx);
	}
	
	public void drawTextBox(long ctx, float x, float y, float breakRowWidth) {
		nvgSave(ctx);
		modContext(ctx);
		nvgTextBox(ctx, x, y, breakRowWidth, text, 0);
		nvgRestore(ctx);
	}
	
	/**
	 * Gets the text bounds
	 * 
	 * See docs for nvgTextBounds ("The bounds value are [xmin,ymin, xmax,ymax]")
	 * 
	 * @param ctx The context to test against
	 * @return
	 */
	public float[] getBounds(long ctx, float x, float y) {
		nvgSave(ctx);
		modContext(ctx);
		FloatBuffer buf = BufferUtils.createFloatBuffer(4);
		nvgTextBounds(ctx, x, y, text, 0, buf);
		nvgRestore(ctx);
		return buf.array();
	}
	
	public String getFont() {
		return font;
	}

	public String getText() {
		return text;
	}

	public float getSize() {
		return size;
	}

	public float getBlur() {
		return blur;
	}

	public float getSpacing() {
		return spacing;
	}

	public float getLineHeight() {
		return lineHeight;
	}

	public int getAlign() {
		return align;
	}

	public GUIText setFont(String font) {
		this.font = font;
		return this;
	}
	
	public GUIText setText(String text) {
		this.text = text;
		return this;
	}
	
	public GUIText setSize(float size) {
		this.size = size;
		return this;
	}
	
	public GUIText setBlur(float blur) {
		this.blur = blur;
		return this;
	}
	
	public GUIText setSpacing(float spacing) {
		this.spacing = spacing;
		return this;
	}
	
	public GUIText setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}
	
	public GUIText setAlign(int align) {
		this.align = align;
		return this;
	}
}
