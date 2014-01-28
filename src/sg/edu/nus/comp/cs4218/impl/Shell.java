package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.fileutils.PwdTool;

import java.io.File;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * instance can be implemented in Java
 */
public class Shell implements IShell {
    /**
     * Code for instance stuff.
     */
    private File cwd = null;

    private Shell() {
        // TODO: Initialise instance variables.
    }

    /**
     * Do Forever
     * 1. Wait for a user input
     * 2. Parse the user input. Separate the command and its arguments
     * 3. Create a new thread to execute the command
     * 4. Execute the command and its arguments on the newly created thread. Exit with the status code of the executed command
     * 5. In the instance, wait for the thread to complete execution
     * 6. Report the exit status of the command to the user
     */
    public void run() {

    }

	@Override
	public ITool parse(String commandline) {
		if(commandline.startsWith("pwd")){
			return new PwdTool();
		} else {
			//TODO Implement all other tools
			Logging.logger().writeLog(Logging.Error, "Cannot parse " + commandline);
			return null;
		}
	}

	@Override
	public Runnable execute(ITool tool) {
		// TODO Implement
		return null;
	}

	@Override
	public void stop(Runnable toolExecution) {
		//TODO Implement
	}

    /**
     * Direct access to working directory for cd tool.
     *
     * Pre-condition: newDirectory has been validated.
     *
     * @param newDirectory The new working directory.
     */
    public void changeWorkingDirectory(File newDirectory) {
        this.cwd = newDirectory;
    }

    /**
     * Code for static stuff.
     */
    private static Shell self = null;

	public static void main(String[] args){
        Logging.logger().setLevel(Logging.All);
		Shell.instance().run();
	}

    public static Shell instance() {
        while (self == null) {
            try {
                self = new Shell();
            } catch (Exception ex) {
                Logging.logger().writeLog(Logging.Fatal, "Could not instantiate shell.");
                System.exit(1);
            }
        }
        return self;
    }
}
