package sg.edu.nus.comp.cs4218.impl.fileutils;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CatTool extends ATool implements ICatTool {
    /**
     * Constructor
     *
     * @param arguments Arguments the tool is going to be executed with.
     */
    public CatTool(String[] arguments) {
        super(arguments);
    }

    @Override
    public String getStringForFile(File toRead) {
    	statusError();
    	
        if (toRead == null) {
            return null;
        }
        if (toRead.exists() && toRead.isFile() && toRead.canRead()) {
            try {
                FileInputStream fis = new FileInputStream(toRead);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                
                int ch;
                while ((ch = fis.read()) != -1) {
                    baos.write(ch);
                }
                fis.close();
                byte[] file = baos.toByteArray();
                
                statusSuccess();
                return new String(file, StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Executes the tool with args provided in the constructor
     *
     * @param workingDir
     * @param stdin      Input on stdin. NOT THE ARGUMENTS! Can be null.
     * @return Output on stdout
     */
    @Override
    public String execute(File workingDir, String stdin) {
        int argLength = this.args.length;
        if (argLength == 0) {
        	statusSuccess();
            return stdin;
        }

        boolean isFirstStdin = true;

        StringBuilder sb = new StringBuilder();
        for (String arg : this.args) {
            if (Thread.interrupted()) {
                statusSuccess();
                return sb.toString();
            }
            
            if(arg.equals("-")){
                // Process only first stdin argument
                if (isFirstStdin) {
                    isFirstStdin = false;
                    sb.append(stdin);
                }
            }else{
            	String result = getStringForFile(workingDir.toPath().resolve(arg).toFile());
            	if (result == null) {
            		statusError();
            		sb.append(String.format("Error: Could not read file: %s%n", arg));
            		return sb.toString();
            	}
            	sb.append(result);
            }
        }
        return sb.toString();
    }
}
