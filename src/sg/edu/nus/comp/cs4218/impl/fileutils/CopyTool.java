package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.*;

public class CopyTool extends ATool implements ICopyTool {
    private final static int BUF_SIZE = 16384;

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

        boolean canCopy = (isValidSource(from) && isValidDestination(to));
        if (!canCopy) {
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream(from);
            FileOutputStream fos = new FileOutputStream(to);

            byte[] buffer = new byte[BUF_SIZE];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }

            fis.close();
            fos.close();

            statusSuccess();
            return true;
        } catch (FileNotFoundException e) {
            cleanup(to);

            statusError();
            return false;
        } catch (IOException e) {
            cleanup(to);

            statusError();
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
     * Checks if the specified File represents a valid destination.
     *
     * @param candidate The file to check.
     * @return `true' iff the File is a valid destination.
     */
    private boolean isValidDestination(File candidate) {
        try {
            // Cannot overwrite a directory.
            if (candidate.exists() && candidate.isDirectory()) {
                return false;
            }

            // Cannot overwrite a readonly file.
            if (candidate.exists() && !candidate.canWrite()) {
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
        if (this.args.length != 2) {
            statusError();
            return "Invalid arguments.";
        }

        File to = workingDir.toPath().resolve(this.args[0]).toFile();
        File from = workingDir.toPath().resolve(this.args[1]).toFile();
        boolean result = copy(to, from);
        if (!result) {
            statusError();
            return String.format("Could not copy file: %s to: %s%n", this.args[0], this.args[1]);
        }

        statusSuccess();
        return "";
    }
}
