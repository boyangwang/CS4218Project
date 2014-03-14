package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.File;

/**
 * Changes CWD.
 *
 * Usage: cd directory
 * Extra parameters are ignored.
 */
public class CdTool extends ATool implements ICdTool {
    private File workingDir;
    private Shell shell;

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public CdTool(String[] arguments, Shell shell) {
        super(arguments);
        this.shell = shell;
        workingDir = new File(System.getProperty("user.dir"));
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
        if (newDirectory != null) {
            File dir = workingDir.toPath().resolve(newDirectory).toFile();
            if (canChangeDirectoryTo(dir)) {
                statusSuccess();
                return dir;
            }
        }

        statusError();
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
        this.workingDir = workingDir;

        if (this.args.length < 1) {
            statusError();
            return "";
        }

        File candidateDir = changeDirectory(this.args[0]);

        if (candidateDir == null) {
            statusError();
            return "";
        }

        shell.changeWorkingDirectory(candidateDir);

        statusSuccess();
        return "";
    }
}
