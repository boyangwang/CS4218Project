package sg.edu.nus.comp.cs4218;

/**
 * Do not modify this file
 */
public interface IShell {
	
	/**
	 * Parses the commandline and instantiates the corresponding tool.
	 * @param commandline
	 * @return
	 */
	ITool parse(String commandline);
	
	/**
	 * Executes the tool, starts a new thread, and returns the thread handle.
	 * @param tool
	 * @return
	 */
	Runnable execute(ITool tool);
	
	/**
	 * Called upon Ctrl+C
	 * @param toolExecution
	 */
	void stop(Runnable toolExecution);
}
