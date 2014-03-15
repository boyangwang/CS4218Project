package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CutTool extends ATool implements ICutTool {
	private class Range{
		//inclusive range
		public Range(int _start, int _end){
			start = _start;
			end = _end;
		}
		int start;
		int end;
	}
	String LINE_SEPARATOR = System.lineSeparator();

	String ERROR_CUT_AND_DELIM_MODE = "Error: please choose to work with cut or delimiter & field\n";
	String ERROR_MORE_THAN_ONE_CUT_LIST = "Error: only one list can be specified\n";
	String ERROR_MORE_THAN_ONE_FIELD_LIST = "Error: only one list can be specified\n";
	String ERROR_NO_CUT_LIST = "Error: no cut list specified\n";
	String ERROR_NO_FIELD_LIST = "Error: no field list specified\n";
	String ERROR_INVALID_CUT_LIST = "Error: invalid cut list\n";
	String ERROR_INVALID_FIELD_LIST = "Error: invalid field list\n";
	String ERROR_DELIM_MORE_THAN_ONE_CHAR = "Error: delimter must be one char\n";
	String ERROR_INVALID_DELIM = "Error: delimter is invalid\n";
	String ERROR_FILE_INACCESSIBLE = "Error: cannot read from file %s " + LINE_SEPARATOR;
	String ERROR_NO_MODE_SPECIFIED = "Error: no mode specified\n";
	String ERROR_NO_FILE = "Error: please specify at least one file\n";


	public CutTool(String[] args) {
		// TODO Auto-generated constructor stub
		super(args);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		boolean isDelimMode	= false;
		boolean isCutMode	= false;
		boolean isFirstStdin = true;
		String delimChar = "\t";
		String cutList = null;
		String fieldList = null;
		ArrayList<String> filesContent = new ArrayList<String>();
		String out = "";
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if(arg.startsWith("-")){
				if (isFirstStdin && arg.compareTo("-")==0){
					isFirstStdin = false;
					filesContent.add(stdin);
				} else if(arg.startsWith("-c")){
					if (isDelimMode){
						return ERROR_CUT_AND_DELIM_MODE;
					}else if(cutList!=null){
						return ERROR_MORE_THAN_ONE_CUT_LIST;
					}else{
						isCutMode=true;
						if(arg.length()>2){
							cutList = arg.substring(2);
						}else if (args.length>i+1){
							cutList = args[++i];
						}else{
							return ERROR_NO_CUT_LIST;
						}
					}
				}else if (arg.startsWith("-f")){
					
					if (isCutMode){
						return ERROR_CUT_AND_DELIM_MODE;
					}else if(fieldList!=null){
						return ERROR_MORE_THAN_ONE_FIELD_LIST;
					}else{
						isDelimMode = true;
						if(arg.length()>2){
							fieldList = arg.substring(2);
						}else if (args.length>i+1){
							fieldList = args[++i];
						}else{
							return ERROR_NO_FIELD_LIST;
						}
					}
				}else if (arg.startsWith("-d")){

					if (isCutMode){
						return ERROR_CUT_AND_DELIM_MODE;
					}else{
						isDelimMode = true;
						if(arg.length()==3){
							delimChar = arg.substring(2);
						}else if(arg.length()>=3){
							return ERROR_DELIM_MORE_THAN_ONE_CHAR;
						} else if (args.length>i+1){
							//arg.length == 2, expect delimiter char
							delimChar = args[++i];
							if(delimChar.length()>1){
								return ERROR_DELIM_MORE_THAN_ONE_CHAR;
							}
						}else{
							return ERROR_NO_FIELD_LIST;
						}
					}
				}
			}else{
				File target = workingDir.toPath().resolve(arg).toFile();
				try {
					filesContent.add(readContentsOfFile(target));
				} catch (IOException e) {
					return String.format(ERROR_FILE_INACCESSIBLE, arg);
				}
			}
		}
		if (filesContent.size() == 0){
			return ERROR_NO_FILE;
		}
		if (isCutMode){
			for (int j = 0; j < filesContent.size(); j++) {
				out += cutSpecfiedCharacters(cutList, filesContent.get(j));
			}
		}else if (isDelimMode){
			for (int j = 0; j < filesContent.size(); j++) {
				out += cutSpecifiedCharactersUseDelimiter(fieldList, delimChar, filesContent.get(j));
			}
		}else {
			return ERROR_NO_MODE_SPECIFIED;
		}

		return out + LINE_SEPARATOR;
	}

	/*
	 * to parse a string list to a boolean mask array
	 * @param list		string list to be converted
	 * @return 			array of inclusive range, null if there is error in the string list
	 */
	private Range[] convertListToRange(String list) {
		String[] ranges = list.split(",");
		ArrayList<Range> out = new ArrayList<Range>();

		for (int i = 0; i < ranges.length; i++) {
			String strRange = ranges[i];
			if(strRange.indexOf("-")>=0){
				String[] tokens = strRange.split("-");
				if (strRange.endsWith("-")){
					tokens = Arrays.copyOf(tokens, tokens.length+1);
					tokens[tokens.length-1]="";
				}

				if(tokens.length==2){
					try {
						int num1;
						if (tokens[0].length()==0){
							num1 = 1;
						}else{
							num1 = Integer.parseInt(tokens[0]);
						}

						int num2;
						if (tokens[1].length()==0){
							num2 = Integer.MAX_VALUE;
						}else{
							num2 = Integer.parseInt(tokens[1]);
						}

						if (num1>0 && num2>0 && num2>=num1){
							out.add(new Range(num1-1,num2-1));
						}else{
							return null;
						}

					} catch (Exception e) {
						return null;
					}
					
				}
			}else{
				try {
					int num = Integer.parseInt(strRange);
					if (num>0){
						out.add(new Range(num-1,num-1));
					}else{
						return null;
					}
				}catch(Exception e){
					return null;
				}
			}
		}
		Range[] result = new Range[out.size()];
		result = out.toArray(result);
		return result;
	}

	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
		String[] inputTokens = input.split(LINE_SEPARATOR);
		Range[] ranges = convertListToRange(list);
		if(ranges==null){
			return ERROR_INVALID_CUT_LIST;
		}
		String out = "";
		boolean isFirstLine = true;
		for (int i = 0; i < inputTokens.length; i++) {

			if (isFirstLine){
				isFirstLine=false;
			}else{
				out+= LINE_SEPARATOR;
			}

			String line = inputTokens[i];
			StringBuilder outLine = new StringBuilder();
			for (int j = 0; j < line.length(); j++) {
				for (int k = 0; k < ranges.length; k++) {
					if (ranges[k].start<=j && j<=ranges[k].end){
						outLine.append(line.charAt(j));
						break;
					}
				}
			}
			out+= outLine.toString();
		}
		return out;
	}

	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {

		if (delim==null || delim.length()!=1){
			delim = "\t";
		}
		if (list==null){
			return "";
		}
		if (input==null){
			return "";
		}
		String[] inputTokens = input.split(LINE_SEPARATOR);
		Range[] ranges = convertListToRange(list);
		if(ranges==null){
			return ERROR_INVALID_FIELD_LIST;
		}
		String out = "";
		boolean isFirstLine = true;
		for (int i = 0; i < inputTokens.length; i++) {

			if(isFirstLine){
				isFirstLine = false;
			}else{
				out+= LINE_SEPARATOR;
			}
			String line = inputTokens[i];
			if (line.indexOf(delim)==-1){
				//don't touch this line
				out += inputTokens[i];
			}else{
				String[] lineTokens = line.split(delim);
				StringBuilder outLine = new StringBuilder();
				boolean isFirstTokenInLine=true;
				for (int j = 0; j < lineTokens.length; j++) {

					for (int k = 0; k < ranges.length; k++) {
						if (ranges[k].start<=j && j<=ranges[k].end){
							if(isFirstTokenInLine){
								isFirstTokenInLine=false;
							}else{
								outLine.append(delim);
							}
							outLine.append(lineTokens[j]);

							break;
						}
					}
				}
				out+= outLine.toString();
			}
			
		}
		return out;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
    private String readContentsOfFile(File file) throws IOException {
    	String out = "";
    	boolean isFirstLine = true;
    	String strLine;
        FileInputStream is;
        is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while((strLine = br.readLine())!= null){
        	if (isFirstLine){
        		isFirstLine = false;
        		out += strLine;
        	}else{
        		out += LINE_SEPARATOR + strLine;
        	}
        }
        is.close();

        return out;
    }

}
