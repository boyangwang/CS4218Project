package sg.edu.nus.comp.cs4218.fileutils;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * Do not modify this file
 */
/**
 * echo writes its arguments separated by blanks 
 * and terminated by a newline on the standard output
 */
public interface IEchoTool extends ITool {
	String echo(String toEcho);
}
