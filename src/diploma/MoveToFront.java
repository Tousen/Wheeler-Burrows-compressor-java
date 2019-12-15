package diploma;

import diploma.edu.princeton.cs.algs4.ByteArrayIn;
import diploma.edu.princeton.cs.algs4.ByteArrayOut;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MoveToFront {
    private static LinkedList<Character> alphabet = null;
    public static int CHAR_SIZE = 8;
    public static void initAlphabet(List<Character> chars){
        alphabet = new LinkedList<>(chars);
    }

    public static byte[] encode(String source){
        if(alphabet == null){
            System.out.println("no Alphabet set");
            return null;
        }
        for (int i=0;i<source.length();i++){
            char c = source.charAt(i);
            int index = alphabet.indexOf(c);
            ByteArrayOut.write(index,CHAR_SIZE);
            alphabet.remove(index);
            alphabet.addFirst(c);
        }
        byte [] res = ByteArrayOut.flush();
        ByteArrayOut.close();
        return res;
    }

    public static String decode(byte [] bytes){
        if(alphabet == null){
            System.out.println("no Alphabet set");
            return null;
        }
        ByteArrayIn inBytes = new ByteArrayIn(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i=0;!ByteArrayIn.isEmpty();i++){
            int index = ByteArrayIn.readChar();
            char c = alphabet.get(index);
            sb.append(c);
            alphabet.remove(index);
            alphabet.addFirst(c);
        }
        return sb.toString();
    }
}
