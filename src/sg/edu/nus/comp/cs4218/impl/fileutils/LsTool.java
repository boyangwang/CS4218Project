package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
import java.util.List;

/**
 * Prints a directory listing.
 *
 * Usage: ls directory
 * Prints a listing of the specified directory. Extra parameters are ignored.
 *
 * Usage: ls
 * Prints a listing of the current working directory.
 */
public class LsTool extends ATool implements ILsTool {
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public LsTool(String[] arguments) {
        super(arguments);
    }

    /**
     * Executes the tool with args provided in the constructor
     *
     * @param workingDir The current working directory.
     * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
    @Override
    public String execute(File workingDir, String stdin) {
        return null;
    }

    @Override
    public List<File> getFiles(File directory) {
        return null;
    }

    @Override
    public String getStringForFiles(List<File> files) {
        return null;
    }
}
