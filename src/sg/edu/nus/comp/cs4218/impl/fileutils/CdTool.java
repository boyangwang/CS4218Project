package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.File;
import java.io.IOException;

/**
 * Changes CWD.
 *
 * Usage: cd directory
 * Extra parameters are ignored.
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

    /**
     * Checks that the new directory is accessible and switchable.
     *
     * Pre-condition: directory != null
     *
     * @param candidate Candidate directory to switch to.
     * @return `true' iff the candidate directory can be switched to.
     */
    private boolean canChangeDirectoryTo(File candidate) {
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
     * Changes current working directory.
     *
     * @param newDirectory Directory to change to.
     * @return The new directory, or `null' if unsuccessful.
     */
    @Override
    public File changeDirectory(String newDirectory) {
        if (newDirectory == null) {
            return null;
        }

        String candidatePath;
        try {
            candidatePath = String.format("%s%s%s", parent.getWorkingDirectory().getCanonicalPath(), File.separator, newDirectory);
        } catch (IOException e) {
            statusError();
            return null;
        }

        File dir = new File(candidatePath);
        if (canChangeDirectoryTo(dir)) {
            return dir;
        }

        return null;
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
        if (this.args.length < 1) {
            statusError();
            return "";
        }

        if (parent == null) {
            statusError();
            return "";
        }

        File candidateDir = changeDirectory(this.args[0]);

        if (candidateDir == null) {
            statusError();
            return "";
        }

        parent.changeWorkingDirectory(candidateDir);

        statusSuccess();
        return "";
    }
}
