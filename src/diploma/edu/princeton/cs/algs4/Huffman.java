/******************************************************************************
 *  Compilation:  javac Huffman.java
 *  Execution:    java Huffman - < input.txt   (compress)
 *  Execution:    java Huffman + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/abra.txt
 *                https://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *                https://algs4.cs.princeton.edu/55compression/medTale.txt
 *                https://algs4.cs.princeton.edu/55compression/tale.txt
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 ******************************************************************************/

package diploma.edu.princeton.cs.algs4;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Huffman} class provides static methods for compressing
 * and expanding a binary input using Huffman codes over the 8-bit extended
 * ASCII alphabet.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Huffman {

    // alphabet size of extended ASCII
    private static final int R = 256;

    private static List<Character> alpabet;


    public static List<Character> getAlpabet(){
        return alpabet;
    }
    // Do not instantiate.
    private Huffman() {
    }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }



    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     */
    public static byte[] compress(String s, List<Character> alpabet) {
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[alpabet.size()];
        for (int i = 0; i < input.length; i++)
            freq[alpabet.indexOf(input[i])]++;

        writeAlphabet(alpabet);

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[alpabet.size()];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);

        // print number of bytes in original uncompressed message
        ByteArrayOut.write(input.length);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[alpabet.indexOf(input[i])];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    ByteArrayOut.write(false);
                } else if (code.charAt(j) == '1') {
                    ByteArrayOut.write(true);
                } else throw new IllegalStateException("Illegal state");
            }
        }

        // close output stream
        byte[] res = ByteArrayOut.flush();
        ByteArrayOut.close();
        return res;
    }

    public static byte [] compress(byte [] preEncoded, List<Character> originalAlpabet) {
        // read the input
        ByteArrayIn baIn = new ByteArrayIn(preEncoded);
        String s = ByteArrayIn.readString();
        char[] input = s.toCharArray();
        writeAlphabet(originalAlpabet);

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);

        // print number of bytes in original uncompressed message
        ByteArrayOut.write(input.length);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    ByteArrayOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    ByteArrayOut.write(true);
                }
                else throw new IllegalStateException("Illegal state");
            }
        }

        // close output stream
        byte[] res = ByteArrayOut.flush();
        ByteArrayOut.close();
        return res;
    }

    private static void writeAlphabet(List<Character> alphabet) {
        int size = alphabet.size();
        ByteArrayOut.write(size);
        for (int i = 0; i < size; i++){
            ByteArrayOut.write(alphabet.get(i),16);
        }
    }

    private static List<Character> readAlphabet() {
        int size = ByteArrayIn.readInt();
        List<Character> res = new LinkedList<>();
        for (int i = 0; i < size; i++){
            char symbol = ByteArrayIn.readChar(16);
            res.add(symbol);
        }
        alpabet = res;
        return res;

    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < freq.length; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            ByteArrayOut.write(true);
            ByteArrayOut.write(x.ch, 8);
            return;
        }
        ByteArrayOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    public static byte[] expand(byte[] encoded) {

        ByteArrayIn byIn = new ByteArrayIn(encoded);
        List<Character> alphabet = readAlphabet();
        // read in Huffman trie from input stream
        Node root = readTrie();

        // number of bytes to write
        int length = ByteArrayIn.readInt();
        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = ByteArrayIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }
            ByteArrayOut.write(x.ch, 8);
        }
        byte [] res = ByteArrayOut.flush();
        ByteArrayOut.close();
        return res;
    }


    private static Node readTrie() {
        boolean isLeaf = ByteArrayIn.readBoolean();
        if (isLeaf) {
            return new Node(ByteArrayIn.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
  /*  public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }*/

}

/******************************************************************************
 *  Copyright 2002-2019, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
