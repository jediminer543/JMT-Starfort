package org.jmt.starfort.logging;

public enum LogLevel {

	CME, //FOR DUMPING COMOD ERRORS, LIKLEY UNUSED
	Trace,
	Debug,
	Info, //Standard output for User usable diagnostics
	Warn,
	Error,
	Fatal; //Generally used immediately before program crashes
	
}
