package grepy;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FileManager {
	public FileManager() {
		
	}
	
	public ArrayList<String> readFile(String file) {
		ArrayList<String> fileContents = new ArrayList<String>();
		BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                fileContents.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return fileContents;
	}
	
	public void writeFile(String fileName, ArrayList<String> output) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			for(int i = 0 ; i < output.size() ; i++) {
				writer.write(output.get(i));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(writer != null) {
				writer.close();
			}
		}
		
	}

}
