import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Load {
	
	
	private static ArrayList<String> instructionMemory = new ArrayList<String>();
	
    public static void main(String[] args) throws IOException {

        
    	
    	
    	String locat;
        
        System.out.println("Enter complete Location of test:");
        
    	Scanner scan = new Scanner(System.in);
    	locat = scan.next();
    	
    	
    	//enter full location of test
    	 //locat = "C:\\Users\\bkara\\RISCVSimulator\\Simulator\\src\\test";
    
    	 
    	 
    	 Scanner scanner = new Scanner(new File(locat));
        int i = 0;
        
        while(scanner.hasNext()) {
            
        	String nextLine = scanner.next();
        	
        	if(nextLine != null)
        	{
        		instructionMemory.add(nextLine);
        	}

            System.out.println(instructionMemory.get(i));
            i++;
        }
        
        scanner.close();
	    
    }
    
    
    
    
	
	

}
