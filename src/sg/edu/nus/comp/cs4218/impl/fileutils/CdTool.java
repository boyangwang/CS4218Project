package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Changes CWD.
 *
 * Usage: cd directory
 * Extra parameters are ignored.
 */
public class CdTool extends ATool implements ICdTool {
    File workingDir;

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public CdTool(String[] arguments) {
        super(arguments);
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

        try {
            // Usually bad practice to use reflection in this manner. However,
            // the other viable alternative would be to pass a reference to
            // the shell to the tool in order to be able to set the shell's
            // working directory, which is arguably an even worse practice,
            // as it breaks encapsulation, and would be more incompatible as it
            // requires the addition and usage of our own private api.
            Field field = workingDir.getClass().getDeclaredField("path");
            field.setAccessible(true);
            field.set(workingDir, candidateDir.getPath());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            statusError();
            return "";
        }

        statusSuccess();
        return "";
    }
}
