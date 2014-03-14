package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CommTool extends ATool implements ICommTool {
	private final static int BUF_SIZE = 4096;
	private final static int CHECK_DEFAULT = 0;
	private final static int CHECK_ORDER = 1;
	private final static int NOCHECK_ORDER= 2;
	
	
	public CommTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		statusError();
		
		boolean isCheckOrder = false;
		boolean isNoCheckOrder = false;

		String fileName1 = null, fileName2 = null;
		
		for (String arg : args) {
			if (arg.equals("-c")) {
				if (!isNoCheckOrder) {
					isCheckOrder = true;
				}
			}
			else if (arg.equals("-d")) {
				if (!isCheckOrder) {
					isNoCheckOrder = true;
				}
			}
			else if (arg.equals("-help")) {
				return getHelp();
			}
			else {	// must be file name
				if (fileName1 == null) {
					fileName1 = arg;
				}
				else if (fileName2 == null) {
					fileName2 = arg;
				}
				else {	// extra non-recognizable arg, return err
					return "Error: comm: unrecognized option '"+arg+"'\nTry 'comm -help' for more information." + System.lineSeparator();
				}
			}
		}
		
		
		File file1, file2;
		Path path1 = null, path2 = null;
		String fileContent1, fileContent2;
		
		try {
			path1 = workingDir.toPath().resolve(fileName1);
			path2 = workingDir.toPath().resolve(fileName2);
			file1 = path1.toFile();
            fileContent1 = readContentsOfFile(file1);
            file2 = path2.toFile();
            fileContent2 = readContentsOfFile(file2);

    		if (!isCheckOrder && !isNoCheckOrder) {
    			return compareFiles(fileContent1, fileContent2);
    		}
    		else if (isCheckOrder) {
    			return compareFilesCheckSortStatus(fileContent1, fileContent2);
    		}
    		else if (isNoCheckOrder) {
    			return compareFilesDoNotCheckSortStatus(fileContent1, fileContent2);
    		}
    		else {	// should be unreachable
    			return "Invalid arguments. -d and -c flags cannot be both present" + System.lineSeparator();
    		}
		}
		catch (InvalidPathException e) {
			return "File invalid." + System.lineSeparator();
		}
        catch (FileNotFoundException ex) {
            return "File not found." + System.lineSeparator();
        } 
        catch (IOException e) {
            return "An error occurred processing this path." + System.lineSeparator();
        }
		catch (Exception e) {
			return "An "+ e +" error has occured";
		}
        
		
	}

	@Override
	public String compareFiles(String input1, String input2) {
		
		return compareFilesHelper(input1, input2, CHECK_DEFAULT);
	}

	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		
		return compareFilesHelper(input1, input2, CHECK_ORDER);
	}

	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		
		return compareFilesHelper(input1, input2, NOCHECK_ORDER);
	}

	private String compareFilesHelper(String input1, String input2, int type) {
		if (input1 == null || input2 == null) {
			return "Internal NullPointerError.\n";
		}
		if (input1.equals("") && input2.equals("")) {
			return "";
		}
		boolean isFile1UnsortedFlagged = false;
		boolean isFile2UnsortedFlagged = false;
		if (type == NOCHECK_ORDER) {
			isFile1UnsortedFlagged = true;
			isFile2UnsortedFlagged = true;
		}
		ArrayList<String> col = new ArrayList<String>();
		String res = "";
		input1.replaceAll("\r\n", "\n");
		input2.replaceAll("\r\n", "\n");
		
		String[] inArr1 = null, inArr2 = null;
		if (input1.equals("")) {
			inArr1 = new String[0];
		}
		else {
			inArr1 = input1.split("\n");
		}
		if (input2.equals("")) {
			inArr2 = new String[0];
		}
		else {
			inArr2 = input2.split("\n");
		}
		
		
		int idx1 = 0, idx2 = 0;
		while (true) {
			
			if (idx1 >= inArr1.length && idx2 >= inArr2.length) {
				break;
			}
			else if (idx1 >= inArr1.length) {

				
				if (idx2 != 0) {
					if (! (inArr2[idx2].compareTo(inArr2[idx2 - 1]) >= 0) && !isFile2UnsortedFlagged) {
						isFile2UnsortedFlagged = true;
						switch(type) {
						case CHECK_DEFAULT:
							col.add("comm: file 2 is not in sorted order" + "\n");
							break;
						case CHECK_ORDER:
							col.add("comm: file 2 is not in sorted order" + "\n");
							for (String str : col) {
								res += str;
							}
							return res;
						case NOCHECK_ORDER:
						default: 
							
						}
					}
				}
				col.add("\t\t"+ inArr2[idx2] +"\n");
				idx2++;
				continue;
			}
			else if (idx2 >= inArr2.length) {
				
				if (idx1 != 0) {
					if (!(inArr1[idx1].compareTo(inArr1[idx1 - 1]) >= 0) && !isFile1UnsortedFlagged) {
						isFile1UnsortedFlagged = true;
						switch (type) {
						case CHECK_DEFAULT:
							col.add("comm: file 1 is not in sorted order"
									+ "\n");
							break;
						case CHECK_ORDER:
							col.add("comm: file 1 is not in sorted order" + "\n");
							for (String str : col) {
								res += str;
							}
							return res;
						case NOCHECK_ORDER:
						default:
						}
					}
				}
				col.add(inArr1[idx1] + "\n");
				idx1++;
				continue;
			}
			
			if (inArr1[idx1].compareTo(inArr2[idx2]) == 0) {
				
				if (idx1 != 0) {
					if (! (inArr1[idx1].compareTo(inArr1[idx1 - 1]) >= 0)  && !isFile1UnsortedFlagged) {
						isFile1UnsortedFlagged = true;
						switch(type) {
							case CHECK_DEFAULT:
								col.add("comm: file 1 is not in sorted order" + "\n");
								break;
							case CHECK_ORDER:
								col.add("comm: file 1 is not in sorted order" + "\n");
								for (String str : col) {
									res += str;
								}
								return res;
							case NOCHECK_ORDER:
							default: 
								
						}
					}
				}
				
				if (idx2 != 0) {
					if (! (inArr2[idx2].compareTo(inArr2[idx2 - 1]) >= 0)  && !isFile2UnsortedFlagged) {
						isFile2UnsortedFlagged = true;
						switch(type) {
						case CHECK_DEFAULT:
							col.add("comm: file 2 is not in sorted order" + "\n");
							break;
						case CHECK_ORDER:
							col.add("comm: file 2 is not in sorted order" + "\n");
							for (String str : col) {
								res += str;
							}
							return res;
						case NOCHECK_ORDER:
						default: 
							
						}
					}
				}
				col.add("\t\t\t\t" + inArr1[idx1] + "\n");
				idx1++;
				idx2++;
			}
			else if (inArr1[idx1].compareTo(inArr2[idx2]) < 0) {
				
				if (idx1 != 0) {
					if (!(inArr1[idx1].compareTo(inArr1[idx1 - 1]) >= 0) && !isFile1UnsortedFlagged) {
						isFile1UnsortedFlagged = true;
						switch (type) {
						case CHECK_DEFAULT:
							col.add("comm: file 1 is not in sorted order"
									+ "\n");
							break;
						case CHECK_ORDER:
							col.add("comm: file 1 is not in sorted order" + "\n");
							for (String str : col) {
								res += str;
							}
							return res;
						case NOCHECK_ORDER:
						default:
						}
					}
				}
				col.add(inArr1[idx1] + "\n");
				idx1++;
				
			}
			else {
				
				if (idx2 != 0) {
					if (! (inArr2[idx2].compareTo(inArr2[idx2 - 1]) >= 0)  && !isFile2UnsortedFlagged) {
						isFile2UnsortedFlagged = true;
						switch(type) {
						case CHECK_DEFAULT:
							col.add("comm: file 2 is not in sorted order" + "\n");
							break;
						case CHECK_ORDER:
							col.add("comm: file 2 is not in sorted order" + "\n");
							for (String str : col) {
								res += str;
							}
							return res;
						case NOCHECK_ORDER:
						default: 
							
						}
					}
				}
				col.add("\t\t"+ inArr2[idx2] +"\n");
				idx2++;
			}
		}
		
		for (String str : col) {
			res += str;
		}
		return res;
	}
	
    private String readContentsOfFile(File file) throws IOException, FileNotFoundException, NullPointerException {
        FileInputStream is;
       	is = new FileInputStream(file);

        StringBuilder sb = new StringBuilder();
        byte buf[] = new byte[BUF_SIZE];
        int read = 0;
        while ((read = is.read(buf)) != -1) {
            sb.append(new String(Arrays.copyOf(buf, read), StandardCharsets.UTF_8));
        }
        is.close();
        return sb.toString();
    }
	
	@Override
	public String getHelp() {

		return "comm : Compares two sorted files line by line. With no options, produce three-column output. Column one\n"
				+ "contains lines unique to FILE1, column two contains lines unique to FILE2, and column three contains lines\n"
				+ "common to both files.\n\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2\n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable\n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n";

	}

}
