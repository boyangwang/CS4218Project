package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

/**
 * The grep command searches one or more input files
 * for lines containing a match to a specified pattern.
 * The grep tool must work on all characters in UTF-8 encoding.
 *
 * Command Format - grep [OPTIONS] PATTERN [FILE]
 * PATTERN - This specifies a regular expression pattern that describes a set of strings
 * FILE - Name of the file, when no file is present (denoted by "-") use standard input
 * OPTIONS
 *   -A NUM : Print NUM lines of trailing context after matching lines
 *   -B NUM : Print NUM lines of leading context before matching lines
 *   -C NUM : Print NUM lines of output context
 *   -c : Suppress normal output. Instead print a count of matching lines for each input file
 *   -o : Show only the part of a matching line that matches PATTERN
 *   -v : Select non-matching (instead of matching) lines
 *   -help : Brief information about supported options
 */
public class GrepTool extends ATool implements IGrepTool {
    static final Charset UTF_8 = StandardCharsets.UTF_8;

    private boolean count, onlyMatching, invertMatch;
    private int afterContext, beforeContext;

    public GrepTool(String[] arguments){
        super(arguments);
    }

    @Override
    public int getCountOfMatchingLines(String pattern, String input) {
        Pattern regex;
        try {
            regex = Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            statusError();
            return -1;
        }

        Matcher matcher = null;
        int lineCount = 0;

        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (matcher == null) {
                matcher = regex.matcher(line);
            } else {
                matcher.reset(line);
            }
            if (matcher.find()) {
                lineCount++;
            }
        }
        scanner.close();

        return lineCount;
    }

    @Override
    public String getOnlyMatchingLines(String pattern, String input) {
        reset();
        return grep(pattern, input);
    }

    @Override
    public String getMatchingLinesWithTrailingContext(int option_A, String pattern, String input) {
        reset();
        afterContext = option_A;
        return grep(pattern, input);
    }

    @Override
    public String getMatchingLinesWithLeadingContext(int option_B, String pattern, String input) {
        reset();
        beforeContext = option_B;
        return grep(pattern, input);
    }

    @Override
    public String getMatchingLinesWithOutputContext(int option_C, String pattern, String input) {
        reset();
        beforeContext = afterContext = option_C;
        return grep(pattern, input);
    }

    @Override
    public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
        reset();
        onlyMatching = true;
        return grep(pattern, input);
    }

    @Override
    public String getNonMatchingLines(String pattern, String input) {
        reset();
        invertMatch = true;
        return grep(pattern, input);
    }

    @Override
    public String getHelp() {
        return "grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]" + System.lineSeparator();
    }

    @Override
    public String execute(File workingDir, String stdin) {
        int i = 0;

        while (i < args.length && args[i].startsWith("-")) {
            String arg = args[i++];

            switch (arg) {
                case "-A":
                case "-B":
                case "-C":
                    try {
                        if (i >= args.length) {
                            throw new NumberFormatException();
                        }
                        int contextNum = Integer.parseInt(args[i++]);
                        if (contextNum < 0) {
                            throw new NumberFormatException();
                        }
                        if (arg.equals("-B") || arg.equals("-C")) {
                            beforeContext = contextNum;
                        }
                        if (arg.equals("-A") || arg.equals("-C")) {
                            afterContext = contextNum;
                        }
                    } catch (NumberFormatException e) {
                        statusError();
                        return arg + " requires a positive number" + System.lineSeparator();
                    }
                    break;
                default:
                    for (int j = 1; j < arg.length(); j++) {
                        char flag = arg.charAt(j);
                        switch (flag) {
                            case 'c':
                                count = true;
                                break;
                            case 'o':
                                onlyMatching = true;
                                break;
                            case 'v':
                                invertMatch = true;
                                break;
                            default:
                                statusError();
                                return "Illegal argument -" + flag + System.lineSeparator();
                        }
                    }
            }
        }

        if (i + 2 > args.length) {
            statusError();
            return getHelp();
        }

        String pattern = args[i++];

        boolean isFirstStdin = true;
        StringBuilder output = new StringBuilder();

        while (i < args.length) {
            String arg = args[i++];
            if(arg.equals("-")){
                // Process only first stdin argument
                if (isFirstStdin) {
                    isFirstStdin = false;
                    output.append(grep(pattern, stdin));
                }
            } else {
                output.append(grepPath(pattern, arg, workingDir));
            }
        }

        return output.toString();
    }

    private void reset() {
        count = onlyMatching = invertMatch = false;
        afterContext = beforeContext = 0;
    }

    private String grepPath(String pattern, String pathname, File workingDir) {
        try {
            byte[] encoded = Files.readAllBytes(workingDir.toPath().resolve(pathname));
            return grep(pattern, UTF_8.decode(ByteBuffer.wrap(encoded)).toString());
        } catch (NoSuchFileException e) {
            return "grep: " + pathname + ": No such file or directory" + System.lineSeparator();
        } catch (IOException e) {
            return "grep: " + pathname + ": " + e + System.lineSeparator();
        }
    }

    public class CircularQueue<E> extends LinkedList<E> {
        private int limit;

        public CircularQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E element) {
            super.add(element);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }

    private String grep(String pattern, String input) {
        if (count) {
            return Integer.toString(getCountOfMatchingLines(pattern, input)) + System.lineSeparator();
        }

        Pattern regex;
        try {
            regex = Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            statusError();
            return e.toString() + System.lineSeparator();
        }

        Matcher matcher = null;
        StringBuilder output = new StringBuilder();
        CircularQueue<String> previousLines = new CircularQueue<>(beforeContext);
        boolean printAfterContext = false;
        int afterContextCount = 0;

        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            if (Thread.interrupted()) {
                statusSuccess();
                return output.toString();
            }

            String line = scanner.nextLine();

            if (matcher == null) {
                matcher = regex.matcher(line);
            } else {
                matcher.reset(line);
            }

            boolean matched = false;
            while (matcher.find()) {
                if (!matched) {
                    matched = true;
                    ListIterator<String> listIterator = previousLines.listIterator(afterContextCount);
                    while (listIterator.hasNext()) {
                        output.append(listIterator.next());
                        output.append(System.lineSeparator());
                    }
                    if (afterContext > 0) {
                        printAfterContext = true;
                        afterContextCount = 0;
                    }
                }

                if (onlyMatching) {
                    output.append(matcher.group());
                    output.append(System.lineSeparator());
                } else {
                    break;
                }
            }
            if (matched != invertMatch) {
                output.append(line);
                output.append(System.lineSeparator());
            }
            if (printAfterContext && (matched == invertMatch)) {
                if (afterContextCount++ < afterContext) {
                    output.append(line);
                    output.append(System.lineSeparator());
                } else {
                    printAfterContext = false;
                    afterContextCount = 0;
                }
            }
            previousLines.add(line);
        }
        scanner.close();

        return output.toString();
    }
}
