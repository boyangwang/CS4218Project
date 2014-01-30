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
		if(directory==null || !directory.exists() || !directory.isDirectory()){
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}
		//Processing the 
		return directory.getAbsolutePath();
	}

	@Override
	public String execute(IShell shell, File workingDir, String stdin) {
		//TODO: check Thread.currentThread().interrupted() regularly and gracefully end;						
		return getStringForDirectory(workingDir);
	}

	@Override
	public String getStdin() {
		// TODO Auto-generated method stub
		return null;
	}
}