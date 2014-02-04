package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.File;

public class EchoTool extends ATool implements IEchoTool {
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     * @param stdin
     */
    public EchoTool(String[] arguments, String stdin) {
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
        StringBuilder sb = new StringBuilder();

        // Concatenates arguments into a space-separated list, terminated by a newline.
        // If no arguments are given, this algorithm returns a newline.
        for (String str : this.args) {
            sb.append(str);
            sb.append(" ");
        }
        sb.append("\n");

        return sb.toString();
    }

    // This method is not used.
    @Override
    public String echo(String toEcho) {
        return toEcho;
    }
}
