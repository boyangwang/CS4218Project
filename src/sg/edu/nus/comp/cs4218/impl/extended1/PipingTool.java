package sg.edu.nus.comp.cs4218.impl.extended1;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class PipingTool extends ATool implements IPipingTool {
	
	private IShell shell;
	private OutputStream pipeOutputStream;
	private File pipeWorkingDirectory;
	
	public PipingTool(String[] arguments, String stdin) {
		super(arguments, stdin);	
		
		this.pipeOutputStream = new ByteArrayOutputStream();
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
		String output = from.execute(shell, shell.getWorkingDirectory());
		
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
    	to.setStdin(stdout);
        String output = to.execute(shell, shell.getWorkingDirectory());

    	return output;
    }
    
    /**
     * 
     */
    @Override
    public String execute(IShell shell, File workingDir) {
    	this.pipeWorkingDirectory = workingDir;
    	this.shell = shell;
    	
    	String[] commands = tokenizeStdinIntoCommands(this.args[0]);
        
    	ITool command;
    	String output = this.stdin;
    	
    	for (int i=0; i<commands.length; i++) {
    		command = shell.parse(commands[i]);
    		output = pipe(output, command);
    		if (command.getStatusCode() != 0) {
    			return output;
    		}
    	}
    	
    	return output;
    }
    
    private String[] tokenizeStdinIntoCommands(String stdin) {
    	String[] commands = stdin.split("\\|");
    	for (int i=0; i<commands.length; i++) {
    		commands[i] = commands[i].trim();
    	}
		return commands;
	}

	public OutputStream getOutputStream() {
    	return pipeOutputStream;
    }
    
    // Stub functions for the time being
    private ITool parseStub(String stdin) {
    	return null;
    }
    
    private Runnable executeStub(ITool tool) {
    	return null;
    }
    
    private File getWorkingDirStub() {
    	return null;
    }
}
