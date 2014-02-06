package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.*;

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

        boolean canCopy = (isValidSource(from) && isValidDestination(to));
        if (!canCopy) {
            return false;
        }

        if (from.isDirectory()) {
            to.mkdir();
            File[] files = from.listFiles();
            for (File f : files) {
                try {
                    String dst = String.format("%s%s%s", to.getCanonicalPath(), File.separator, f.getName());
                    File dstFile = new File(dst);
                    if (!dstFile.canRead()) {
                        return false;
                    }

                    boolean result = (new CopyTool(new String[0])).copy(f, new File(dst));
                    if (!result) {
                        return false;
                    }
                } catch (IOException e) {
                    return false;
                }
            }
            statusSuccess();
            return true;
        } else if (from.isFile()) {
            if (copyf(from, to)) {
                statusSuccess();
                return true;
            }
        }
        return false;
    }

    private boolean copyf(File src, File dst) {
        try {
            FileInputStream fis = new FileInputStream(src);
            FileOutputStream fos = new FileOutputStream(dst);

            byte[] buffer = new byte[BUF_SIZE];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }

            fis.close();
            fos.close();

            return true;
        } catch (FileNotFoundException e) {
            cleanup(dst);
            return false;
        } catch (IOException e) {
            cleanup(dst);
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
