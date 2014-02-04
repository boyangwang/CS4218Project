package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.ReverseTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PwdTool;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * instance can be implemented in Java
 */
public class Shell implements IShell {
	/**
	 * TaskExecution Thread
	 */
	private static class TaskExecution implements Runnable{
		ITool _tool;
		File _cwd;
		String _stdin;
		IShell _shell;
		OutputStream _stdout;
		public TaskExecution(IShell shell, ITool tool, File cwd, String stdin, OutputStream stdout){
			_shell = shell;
			_tool = tool;
			_cwd = cwd;
			_stdin = stdin;
			_stdout = stdout;
		}		
		@Override
		public void run() {			
			handleOutput(_tool.execute(_shell, _cwd));
			System.out.print(_cwd.getAbsolutePath() + "> ");
			
		}
		
		public void handleOutput(String output) {
			if (_stdout instanceof PrintStream) {
				((PrintStream)_stdout).print(output);
			}
			else {
				try {
					_stdout.write(output.getBytes());
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
    /**
     * Code for instance stuff.
     */
    private File cwd = null;    
    public Shell() {
    	String userDir = System.getProperty("user.dir");
    	cwd = new File(userDir);
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
        Scanner sc = new Scanner(System.in);
		Thread runningThread = null;

        System.out.print(cwd.getAbsolutePath() + "> ");
        while (true) {
            String cmd;
            try {
                cmd = sc.nextLine().trim();
            } catch (NoSuchElementException ex) {
                // Terminate gracefully.
                break;
            }

            if (cmd.equals("ctrl-z")) {
                if (null != runningThread && runningThread.isAlive()) {
                    stop(runningThread);
                }
            } else {
                ITool tool = parse(cmd);
                if ((runningThread == null || !runningThread.isAlive()) && tool != null) {
                    runningThread = (Thread)execute(tool);
                } else {
                    System.out.print(cwd.getAbsolutePath() + "> ");
                }
            }
        }
    }

	@Override
	public ITool parse(String commandline) {
		return CommandParser.parse(commandline);
	}

	@Override
	public Runnable execute(ITool tool) {
		// TODO stdin, do piping
		Thread t;
        t = new Thread(new TaskExecution(this, tool, cwd, "", System.out));
		t.start();
		return t;
	}

	@Override
	public void stop(Runnable toolExecution) {
		if (toolExecution instanceof Thread){
			Thread t = (Thread) toolExecution;
			t.interrupt();
		}
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
    
    public File getWorkingDirectory() {
    	return this.cwd;
    }

    
    /**
     * Code for static stuff.
     */
	public static void main(String[] args){
        Logging.logger(System.out).setLevel(Logging.All);
		Shell sh = new Shell();
        sh.run();
	}
}
