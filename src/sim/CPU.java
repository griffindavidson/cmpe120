package sim;

import java.util.List;

public class CPU {
    private int programCounter, oldProgramCounter;
    private int reg[] = new int[32];
    private byte memory[] = new byte[0x0ffffff3];
    private int program[];
    private String assemblyCode;

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
        reg[2] = memory.length - 5;
    }

    public boolean step() {
        assemblyCode = "";
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
                reg[rd] = immu;
                assemblyCode = "lui x" + rd + ", " + immu;
                break;
            case 0x17: // 0010111 - U-TYPE ADD UPPER IMMEDIATE TO PC (AUIPC)
                reg[rd] = programCounter + immu;
                assemblyCode = "auipc x" + rd + ", " + immb;
                break;
            case 0x6f: // 1101111 - JUMP AND LINK (JAL)
                if (rd != 0)
                	reg[rd] = programCounter + 4;
                jumpImm(immj);
                assemblyCode = "jal x" + rd + ", " + immj;
                break;
            case 0x67: // 1100111 - JUMP AND LINK REGISTER (JALR)
                if (rd != 0)
                	reg[rd] = programCounter + 4;
                jump = true;
                programCounter = reg[rs1] + immi;
                assemblyCode = "jalr x" + rd + ", " + rs1 + ", " + immi;
                break;
            case 0x63: // 1100011 B-TYPE
                switch (f3) {
                    case 0x0: // 000 BRANCH IF EQUAL (BEQ)
                        if (reg[rs1] == reg[rs2])
                            jumpImm(immb);
                        assemblyCode = "beq x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    case 0x1: // 001 BRANCH IF NOT EQUAL (BNE)
                        if (reg[rs1] != reg[rs2])
                            jumpImm(immb);
                        assemblyCode = "bne x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    case 0x4: // 100 BRANCH LESS THAN (BLT)
                        if (reg[rs1] < reg[rs2])
                            jumpImm(immb);
                        assemblyCode = "blt x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    case 0x5: // 101 BRANCH GREATER THAN (BGE)
                        if (reg[rs1] >= reg[rs2])
                            jumpImm(immb);
                        assemblyCode = "bge x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    case 0x6: // 110 BRANCH IF LESS THAN UNSIGNED (BLTU)
                        if ((reg[rs1] < reg[rs2]) ^ (reg[rs2] < 0))
                            jumpImm(immb);
                        assemblyCode = "bltu x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    case 0x7: // 111 BRANCH IF GREATER THAN OR EQUAL UNSIGNED (BGEU)
                        if (!((reg[rs1] < reg[rs2]) ^ (reg[rs2] < 0)))
                            jumpImm(immb);
                        assemblyCode = "bgeu x" + rs1 + ", " + rs2 + ", " + immb;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
                break;
            case 0x03: // 0000011 I-TYPE LOAD
                switch (f3) {
                    case 0x0: // 000 LOAD BYTE (LB)
                        reg[rd] = memory[reg[rs1] + immi];
                        assemblyCode = "lb x" + rd + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x1: // 001 LOAD HALF (LH)
                        reg[rd] = memory[reg[rs1] + immi] & 0xff;
                        reg[rd] += (memory[(reg[rs1] + immi) + 1]) << 8;
                        assemblyCode = "lh x" + rd + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x2: // 010 LOAD WORD (LW)
                        reg[rd] = 0;
                        for (int i =0; i < 4; i++)
                            reg[rd] += ((memory[(reg[rs1] + immi) + i] & 0xff) << (8 * i));
                        assemblyCode = "lw x" + rd + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x4: // 100 LOAD BYTE UNSIGNED (LBU)
                        reg[rd] = memory[reg[rs1] + immi] & 0xff;
                        assemblyCode = "lbu x" + rd + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x5: // 101 LOAD HALF UNSIGNED (LHU)
                        reg[rd] = (memory[reg[rs1] + immi] & 0xff);
                        reg[rd] += (memory[(reg[rs1] + immi) + 1] & 0xff) << 8;
                        assemblyCode = "lhu x" + rd + ", " + immi + "(x" + rs1 + ")";
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
                break;
            case 0x23: // 0100011 - I-TYPE STORE
                switch (f3) {
                    case 0x0: // 000 STORE BYTE (SB)
                        memory[reg[rs1] + imms] = (byte) (reg[rs2] & 0xff);
                        assemblyCode = "sb x" + rs2 + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x1: // 001 STORE HALF (SH)
                        memory[reg[rs1] + imms] = (byte) (reg[rs2] & 0xff);
                        memory[reg[rs1] + imms + 1] = (byte) ((reg[rs2] >> 8) & 0xff);
                        assemblyCode = "sh x" + rs2 + ", " + immi + "(x" + rs1 + ")";
                        break;
                    case 0x2: // 010 STORE WORD (SW)
                        for (int i = 0; i < 4; i++)
                            memory[reg[rs1] + imms + i] = (byte) ((reg[rs2] >> (8 * i)) & 0xff);
                        assemblyCode = "sw x" + rs2 + ", " + immi + "(x" + rs1 + ")";
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
                break;
            case 0x13: // 0010011 - I-TYPE
                switch (f3) {
                    case 0x0: // 000 ADD IMMEDIATE (ADDI)
                        reg[rd] = reg[rs1] + immi;
                        assemblyCode = "addi x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    case 0x1: // 001 SHIFT LEFT LOGICAL IMMEDIATE (SLLI)
                        reg[rd] = reg[rs1] << (immi & 0x1f);
                        assemblyCode = "slli x" + rd + ", x" + rs1 + ", " + (immi & 0x1f);
                        break;
                    case 0x2: // 010 SET ON LESS THAN IMMEDIATE (SLTI)
                        reg[rd] = reg[rs1] < immi ? 1 : 0;
                        assemblyCode = "slti x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    case 0x3: // 011 SET ON LESS THAN IMMEDIATE UNSIGNED (SLTIU)
                        reg[rd] = ((reg[rs1] < immi) ^ (reg[rs1] < 0) ^ (immi < 0)) ? 1 : 0;
                        assemblyCode = "sltiu x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    case 0x4: // 100 BITWISE EXCLUSIVE OR IMMEDIATE (XORI)
                        reg[rd] = reg[rs1] ^ immi;
                        assemblyCode = "xori x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    case 0x5: // 101 
                        if ((immi >>> 7) == 0x00) { // SHIFT RIGHT LOGICAL IMMEDIATE (SRLI)
                            reg[rd] = reg[rs1] >>> (immi & 0x1f);
                            assemblyCode = "srli x" + rd + ", x" + rs1 + ", " + (immi & 0x1f);
                        } else { // SHIFT RIGHT ARITHMETIC IMMEDIATE (SRAI)
                            reg[rd] = reg[rs1] >> (immi & 0x1f);
                            assemblyCode = "srai x" + rd + ", x" + rs1 + ", " + (immi & 0x1f);
                        }
                        break;
                    case 0x6: // 110 BITWISE OR IMMEDIATE (ORI)
                        reg[rd] = reg[rs1] | immi;
                        assemblyCode = "ori x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    case 0x7: // 111 BITWISE AND IMMEDIATE (ANDI)
                        reg[rd] = reg[rs1] & immi;
                        assemblyCode = "andi x" + rd + ", x" + rs1 + ", " + immi;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
                break;
            case 0x33: // 0110011 - R-TYPE
                switch (f3) {
                    case 0x0: // 000
                        if (f7 == 0x00) { // ADDITION (ADD)
                            reg[rd] = reg[rs1] + reg[rs2];
                            assemblyCode = "add x" + rd + ", x" + rs1 + ", x" + rs2;
                        } else {// SUBTRACTION (SUB)
                            reg[rd] = reg[rs1] - reg[rs2];
                            assemblyCode = "sub x" + rd + ", x" + rs1 + ", x" + rs2;
                        }
                        break;
                    case 0x1: // 001 SHIFT LEFT LOGICAL (SLL)
                        reg[rd] = reg[rs1] << (reg[rs2] & 0x1f);
                        assemblyCode = "sll x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    case 0x2: // 010 SET IF LESS THAN (SLT)
                        reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0;
                        assemblyCode = "slt x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    case 0x3: // 011 SET IF LESS THAN UNSIGNED (SLTU)
                        reg[rd] = ((reg[rs1] < reg[rs2]) ^ (reg[rs1] < 0) ^ (reg[rs2] < 0)) ? 1 : 0;
                        assemblyCode = "sltu x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    case 0x4: // 100 BTIWISE EXCLUSIVE OR (XOR)
                        reg[rd] = reg[rs1] ^ reg[rs2];
                        assemblyCode = "xor x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    case 0x5: // 101
                        if (f7 == 0x00) { // SHIFT RIGHT LOGICAL (SRL)
                            reg[rd] = reg[rs1] >>> (reg[rs2] & 0x1f);
                            assemblyCode = "srl x" + rd + ", x" + rs1 + ", x" + rs2;
                        } else { // SHIFT RIGHT ARITHMETIC (SRA)
                            reg[rd] = reg[rs1] >> (reg[rs2] & 0x1f);
                            assemblyCode = "sra x" + rd + ", x" + rs1 + ", x" + rs2;
                        }
                        break;
                    case 0x6: // 110 BITWITSE OR (OR)
                        reg[rd] = reg[rs1] | reg[rs2];
                        assemblyCode = "or x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    case 0x7: // 111 BITWISE AND (AND)
                        reg[rd] = reg[rs1] & reg[rs2];
                        assemblyCode = "amd x" + rd + ", x" + rs1 + ", x" + rs2;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
                break;
        }
        if (!jump) {
            programCounter += 4;
        } else jump = false;
        if (programCounter / 4 >= program.length || exit != -1) {
            return false;
        } else return true;
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
    	if (line.length() != 32) {
            throw new IllegalArgumentException("Invalid instruction format: " + line);
        }

        int instruction = 0;

        // Convert each character of the line to the corresponding bit in the instruction
        for (int i = 0; i < 32; i++) {
            char bitChar = line.charAt(i);
            int bitValue = bitChar == '0' ? 0 : 1;  // Treat as unsigned integer
            
            // Set the corresponding bit in the instruction
            instruction = (instruction << 1) | bitValue;
        }

        return instruction;
    }


    public int[] getReg() {
        return reg;
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
