package wfsnrnet.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LogFile {
	
	public static void setLogFile(String logFile){
		(new File(logFile)).delete(); 
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile);
        } catch(IOException ioe) {
            System.err.println("redirection not possible: "+ioe);
            System.exit(-1);
        }
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
	}
	
}