package diploma.WBT;

public class BurrowsWheelerString {

    public static int ASCII_ALLOCATION = 10000;
    public static String encode(String word) {
        //IStringToIndexTransformer csa = new CircularSuffixArray(word);
        // MORE OPTIMAL ALGORITHM ~ 4 times slower
         IStringToIndexTransformer csa = new ArraySuffix(word);
        StringBuilder builder = new StringBuilder();

        String formatedNumber =  String.format("%1$10s",csa.getFirst());
        builder.append(formatedNumber);
        for (int i = 0; i < csa.length(); i++) {
            builder.append(word.charAt((csa.index(i) - 1 + csa.length()) % csa.length()));
        }
        return builder.toString();
    }

    public static String decode(String word) {
        int first = Integer.parseInt(word.substring(0,10).replace(" ",""));
        String text = word.substring(10,word.length());
        int len = text.length();
        int[] next = new int[len];
        int[] count = new int[ASCII_ALLOCATION];
        for (int i = 0; i < len; i++)
            count[text.charAt(i)+1]++;
        for (int r = 0; r < ASCII_ALLOCATION-1; r++)
            count[r+1] += count[r];
        for (int i = 0; i < len; i++)
            next[count[text.charAt(i)]++] = i;
        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        while (cnt < len)
        {
            sb.append(text.charAt(next[first]));
            first = next[first];
            cnt++;
        }
        return sb.toString();
    }

    public static void main(String[] args)
    {
       System.out.println(encode("WEEKEND"));
       System.out.println(decode("         6NWEKEED"));
    }
}
