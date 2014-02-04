package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;


public class PwdTool extends ATool implements IPwdTool{
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
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}
		return directory.getAbsolutePath();
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
        return getStringForDirectory(workingDir);
    }
}
