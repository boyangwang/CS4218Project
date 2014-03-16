package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class PasteTool extends ATool implements IPasteTool {
	String LINE_SEPARATOR = System.lineSeparator();
	//String LINE_SEPARATOR = "\n";

	String ERROR_NO_DELIMITER = "Error: please specify delimiter" + LINE_SEPARATOR;
	String ERROR_INVALID_OPTION = "Error: unknown option %s" + LINE_SEPARATOR;
	String ERROR_FILE_INACCESSIBLE = "Error: cannot read from file %s" + LINE_SEPARATOR;
	String ERROR_NO_FILE = "Error: no file is specified" + LINE_SEPARATOR;

	public PasteTool(String[] arguments) {
        super(arguments);
	}
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		ArrayList<String> tmpFilesContent = new ArrayList<String>();
		boolean serialDisplay = false;
		String strDelimiters = "\t";
		boolean isFirstStdin = true;


		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length()>1){
				if(args[i].compareTo("-s")==0){
					serialDisplay=true;
				}else if(args[i].startsWith("-d")){
					if (args[i].length()>2){
						strDelimiters = args[i].substring(2);
						//
					}else if (args.length>i+1){
						strDelimiters = args[i+1];
						i++;
					}else{
						statusError();
						return ERROR_NO_DELIMITER;
					};

				}else if (args[i].compareTo("-help")==0){
					return getHelp();

				}else{
					statusError();
					return String.format(ERROR_INVALID_OPTION, args[i]);
				}
			}else{
				if (args[i].compareTo("-")==0){
					if (isFirstStdin){
						isFirstStdin=false;
						tmpFilesContent.add(stdin);
					}
				}else{

					File target = workingDir.toPath().resolve(args[i]).toFile();
					try {
						tmpFilesContent.add(readContentsOfFile(target));
					} catch (IOException e) {
						statusError();
						return String.format(ERROR_FILE_INACCESSIBLE, args[i]);
					}
				}
			}
		}

		if (tmpFilesContent.size() == 0){
			if (stdin!=null){
				tmpFilesContent.add(stdin);
			}
		}
		if (tmpFilesContent.size()==0){
			statusError();
			return ERROR_NO_FILE;
		}
		String[] filesContent = new String[tmpFilesContent.size()];
		filesContent = tmpFilesContent.toArray(filesContent);
		String out = "";
		if (!serialDisplay){
			out = pasteUseDelimiter(strDelimiters, filesContent);

		}else {
			out = pasteSerial(filesContent);
		}

		return out;
	}

	@Override
	public String pasteSerial(String[] filesContent) {
		String out = "";
		for (int i = 0; i < filesContent.length; i++) {
			String[] fileContent = filesContent[i].split(LINE_SEPARATOR,-1);
			for (int j = 0; j < fileContent.length; j++) {
				out += fileContent[j];
				if(j<fileContent.length-1){
					out += "\t";
				}
			}
			out += LINE_SEPARATOR;
		}
		return out;
	}

	@Override
	public String pasteUseDelimiter(String strDelimiters, String[] filesContent) {
		if (strDelimiters == null || strDelimiters.length()==0){
			strDelimiters = "\t";
		}
		if (filesContent ==null){
			return "";
		}
		String out ="";
		int numFilesLeft = filesContent.length;
		int lineNumber = 0;
		int currDelimiter = 0;

		String[] delimiters = new String[strDelimiters.length()];
		for (int j = 0; j < strDelimiters.length(); j++) {
			delimiters[j] = strDelimiters.substring(j, j+1);
		}
		while(true){

			for (int i = 0; i < filesContent.length; i++) {
				String[] fileContent = filesContent[i].split(LINE_SEPARATOR,-1);
				if(fileContent.length>lineNumber){
					out+=fileContent[lineNumber];
					if (fileContent.length==lineNumber+1){
						numFilesLeft--;
					}
				}

				if(i<filesContent.length-1){
					out+=delimiters[currDelimiter];
				}
				if(currDelimiter<delimiters.length-1){
					currDelimiter++;
				}
			}
			out+=LINE_SEPARATOR;
			currDelimiter=0;
			lineNumber++;
			if(numFilesLeft==0){
				break;
			}
		}		
		return out;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Paste - merge lines of files" + LINE_SEPARATOR +
				"-d DELIM:\tUser custom delimiter instead of the default tab" + LINE_SEPARATOR +
				"-s\t\tpaste one file at a time instead of in parallel"   + LINE_SEPARATOR +
				"-help\t\tBrief information about supported options." + LINE_SEPARATOR ;
	}
    private String readContentsOfFile(File file) throws IOException {
    	ArrayList<String> outTmp = new ArrayList<String>();
    	String strLine;
    	String out = "";
        FileInputStream is;
        is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        while((strLine = br.readLine())!= null){
        	out += strLine + LINE_SEPARATOR;
        }
        out = out.substring(0,out.length()-LINE_SEPARATOR.length());

        is.close();

        return out;
    }

}
