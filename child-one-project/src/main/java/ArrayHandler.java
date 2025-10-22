
public class ArrayHandler {
    public native String[] returnArray();

    static{
        System.loadLibrary("nativelib");
    }

    public static void main(String args[]) {
        String ar[];
        ArrayHandler ah= new ArrayHandler();
        ar = ah.returnArray();
        for (int i=0; i<5; i++) {
            System.out.println("array element"+i+ "=" + ar[i]);
        }
    }
}