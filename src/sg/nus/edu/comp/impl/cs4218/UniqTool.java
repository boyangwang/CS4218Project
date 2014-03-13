package sg.nus.edu.comp.impl.cs4218;

import java.io.File;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;

public class UniqTool implements IUniqTool {

	public UniqTool() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUnique(boolean checkCase, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUniqueSkipNum(int num, boolean checkCase, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		return "NAME\n\nuniq - report or omit repeated lines\n-help\tBrief information about supported options\n-f " +
               "NUM\tSkips NUM fields on each line before checking for uniqueness separated from each other by at "  +
               "least one space or";
	}

}
