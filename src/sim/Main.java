package sim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static void runProgram(Scanner consoleReader) {

		while (true) {
			List<String> programLines = getProgramFile(consoleReader);
			CPU cpu = new CPU();
			cpu.loadProgram(programLines);

			boolean nextStep = true;
			while (nextStep) {
				nextStep = cpu.step();
			}

			System.out.println("The content of the registers was:\n");
			int[] reg = cpu.getReg();
			for (int i = 0; i < reg.length; i++)
				System.out.println("x" + String.format("%02d", i) + ": " + String.format("0x%08X", reg[i]));
			System.out.println();

			System.out.println("Do you want to run another program? (Y/n):");
			if (getScannerString(consoleReader).toLowerCase().equals("n"))
				break;
		}
	}
	
	private static void runStep(Scanner consoleReader) {
		Scanner temp = consoleReader;
		List<String> programLines = getProgramFile(consoleReader);
		CPU cpu = new CPU();
		cpu.loadProgram(programLines);
		outerloop: while (true) {
			if(!cpu.step()) {
				break;
			}
			
			System.out.println("The content of the registers are currently:\n");
			int[] reg = cpu.getReg();
			for (int i = 0; i < reg.length; i++)
				System.out.println("x" + String.format("%02d", i) + ": " + String.format("0x%08X", reg[i]));
			System.out.println();
			System.out.println("s - Run the next instruction and then stop and wait for the next command.");
            System.out.println("Enter a register (from x0 - x31) - Return the contents of the register from the register file (x0 must always stay 0).");
            System.out.println("Enter address (e.g. 0x12345678) - Return the contents from the address 0x12345678 in the data memory.");
            System.out.println("pc - Return the value of the PC.");
            System.out.println("insn - Print the assembly of the instruction that will be executed next.");
            System.out.println("b [pc] - Put a breakpoint at a particular/specified [pc].");
            System.out.println("c - Continue execution until it hits the next breakpoint pc or exits.");
			innerloop: while (true) {
	            String input = getScannerString(consoleReader);
	            int inputLen = input.length();
	            if (input.toLowerCase().equals("s"))
					break innerloop;
	            else if (input.toLowerCase().startsWith("x") && (input.length() < 4)) {
	            	System.out.println("x" + String.format("%02d", Integer.parseInt(input.substring(1))) + ": " + String.format("0x%08X", reg[Integer.parseInt(input.substring(1))]));
					
				} else if (input.toLowerCase().equals("pc")) {
					System.out.println(cpu.getProgramCounter());
				} else if (input.toLowerCase().equals("insn")) {
					System.out.println(cpu.getAssembly());
				} else if (input.toLowerCase().equals("c")) {
					runThrough(cpu);
					break outerloop;
				}
				else
					break innerloop;
			}
		}
	}
	
	private static void runThrough(CPU cpu) {
		boolean nextStep = true;
		while (nextStep) {
			nextStep = cpu.step();
		}

		System.out.println("The content of the registers was:\n");
		int[] reg = cpu.getReg();
		for (int i = 0; i < reg.length; i++)
			System.out.println("x" + String.format("%02d", i) + ": " + String.format("0x%08X", reg[i]));
		System.out.println();
	}


	private static List<String> getProgramFile(Scanner consoleReader) {
	    System.out.println("Which program do you want to load? Type in the wanted number");
	    File folder = new File("test");
	    File[] listOfFiles = folder.listFiles();
		
	    int i = 0;
	    for (File f : listOfFiles)
	        System.out.print(i++ + ": " + f.getName() + "; ");

	    System.out.println();
	    
	    Reader test = new Reader();
	    
	    while (true) {
            int x = getScannerInt(consoleReader);
            if (x < listOfFiles.length) {
            	test.readFile("test" + File.separator + listOfFiles[x].getName());
                return test.getInstructions();
            }
            else
                System.out.println("Please enter a valid program number!");
	    }
	}

	private static int getScannerInt(Scanner consoleReader) {
		while (true) {
			try {
				return Integer.parseInt(getScannerString(consoleReader));
			} catch (NumberFormatException e) {
				System.out.println("The input must only be numbers and no letters");
			}
		}
	}

	private static String getScannerString(Scanner consoleReader) {
		while (true) {
			String text = consoleReader.nextLine();
			if(text.length() != 0)
				return text;
			else
				System.out.println("Please input a valid string");
		}
	}

	private static void printRegistersToFile(CPU cpu, String nameOfOutputFile) {
		byte data[] = new byte[cpu.getReg().length * 4];
		for (int i = 0; i < 32; i++)
			for (int x = 0; x < 4; x++)
				data[i*4 + x] = (byte) ((cpu.getReg()[i] >> 8 * x) & 0xff);
		Path file = Paths.get(nameOfOutputFile + ".res");
		try {
			Files.write(file, data);
		} catch (IOException e) {
			System.out.println("File was not saved");
		}
	}
	
    public static void main(String[] args) 
    {
    	Scanner consoleReader = new Scanner(System.in);
    	boolean breakpoint = false;
        String[] breakpoints = new String[5];
        int breakpointCount = 0;

		System.out.println("Welcome to a RISC-V simulator \n" + "Made by Griffin, Lac, Viet, Karan, and Ali");
		while (true) {
			System.out.println("Select an option:");
            System.out.println("r - Run a program");
            System.out.println("s - Run the next instruction and then stop and wait for the next command.");
            System.out.println("other - Exit program");
			String input = getScannerString(consoleReader);
			if (input.equals("r"))
				runProgram(consoleReader);
			else if (input.toLowerCase().equals("s"))
				runStep(consoleReader);
			else
				break;
		}
		System.out.println("Thank you for using the simulator");
		System.exit(0);
		
    }
}
