package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class WcTool extends ATool implements IWcTool {
	static final Charset UTF_8 = StandardCharsets.UTF_8;

	private boolean printCharacterCount, printWordCount, printNewLineCount;

	public WcTool(String[] arguments) {
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
                        case 'm':
                        	printCharacterCount = true;
                            break;
                        case 'w':
                        	printWordCount = true;
                            break;
                        case 'l':
                        	printNewLineCount = true;
                            break;
                        default:
                            statusError();
                            return String.format("wc: illegal option -- %s%n", flag);
                    }
                }
            }
        }
        
        StringBuilder output = new StringBuilder();

        if (!printCharacterCount && !printWordCount && !printNewLineCount) {
        	printCharacterCount = printWordCount = printNewLineCount = true;
        }
        
        if (i == args.length) {
        	if (stdin == null) {
	            statusError();
	            return getHelp();
        	} else {
        		output.append(wc(stdin));
        		output.append(String.format("%n"));
        	}
        }

        boolean isFirstStdin = true;

        while (i < args.length) {
            String arg = args[i++];
            if(arg.equals("-")){
                // Process only first stdin argument
                if (isFirstStdin) {
                    isFirstStdin = false;
                    output.append(wc(stdin));
            		output.append(String.format("%n"));
                }
            } else {
                output.append(wc(arg, workingDir));
            }
        }

        return output.toString();
	}

	@Override
	public String getCharacterCount(String input) {
		return String.valueOf(input.length());
	}

	@Override
	public String getWordCount(String input) {
		Matcher matcher = Pattern.compile("\\S+").matcher(input);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return String.valueOf(count);
	}

	@Override
	public String getNewLineCount(String input) {
		Matcher matcher = Pattern.compile(System.lineSeparator()).matcher(input);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return String.valueOf(count);
	}

	@Override
	public String getHelp() {
		statusSuccess();
		return "usage: wc [-lmw] [file ...]" + System.lineSeparator();
	}

	private String wc(String input) {
		StringBuilder output = new StringBuilder();
		if (printNewLineCount) {
			output.append(getNewLineCount(input) + "\t");
		}
		if (printWordCount) {
			output.append(getWordCount(input) + "\t");
		}
		if (printCharacterCount) {
			output.append(getCharacterCount(input) + "\t");
		}
		return output.toString();
	}
	
	private String wc(String pathname, File workingDir) {
        try {
        	Path path = workingDir.toPath().resolve(pathname);
            byte[] encoded = Files.readAllBytes(path);
            return String.format("%s%s%n", wc(UTF_8.decode(ByteBuffer.wrap(encoded)).toString()), path.getFileName());
        } catch (NoSuchFileException e) {
        	statusError();
            return String.format("Error: SOURCE file not found%n");
        } catch (IOException e) {
        	statusError();
            return String.format("wc: %s: %s%n", pathname, e);
        }
	}
}
