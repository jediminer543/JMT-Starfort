package org.jmt.starfort.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class does stuff that needs to be done in line. I.e. during a return call.
 * <br>
 * There is probably a proper java way to do some of this, but this is here for those who can't remember.
 * <br>
 * Or can't be bothered to fight with java to get it to work.
 * 
 * @author Jediminer543
 *
 */
public class InlineFunctions {

	/**
	 * Creates an inline array, thus: <br>
	 * <code> T[] = inlineArray(FOO, BAR, ALICE, BOB); </code><br>
	 * Is the equvelent of doing:<br>
	 * <code> T[] = new T[] {FOO, BAR, ALICE, BOB}; </code><br>
	 * 
	 * @param args The elements to put into the array
	 * @return The array itself
	 */
	@SafeVarargs
	public static <T> T[] inlineArray(T... args) {
		return args;
	}
	
	/**
	 * Creates an array list inline that is fully manipulatable
	 * 
	 * Unlike Arrays.asList(). Hintidy hint hint
	 * 
	 * @param args
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> inlineArrayList(T... args) {
		List<T> out = new ArrayList<T>();
		out.addAll(Arrays.asList(args));
		return out;
		//return Arrays.asList(args); Doesn't allow add operations
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
				throw new IllegalArgumentException("One of the arrays you provided was defective", e);
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
