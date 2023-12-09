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
			buffer.readLine(); // skip spacer byte
			while (true)
			{
				instruction = buffer.readLine();
				// Every file has x/4 lines, since each line is one byte and each file needs to have
				// 4 bytes per instruction
				for (int i = 0; i < 3; i++)
				{
					String readByte = buffer.readLine();
					
					instruction += readByte;
					
					if (readByte == null)
					{
						instruction = null;
						break;
					}
				}
				
				if (instruction == null) {
					System.out.println(""); // spacer for output
					break;
				}
				
				// perhaps some instruction execution block should go here
				// since by now a complete instruction has formed.... we may
				// not need the arraylist to hold instructions
				
				String hex = convertToHex(instruction);		
				
				System.out.println(hex);
				
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
	
	private String convertToHex(String instruction)
	{
		String hex = "";
		
		for (int i = 0; i < instruction.length(); i += 4) {
			String value = instruction.substring(i, i + 4);
			
			if (value.equals("0000")) {
				hex += "0";
			}
			else if (value.equals("0001")) {
				hex += "1";
			}
			else if (value.equals("0010")) {
				hex += "2";
			}
			else if (value.equals("0011")) {
				hex += "3";
			}
			else if (value.equals("0100")) {
				hex += "4";
			}
			else if (value.equals("0101")) {
				hex += "5";
			}
			else if (value.equals("0110")) {
				hex += "6";
			}
			else if (value.equals("0111")) {
				hex += "7";
			}
			else if (value.equals("1000")) {
				hex += "8";
			}
			else if (value.equals("1001")) {
				hex += "9";
			}
			else if (value.equals("1010")) {
				hex += "a";
			}
			else if (value.equals("1011")) {
				hex += "b";
			}
			else if (value.equals("1100")) {
				hex += "c";
			}
			else if (value.equals("1101")) {
				hex += "d";
			}
			else if (value.equals("1110")) {
				hex += "e";
			}
			else {
				hex += "f";
			}
		}
		
		return hex;
	}
}
