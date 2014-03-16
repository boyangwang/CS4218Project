package sg.edu.nus.comp.cs4218.impl;


import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {
	private final static char DELIMITER_CHAR = ' ';
	private final static char QUOTE_CHAR = '\'';
	private final static char DQUOTE_CHAR = '"';
	private final static char BQUOTE_CHAR = '`';
	private final static char PIPE_CHAR = '|';
	/**
	 * parse user input and return an instance of ITool for execution
	 * @param rawInput user command
	 * @return ITool if user command is valid, null otherwise
	 */
    public static ITool parse(String rawInput, Shell shell) {
    	String trimmedCmd = rawInput.trim();
        // at the beginning of Shell.parse, if pipe operator is present, pass to PipingTool
    	if (!verifyCommand(trimmedCmd)) {
    		return null;
    	}
    	String[] argList = tokenizePipeCommands(trimmedCmd);
        if (argList != null) {
            return new PipingTool(argList, shell);
        }

        String[] tokens = tokenizeString(trimmedCmd);
        argList = getArgumentList(tokens);
        String cmd = getCommand(tokens);

        switch (cmd.toLowerCase()) {
            case "cat":
                return new CatTool(argList);

            case "cd":
                return new CdTool(argList, shell);

            case "copy":
                return new CopyTool(argList);

            case "delete":
                return new DeleteTool(argList);

            case "echo":
                return new EchoTool(argList);

            case "ls":
                return new LsTool(argList);

            case "move":
                return new MoveTool(argList);

            case "paste":
                return new PasteTool(argList);
                
            case "pwd":
                return new PwdTool(argList);
                
            case "cut":
                return new CutTool(argList);
                
            case "grep":
                return new GrepTool(argList);
                
            case "wc":
            	return new WcTool(argList);

            case "uniq":
                return new UniqTool(argList);

            case "comm":
            	return new CommTool(argList);
            	
            case "sort":
            	return new SortTool(argList);

            default:
                Logging.logger(System.out).writeLog(Logging.ERROR, "Cannot parse " + trimmedCmd);
                return null;
        }
    }

    /**
     * verify user command, 
     * @param cmd user command
     * @return true if quotes are closed properly, false otherwise
     */
    private static boolean verifyCommand(String cmd) {
    	boolean isInQuote = false;
    	char currentQuote = 0;
    	for(int i=0;i<cmd.length();i++){
    		char c = cmd.charAt(i);
    		if (isInQuote){
    			if(currentQuote == c){
    				currentQuote = 0;
    				isInQuote = false;
    			}
    		}else{
    			if (c==BQUOTE_CHAR || c== QUOTE_CHAR || c== DQUOTE_CHAR){
    				currentQuote = c;
    				isInQuote = true;
    			}
    		}
    	}
		return !isInQuote;
	}

	/**
     * Parse inputString and returns an array of commands string if the control should be
     * passed to PipingTool
     *
     * Null otherwise
     *
     * @param str the user input string
     * @return `null` if the control shouldn't be passed to PipingTool
     */
	private static String[] tokenizePipeCommands(String str) {
		ArrayList<String> cmds = new ArrayList<String>();
		StringBuilder curCmd = new StringBuilder();
		boolean inQuotes = false;
		char currentQuote = 0;
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			if (c == PIPE_CHAR && !inQuotes) {
				cmds.add(curCmd.toString().trim());
				curCmd = new StringBuilder();
				continue;
			}
			
			if (!inQuotes && (c == DQUOTE_CHAR || c==QUOTE_CHAR || c==BQUOTE_CHAR)) {
				inQuotes = true;
				currentQuote = c;
				continue;
			} else if (inQuotes && (currentQuote == c)) {
				inQuotes = false;
				currentQuote = 0;
				continue;
			}
			
			curCmd.append(c);
		}
		
		if (cmds.size() >= 1) {
			cmds.add(curCmd.toString().trim());
			return cmds.toArray(new String[cmds.size()]);
		}
		else {
			return null;
		}
	}

	/**
     * Parse inputString and array of token strings
     * @param str the user input string
     * @return array of parsed token strings (can be empty)
     */
    private static String[] tokenizeString(String str) {
    	ArrayList<String> out = new ArrayList<String>();
    	if (str!=null){
    		char currentQuote = 0;
    		boolean isInQuote = false;
    		StringBuilder sb = new StringBuilder();
    		for (int i = 0; i < str.length(); i++){
    			char c = str.charAt(i);
    			if (isInQuote){
    				if (currentQuote == c){
    					//exiting from quote
    					currentQuote = 0; 
    					isInQuote = false;
    					continue;
    				}
    			}else{
    				if (c==QUOTE_CHAR || c==DQUOTE_CHAR || c==BQUOTE_CHAR){
    					currentQuote = c;
    					isInQuote = true;
    					continue;
    				}
    			}
    			if (c==DELIMITER_CHAR && !isInQuote){
    				if(sb.length()>0){
    					out.add(sb.toString());
    				}
    				sb = new StringBuilder();
    			}else{
    				sb.append(c);
    			}
    		}

    		if(sb.length()>0){
    			out.add(sb.toString().trim());
    		}
    	}

    	return out.toArray(new String[out.size()]);
    }

  

	private static String[] getArgumentList(String[] tokens) {
        if (tokens.length < 2) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(tokens, 1, tokens.length);
        }
    }
	/**
	 * return text determine which tool to execute
	 * @param tokens array of tokens parsed from user input
	 * @return string determine the tool to execute
	 */
    private static String getCommand(String[] tokens) {
        if (tokens.length > 0) {
            return tokens[0];
        } else {
            return "";
        }
    }
}
