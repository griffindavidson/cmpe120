package sim;

import java.util.List;

public class CPU {
    private int programCounter, oldProgramCounter;
    private byte memory[] = new byte[0x0ffffff3];
    private int program[];

    private int instr;
    private int opcode;
    private int rd;
    private int f3;
    private int rs1;
    private int rs2;
    private int f7;
    private int immi;
    private int imms;
    private int immb;
    private int immu;
    private int immj;

    private int exit;
    private boolean jump;

    public CPU() {
        exit = -1;
        jump = false;
        RF.registers[2] = memory.length - 5;
    }

    public boolean step() {
        oldProgramCounter = programCounter;
        instr = program[programCounter / 4];
        opcode = instr & 0x7f;
        rd  = (instr >> 7) & 0x1f;
        f3 = (instr >> 12) & 0x7;
        rs1 = (instr >> 15) & 0x1f;
        rs2 = (instr >> 20) & 0x1f;
        f7 = (instr >> 25) & 0x7f;
        immi = instr >> 20;
        imms = ((instr >> 7) & 0x1f) + ((instr >> 25) << 5);
        immb = (((instr >> 8) & 0x0f) << 1) + (((instr >> 25) & 0x3f) << 5) + (((instr >> 7) & 0x01) << 11) + ((instr >> 31) << 12);
        immu = instr & (0xfffff << 12);
        immj = (((instr >> 21) & 0x3ff) << 1) + (((instr >> 20) & 0x001) << 11) + (instr & (0x0ff << 12)) + ((instr >> 31) << 20);
        switch (opcode) {
            case 0x37: // 0110111 - U-TYPE LOAD UPPER IMMEDIATE (LUI)
                RF.registers[rd] = immu;
                break;
            case 0x17: // 0010111 - U-TYPE ADD UPPER IMMEDIATE TO PC (AUIPC)
                RF.registers[rd] = programCounter + immu;
                break;
            case 0x6f: // 1101111 - JUMP AND LINK (JAL)
                if (rd != 0)
                	RF.registers[rd] = programCounter + 4;
                jumpImm(immj);
                break;
            case 0x67: // 1100111 - JUMP AND LINK REGISTER (JALR)
                if (rd != 0)
                	RF.registers[rd] = programCounter + 4;
                jump = true;
                programCounter = RF.registers[rs1] + immi;
                break;
            case 0x63: // 1100011 B-TYPE
                switch (f3) {
                    case 0x0: // 000 BRANCH IF EQUAL (BEQ)
                        if (RF.registers[rs1] == RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x1: // 001 BRANCH IF NOT EQUAL (BNE)
                        if (RF.registers[rs1] != RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x4: // 100 BRANCH LESS THAN (BLT)
                        if (RF.registers[rs1] < RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x5: // 101 BRANCH GREATER THAN (BGE)
                        if (RF.registers[rs1] >= RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x6: // 110 BRANCH IF LESS THAN UNSIGNED (BLTU)
                        if ((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs2] < 0))
                            jumpImm(immb);
                        break;
                    case 0x7: // 111 BRANCH IF GREATER THAN OR EQUAL UNSIGNED (BGEU)
                        if (!((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs2] < 0)))
                            jumpImm(immb);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x03: // 0000011 I-TYPE STORE
                switch (f3) {
                    case 0x0: // 000 LOAD BYTE (LB)
                        RF.registers[rd] = memory[RF.registers[rs1] + immi];
                        break;
                    case 0x1: // 001 LOAD HALF (LH)
                        RF.registers[rd] = memory[RF.registers[rs1] + immi] & 0xff;
                        RF.registers[rd] += (memory[(RF.registers[rs1] + immi) + 1]) << 8;
                        break;
                    case 0x2: // 010 LOAD WORD (LW)
                        RF.registers[rd] = 0;
                        for (int i =0; i < 4; i++)
                            RF.registers[rd] += ((memory[(RF.registers[rs1] + immi) + i] & 0xff) << (8 * i));
                        break;
                    case 0x4: // 100 LOAD BYTE UNSIGNED (LBU)
                        RF.registers[rd] = memory[RF.registers[rs1] + immi] & 0xff;
                        break;
                    case 0x5: // 101 LOAD HALF UNSIGNED (LHU)
                        RF.registers[rd] = (memory[RF.registers[rs1] + immi] & 0xff);
                        RF.registers[rd] += (memory[(RF.registers[rs1] + immi) + 1] & 0xff) << 8;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x23: // 0100011 - I-TYPE LOAD
                switch (f3) {
                    case 0x0: // 000 STORE BYTE (SB)
                        memory[RF.registers[rs1] + imms] = (byte) (RF.registers[rs2] & 0xff);
                        break;
                    case 0x1: // 001 STORE HALF (SH)
                        memory[RF.registers[rs1] + imms] = (byte) (RF.registers[rs2] & 0xff);
                        memory[RF.registers[rs1] + imms + 1] = (byte) ((RF.registers[rs2] >> 8) & 0xff);
                        break;
                    case 0x2: // 010 STORE WORD (SW)
                        for (int i = 0; i < 4; i++)
                            memory[RF.registers[rs1] + imms + i] = (byte) ((RF.registers[rs2] >> (8 * i)) & 0xff);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x13: // 0010011 - I-TYPE
                switch (f3) {
                    case 0x0: // 000 ADD IMMEDIATE (ADDI)
                        RF.registers[rd] = RF.registers[rs1] + immi;
                        break;
                    case 0x1: // 001 SHIFT LEFT LOGICAL IMMEDIATE (SLLI)
                        RF.registers[rd] = RF.registers[rs1] << (immi & 0x1f);
                        break;
                    case 0x2: // 010 SET ON LESS THAN IMMEDIATE (SLTI)
                        RF.registers[rd] = RF.registers[rs1] < immi ? 1 : 0;
                        break;
                    case 0x3: // 011 SET ON LESS THAN IMMEDIATE UNSIGNED (SLTIU)
                        RF.registers[rd] = ((RF.registers[rs1] < immi) ^ (RF.registers[rs1] < 0) ^ (immi < 0)) ? 1 : 0;
                        break;
                    case 0x4: // 100 BITWISE EXCLUSIVE OR IMMEDIATE (XORI)
                        RF.registers[rd] = RF.registers[rs1] ^ immi;
                        break;
                    case 0x5: // 101 
                        if ((immi >>> 7) == 0x00) // SHIFT RIGHT LOGICAL IMMEDIATE (SRLI)
                            RF.registers[rd] = RF.registers[rs1] >>> (immi & 0x1f);
                        else // SHIFT RIGHT ARITHMETIC IMMEDIATE (SRAI)
                            RF.registers[rd] = RF.registers[rs1] >> (immi & 0x1f);
                        break;
                    case 0x6: // 110 BITWISE OR IMMEDIATE (ORI)
                        RF.registers[rd] = RF.registers[rs1] | immi;
                        break;
                    case 0x7: // 111 BITWISE AND IMMEDIATE (ANDI)
                        RF.registers[rd] = RF.registers[rs1] & immi;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x33: // 0110011 - R-TYPE
                switch (f3) {
                    case 0x0: // 000
                        if (f7 == 0x00) // ADDITION (ADD)
                            RF.registers[rd] = RF.registers[rs1] + RF.registers[rs2];
                        else // SUBTRACTION (SUB)
                            RF.registers[rd] = RF.registers[rs1] - RF.registers[rs2];
                        break;
                    case 0x1: // 001 SHIFT LEFT LOGICAL (SLL)
                        RF.registers[rd] = RF.registers[rs1] << (RF.registers[rs2] & 0x1f);
                        break;
                    case 0x2: // 010 SET IF LESS THAN (SLT)
                        RF.registers[rd] = RF.registers[rs1] < RF.registers[rs2] ? 1 : 0;
                        break;
                    case 0x3: // 011 SET IF LESS THAN UNSIGNED (SLTU)
                        RF.registers[rd] = ((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs1] < 0) ^ (RF.registers[rs2] < 0)) ? 1 : 0;
                        break;
                    case 0x4: // 100 BTIWISE EXCLUSIVE OR (XOR)
                        RF.registers[rd] = RF.registers[rs1] ^ RF.registers[rs2];
                        break;
                    case 0x5: // 101
                        if (f7 == 0x00) // SHIFT RIGHT LOGICAL (SRL)
                            RF.registers[rd] = RF.registers[rs1] >>> (RF.registers[rs2] & 0x1f);
                        else // SHIFT RIGHT ARITHMETIC (SRA)
                            RF.registers[rd] = RF.registers[rs1] >> (RF.registers[rs2] & 0x1f);
                        break;
                    case 0x6: // 110 BITWITSE OR (OR)
                        RF.registers[rd] = RF.registers[rs1] | RF.registers[rs2];
                        break;
                    case 0x7: // 111 BITWISE AND (AND)
                        RF.registers[rd] = RF.registers[rs1] & RF.registers[rs2];
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
        }
        if (!jump) {
            programCounter += 4;
        } else jump = false;
        if (programCounter / 4 >= program.length || exit != -1) {
            return false;
        } else return false;
    }

    private void jumpImm(int immType) {
		programCounter += immType;
		jump = true;
	}

    private void printOpCode() {
        System.out.println("Opcode: " + String.format("0x%01X", opcode));
    }

    public void loadProgram(List<String> programLines) {
        this.program = new int[programLines.size()];
        for (int i = 0; i < programLines.size(); i++) {
            this.program[i] = parseInstruction(programLines.get(i));
        }
    }
    
    private int parseInstruction(String line) {
        // Ensure the line has exactly 32 characters
    	int length = line.length();
        if (line.length() != 32) {
            throw new IllegalArgumentException("Invalid instruction format: " + line);
        }

        int instruction = 0;

        // Convert each character of the line to the corresponding bit in the instruction
        for (int i = 0; i < 32; i++) {
            char bitChar = line.charAt(i);
            int bitValue = Character.getNumericValue(bitChar);

            // Ensure the character is a valid binary digit
            if (bitValue != 0 && bitValue != 1) {
                // Print the problematic instruction line for debugging
                System.out.println("Invalid binary digit in instruction: " + bitChar);
                System.out.println("Problematic instruction line: " + line);
                
                throw new IllegalArgumentException("Invalid binary digit in instruction: " + bitChar);
            }

            // Set the corresponding bit in the instruction
            instruction = (instruction << 1) | bitValue;
        }

        return instruction;
    }


    public int[] getReg() {
        return RF.registers;
    }

    public int getExitCode() {
        return exit;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getOldProgramCounter() {
        return oldProgramCounter;
    }
}
