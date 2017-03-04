package org.jmt.starfort.world.save;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jmt.starfort.world.World;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Saves and loads worlds
 * 
 * Uses kryo serialisation to a binary file;
 * 
 * Yes it's a cheap hack, but it's faster than writing
 * a serialisation template for each class. Reflection
 * exists for a reason...
 * 
 * @author Jediminer543
 *
 */
public class WorldSaver {

	public static Kryo kryo = new Kryo();
	
	public static void saveWorld(World w, OutputStream out) {
		kryo.setRegistrationRequired(false);
		Output output = new Output(out);
		//kryo.register(World.class);
		kryo.writeObject(output, w);
		output.close();
	}
	
	static Objenesis generator = new ObjenesisStd(true);
	
	public static World loadWorld(InputStream in) {
		//Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new InstantiatorStrategy() {
			
			@Override
			public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
				return generator.getInstantiatorOf(type);
			}
		});
		try (Input input = new Input(in)) {
			return kryo.readObject(input, World.class);
		}
	}
}
