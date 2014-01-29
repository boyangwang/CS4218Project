package sg.edu.nus.comp.cs4218;

import java.io.File;

public interface ITool {
    /**
     * Executes the tool with args provided in the constructor
     *
     * @param shell A reference to the calling shell.
     * @param workingDir The current working directory.
     * @param stdin Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
	public String execute(IShell shell, File workingDir, String stdin);

    /**
     * Returns the contents of this ITool's stdin.
     *
     * @return Contents of stdin as a String.
     */
    public String getStdIn();

    /**
     * After execution, returns the status of the tool.
     *
     * @return Tools that execute correctly should return 0.
     */
	public int getStatusCode();
}
