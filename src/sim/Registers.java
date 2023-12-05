public class Registers {
    int[] registers;

    Registers(int x) {
        registers = new int[x]; 
    }

    public int getRegister(int x) {
        return registers[x];
    }

    public void setRegister(int x, int value) {
        registers[x] = value;
    }
}
