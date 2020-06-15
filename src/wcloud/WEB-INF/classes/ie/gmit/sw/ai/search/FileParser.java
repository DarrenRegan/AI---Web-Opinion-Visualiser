package ie.gmit.sw.ai.search;
//Darren Regan - G00326934 - Group C - 4th yr AI Project
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class FileParser {
	private static String fileName = "ignoreWords.txt";
	private static FileParser instance = new FileParser();
	
	private FileParser() {
		//constructor
	}
	
	public static FileParser getInstance() {
		return instance;
	}
	
	public static TreeSet<String> getIgnoreWords() {
        TreeSet<String> ignoreListOfWords = new TreeSet<>();
        //Parse File
        try {
        	String line;
            File dir = new File(fileName);           
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dir)));       
            
            while ((line = br.readLine()) != null) {
            	ignoreListOfWords.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ignoreListOfWords;
    }	
}

