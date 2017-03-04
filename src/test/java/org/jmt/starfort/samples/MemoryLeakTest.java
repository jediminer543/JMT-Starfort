package org.jmt.starfort.samples;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class MemoryLeakTest {

	public static void main(String[] args) {
		boolean testMemStack = true;
		while (testMemStack) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				//stack.calloc(128);
				FloatBuffer buf = stack.floats(5,2,9,3,25);
				//stack.ASCII("TESTING TESTING TESTING 123");
				//MemoryUtil.memFree(buf);
			}
		}
		while (!testMemStack) {
			FloatBuffer buf = BufferUtils.createFloatBuffer(5);
			buf.put(new float[] {5,2,9,3,25});
		}
	}
}
