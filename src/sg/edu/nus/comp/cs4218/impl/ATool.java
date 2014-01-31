package sg.edu.nus.comp.cs4218.impl;

import java.io.File;

import sg.edu.nus.comp.cs4218.IShell;

public abstract class ATool {
	protected String[] args;
	protected String stdin;
	protected int statusCode = 0;
	
	/**
	 * Constructor
     *
	 * @param arguments Arguments the tool is going to be executed with.
	 */
	public ATool(String[] arguments, String stdin){
		this.args = arguments;
		this.stdin = stdin;
	}
	
	/**
	 * Executes the tool with args provided in the constructor
     *
     * @param workingDir The current working directory.
	 * @return Output on stdout
	 */
	public abstract String execute(IShell shell, File workingDir);

    /**
     * Returns the contents of this ATool's stdin.
     *
     * @return Contents of stdin as a String.
     */
    public String getStdin() {
        return this.stdin;
    }
	
	/**
	 * After execution returns the status of the tool
     *
	 * @return Returns 0 if executed properly
	 */
	public int getStatusCode(){
		return statusCode;
	}
	
	/**
	 * Set the status code during or after execution of the tool
     *
	 * @param statusCode 0 if executed normally. Otherwise, see http://tldp.org/LDP/abs/html/exitcodes.html#EXITCODESREF
	 */
	protected void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}
	

}
