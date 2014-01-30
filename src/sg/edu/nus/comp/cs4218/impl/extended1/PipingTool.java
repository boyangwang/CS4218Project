package sg.edu.nus.comp.cs4218.impl.extended1;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * Created by boyang on 1/22/14.
 */
public class PipingTool extends ATool implements IPipingTool {
	
	private IShell shell;
	private OutputStream pipeOutputStream;
	
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
    	String outputOfFrom = from.execute(shell, getWorkingDirStub(), from.getStdin());
    	
        return pipe(outputOfFrom, to);
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
        String output = to.execute(shell, getWorkingDirStub(), stdout);
        
    	return output;
    }
    
    /**
     * 
     */
    @Override
    public String execute(IShell shell, File workingDir, String stdin) {
    	this.stdin = stdin;
    
        return null;
    }
    
    public OutputStream getOutputStream() {
    	return pipeOutputStream;
    }
    
    /**
     * stdin getter
     */
	@Override
	public String getStdin() {
		return stdin;
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
