package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;


public class PwdTool extends ATool implements IPwdTool{
	public PwdTool(String[] arguments, String stdin) {
		super(arguments, stdin);
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

    @Override
	public String execute(IShell shell, File workingDir) {
        // Note that we do not check for Thread.interrupted() here as there is no blocking operation.

        return getStringForDirectory(workingDir);
    }
}
