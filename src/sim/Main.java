package sim;

import java.io.*;

public class Main 
{
    public static void main(String[] args) 
    {
    	
    	FileReader reader = null;
    	BufferedReader buffer = null;
    	String line = "";
    	
    	try {
			reader = new FileReader("addi_nohazard.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	buffer = new BufferedReader(reader);
    	
    	try {
			while ((line = buffer.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
        
  
      
    }
}
