package org.jmt.starfort.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class does stuff that needs to be done in line. I.e. during a return call.
 * <br>
 * There is probably a proper java way to do some of this, but this is here for those who can't remember.
 * 
 * @author Jediminer543
 *
 */
public class InlineFunctions {

	@SafeVarargs
	public static <T> T[] inlineArray(T... args) {
		return args;
	}
	
	/**
	 * Generates keys for inline map generation
	 * 
	 * @param key The key of the entry
	 * @param value The value of the entry
	 * @return The constructed entry
	 */
	public static <T, V> Entry<T, V> inlineKey(T key, V value) {
		return new InlineEntry<T, V>(key, value);
	}
	
	@SafeVarargs
	public static <T, V> Map<T, V> inlineMap(Entry<T, V>... args) {
		Map<T, V> map = new HashMap<T, V>();
		for (Entry<T, V> i : args) {
			map.put(i.getKey(), i.getValue());
		}
		return map;
	}
	
	/**
	 * 
	 * Used to build arrays
	 * @param args Arrays with 2 arguments
	 * @return
	 */
	@SafeVarargs
	public static <T> Map<T, T> inlineMap(T[] ... args) {
		Map<T, T> map = new HashMap<T, T>();
		for (T[] i : args) {
			try {
				map.put(i[0], i[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				//TODO logging
			}
		}
		return map;
	}
	
	public static int[] inlineIntArray(int... args) {
		return args;
	}
	
	public static long[] inlineLongArray(long... args) {
		return args;
	}
	
	static final class InlineEntry<T, V> implements Entry<T, V> {

		T Key;
		V Value;
		
		public InlineEntry(T key, V value) {
			Key = key;
			Value = value; 
		}
		
		@Override
		public T getKey() {
			return Key;
		}

		@Override
		public V getValue() {
			return Value;
		}

		@Override
		public V setValue(V value) {
			V OldValue = Value;
			Value = value;
			return OldValue;
		}
		
	}
}
