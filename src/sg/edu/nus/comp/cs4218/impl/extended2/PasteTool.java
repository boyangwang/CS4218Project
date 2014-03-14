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

	public PasteTool(String[] arguments) {
        super(arguments);
	}
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		ArrayList<String[]> filesContent = new ArrayList<String[]>();
		boolean serialDisplay = false;
		String[] delimiters = {"\t"};
		boolean isFirstStdin = true;


		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length()>1){
				if(args[i].compareTo("-s")==0){
					serialDisplay=true;
				}else if(args[i].compareTo("-d")==0){
					if (args.length>i+1){
						String strDelimiters = args[i+1];
						delimiters = new String[strDelimiters.length()];
						for (int j = 0; j < strDelimiters.length(); j++) {
							delimiters[j] = strDelimiters.substring(j, j+1);
						}
						i++;
					}else{
						return "Error: please specify delimiter" + LINE_SEPARATOR;
					};
				}else if (args[i].compareTo("-help")==0){
					return getHelp();
					
				}else{
					return "Error: unknown option " + args[i] + LINE_SEPARATOR;
				}
			}else{
				if (args[i].compareTo("-")==0){
					if (isFirstStdin){
						isFirstStdin=false;
						filesContent.add(stdin.split(LINE_SEPARATOR));
					}
				}else{

					File target = workingDir.toPath().resolve(args[i]).toFile();
					try {
						filesContent.add(readContentsOfFile(target));
					} catch (IOException e) {
						return "Error: cannot read file " + args[i] + LINE_SEPARATOR;
					}
				}
			}
		}
		String out = "";
		int lineNumber = 0;
		int numFilesLeft = filesContent.size();
		int currDelimiter = 0;
		if (!serialDisplay){
			while(true){
				for (int i = 0; i < filesContent.size(); i++) {
					String[] fileContent = filesContent.get(i);
					if(fileContent.length>lineNumber){
						out+=fileContent[lineNumber];
						if (fileContent.length==lineNumber+1){
							numFilesLeft--;
						}
					}

					if(i<filesContent.size()-1){
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
		}else {
			for (int i = 0; i < filesContent.size(); i++) {
				currDelimiter = 0;
				String[] fileContent = filesContent.get(i);
				for (int j = 0; j < fileContent.length; j++) {
					out += fileContent[j];
					if(j<fileContent.length-1){
						out += delimiters[currDelimiter];
					}
					if(currDelimiter<delimiters.length-1){
						currDelimiter++;
					}
				}
				out += LINE_SEPARATOR;
			}
		}

		return out;
	}

	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String pasteSerial(String[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "NAME" + LINE_SEPARATOR + LINE_SEPARATOR +
				"Paste - merge lines of files" + LINE_SEPARATOR +
				"-d DELIM:\tUser custom delimiter instead of the default tab" + LINE_SEPARATOR +
				"-s\t\tpaste one file at a time instead of in parallel"   + LINE_SEPARATOR +
				"-help\t\tBrief information about supported options." + LINE_SEPARATOR ;
	}
    private String[] readContentsOfFile(File file) throws IOException {
    	ArrayList<String> outTmp = new ArrayList<String>();
    	String strLine;
        FileInputStream is;
        is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while((strLine = br.readLine())!= null){
        	outTmp.add(strLine);
        }
        is.close();

    	String[] out = new String[outTmp.size()];
        out = outTmp.toArray(out);
        return out;
    }

}
