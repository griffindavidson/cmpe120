package sim;

import java.io.*;
import java.util.ArrayList;

public class Main 
{
    public static void main(String[] args) 
    {
    	
    	FileReader reader = null;
    	BufferedReader buffer = null;
    	ArrayList<String> instructionMemory = new ArrayList<>();
    	String line = null;
    	
    	try {
			reader = new FileReader("addi_nohazard.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
    	
    	buffer = new BufferedReader(reader);
    	
    	try {
			while ((line = buffer.readLine()) != null) {
				System.out.println(line);
				instructionMemory.add(line);			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
    	
    
        
  
      
    }
}
