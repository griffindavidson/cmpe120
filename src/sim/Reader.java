package sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
	private ArrayList<String> instructions = new ArrayList<>();

	public void readFile(String fileName) {
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

		System.out.println("Reading: " + fileName);
		try {
			buffer.readLine(); // skip spacer byte
			while (true)
			{
				String readByte = buffer.readLine();
				instruction = readByte;

				if(instruction == null) break;
				instructions.add(readByte);
				// Every file has x/4 lines, since each line is one byte and each file needs to have
				// 4 bytes per instruction
				for (int i = 0; i < 3; i++)
				{
					readByte = buffer.readLine();

					if (readByte == null) {
						instruction = null;
						break;
					}
					instruction += readByte;
					instructions.add(readByte);
				}

				// perhaps some instruction execution block should go here
				// since by now a complete instruction has formed.... we may
				// not need the arraylist to hold instructions

				// instructions.add(instruction);
				

				if (instruction != null && instruction.length() > 25) {
					System.out.println(instruction);
					String opcode = instruction.substring(25);
					System.out.println("Opcode: " + opcode);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Instruction parse failure");
			e.printStackTrace();
		}
	}

	public ArrayList<String> getInstructions() {
        return instructions;
    }

	public ArrayList<String> getInstructionStrings() {
        ArrayList<String> instructionStrings = new ArrayList<>();
        StringBuilder instructionBuilder = new StringBuilder();

        for (int i = 0; i < instructions.size(); i += 4) {
            for (int j = 0; j < 4; j++) {
                if (i + j < instructions.size()) {
                    instructionBuilder.append(instructions.get(i + j));
                }
            }

            instructionStrings.add(instructionBuilder.toString());
            instructionBuilder.setLength(0); // Clear the StringBuilder for the next iteration
        }

        return instructionStrings;
    }

	// public void readAddiNoHazard() {
	// 	System.out.println("Reading: addi_nohazard.dat");
	// 	readFile("addi_nohazard.dat");
	// }

	// public void readAddiHazards() {
	// 	System.out.println("Reading: addi_hazards.dat");
	// 	readFile("addi_hazards.dat");
	// }

	// public void readIType() {
	// 	System.out.println("Reading: i_type.dat");
	// 	readFile("i_type.dat");
	// }

	// public void readRType() {
	// 	System.out.println("Reading: r_type.dat");
	// 	readFile("r_type.dat");
	// }
}