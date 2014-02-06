package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

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
		ITool tool;
		Shell shell;
        String stdin;
		OutputStream stdout;

		public TaskExecution(Shell shell, ITool tool, String stdin, OutputStream stdout){
			this.shell = shell;
			this.tool = tool;
			this.stdin = stdin;
			this.stdout = stdout;
		}

		@Override
		public void run() {
			handleOutput(tool.execute(shell.getWorkingDirectory(), stdin));
            try {
                Shell.printPrompt(shell.getWorkingDirectory().getCanonicalPath());
            } catch (IOException e) {
                // TODO:
            }

        }
		
		public void handleOutput(String output) {
			if (stdout instanceof PrintStream) {
				((PrintStream) stdout).print(output);
			}
			else {
				try {
					stdout.write(output.getBytes());
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

        printPrompt();
        while (true) {
            String cmd;
            try {
                cmd = sc.nextLine().trim();
            } catch (NoSuchElementException ex) {
                // Terminate gracefully.
                break;
            }

            if (cmd.equals("ctrl-z")) {
                if (runningThread != null && runningThread.isAlive()) {
                    stop(runningThread);
                }
            } else {
                ITool tool = parse(cmd);

                // Report an invalid command immediately.
                if (tool == null) {
                    System.out.println("Invalid command.");
                    printPrompt();
                    continue;
                }

                // Block until previous command has finished execution.
                if (runningThread == null || !runningThread.isAlive()) {
                	runningThread = (Thread)execute(tool);
                }

            }
        }
    }

    private void printPrompt() {
        try {
            Shell.printPrompt(cwd.getCanonicalPath());
        } catch (IOException e) {
            // TODO:
        }
    }

    private static void printPrompt(String cwd) {
        System.out.print(cwd + "> ");
    }

	@Override
	public ITool parse(String commandline) {
		ITool tool = CommandParser.parse(commandline);
		return tool;
	}

	@Override
	public Runnable execute(ITool tool) {
		if (tool==null || !(tool instanceof ATool)){
			return null;
		}
        ((ATool)tool).setShell(this);
		Thread t;
        t = new Thread(new TaskExecution(this, tool, "", System.out));
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
        Logging.logger(System.out).setLevel(Logging.ALL);
		Shell sh = new Shell();
        sh.run();
	}
}
