package sg.edu.nus.comp.cs4218.impl.extended1;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;

import java.io.File;

/**
 * Created by boyang on 1/22/14.
 */
public class PipingTool implements IPipingTool {
	public static final String EMPTY_STDIN = "";
	
	private int statusCode = 0;
	private String stdin = "";
	
	private IShell shell = null;
	// Should be able to get this from shell
	private File workingDir = null;
	
    /**
     * Pipe the stdout of *from* to stdin of *to*
     * 
     * 
     * @param from
     * @param to
     * @return The stdout of *to*
     */
	
	public PipingTool(IShell shell) {
		this.shell = shell;
	}
	
    @Override
    public String pipe(ITool from, ITool to) {
    	String outputOfFrom = from.execute(getWorkingDir(), PipingTool.EMPTY_STDIN);
    	
        return pipe(outputOfFrom, to);
    }

    @Override
    public String pipe(String stdout, ITool to) {
        String output = to.execute(getWorkingDir(), stdout);
        
    	return output;
    }

    private ITool parseStub(String stdin) {
    	return null;
    }
    
    private Runnable executeStub(ITool tool) {
    	return null;
    }
    
    private File getWorkingDir() {
    	return null;
    }
    
    @Override
    public String execute(File workingDir, String stdin) {
    	this.workingDir = workingDir;
    	this.stdin = stdin;
    	
    	
    	
        return null;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }
}
