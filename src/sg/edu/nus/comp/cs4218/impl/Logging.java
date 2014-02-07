package sg.edu.nus.comp.cs4218.impl;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * A basic logger.
 *
 * ...because log4j also cannot =[
 */
public final class Logging {
    /**
     * Code for instance stuff.
     */
    private int level = 0;
    private PrintWriter out;

    private Logging(OutputStream os) {
        this.out = new PrintWriter(os);
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void writeLog(int level, String message) {
        if (level >= this.level) {
            out.println(message);
        }
    }

    /**
     * Code for static stuff.
     */
    private static Logging lg = null;

    public static Logging logger(OutputStream os) {
        while (lg == null) {
            try {
                lg = new Logging(os);
            } catch (Exception ex) {
                // TODO: How now brown cow?
            }
        }
        return lg;
    }

    /**
     * Log levels.
     */
    public final static int INFO  = 0;
    public final static int ALL   = 0;
    public final static int WARN  = 1;
    public final static int ERROR = 2;
    public final static int FATAL = 3;
}
