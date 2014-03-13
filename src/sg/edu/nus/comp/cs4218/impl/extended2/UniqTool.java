package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class UniqTool extends ATool implements IUniqTool {

    private final static int BUF_SIZE = 4096;

	public UniqTool(String[] arguments) {
        super(arguments);
	}

	@Override
	public String execute(File workingDir, String stdin) {
        statusError();

        if (this.args.length != 1) {
            return "";
        }

        File target = workingDir.toPath().resolve(this.args[0]).toFile();
        try {
            FileInputStream is = new FileInputStream(target);
            String contents = this.readFile(is);

        } catch (FileNotFoundException e) {
            return "";
        }
    }

    private String readFile(FileInputStream is) {
        StringBuilder sb = new StringBuilder();
        byte buf[] = new byte[BUF_SIZE];
        int read = 0;
        try {
            while ((read = is.read(buf)) != -1) {
                sb.append(new String(Arrays.copyOf(buf, read), StandardCharsets.UTF_8));
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

	@Override
	public String getUnique(boolean checkCase, String input) {
        return getUniqueSkipNum(0, checkCase, input);
	}

	@Override
	public String getUniqueSkipNum(int num, boolean checkCase, String input) {
        input = input.trim();  // Remove leading/trailing whitespace to prevent blank lines.

        if (!checkCase) {
            input = input.toLowerCase();  // Case-fold to lower case.
        }

        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();
        String lastLine = null;
        for (String line : lines) {
            line = line.trim();

            line = skipFields(num, line);

            if (!line.equals(lastLine)) {
                lastLine = line;
                sb.append(String.format("%s\n", line));
            }
        }
        return sb.toString();
	}

    private String skipFields(int skipNum, String str) {
        if (skipNum == 0) {
            return str;
        } else {
            int ws = Math.min(str.indexOf(' '), str.indexOf('\t'));
            if (ws < 0) {
                return "";
            }
            while (str.charAt(ws) == ' ' || str.charAt(ws) == '\t') {
                ws++;
            }
            return skipFields(skipNum - 1, str.substring(ws));
        }
    }

	@Override
	public String getHelp() {
		return "NAME\n\nuniq - report or omit repeated lines\n-help\tBrief information about supported options\n-f " +
               "NUM\tSkips NUM fields on each line before checking for uniqueness separated from each other by at "  +
               "least one space or";
	}

}
