package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (this.args.length < 1) {
            return listDirectory(workingDir);
        } else {
            File specified = new File(this.args[0]);
            return listDirectory(specified);
        }
    }

    @Override
    public List<File> getFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<File>(); // Return empty array.
        } else {
            return Arrays.asList(files);
        }
    }

    @Override
    public String getStringForFiles(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            sb.append(String.format("%s\n", f.getName()));
        }
        return sb.toString();
    }

    private String listDirectory(File wd) {
        if (wd == null) {
            return null;
        }

        if (!validDirectory(wd)) {
            return null;
        }

        return getStringForFiles(getFiles(wd));
    }

    private boolean validDirectory(File directory) {
        // TODO: check whether given directory is valid.
        return false;
    }

    private void statusError() {
        this.setStatusCode(1);
    }

    private void statusSuccess() {
        this.setStatusCode(0);
    }
}
