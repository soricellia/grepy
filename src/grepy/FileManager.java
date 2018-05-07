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
            System.out.println("Error, the file named: "+ file +" does not exist. Finishing execution.");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
            	System.out.println("Error, the file named: "+ file +" does not exist. Finishing execution.");
            }
        }
        
        return fileContents;
	}
	
	public void writeDotFile(String fileName, ArrayList<ArrayList<String>> output) {
		PrintWriter writer = null;
		ArrayList<ArrayList<String>> states = new ArrayList<ArrayList<String>>(); 
		
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			
			//write the digraph portion 
			writer.write("digraph grepy {\n");
			

			// write the states
			for(int i = 0 ; i < output.size() ; i++) {		
				if(states.contains(output.get(i)) && i!=0) {
					// make a link from the last state to this state
					writer.write("\n");
					writer.write(output.get(i-1).get(output.get(i-1).size()-1));
					writer.write("->");
					writer.write(output.get(i).get(0));
					writer.write("\n");
				}else {
					states.add(output.get(i));
					
					for(int j = 0; j < output.get(i).size(); j++) {
						if(i!=0 && j==0) {
							writer.write("->");
						}
						writer.write(output.get(i).get(j));
						if(j < output.get(i).size() -1) {
							writer.write("->");
						}
					}
					
				}
				
			}
			
			// end the digraph
			writer.write("\n}");
			
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
