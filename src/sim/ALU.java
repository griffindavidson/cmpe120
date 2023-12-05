package sim;

public class ALU {
    private int input1; // ReadData1
    private int input2; // ReadData2 or ImmGen
    private int modeNum;
    private Mode mode;

    public ALU(int input1, int input2, int modeNum) {
        this.input1 = input1;
        this.input2 = input2;

        switch(modeNum) {
            case 0:
               mode = Mode.AND;
                break;
            case 1:
                mode = Mode.OR;
                break;
            case 2:
                mode = Mode.XOR;
                break;
            case 3:
                mode = Mode.NOT;
                break;
            default:
                System.out.println("ERROR: INVALID ALU MODE SELECTION");
                return;
        }
    }

}


enum Mode {
    AND,
    OR,
    XOR,
    NOT
}

