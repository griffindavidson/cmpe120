package sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {

	private void readFile(String fileName) {
		FileReader reader = null;
		BufferedReader buffer = null;
		ArrayList<String> instructions = new ArrayList<>();
		String instruction = null;
		
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		buffer = new BufferedReader(reader);
		
		try {
			while ((instruction = buffer.readLine()) != null)
			{
				// Every file has x/4 lines, since each line is one byte and each file needs to have
				// 4 bytes per instruction
				for (int i = 0; i < 3; i++)
				{
					instruction += buffer.readLine();
				}
				
				// perhaps some instruction execution block should go here
				// since by now a complete instruction has formed.... we may
				// not need the arraylist to hold instructions
				
				instructions.add(instruction);
				System.out.println(instruction);
				
				String opcode = instruction.substring(25);
				System.out.println("Opcode: " + opcode);
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Instruction parse failure");
			e.printStackTrace();
		}
	}
	
	public void readAddiNoHazard() {
		System.out.println("Reading: addi_nohazard.dat");
		readFile("addi_nohazard.dat");
	}
	
	public void readAddiHazards() {
		System.out.println("Reading: addi_hazards.dat");
		readFile("addi_hazards.dat");
	}
	
	public void readIType() {
		System.out.println("Reading: i_type.dat");
		readFile("i_type.dat");
	}
	
	public void readRType() {
		System.out.println("Reading: r_type.dat");
		readFile("r_type.dat");
	}
}
