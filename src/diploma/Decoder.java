package diploma;

import diploma.WBT.BurrowsWheelerString;
import diploma.edu.princeton.cs.algs4.Huffman;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Decoder {
    public static void main(String[] args){
        decode("res/compressed.bz","res/decoded.txt");
    }

    public static void decode(String compressed, String uncompressed){
        File file = new File(compressed);
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(file.toPath());
        }catch (IOException e){
            System.out.println("cannot open file");
            return;
        }

        byte[] movedToFront = Huffman.expand(fileContent);
        List<Character> alphabet = Huffman.getAlpabet();
        MoveToFront.initAlphabet(alphabet);
        String burrowed = MoveToFront.decode(movedToFront);
        String result = BurrowsWheelerString.decode(burrowed);
        FileUtil.writeToFile(result.getBytes(), uncompressed);

    }


}
