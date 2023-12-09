package sim;

import java.io.*;
import java.util.ArrayList;

public class Main 
{
    public static void main(String[] args) 
    {
    	
    	FileReader reader = null;
    	BufferedReader buffer = null;
    	ArrayList<String> instructions = new ArrayList<>();
    	String instruction = null;
    	
    	try {
			reader = new FileReader("addi_nohazard.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
    	
    	buffer = new BufferedReader(reader);
    	
    	try {
			while ((instruction = buffer.readLine()) != null)
			{
				for (int i = 0; i < 3; i++)
				{
					instruction += buffer.readLine();
				}
				
				System.out.println(instruction);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Instruction parse failure");
			e.printStackTrace();
		}
    	
    	
    	
    
        
  
      
    }
}
