public class ALU {
    private int input1; // ReadData1
    private int input2; // ReadData2 or ImmGen
    private int modeNum;
    private Mode mode;

    public ALU(int input1, int input2, int modeNum) {
        this.input1 = input1;
        this.input2 = input2;

        switch modeNum {
            case 0:
               mode = AND;
                break;
            case 1:
                mode = OR;
                break;
            case 2:
                mode = XOR;
                break;
            case 3:
                mode = NOT;
                break;
            default:
                System.out.println("ERROR: INVALID ALU MODE SELECTION");
                return;
        }
    }

    switch mode {
        case AND:
            and(input1, input2);
            break;
        case OR:
            or(input1, input2);
            break;
        case XOR:
            xor(input1, input2);
            break;
        case NOT:
            not(input1, input2);
            break;
    }




enum Mode {
    AND,
    OR,
    XOR,
    NOT
}

}
