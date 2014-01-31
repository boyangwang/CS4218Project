package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

public class CatTool extends ATool implements ICatTool {
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     * @param stdin
     */
    public CatTool(String[] arguments, String stdin) {
        super(arguments, stdin);
    }

    /**
     * Executes the tool with args provided in the constructor
     *
     * @param shell
     * @param workingDir The current working directory.
     * @return Output on stdout
     */
    @Override
    public String execute(IShell shell, File workingDir) {
        if (Thread.interrupted()) {
            return null;
        }

        return null;
    }

    @Override
    public String getStringForFile(File toRead) {
        return null;
    }
}
