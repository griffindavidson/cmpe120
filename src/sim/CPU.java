package sim;

public class CPU {
    private int programCounter, oldProgramCounter;
    private byte memory[] = new byte[0x0ffffff3];
    private int program[];

    private int instr;
    private String instruction;
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

    public CPU(String instruction) {
        exit = -1;
        jump = false;
        RF.registers[2] = memory.length - 5;
        this.instruction = instruction;
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
            case 0x37: // 0110111 - U-TYPE LOAD UPPER IMMIEDIATE
                RF.registers[rd] = immu;
            case 0x17: // 0010111 - U-TYPE ADD UPPER IMM TO PC
                RF.registers[rd] = programCounter + immu;
            case 0x6f: // 1101111 - JUMP AND LINK
                if (rd != 0)
                	RF.registers[rd] = programCounter + 4;
                jumpImm(immj);
            case 0x67: // 1100111 - JUMP AND LINK REGISTER
                if (rd != 0)
                	RF.registers[rd] = programCounter + 4;
                jump = true;
                programCounter = RF.registers[rs1] + immi;
            case 0x63: // 1100011 B-TYPE
                switch (f3) {
                    case 0x0: // 000 BRANCH IF EQUAL
                        if (RF.registers[rs1] == RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x1: // 001 BRANCH IF NOT EQUAL
                        if (RF.registers[rs1] != RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x4: // 100 BRANCH LESS THAN
                        if (RF.registers[rs1] < RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x5: // 101 BRANCH GREATER THAN
                        if (RF.registers[rs1] >= RF.registers[rs2])
                            jumpImm(immb);
                        break;
                    case 0x6: // 110 
                        if ((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs2] < 0))
                            jumpImm(immb);
                        break;
                    case 0x7: // 111
                        if (!((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs2] < 0)))
                            jumpImm(immb);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x03: // 0000011 I-TYPE STORE
                switch (f3) {
                    case 0x0: // 000 LOAD BYTE
                        RF.registers[rd] = memory[RF.registers[rs1] + immi];
                        break;
                    case 0x1: // 001 LOAD HALF
                        RF.registers[rd] = memory[RF.registers[rs1] + immi] & 0xff;
                        RF.registers[rd] += (memory[(RF.registers[rs1] + immi) + 1]) << 8;
                        break;
                    case 0x2: // 010 LOAD WORD
                        RF.registers[rd] = 0;
                        for (int i =0; i < 4; i++)
                            RF.registers[rd] += ((memory[(RF.registers[rs1] + immi) + i] & 0xff) << (8 * i));
                        break;
                    case 0x4: // 100 LOAD BYTE (U)
                        RF.registers[rd] = memory[RF.registers[rs1] + immi] & 0xff;
                        break;
                    case 0x5: // 101 LOAD HALF (U)
                        RF.registers[rd] = (memory[RF.registers[rs1] + immi] & 0xff);
                        RF.registers[rd] += (memory[(RF.registers[rs1] + immi) + 1] & 0xff) << 8;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x23: // 0100011 - I-TYPE LOAD
                switch (f3) {
                    case 0x0: // 000 ADD IMMIDATE
                        memory[RF.registers[rs1] + imms] = (byte) (RF.registers[rs2] & 0xff);
                        break;
                    case 0x1: // 001 SHIFT LEFT LOGICAL IMM
                        memory[RF.registers[rs1] + imms] = (byte) (RF.registers[rs2] & 0xff);
                        memory[RF.registers[rs1] + imms + 1] = (byte) ((RF.registers[rs2] >> 8) & 0xff);
                        break;
                    case 0x2: // 010 SET LESS THAN IMM
                        for (int i = 0; i < 4; i++)
                            memory[RF.registers[rs1] + imms + i] = (byte) ((RF.registers[rs2] >> (8 * i)) & 0xff);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x13: // 0010011 - I-TYPE
                switch (f3) {
                    case 0x0: // 000
                        RF.registers[rd] = RF.registers[rs1] + immi;
                        break;
                    case 0x1: // 001
                        RF.registers[rd] = RF.registers[rs1] << (immi & 0x1f);
                        break;
                    case 0x2: // 010
                        RF.registers[rd] = RF.registers[rs1] < immi ? 1 : 0;
                        break;
                    case 0x3: // 011
                        RF.registers[rd] = ((RF.registers[rs1] < immi) ^ (RF.registers[rs1] < 0) ^ (immi < 0)) ? 1 : 0;
                        break;
                    case 0x4: // 100
                        RF.registers[rd] = RF.registers[rs1] ^ immi;
                        break;
                    case 0x5: // 101
                        if ((immi >>> 7) == 0x00) 
                            RF.registers[rd] = RF.registers[rs1] >>> (immi & 0x1f);
                        else 
                            RF.registers[rd] = RF.registers[rs1] >> (immi & 0x1f);
                        break;
                    case 0x6: // 110
                        RF.registers[rd] = RF.registers[rs1] | immi;
                        break;
                    case 0x7: // 111
                        RF.registers[rd] = RF.registers[rs1] & immi;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x33: // 0110011 - R-TYPE
                switch (f3) {
                    case 0x0:
                        if (f7 == 0x00) // add
                            RF.registers[rd] = RF.registers[rs1] + RF.registers[rs2];
                        else // sub
                            RF.registers[rd] = RF.registers[rs1] - RF.registers[rs2];
                        break;
                    case 0x1: // sll
                        RF.registers[rd] = RF.registers[rs1] << (RF.registers[rs2] & 0x1f);
                        break;
                    case 0x2: // slt
                        RF.registers[rd] = RF.registers[rs1] < RF.registers[rs2] ? 1 : 0;
                        break;
                    case 0x3: // sltu
                        RF.registers[rd] = ((RF.registers[rs1] < RF.registers[rs2]) ^ (RF.registers[rs1] < 0) ^ (RF.registers[rs2] < 0)) ? 1 : 0;
                        break;
                    case 0x4:// xor
                        RF.registers[rd] = RF.registers[rs1] ^ RF.registers[rs2];
                        break;
                    case 0x5:
                        if (f7 == 0x00) // srl
                            RF.registers[rd] = RF.registers[rs1] >>> (RF.registers[rs2] & 0x1f);
                        else // sra
                            RF.registers[rd] = RF.registers[rs1] >> (RF.registers[rs2] & 0x1f);
                        break;
                    case 0x6: // or
                        RF.registers[rd] = RF.registers[rs1] | RF.registers[rs2];
                        break;
                    case 0x7: // and
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

    public void loadProgram(int[] program) {
        this.program = program;
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
