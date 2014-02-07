package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

public class EchoTool extends ATool implements IEchoTool {
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public EchoTool(String[] arguments) {
        super(arguments);
    }

    // This method is not used.
    @Override
    public String echo(String toEcho) {
        setStatusCode(((toEcho == null) ? 1 : 0));
        return toEcho;
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
        StringBuilder sb = new StringBuilder();

        // Concatenates arguments into a space-separated list, terminated by a newline.
        // If no arguments are given, this algorithm returns a newline.
        for (String str : this.args) {
            if (Thread.interrupted()) {
                statusSuccess();
                sb.append(System.lineSeparator());
                return sb.toString();
            }

            sb.append(str);
            sb.append(" ");
        }
        
        if (sb.length() >=1 && sb.charAt(sb.length() - 1) == ' ') {
        	sb.deleteCharAt(sb.length() - 1);
        }
        
        sb.append(System.lineSeparator());
        
        return sb.toString();
    }
}
