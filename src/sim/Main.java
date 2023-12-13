package sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main 
{
	static boolean debug;

	private static void runProgram(Scanner consoleReader) {

		while (true) {
			List<String> programLines = getTheProgramFromAFile(consoleReader);
			CPU cpu = new CPU();
			cpu.loadProgram(programLines);

			boolean nextStep = true;
			while (nextStep) {
				nextStep = cpu.step();
				if (debug)
					printRG(cpu); // For testing purpose
			}

			System.out.println("The content of the registers was:\n");
			int[] reg = cpu.getReg();
			for (int i = 0; i < reg.length; i++)
				System.out.println("x" + String.format("%02d", i) + ": " + String.format("0x%08X", reg[i]));
			System.out.println();

			if (debug)
				System.out.println("Exit code was: " + cpu.getExitCode());

			System.out.println("Do you want to save the registers to a file? (Y/n):");
			if (!getScannerString(consoleReader).toLowerCase().equals("n")) {
				System.out.println("Write name of the output file, it automatic adds .res: ");
				String nameOfOutputFile = getScannerString(consoleReader);
				printRegistersToFile(cpu, nameOfOutputFile);
			}

			System.out.println("Do you want to run another program? (Y/n):");
			if (getScannerString(consoleReader).toLowerCase().equals("n"))
				break;
		}
	}

	private static List<String> getTheProgramFromAFile(Scanner consoleReader) {
	    List<String> programFile = getProgramFile(consoleReader);
	    List<String> programLines = new ArrayList<>();

	    for (int i = 0; i < programFile.size() - 3; i += 4) {
	        int instructionValue = 0;
	        for (int x = 0; x < 4; x++) {
	            BigInteger byteValue = new BigInteger(programFile.get(i + x), 2);
	            instructionValue += byteValue.intValue() << (8 * (3 - x));
	        }
	        programLines.add(String.format("%32s", Integer.toBinaryString(instructionValue)).replace(' ', '0'));
	    }

	    return programLines;
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
            	test.readFile(listOfFiles[x].getName());
                return test.getInstructions();
            }
            else
                System.out.println("Please enter a valid program number!");
	    }
	}


	private static void printRG(CPU cpu1) {
		System.out.print("After PC: " + cpu1.getOldProgramCounter()/4 + " ");
		for (int i = 0; i < cpu1.getReg().length; ++i) {
			System.out.print(String.format("0x%08X", cpu1.getReg()[i]) + " ");
		}
		System.out.println();
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
		debug = false;

		System.out.println("Welcome to a RISC-V simulator \n" + "Made by Griffin, Lac, Viet, Karan, and Ali");
		while (true) {
			System.out.print("1: Run a program\n" + "2: Debugging\n" + "3: Exit \n");
//			System.out.println("Select an option:");
//            System.out.println("r - Run a program");
//            System.out.println("2 - Run the next instruction and then stop and wait for the next command.");
//            System.out.println("3 - Return the contents of the register from the register file (x0 must always stay 0).");
//            System.out.println("4 0x12345678 - Return the contents from the address 0x12345678 in the data memory.");
//            System.out.println("5 - Return the value of the PC.");
//            System.out.println("6 insn - Print the assembly of the instruction that will be executed next.");
//            System.out.println("7 [pc] - Put a breakpoint at a particular/specified [pc].");
//            System.out.println("8 - Continue execution until it hits the next breakpoint pc or exits.");
			int number = getScannerInt(consoleReader);
			if (number == 1)
				runProgram(consoleReader);
			else if (number == 2)
				debug = !debug;
			else
				break;
		}
		System.out.println("Thank you for using the simulator");
		
		
    }
}
