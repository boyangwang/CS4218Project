package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class UniqTool extends ATool implements IUniqTool {
    private final static int BUF_SIZE = 4096;
    private final static String ERR_NUMFMT = "Error: NUM has to be a positive number.";
    private final static String ERR_INVALID_ARG = "Error: Invalid option.";
    private final static String ERR_NOT_FOUND = "Error: FILE is not found.";
    private final static String ERR_MISSING_PARAM = "Error: Must supply a skip parameter with -f.";
    private final static String ERR_IO = "Error: Generic IO Error.";

	public UniqTool(String[] arguments) {
        super(arguments);
	}

    private boolean isNonNegativeInteger(String str) {
        int val;
        try {
            val = Integer.parseInt(str);
            if (val < 0) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

	@Override
	public String execute(File workingDir, String stdin) {
        statusError();

        boolean checkCase = true;
        boolean help = false;
        int skip = 0;
        String file = null;

        int i = 0;
        while (i < args.length && !help) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-f":
                        if (++i < args.length) {
                            if (isNonNegativeInteger(args[i])) {
                                skip = Integer.parseInt(args[i]);
                            } else {
                                return ERR_NUMFMT;
                            }
                        } else {
                            return ERR_MISSING_PARAM;
                        }
                        break;

                    case "-i":
                        checkCase = false;
                        break;

                    case "-help":
                        help = true;
                        break;

                    default:
                        return ERR_INVALID_ARG;
                }
            } else {
                file = arg;
            }

            i++;
        }

        if (file != null && !help) {
            File target;
            try {
                target = new File(workingDir.getCanonicalPath() + "/" + file);
                String contents = this.readContentsOfFile(target);

                statusSuccess();
                return this.getUniqueSkipNum(skip, checkCase, contents);
            } catch (FileNotFoundException ex) {
                return ERR_NOT_FOUND;
            } catch (IOException e) {
                return ERR_IO;
            }
        } else if (stdin != null && !stdin.equals("")) {
            statusSuccess();
            return this.getUniqueSkipNum(skip, checkCase, stdin);
        } else {
            statusSuccess();
            return this.getHelp();
        }
    }

    private String readContentsOfFile(File file) throws IOException {
        FileInputStream is;
        is = new FileInputStream(file);

        StringBuilder sb = new StringBuilder();
        byte buf[] = new byte[BUF_SIZE];
        int read = 0;
        while ((read = is.read(buf)) != -1) {
            sb.append(new String(Arrays.copyOf(buf, read), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

	@Override
	public String getUnique(boolean checkCase, String input) {
        return getUniqueSkipNum(0, checkCase, input);
	}

	@Override
	public String getUniqueSkipNum(int num, boolean checkCase, String input) {
        input = input.trim();  // Remove leading/trailing whitespace to prevent blank lines.

        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();

        // Input contains no lines.
        if (lines.length == 0) {
            return "";
        }

        // The first line is always unique.
        String firstLine = lines[0].trim();
        sb.append(String.format("%s\n", firstLine));
        String lastLine = skipFields(num, firstLine);

        // Compare all other lines against the last line.
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            line = skipFields(num, line);

            boolean equals;
            if (checkCase) {
                equals = line.equals(lastLine);
            } else {
                equals = line.equalsIgnoreCase(lastLine);
            }
            if (!equals) {
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
		return "NAME\n\nuniq - report or omit repeated lines\n-f NUM\tSkips NUM fields on each line before checking " +
               "for uniqueness separated from each other by at least one space or tab.\n-i\tIgnore differences in "   +
               "case when comparing lines.\n-help\tBrief information about supported options.\n";
	}
}
