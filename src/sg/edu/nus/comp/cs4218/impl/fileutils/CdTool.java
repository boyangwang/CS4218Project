package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

/**
 * Created by cgcai on 22/1/14.
 */
public class CdTool extends ATool implements ICdTool {

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public CdTool(String[] arguments) {
        super(arguments);
    }

    private boolean isValidDirectory(File file) {
        return false;
    }

    @Override
    public File changeDirectory(String newDirectory) {
        return null;
    }

    @Override
    public String execute(File workingDir, String stdin) {
        return null;
    }
}
