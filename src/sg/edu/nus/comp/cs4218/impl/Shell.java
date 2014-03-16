package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 
 * IMPORTANT METHOD COVERAGE NOTE: since many methods in this shell cannot be automated, 
 * we do not guarantee 100% method coverage for our shell. 
 * Those methods are main() and those which are supposed to be called by main.
 * 
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * instance can be implemented in Java
 * 
 */
public class Shell implements IShell {
	String LINE_SEPARATOR = System.lineSeparator();
	CommandParser cmdParser = new CommandParser();
    InputStream is;
    OutputStream os;
    PrintStream ps;

	/**
	 * TaskExecution Thread
	 */
	private static class TaskExecution implements Runnable{
		ITool tool;
		Shell shell;
        String stdin;
		OutputStream stdout;

		/**
		 * 
		 * @param shell
		 * @param tool the tool run by this executor
		 * @param stdin 
		 * @param stdout
		 */
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
                Shell.printPrompt(shell.getWorkingDirectory().getCanonicalPath(), (new PrintStream(stdout)));
            } catch (IOException e) {
                // TODO:
            }

        }
		/**
		 * print output stream to console
		 * @param output the text to print out to users
		 */
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
    public Shell(InputStream in, OutputStream out) {
        this.is = in;
        this.os = out;

        ps = new PrintStream(this.os);

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
        Scanner sc = new Scanner(is);
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

            if (cmd.equalsIgnoreCase("ctrl-z")) {
                if (runningThread != null && runningThread.isAlive()) {
                    stop(runningThread);
                    try {
						runningThread.join();
					} catch (InterruptedException e) {
                        // Nothing here.
					}
                }
            } else if(cmd.equals("ctrl-c")) {
                while (runningThread != null && runningThread.isAlive());
                break;
            } else {
                ITool tool = parse(cmd);

                // Report an invalid command immediately.
                if (tool == null) {
                    ps.println("Invalid command.");
                    printPrompt();
                    continue;
                }

                // Block until previous command has finished execution.
                while (runningThread != null && runningThread.isAlive());
                runningThread = (Thread)execute(tool);

            }
        }
        sc.close();
    }

	/**
	 * print the command prompt containing current working dir
	 */
    private void printPrompt() {
        try {
            Shell.printPrompt(cwd.getCanonicalPath(), this.ps);
        } catch (IOException e) {
            // TODO:
        }
    }

	/**
	 * print the command prompt to users
	 * @param cwd current working directory string
	 */
    private static void printPrompt(String cwd, PrintStream ps) {
        assert ps != null;
        ps.print(cwd + "> ");
    }

	@Override
	public ITool parse(String commandline) {
		return CommandParser.parse(commandline, this);
	}

	@Override
	public Runnable execute(ITool tool) {
		if (!(tool instanceof ATool)){
			return null;
		}
		String stdin = "";
		if(tool instanceof IGrepTool || tool instanceof ICatTool 
				|| tool instanceof IPasteTool || tool instanceof ICutTool 
				|| tool instanceof ISortTool || tool instanceof UniqTool 
				|| tool instanceof WcTool){

			ATool aTool = (ATool) tool;
			int argLength = aTool.args.length;
			boolean alreadyReadFromStdin = false;;
			for(int i=0;i<argLength;i++){
				if (aTool.args[i].equals("-") && !alreadyReadFromStdin){
					stdin = readFromUserInput();
					alreadyReadFromStdin = true;
				}
			}
		}

		Thread t;
        t = new Thread(new TaskExecution(this, tool, stdin, this.os));
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
		Shell sh = new Shell(System.in, System.out);
        sh.run();
	}

    /**
     * To read from user input system.in
     * until ctrl-d<newlinechar> is encountered
	 * @return string containing the text user inputs, excluding ctrl-d
     */
	private String readFromUserInput() {
    	@SuppressWarnings("resource")
		Scanner sc = new Scanner(this.is);
    	StringBuilder sb = new StringBuilder();
    	String str= sc.nextLine();
    	while (true){
    		if (str.toLowerCase().endsWith("ctrl-d")){
    			String realArg = str.substring(0,str.length()-"ctrl-d".length());
    			sb.append(realArg);
    			break;
    		}else{
    			sb.append(str + LINE_SEPARATOR );
    		}
    		str= sc.nextLine();
    	}
    	return sb.toString();
	}
}
