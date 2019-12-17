package diploma;

import diploma.WBT.BurrowsWheelerString;
import diploma.edu.princeton.cs.algs4.Huffman;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static diploma.FileUtil.writeToFile;

public class Encoder {
    public static void main(String[] args){
        encode("res/bible.txt", "res/compressed.bz");
    }

    public static void encode(String sourceUrl, String destUrl){
        String content = readLineByLine(sourceUrl);
        //get alphabet
        Set<Character> alphabetSet = content.chars().mapToObj(c->(char)c).collect(Collectors.toSet());
        List<Character> alphabet = new LinkedList<>();
        alphabet.add(' ');
        for (int i=0;i<10;i++ ){
            alphabet.add(String.valueOf(i).charAt(0));
        }
        alphabetSet.forEach(character -> alphabet.add(character));

        // step 1 burrow-Wheller encoding

        String burrowed = BurrowsWheelerString.encode(content);

        // step 2 moveToFront Encoder
        MoveToFront.initAlphabet(alphabet);
        byte [] encoded = MoveToFront.encode(burrowed);

        // step 3 Huffman Encoder
        byte [] huffmaned = Huffman.compress(encoded, alphabet);

        writeToFile(huffmaned, destUrl);

    }


    private static String readLineByLine(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( new File(filePath).toPath(), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


}
