package org.jmt.starfort.renderer;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class RenderUtil {

	public static FloatBuffer arrayToBuffer(float[] arr) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(arr.length);
		buffer.put(arr);
		//buffer.flip();
		return buffer;
	}
	
	/**
	 * Creates a float buffer for a VBO of a texture atlas
	 * 
	 * @param xSize the number of textures on the atlas width-wise
	 * @param ySize the number of textures on the atlas height-wise
	 * @param xPos the texture wanted, width-wise
	 * @param yPos the texture wanted, height-wise
	 * @return
	 */
	public static float[] getAtlasCoord(int xSize, int ySize, int xPos, int yPos) {
		float width = 1f/xSize;
		float height = 1f/ySize;
		float[] out = new float[] {
			width * xPos    , height * yPos  ,
			width * xPos    , height * (yPos+1),
			width * (xPos+1), height * (yPos+1),
			
			width * xPos    , height * yPos    ,
			width * (xPos+1), height * (yPos+1),
			width * (xPos+1), height * yPos    
		};
		System.out.println("item-width:" + width + "; item-height:" + height + "; " + Arrays.toString(out));
		return out;
	}
	
	/**
	 * Creates a float buffer for a VBO of a texture atlas
	 * 
	 * @param xSize the number of textures on the atlas width-wise
	 * @param ySize the number of textures on the atlas height-wise
	 * @param xPos the texture wanted, width-wise
	 * @param yPos the texture wanted, height-wise
	 * @return
	 */
	public static FloatBuffer getAtlasCoordBuf(int xSize, int ySize, int xPos, int yPos) {
		return arrayToBuffer(getAtlasCoord(xSize, ySize, xPos, yPos));
	}
	
	private static float[] singleBufferGen = null;
	
	/**
	 * Purge the buffer to begin generation of a new one
	 */
	public static void singleBufClean() {
		singleBufferGen = null;
	}
	
	public static void singleBufCreate(int size) {
		singleBufferGen = new float[size];
	}
	
	public static void singleBufMap(float[] arr, int size, int stride, int pointer) {
		//System.out.println(Arrays.toString(singleBufferGen));
		//System.out.println(Arrays.toString(arr));
		int currCount = 0;
		for (int i = 0; i < arr.length; i++) {
			//System.out.println(pointer);
			singleBufferGen[pointer] = arr[i];
			System.out.println(singleBufferGen[pointer] == arr[i]);
			pointer++;
			currCount++;
			if (currCount == size) {
				pointer += stride;
				currCount = 0;
			}
		}
	}
	
	public static FloatBuffer singleBufGen() {
		System.out.println(Arrays.toString(singleBufferGen));
		return arrayToBuffer(singleBufferGen);
	}
}
