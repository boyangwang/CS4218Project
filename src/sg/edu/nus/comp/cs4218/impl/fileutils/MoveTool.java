package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MoveTool extends ATool implements IMoveTool {

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

        if (to.exists() && !to.canWrite()) {
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

    /**
     * Executes the tool with args provided in the constructor
     *
     * @param workingDir
     * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
    @Override
    public String execute(File workingDir, String stdin) {
        if (this.args.length != 2) {
            statusError();
            return "";
        }

        File from = workingDir.toPath().resolve(this.args[0]).toFile();
        File to = workingDir.toPath().resolve(this.args[1]).toFile();
        if (move(from, to)) {
            return "";
        } else {
            return String.format("Could not move file: %s to: %s%n", this.args[0], this.args[1]);
        }
    }
}
