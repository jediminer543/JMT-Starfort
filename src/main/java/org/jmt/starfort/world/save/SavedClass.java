package org.jmt.starfort.world.save;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class SavedClass <T> {

	Class<T> clazz;
	Constructor<T> cons;
	ArrayList<Object> consArgs;
	
	Map<String, Object> attributes;
	
	public SavedClass() {}
	
	public SavedClass(Class<T> clazz, Constructor<T> cons, ArrayList<Object> conArgs, Map<String, Object> attributes) {
		this.clazz = clazz;
		this.cons = cons;
		this.consArgs = conArgs;
		
		this.attributes = attributes;
	}
	
	public T recreateComponent() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
		T output = cons.newInstance(consArgs);
		for (Entry<String, Object> field : attributes.entrySet()) {
			clazz.getField(field.getKey()).set(output, field.getValue());
		}
		return output;
	}
}
