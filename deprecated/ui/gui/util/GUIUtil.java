package org.jmt.starfort.ui.gui.util;

import static org.lwjgl.nanovg.NanoVGGL2.NVG_ANTIALIAS;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.nanovg.NanoVGGL2;

public class GUIUtil {
	
	public static boolean between(double max, double min, double value) {
		return (value < min & value > max);
	}
	
	private static Map<Long, Boolean> tempContexts = new HashMap<Long, Boolean>();
	
	/**
	 * Gets a NVG context that is thread safe; used for functions like finding text box bounds
	 * 
	 * @return
	 */
	public static long getTempContext() { 
		for (Entry<Long, Boolean> e : tempContexts.entrySet()) {
			if (!e.getValue()) {
				e.setValue(true);
				return e.getKey();
			}
		}
		long ctx = NanoVGGL2.nvgCreateGL2(NVG_ANTIALIAS);
		tempContexts.put(ctx, true);
		return ctx;
	}
	
	public static void unlockTempContext(long ctx) {
		if (tempContexts.containsKey(ctx)) {
			tempContexts.put(ctx, false);
		}
	}
}
