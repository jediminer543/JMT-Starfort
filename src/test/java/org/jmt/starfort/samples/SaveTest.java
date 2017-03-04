package org.jmt.starfort.samples;

import java.util.ArrayList;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class SaveTest {

	public static void main(String[] args) {
		Objenesis generator = new ObjenesisStd(true);
		generator.getInstantiatorOf(ArrayList.class).newInstance();
	}
}
