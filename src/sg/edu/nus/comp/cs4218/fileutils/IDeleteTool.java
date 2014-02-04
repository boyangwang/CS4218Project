package sg.edu.nus.comp.cs4218.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * Do not modify this file
 */
/**
 * delete a file
 */
public interface IDeleteTool extends ITool {
	boolean delete(File toDelete);
}
