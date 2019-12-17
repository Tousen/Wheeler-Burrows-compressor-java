package diploma.WBT;


import java.util.Arrays;

public class CircularSuffixArray implements IStringToIndexTransformer {
    // circular suffix array of s
    String s;
    private Integer[] index;
    private int length;
    private int first;

    public CircularSuffixArray(String s) {
        this.s = s;
        this.length = s.length();
        index = java.util.stream.IntStream.rangeClosed(0, length - 1).boxed().toArray(Integer[]::new);
        Arrays.sort(index, (a, b) -> {
            int p1 = a;
            int p2 = b;
            int c1, c2;
            for (int i = 0; i < length; i++) {
                p1 = p1 % length;
                p2 = p2 % length;
                c1 = s.charAt(p1);
                c2 = s.charAt(p2);
                if (c1 != c2) {
                    return Integer.compare(c1, c2);
                }

                p1++;
                p2++;
            }
            return 0;
        });

        for (int i = 0; i < length; i++) {
            if(index[i]==0){
                first =i;
                return;
            }
        }
    }

    // length of s
    public int length() {
        return length;
    }
    public int getFirst() {
        return first;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return index[i];
    }

    // Driver Code
    public static void main(String[] args)
    {
        String txt = "WEEKEND";
        IStringToIndexTransformer sortedString  = new CircularSuffixArray(txt);
        System.out.println("Following is suffix array for WEEKEND:");
        for(int i = 0; i < sortedString.length(); i++){
            System.out.print(sortedString.index(i)+" ");
        }
    }

}
