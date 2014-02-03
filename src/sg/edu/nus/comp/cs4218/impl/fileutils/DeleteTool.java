package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
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
     * @param stdin
     */
    public DeleteTool(String[] arguments, String stdin) {
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
        final String nothing = "";

        if (this.args.length < 1) {
            statusError();
            return nothing;
        }

        for (int i = 0; i < this.args.length; i++) {
            // Check for program termination.
            // Remaining files at this point are not deleted.
            if (Thread.interrupted()) {
                statusError();
                return "Interrupted!";
            }

            File f = new File(this.args[i]);
            boolean result = delete(f);

            // Stop processing the list on an error.
            // Remaining files in the list are not deleted.
            if (!result) {
                statusError();
                return "Could not delete file: " + this.args[i];
            }
        }

        statusSuccess();
        return nothing;
    }

    /**
     * Deletes the specified file.
     *
     * @param toDelete The file to delete.
     * @return `true' iff the file was deleted successfully.
     */
    @Override
    public boolean delete(File toDelete) {
        if (canDeleteFile(toDelete)) {
            toDelete.delete();
            return true;
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
}
