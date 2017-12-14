package org.jmt.starfort.logging;

@FunctionalInterface
public interface ILogOutput {

	public void logLine(LogLevel l, String message);
}
