package sg.edu.nus.comp.cs4218.impl.extended1;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

/**
 * Created by boyang on 1/22/14.
 */
public class PipingTool extends ATool implements IPipingTool {
	
	public PipingTool(String[] arguments, String stdin) {
		super(arguments, stdin);
		// TODO Auto-generated constructor stub
	}
	
	private int statusCode;
	private String stdin;
	private IShell shell;
	
    /**
     * Pipe the stdout of *from* to stdin of *to*
     * 
     * 
     * @param from
     * @param to
     * @return The stdout of *to*
     * Currently the method is not in complete form
     * 
     * And it's not used by system execution
     */
	@Override
    public String pipe(ITool from, ITool to) {
    	String outputOfFrom = from.execute(shell, getWorkingDir(), from.getStdin());
    	
        return pipe(outputOfFrom, to);
    }

    @Override
    public String pipe(String stdout, ITool to) {
        String output = to.execute(shell, getWorkingDir(), stdout);
        
    	return output;
    }
    
    @Override
    public String execute(IShell shell, File workingDir, String stdin) {
    	this.stdin = stdin;
    
        return null;
    }
    
	@Override
	public String getStdin() {
		return stdin;
	}
        
    @Override
    public int getStatusCode() {
        return this.statusCode;
    }
    
    
    // Stub functions for the time being
    
    private ITool parseStub(String stdin) {
    	return null;
    }
    
    private Runnable executeStub(ITool tool) {
    	return null;
    }
    
    private File getWorkingDir() {
    	return null;
    }


}
