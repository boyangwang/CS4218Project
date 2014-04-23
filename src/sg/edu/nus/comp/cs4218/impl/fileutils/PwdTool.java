package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;

public class PwdTool extends ATool implements IPwdTool {
	/**
	 * Constructor
	 *
	 * @param arguments Arguments the tool is going to be executed with.
	 */
	public PwdTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String getStringForDirectory(File directory) {
		//Error Handling
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			statusError();
			return null;
		}

		String canonicalPath;
		try {
			canonicalPath = directory.getCanonicalPath();
		} catch (IOException e) {
			canonicalPath = null;
			statusError();
		}

		statusSuccess();
		return canonicalPath;
	}

	/**
	 * Executes the tool with args provided in the constructor
	 *
	 * @param workingDir
	 * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
	 * @return Output on stdout
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		String result = getStringForDirectory(workingDir);
		if (result == null) {
			statusError();
			return String.format("Unable to get working directory.%n");
		}

		statusSuccess();
		return String.format("%s%n", result);
	}
}
