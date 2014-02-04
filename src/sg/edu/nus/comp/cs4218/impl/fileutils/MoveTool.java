package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

public class MoveTool extends ATool implements IMoveTool {
    private IShell shell;
    private File cwd;

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public MoveTool(String[] arguments) {
        super(arguments);
    }

    @Override
    public boolean move(File from, File to) {
        if (canMoveSource(from)) {
            String[] cpArgs = new String[2];
            cpArgs[0] = from.getAbsolutePath(); // Source.
            cpArgs[1] = to.getAbsolutePath(); // Destination.
            String[] rmArgs = new String[1];
            rmArgs[0] = from.getAbsolutePath();

            CopyTool cp = new CopyTool(cpArgs);
            cp.execute(cwd, "");
            if (cp.getStatusCode() != 0) {
                cleanup(to);

                statusError();
                return false;
            }

            DeleteTool rm = new DeleteTool(rmArgs);
            rm.execute(cwd, "");
            if (rm.getStatusCode() != 0) {
                statusError();
                return false;
            }

            statusSuccess();
            return true;
        }

        statusError();
        return false;
    }

    private boolean canMoveSource(File candidate) {
        try {
            if (!candidate.exists()) {
                return false;
            }

            if (!candidate.canRead()) {
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
     * Tries to remove a partially copied file in case of failure.
     *
     * @param trash The file to remove.
     */
    private void cleanup(File trash) {
        if (trash.exists() && trash.canWrite()) {
            trash.delete();
        }
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
        this.shell = shell;
        this.cwd = workingDir;

        if (this.args.length != 2) {
            statusError();
            return "";
        }

        File from = new File(this.args[0]);
        File to = new File(this.args[1]);
        if (move(from, to)) {
            return "";
        } else {
            return "Could not move file: " + this.args[0] + " to: " + this.args[1];
        }
    }
}
