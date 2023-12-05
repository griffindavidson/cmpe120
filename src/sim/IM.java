package sim;

public class IM {

    // Opcode 0x37
    // LUI instruction


    // Opcode 0x17
    // AUIPC instruction


    // Opcode 0x6F
    // JAL instruction


    // Opcode 0x67
    // JALR instruction


    // Opcode 0x63
    // BEQ instruction
    public boolean beq(int rs1, int rs2) {
        return rs1 == rs2;
    }

    // BNE instruction
    public boolean bne(int rs1, int rs2) {
        return rs1 != rs2;
    }

    // BLT instruction
    public boolean blt(int rs1, int rs2) {
        return rs1 < rs2;
    }

    // BGE instruction
    public boolean bge(int rs1, int rs2) {
        return rs1 >= rs2;
    }

    // BLTU instruction
    public boolean bltu(int rs1, int rs2) {
        return Integer.compareUnsigned(rs1, rs2) < 0;
    }

    // BGEU instruction
    public boolean bgeu(int rs1, int rs2) {
        return Integer.compareUnsigned(rs1, rs2) >= 0;
    }


    // Opcode 0x03
    // LB instruction

    // LH instruction

    // LW instruction

    // LBU instruction

    // LHU instruction


    // Opcode 0x23
    // SB instruction

    // SH instruction

    // SW instruction


    // Opcode 0x13
    // ANDI instruction
    public int andi(int rs1, int imm) {
        return rs1 & imm;
    }

    // SLTI instruction
    public int slti(int rs1, int imm) {
        if(rs1 < imm) {
            return 1;
        } else {
            return 0;
        }
    }

    // SLTIU instruction
    public int sltiu(int rs1, int imm) {
        if (Integer.compareUnsigned(rs1, imm) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // XORI instruction
    public int xori(int rs1, int imm) {
        return rs1 ^ imm;
    }

    // ORI instruction
    public int ori(int rs1, int imm) {
        return rs1 | imm;
    }

    // ADDI instruction
    public int addi(int rs1, int imm) {
        return rs1 + imm;
    }

    // SLLI instruction
    public int slli(int rs1, int shamt) {
        return rs1 << shamt;
    }

    // SRLI instruction
    public int srli(int rs1, int shamt) {
        return rs1 >>> shamt;
    }

    // SRAI instruction
    public int srai(int rs1, int shamt) {
        return rs1 >> shamt;
    }

    
    //Opcode 0x33
    // ADD instruction
    public int add(int rs1, int rs2) {
        return rs1 + rs2;
    }

    // SUB instruction
    public int sub(int rs1, int rs2) {
        return rs1 - rs2;
    }

    // SLL instruction
    public int sll(int rs1, int rs2) {
        return rs1 << rs2;
    }

    // SLT instruction
    public int slt(int rs1, int rs2) {
        if(rs1 < rs2) {
            return 1;
        } else {
            return 0;
        }
    }

    // SLTU instruction
    public int sltu(int rs1, int rs2) {
        if(Integer.compareUnsigned(rs1, rs2) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // XOR instruction
    public int xor(int rs1, int rs2) {
        return rs1 ^ rs2;
    }

    // SRL instruction
    public int srl(int rs1, int rs2) {
        return rs1 >>> rs2;
    }

    // SRA instruction
    public int sra(int rs1, int rs2) {
        return rs1 >> rs2;
    }

    // OR instruction
    public int or(int rs1, int rs2) {
        return rs1 | rs2;
    }

    // AND instruction
    public int and(int rs1, int rs2) {
        return rs1 & rs2;
    }
}
