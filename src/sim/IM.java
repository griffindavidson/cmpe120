package sim;

public class IM {

    // add instruction
    public int add(int rs1, int rs2) {
        return rs1 + rs2;
    }

    // addi instruction
    public int addi(int rs1, int imm) {
        return rs1 + imm;
    }

    // sub instruction
    public int sub(int rs1, int rs2) {
        return rs1 - rs2;
    }

    // or instruction
    public int or(int rs1, int rs2) {
        return rs1 | rs2;
    }

    // ori instruction
    public int ori(int rs1, int imm) {
        return rs1 | imm;
    }

    // and instruction
    public int and(int rs1, int rs2) {
        return rs1 & rs2;
    }

    // andi instruction
    public int andi(int rs1, int imm) {
        return rs1 & imm;
    }

    // xor instruction
    public int xor(int rs1, int rs2) {
        return rs1 ^ rs2;
    }

    // xori instruction
    public int xori(int rs1, int imm) {
        return rs1 ^ imm;
    }

    // sll instruction
    public int sll(int rs1, int rs2) {
        return rs1 << rs2;
    }

    // slli instruction
    public int slli(int rs1, int shamt) {
        return rs1 << shamt;
    }

    // srl instruction
    public int srl(int rs1, int rs2) {
        return rs1 >>> rs2;
    }

    // srli instruction
    public int srli(int rs1, int shamt) {
        return rs1 >>> shamt;
    }

    // sra instruction
    public int sra(int rs1, int rs2) {
        return rs1 >> rs2;
    }

    // srai instruction
    public int srai(int rs1, int shamt) {
        return rs1 >> shamt;
    }

    // slt instruction
    public int slt(int rs1, int rs2) {
        if(rs1 < rs2) {
            return 1;
        } else {
            return 0;
        }
    }

    // slti instruction
    public int slti(int rs1, int imm) {
        if(rs1 < imm) {
            return 1;
        } else {
            return 0;
        }
    }

    // sltu instruction
    public int sltu(int rs1, int rs2) {
        if(Integer.compareUnsigned(rs1, rs2) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // bge instruction
    public boolean bge(int rs1, int rs2) {
        return rs1 >= rs2;
    }

    // beq instruction
    public boolean beq(int rs1, int rs2) {
        return rs1 == rs2;
    }
}
