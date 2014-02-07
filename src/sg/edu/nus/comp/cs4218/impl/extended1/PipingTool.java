package sg.edu.nus.comp.cs4218.impl.extended1;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Logging;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class PipingTool extends ATool implements IPipingTool {
	private Shell shell;
    private File pipeWorkingDirectory;
	public static final String ERROR_MSG_NULL_SHELL = "Shell internal error- Null shell reference";
	public static final String ERROR_MSG_NULL_CMD = "Shell internal error- Null cmd reference";
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public PipingTool(String[] arguments) {
        super(arguments);
    }

    /**
     * Pipe the stdout of *from* to stdin of *to*
     * Currently the method is not in complete form
     * And it's not used by system execution
     * 
     * @param from
     * @param to
     * @return The stdout of *to* 
     */
	@Override
    public String pipe(ITool from, ITool to) {
		String output = from.execute(shell.getWorkingDirectory(), "");

        return pipe(output, to);
    }

	/**
     * Pipe the stdout to stdin of *to*
     * 
     * @param stdout
     * @param to
     * @return The stdout of *to* 
     */
    @Override
    public String pipe(String stdout, ITool to) {
        String output = to.execute(shell.getWorkingDirectory(), stdout);
        
    	return output;
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
    	this.pipeWorkingDirectory = workingDir;
          	
    	ITool command;
    	String output = stdin;
    	
    	if (this.shell == null) {
    		statusError();
    		return ERROR_MSG_NULL_SHELL + System.lineSeparator();
    	}
    	for (int i=0; i<args.length; i++) {
            if (Thread.interrupted()) {
                statusSuccess();
                return output;
            }

    		command = this.shell.parse(args[i]);
    		
    		if (command == null) {
    			Logging.logger(System.out).writeLog(5, args[i]);
    			statusError();
    			return ERROR_MSG_NULL_CMD + System.lineSeparator();
    		}

    		((ATool)command).setShell(this.shell);
    		output = pipe(output, command);
    		
    		if (command.getStatusCode() != 0) {
    			statusError();
    			return output;
    		}
    	}
    	
    	statusSuccess();
    	return output;
    }
    
    public void setShell(Shell shell) {
    	this.shell = shell;
    }
}
