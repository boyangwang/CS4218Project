/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.lang.StringBuilder;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class ReverseTool extends ATool implements ITool {

	/**
	 * @param arguments
	 * @param stdin
	 */
	public ReverseTool(String[] arguments, String stdin) {
		super(arguments, stdin);
		// Uses arguments only; not using stdin ATM

	}

	/**
	 * 
	 */
	@Override
	public String execute(IShell shell, File workingDir) {
		String concated = stdin;// formConcatString(args);
		String reversed = reverseString(concated);
		return reversed;
	}
	
	private String formConcatString(String[] arguments) {
		StringBuilder sb = new StringBuilder("");
		for (int i=0; i<arguments.length-1; i++) {
			sb.append(arguments[i]);
			sb.append(' ');
		}
		sb.append(arguments[arguments.length-1]);
		
		return sb.toString();
	}
	
	private String reverseString(String str) {
		StringBuilder sb = new StringBuilder("");
		for (int i=str.length()-1; i>=0; i--) {
			char c = str.charAt(i);
			sb.append(c);
		}
		
		return sb.toString();
	}
}
