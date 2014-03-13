package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
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

    @Override
    public List<File> getFiles(File directory) {
        statusError();
        if (directory == null) {
            return null;
        }

        if (!validDirectory(directory)) {
            return null;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        } else {
            statusSuccess();
            return Arrays.asList(files);
        }
    }

    @Override
    public String getStringForFiles(List<File> files) {
        if (files == null) {
            return null;
        }

        if (files.isEmpty()) {
            return System.lineSeparator();
        }

        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            if (Thread.interrupted()) {
                statusSuccess();
                return sb.toString();
            }

            sb.append(String.format("%s%n", f.getName()));
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

    private boolean validDirectory(File candidate) {
        /**
         * Checks for the following conditions:
         * - File does not exist.
         * - Not a directory.
         * - Directory not readable.
         * - File Exception (returns `false').
         */

        try {
            if (!candidate.exists()) {
                return false;
            }

            if (!candidate.isDirectory()) {
                return false;
            }

            if (!candidate.canRead()) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    /**
     * Executes the tool with args provided in the constructor
     *
     * @param workingDir
     * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
    @Override
    public String execute(File workingDir, String stdin) {
        String result;
        if (this.args.length < 1) {
            result = listDirectory(workingDir);
            return ((result != null) ? result : "");
        } else {
            File specified = workingDir.toPath().resolve(this.args[0]).toFile();
            result = listDirectory(specified);
        }
        return ((result != null) ? result : "");
    }
}
