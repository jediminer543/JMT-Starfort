package org.jmt.starfort.world.save;

import java.lang.reflect.Constructor;
import java.util.Map;

public class SavedComponent <T> {

	
	Class<T> component;
	Constructor<T> cons;
	
	Map<String, Object> attributes;
}
