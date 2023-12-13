package sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Reader {
	private List<String> instructions = new ArrayList<>();

	public void readFile(String fileName) {
	    FileReader reader = null;
	    BufferedReader buffer = null;

	    try {
	        reader = new FileReader(fileName);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }

	    buffer = new BufferedReader(reader);

	    System.out.println("Reading: " + fileName);

	    // Read lines into a list
	    List<String> lines = new ArrayList<>();
	    String line;
	    try {
	        while ((line = buffer.readLine()) != null) {
	            lines.add(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    // Process the lines in reverse order
	    for (int i = lines.size() - 1; i >= 0; i--) {
	        String instruction = lines.get(i);
	        for (int j = 0; j < 3; j++) {
	            if (i - 1 >= 0) {
	                instruction = instruction + lines.get(--i);
	            } else {
	                break;
	            }
	        }

	        // Add the reversed instruction to the list
	        instructions.add(instruction);

	        if (instruction.length() > 25) {
	            System.out.println(instruction);
	            String opcode = instruction.substring(25);
	            System.out.println("Opcode: " + opcode);
	        }
	    }
	    
	    Collections.reverse(instructions);
	}


	public List<String> getInstructions() {
        return instructions;
    }

	public ArrayList<String> getInstructionStrings() {
        ArrayList<String> instructionStrings = new ArrayList<>();
        StringBuilder instructionBuilder = new StringBuilder();

        for (int i = 0; i < instructions.size(); i += 4) {
        	instructionBuilder.append(instructions.get(i));
            if ((i + 1) % 4 == 0) {
            	instructionStrings.add(instructionBuilder.toString());
                instructionBuilder.setLength(0); // Clear the StringBuilder for the next iteration
            }
        }

        return instructionStrings;
    }
}