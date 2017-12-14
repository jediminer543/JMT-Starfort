package org.jmt.starfort.logging;

import java.util.ArrayList;
import java.util.Date;

public class Logger {
	
	static ArrayList<ILogOutput> logOutputs = new ArrayList<>();
	
	public static void addLogOutput(ILogOutput out) {
		logOutputs.add(out);
	}
	
	public static void removeLogOutput(ILogOutput out) {
		logOutputs.remove(out);
	}
	
	protected static void logAll(LogLevel l, String s) {
		for (ILogOutput lo:logOutputs) {
			lo.logLine(l, s);
		}
	}
	
	public static void log(LogLevel l, String message, String source) {
		logAll(l, String.format("[%TT][%s][%s] %s", new Date(), l, source, message));
	}
	
	public static void info(String message) {
		log(LogLevel.Info, message, Thread.currentThread().getName());
	}
	
	public static void info(String message, String source) {
		log(LogLevel.Info, message, source);
	}
	
	public static void debug(String message) {
		log(LogLevel.Debug, message, Thread.currentThread().getName());
	}
	
	public static void debug(String message, String source) {
		log(LogLevel.Debug, message, source);
	}
	
	public static void trace(String message) {
		log(LogLevel.Trace, message, Thread.currentThread().getName());
	}
	
	public static void trace(String message, String source) {
		log(LogLevel.Trace, message, source);
	}
	
	public static void warn(String message) {
		log(LogLevel.Warn, message, Thread.currentThread().getName());
	}
	
	public static void warn(String message, String source) {
		log(LogLevel.Warn, message, source);
	}
	
	public static void error(String message) {
		log(LogLevel.Error, message, Thread.currentThread().getName());
	}
	
	public static void error(String message, String source) {
		log(LogLevel.Error, message, source);
	}
	
	public static void fatal(String message) {
		log(LogLevel.Fatal, message, Thread.currentThread().getName());
	}
	
	public static void fatal(String message, String source) {
		log(LogLevel.Fatal, message, source);
	}
}
