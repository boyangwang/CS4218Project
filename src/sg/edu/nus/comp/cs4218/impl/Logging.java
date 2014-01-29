package sg.edu.nus.comp.cs4218.impl;

/**
 * A basic logger.
 *
 * ...because log4j also cannot =[
 */
public class Logging {
    /**
     * Code for instance stuff.
     */
    private int level = 0;

    private Logging() {
        // Empty.
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void writeLog(int level, String message) {
        if (level >= this.level) {
            System.err.println(message);
        }
    }

    /**
     * Code for static stuff.
     */
    private static Logging lg = null;

    public static Logging logger() {
        while (lg == null) {
            try {
                lg = new Logging();
            } catch (Exception ex) {
                // TODO: How now brown cow?
            }
        }
        return lg;
    }

    /**
     * Log levels.
     */
    public final static int Info  = 0;
    public final static int All   = 0;
    public final static int Warn  = 1;
    public final static int Error = 2;
    public final static int Fatal = 3;
}
