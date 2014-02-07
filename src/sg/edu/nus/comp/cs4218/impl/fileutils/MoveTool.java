package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        statusError();

        if (from == null || to == null) {
            return false;
        }

        if (!from.canRead()) {
            return false;
        }

        if (!to.canWrite()) {
            return false;
        }

        if (from.isDirectory()) {
            return false;
        }

        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            statusSuccess();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

//    private boolean validSource(File candidate) {
//        try {
//            if (!candidate.exists()) {
//                return false;
//            }
//
//            if (!candidate.canRead()) {
//                return false;
//            }
//
//            if (!candidate.canWrite()) {
//                return false;
//            }
//        } catch (Exception ex) {
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean validDest(File candidate) {
//        try {
//            if (!candidate.canWrite()) {
//                return false;
//            }
//
//            if (candidate.isDirectory()) {
//                return false;
//            }
//        } catch (Exception ex) {
//            return false;
//        }
//
//        return true;
//    }

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
            return String.format("Could not move file: %s to: %s%n", this.args[0], this.args[1]);
        }
    }
}
