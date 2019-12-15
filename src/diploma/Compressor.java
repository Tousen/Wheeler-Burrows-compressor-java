package diploma;

public class Compressor {
    public static void main(String[] args){
        if(args.length!=3){
            printInstruction();
            return;
        }

        if("+".equals(args[0])){
            Decoder.decode(args[1], args[2]);
            return;
        }

        if("-".equals(args[0])){
            Encoder.encode(args[1], args[2]);
            return;
        }
        printInstruction();
    }

    private static void printInstruction(){
        System.out.println("\n application accepts 3 params");
        System.out.println("\n 1. \"-\" or \"+\" to compress or decompress");
        System.out.println("\n 2. input file path");
        System.out.println("\n 3. output file path");
    }
}
