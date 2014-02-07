package sg.edu.nus.comp.cs4218.impl.extended1;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Logging;
import sg.edu.nus.comp.cs4218.impl.Shell;
import java.io.File;

/**
 * PipingTool pipes the output of previous command to the next command as input
 * If any of the command encounters error (i.e. exit with non-0 code), stop
 * piping and print generated output up to that point
 * 
 * If any of the command string given is not valid, it prints ERROR_MSG_NULL_CMD
 * 
 * If shell reference is not set, prints ERROR_MSG_NULL_SHELL
 * 
 * On interrupt (CTRL-Z), exit with code 0 and print generated output up to that
 * point
 */
public class PipingTool extends ATool implements IPipingTool {
	private Shell shell;
    private File pipeWorkingDirectory;
	public static final String ERROR_MSG_NULL_SHELL = "Shell internal error- Null shell reference";
	public static final String ERROR_MSG_NULL_CMD = "Shell internal error- Null cmd reference";
	
    /**
     * Constructor
     *
     * @param arguments arguments is the array of commands, connected by pipe operator and parsed by CommandParser
     */
    public PipingTool(String[] arguments) {
        super(arguments);
    }

    /**
     * Pipe the stdout of *from* to stdin of *to*
     * 
     * Currently the method is not used by system execution, as the other form, pipe(String, ITool),
     * is enough for piping
     * 
     * ASSUMPTION: stdin for the from tool is empty 
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
     * stdout becomes the stdin content of to, and the output of to is returned by the method
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
     * If any of the command encounters error (i.e. exit with non-0 code), stop piping and print generated output up to that point 
     * 
     * If any of the command string given is not valid, it prints ERROR_MSG_NULL_CMD
     * 
     * If shell reference is not set, prints ERROR_MSG_NULL_SHELL
     * 
     * On interrupt (CTRL-Z), exit with code 0 and print generated output up to that point
     *
     * @param workingDir
     * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
    @Override
    public String execute(File workingDir, String stdin) {    
    	if (this.shell == null) {
    		statusError();
    		return ERROR_MSG_NULL_SHELL + System.lineSeparator();
    	}
    	
    	this.pipeWorkingDirectory = workingDir;
    	ITool command;
    	String output = stdin;
    	
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
    
    /**
     * set the reference to a shell instance so that pipingTool has access to its public methods
     * @param shell the shell to be set
     */
    public void setShell(Shell shell) {
    	this.shell = shell;
    }
}
