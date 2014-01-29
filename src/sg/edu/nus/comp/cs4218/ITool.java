package sg.edu.nus.comp.cs4218;

import java.io.File;

public interface ITool {
	public String execute(IShell shell, File workingDir, String stdin);
    public String getStdIn();
	public int getStatusCode();
}
