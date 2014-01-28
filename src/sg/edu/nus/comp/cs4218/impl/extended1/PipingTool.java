package sg.edu.nus.comp.cs4218.impl.extended1;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;

import java.io.File;

/**
 * Created by boyang on 1/22/14.
 */
public class PipingTool implements IPipingTool {
	private int statusCode = 0;
	private File workingDir = null;
	private String stdin = "";
	
    /**
     * Pipe the stdout of *from* to stdin of *to*
     *
     * @param from
     * @param to
     * @return The stdout of *to*
     */
    @Override
    public String pipe(ITool from, ITool to) {
    	
        return null;
    }

    @Override
    public String pipe(String stdout, ITool to) {
        String output = to.execute(this.workingDir, stdout);
    	return output;
    }

    @Override
    public String execute(File workingDir, String stdin) {
    	
        return null;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }
}
