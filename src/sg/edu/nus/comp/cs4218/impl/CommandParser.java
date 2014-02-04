package sg.edu.nus.comp.cs4218.impl;


import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {
    public static ITool parse(String str) {
        // at the beginning of Shell.parse, if pipe operator is present, pass to PipingTool
    	String[] argList = tokenizePipeCommands(str);
        if (argList != null) {
            PipingTool pipingTool = new PipingTool(argList);
            return pipingTool;
        }

        String[] tokens = tokenizeString(str);
        argList = getArgumentList(tokens);
        String cmd = getCommand(tokens);

        switch (cmd) {
            case "cat":
                break;

            case "cd":
                return new CdTool(argList);

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

            case "pwd":
                return new PwdTool(argList);

            default:
                Logging.logger(System.out).writeLog(Logging.Error, "Cannot parse " + str);
                return null;
        }

        return null;
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
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\"') {
				inQuotes = !inQuotes;
				continue;
			}
			
			if (c == '|' && !inQuotes) {
				cmds.add(curCmd.toString());
				curCmd = new StringBuilder();
				continue;
			}
			
			curCmd.append(c);
		}
		
		if (cmds.size() >= 1) {
			cmds.add(curCmd.toString());
			return cmds.toArray(new String[cmds.size()]);
		}
		else {
			return null;
		}
	}

    private static String[] tokenizeString(String str) {
        return str.split(" ");
    }

    private static String[] getArgumentList(String[] tokens) {
        if (tokens.length < 2) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(tokens, 1, tokens.length);
        }
    }

    private static String getCommand(String[] tokens) {
        if (tokens.length > 0) {
            return tokens[0];
        } else {
            return "";
        }
    }
}
