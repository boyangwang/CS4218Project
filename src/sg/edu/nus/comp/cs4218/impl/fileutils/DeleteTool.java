package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

/**
 * Deletes a specified file/directory, or list of files/directories.
 *
 * Note: Treats files and directories the same way. There is no need to specify -rf.
 */
public class DeleteTool extends ATool implements IDeleteTool {

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public DeleteTool(String[] arguments) {
        super(arguments);
    }

    /**
     * Deletes the specified file.
     *
     * @param toDelete The file to delete.
     * @return `true' iff the file was deleted successfully.
     */
    @Override
    public boolean delete(File toDelete) {
        statusError();

        if (canDeleteFile(toDelete)) {
            if (toDelete.isDirectory()) {
                File[] files = toDelete.listFiles();
                if (files == null) {
                    return false;
                }

                for (File f : files){
                    boolean result = f.delete();
                    if (!result) {
                        return false;
                    }
                }
            }

            if (toDelete.delete()) {
                statusSuccess();
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a file can be deleted.
     *
     * @param candidate The file to check.
     * @return `true' iff the file can be deleted without errors.
     */
    private boolean canDeleteFile(File candidate) {
        try {
            if (!candidate.exists()) {
                return false;
            }

            if (!candidate.canWrite()) {
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
        if (this.args.length < 1) {
            statusError();
            return "Invalid arguments.";
        }

        for (int i = 0; i < this.args.length; i++) {
            // Check for program termination.
            // Remaining files at this point are not deleted.
            if (Thread.interrupted()) {
                statusSuccess();
                return "";
            }

            File f = workingDir.toPath().resolve(this.args[i]).toFile();
            boolean result = delete(f);

            // Stop processing the list on an error.
            // Remaining files in the list are not deleted.
            if (!result) {
                statusError();
                return String.format("Could not delete file: %s%n", this.args[i]);
            }
        }

        statusSuccess();
        return "";
    }
}
