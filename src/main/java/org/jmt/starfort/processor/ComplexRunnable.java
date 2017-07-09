package org.jmt.starfort.processor;

import java.io.Serializable;

@FunctionalInterface
public interface ComplexRunnable extends Serializable {

	public void run(Object... args);
}
