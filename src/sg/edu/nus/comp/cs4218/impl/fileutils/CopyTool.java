package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;

public class CopyTool extends ATool implements ICopyTool {
    private final int BUF_SIZE = 16384;

    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public CopyTool(String[] arguments) {
        super(arguments);
    }

    /**
     * Copies a file.
     *
     * @param from The file to copy.
     * @param to The destination file.
     * @return `true' iff the file was successfully copied.
     */
    @Override
    public boolean copy(File from, File to) {
        statusError();

        if (from == null || to == null) {
            return false;
        }

        if (!to.canWrite()) {
            return false;
        }

        try {
            Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
            statusSuccess();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if the specified File is a valid source.
     *
     * @param candidate The File to check.
     * @return `true' iff the File is a valid source.
     */
    private boolean isValidSource(File candidate) {
        try {
            if (!candidate.exists()) {
                return false;
            }

            if (!candidate.isFile()) {
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
        if (this.args.length != 2) {
            statusError();
            return "";
        }

        File to = new File(this.args[0]);
        File from = new File(this.args[1]);
        boolean result = copy(to, from);
        if (!result) {
            statusError();
            return "Could not copy file: " + this.args[0] + " to: " + this.args[1];
        }

        statusSuccess();
        return "";
    }
}
