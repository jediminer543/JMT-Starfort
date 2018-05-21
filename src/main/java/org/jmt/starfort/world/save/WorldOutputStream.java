package org.jmt.starfort.world.save;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class WorldOutputStream extends ObjectOutputStream {

	public WorldOutputStream(OutputStream out) throws IOException {
		super(out);
	}

}
