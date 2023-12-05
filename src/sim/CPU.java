package sim;

public class CPU {
    private int programCounter, oldProgramCounter;
    private int reg[] = new int[32];
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
        reg[2] = memory.length - 5;
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
            case 0x37:
                reg[rd] = immu;
            case 0x17:
                reg[rd] = programCounter + immu;
            case 0x6f:
                if (rd != 0)
                    reg[rd] = programCounter + 4;
                jumpImm(immj);
            case 0x67:
                if (rd != 0)
                    reg[rd] = programCounter + 4;
                jump = true;
                programCounter = reg[rs1] + immi;
            case 0x63:
                switch (f3) {
                    case 0x0:
                        if (reg[rs1] == reg[rs2])
                            jumpImm(immb);
                        break;
                    case 0x1:
                        if (reg[rs1] != reg[rs2])
                            jumpImm(immb);
                        break;
                    case 0x4:
                        if (reg[rs1] < reg[rs2])
                            jumpImm(immb);
                        break;
                    case 0x5:
                        if (reg[rs1] >= reg[rs2])
                            jumpImm(immb);
                        break;
                    case 0x6:
                        if ((reg[rs1] < reg[rs2]) ^ (reg[rs2] < 0))
                            jumpImm(immb);
                        break;
                    case 0x7:
                        if (!((reg[rs1] < reg[rs2]) ^ (reg[rs2] < 0)))
                            jumpImm(immb);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x03:
                switch (f3) {
                    case 0x0:
                        reg[rd] = memory[reg[rs1] + immi];
                        break;
                    case 0x1:
                        reg[rd] = memory[reg[rs1] + immi] & 0xff;
                        reg[rd] += (memory[(reg[rs1] + immi) + 1]) << 8;
                        break;
                    case 0x2:
                        reg[rd] = 0;
                        for (int i =0; i < 4; i++)
                            reg[rd] += ((memory[(reg[rs1] + immi) + i] & 0xff) << (8 * i));
                        break;
                    case 0x4: 
                        reg[rd] = memory[reg[rs1] + immi] & 0xff;
                        break;
                    case 0x5: 
                        reg[rd] = (memory[reg[rs1] + immi] & 0xff);
                        reg[rd] += (memory[(reg[rs1] + immi) + 1] & 0xff) << 8;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x23:
                switch (f3) {
                    case 0x0: 
                        memory[reg[rs1] + imms] = (byte) (reg[rs2] & 0xff);
                        break;
                    case 0x1: 
                        memory[reg[rs1] + imms] = (byte) (reg[rs2] & 0xff);
                        memory[reg[rs1] + imms + 1] = (byte) ((reg[rs2] >> 8) & 0xff);
                        break;
                    case 0x2: 
                        for (int i = 0; i < 4; i++)
                            memory[reg[rs1] + imms + i] = (byte) ((reg[rs2] >> (8 * i)) & 0xff);
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x13:
                switch (f3) {
                    case 0x0: 
                        reg[rd] = reg[rs1] + immi;
                        break;
                    case 0x1: 
                        reg[rd] = reg[rs1] << (immi & 0x1f);
                        break;
                    case 0x2: 
                        reg[rd] = reg[rs1] < immi ? 1 : 0;
                        break;
                    case 0x3: 
                        reg[rd] = ((reg[rs1] < immi) ^ (reg[rs1] < 0) ^ (immi < 0)) ? 1 : 0;
                        break;
                    case 0x4: 
                        reg[rd] = reg[rs1] ^ immi;
                        break;
                    case 0x5:
                        if ((immi >>> 7) == 0x00) 
                            reg[rd] = reg[rs1] >>> (immi & 0x1f);
                        else 
                            reg[rd] = reg[rs1] >> (immi & 0x1f);
                        break;
                    case 0x6: 
                        reg[rd] = reg[rs1] | immi;
                        break;
                    case 0x7: 
                        reg[rd] = reg[rs1] & immi;
                        break;
                    default:
                        System.out.println("funt3: " + String.format("0x%01X", f3) + "doesn't work");
                        break;
                }
            case 0x33:
                switch (f3) {
                    case 0x0:
                        if (f7 == 0x00) // add
                            reg[rd] = reg[rs1] + reg[rs2];
                        else // sub
                            reg[rd] = reg[rs1] - reg[rs2];
                        break;
                    case 0x1: // sll
                        reg[rd] = reg[rs1] << (reg[rs2] & 0x1f);
                        break;
                    case 0x2: // slt
                        reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0;
                        break;
                    case 0x3: // sltu
                        reg[rd] = ((reg[rs1] < reg[rs2]) ^ (reg[rs1] < 0) ^ (reg[rs2] < 0)) ? 1 : 0;
                        break;
                    case 0x4:// xor
                        reg[rd] = reg[rs1] ^ reg[rs2];
                        break;
                    case 0x5:
                        if (f7 == 0x00) // srl
                            reg[rd] = reg[rs1] >>> (reg[rs2] & 0x1f);
                        else // sra
                            reg[rd] = reg[rs1] >> (reg[rs2] & 0x1f);
                        break;
                    case 0x6: // or
                        reg[rd] = reg[rs1] | reg[rs2];
                        break;
                    case 0x7: // and
                        reg[rd] = reg[rs1] & reg[rs2];
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
