package sg.edu.nus.comp.cs4218.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * Do not modify this file
 */
/**
 * report present working directory
 */
public interface IPwdTool extends ITool {
	String getStringForDirectory(File directory);
}
