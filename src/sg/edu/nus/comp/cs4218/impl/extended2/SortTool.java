package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class SortTool extends ATool implements ISortTool {
	static final Charset UTF_8 = StandardCharsets.UTF_8;

	private boolean checkIfSorted;
	
	public SortTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String execute(File workingDir, String stdin) {
        int i = 0;

        while (i < args.length && args[i].startsWith("-")) {
            String arg = args[i++];
            
            if (arg.equals("-help")) {
            	return getHelp();
            } else {
                for (int j = 1; j < arg.length(); j++) {
                    char flag = arg.charAt(j);
                    switch (flag) {
                        case 'c':
                        	checkIfSorted = true;
                            break;
                        default:
                            statusError();
                            return String.format("Error: sort: unrecognized option '-%s'%nTry 'sort -help' for more information.%n", flag);
                    }
                }
            }
        }
        
        StringBuilder lines = new StringBuilder();
        
        if (i == args.length) {
        	if (stdin == null) {
                statusError();
                return String.format("Error: Missing parameter for OPTION FILE%n");
        	} else {
        		addLines(lines, stdin);
        	}
        }
        
        if (checkIfSorted && i + 1 < args.length) {
        	statusError();
        	return String.format("sort: extra operand `%s' not allowed with -c%n", args[i + 1]);
        }
        
        String arg = "";
        boolean isFirstStdin = true;

        while (i < args.length) {
            arg = args[i++];
            if(arg.equals("-")){
                // Process only first stdin argument
                if (isFirstStdin) {
                    isFirstStdin = false;
                    addLines(lines, stdin);
                }
            } else {
            	String error = addLines(lines, arg, workingDir);
            	if (error != null) {
            		statusError();
            		return error;
            	}
            }
        }

        String input = lines.toString();
        if (checkIfSorted) {
        	String output = checkIfSorted(input);
        	return output.isEmpty() ? output : String.format("sort: %s %s", arg, output);
        }
        return sortFile(input);
	}

	@Override
	public String sortFile(String input) {
		List<String> list = new LinkedList<String>();
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            list.add(String.format("%s%n", scanner.nextLine()));
        }
        scanner.close();
        Collections.sort(list);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : list) {
        	stringBuilder.append(string);
        }
        return stringBuilder.toString();
	}

	@Override
	public String checkIfSorted(String input) {
		String prevLine = "";
		String nextLine = "";
		String output = "";
		int lineCount = 0;
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
        	nextLine = scanner.nextLine();
        	lineCount++;
        	if (prevLine.compareTo(nextLine) > 0) {
        		output = String.format("%d: disorder:%s%n", lineCount, nextLine);
        		break;
        	}
        	prevLine = nextLine;
        }
        scanner.close();
        return output;
	}

	@Override
	public String getHelp() {
		return String.format("NAME%n%nsort - sort lines of text files%n%n" +
				"DESCRIPTION%n%nWrite sorted concatenation of all FILE(s)%n" +
				"-c\tCheck whether the given file is already sorted, if it%n" +
				"-help\tBrief information about supported options%n");
	}
	
	private void addLines(StringBuilder stringBuilder, String input) {
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
        	stringBuilder.append(String.format("%s%n", scanner.nextLine()));
        }
        scanner.close();
	}
	
	private String addLines(StringBuilder stringBuilder, String pathname, File workingDir) {
        try {
        	Path path = workingDir.toPath().resolve(pathname);
            byte[] encoded = Files.readAllBytes(path);
            addLines(stringBuilder, UTF_8.decode(ByteBuffer.wrap(encoded)).toString());
            return null;
        } catch (NoSuchFileException e) {
            return String.format("Error: FILE is not found%n");
        } catch (IOException e) {
            return String.format("Error: FILE 01 is not a file%n");
        }
	}
}
