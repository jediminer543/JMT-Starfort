package org.jmt.starfort.renderer;

/**
 * TODO MAKE WORK
 * @author jediminer543
 *
 */
public class TextureSheet {

	Texture tex;
	int width, height;
	int texCount;
	
	public TextureSheet(Texture tex, int width, int height) {
		this.tex = tex;
		this.width = width;
		this.height = height;
		this.texCount = width*height;
	}
	
	public void bind()
	{
		tex.bind();
	}
	
	public int getTexCount() {
		return texCount;
	}
}
