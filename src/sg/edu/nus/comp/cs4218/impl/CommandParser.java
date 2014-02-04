package sg.edu.nus.comp.cs4218.impl;


import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

import java.util.Arrays;

public class CommandParser {
    public static ITool parse(String str) {
        // at the beginning of Shell.parse, if pipe operator is present, pass to PipingTool
        if (containsPipeOperator(str)) {
            String[] args = new String[1];
            args[0] = str;
            IPipingTool pipingTool = new PipingTool(args, "");
            return pipingTool;
        }

        String[] tokens = tokenizeString(str);
        String[] argList = getArgumentList(tokens);
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
                break;

            case "pwd":
                return new PwdTool(argList);

            default:
                Logging.logger(System.out).writeLog(Logging.Error, "Cannot parse " + str);
                return null;
        }

        return null;
    }

    /**
     * Parse inputString and returns true if the control should be
     * passed to PipingTool
     *
     * @param str the user input string
     * @return `true` if the control should be passed to PipingTool
     */
    private static boolean containsPipeOperator(String str) {
        return str.contains("|");
    }

    private static String[] tokenizeString(String str) {
        return str.split(" ");
    }

    private static String[] getArgumentList(String[] tokens) {
        return Arrays.copyOfRange(tokens, 0, tokens.length - 1);
    }

    private static String getCommand(String[] tokens) {
        if (tokens.length > 0) {
            return tokens[0];
        } else {
            return "";
        }
    }
}
