package sim;

import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
		Scanner scan = new Scanner(System.in);
		System.out.println("File to read: ");
		String fileName = scan.nextLine();

    	Reader test = new Reader();
		test.readFile(fileName);		

		ArrayList<String> instructions = test.getInstructions();
		String instruction;
		int[] pgrm = new int[4];
		for (int i = 0; i < instructions.size(); i+= 4) {
			instruction = "";
			if (instructions.size() - i < 4) {
				break;
			}
			for (int j = 0; j < 4; j++) {
				instruction += instructions.get(i + j);
				pgrm[j] = Integer.parseInt(instructions.get(i + j));
			}
			CPU cpu = new CPU(instruction);
			cpu.loadProgram(pgrm);

			boolean nextStep = true;
			while(nextStep) nextStep = cpu.step();
			System.out.println("\nThe content of the registers was:");
			int[] reg = cpu.getReg();
			for (int k = 0; k < reg.length; k++)
				System.out.println("x" + String.format("%02d", k) + ": " + String.format("0x%08X", reg[k]));
			System.out.println();
		}
		

		// boolean exitProgram = false;
        // boolean breakpoint = false;
        // String[] breakpoints = new String[5];
        // int breakpointCount = 0;
		
		// // currently an infinite loop
        // while (!exitProgram) {
        //     System.out.println("Select an option:");
        //     System.out.println("r - Run the entire program in one go till it hits a breakpoint or exits.");
        //     System.out.println("s - Run the next instruction and then stop and wait for the next command.");
        //     System.out.println("rf - Return the contents of the register from the register file (x0 must always stay 0).");
        //     System.out.println("0x12345678 - Return the contents from the address 0x12345678 in the data memory.");
        //     System.out.println("pc - Return the value of the PC.");
        //     System.out.println("insn - Print the assembly of the instruction that will be executed next.");
        //     System.out.println("b [pc] - Put a breakpoint at a particular/specified [pc].");
        //     System.out.println("c - Continue execution until it hits the next breakpoint pc or exits.");

        //     String option = scan.nextLine();

        //     switch (option) {
        //         case "r":
        //             // Implement logic for running the entire program till a breakpoint is hit or exits
        //             break;
        //         case "s":
        //             // Implement logic for running the next instruction and then stopping
        //             break;
        //         case "rf":
        //             System.out.println("Select a register to obtain its cocntents\nRegisters x0 to x31 (integer value)");
		// 			int register = scan.nextInt();
		// 			scan.nextLine();
		// 			System.out.println("x" + register + ": " + cpu.getReg()[register]);
        //         case "0x12345678":
        //             // Implement logic for returning contents from a specified address in data memory
        //             break;
        //         case "pc":
        //             // Implement logic for returning the value of the PC
        //             break;
        //         case "insn":
        //             // Implement logic for printing the assembly of the next instruction
        //             break;
        //         case "b [pc]":
        //             // Implement logic for putting a breakpoint at a specified [pc]
        //             break;
        //         case "c":
                    
        //             break;
        //         default:
        //             System.out.println("Invalid option. Please try again.");
        //     }
		// }
		// test.readFile("addi_nohazard.dat");
		// test.readFile("addi_hazards.dat");
		// test.readFile("i_type.dat");
    	// test.readFile("r_type.dat");

    	// test.readAddiNoHazard();
    	// test.readAddiHazards();
    	// test.readIType();
    	// test.readRType();
    }
}
