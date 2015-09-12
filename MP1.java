import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Override;
import java.lang.String;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Comparator;
import java.util.TreeMap;


public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }
    public boolean contains(String word, String[] stopWords){
        for (String stopWord : stopWords) {
            if(stopWord.equals(word)){
                return true;
            }
        }
        return false;

    }
    class Item implements Comparable<Item>{
        String key;
        Long value;

        public Item(String key, Long value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        @Override
        public int compareTo(Item o) {
            int i = o.value.compareTo(value);
            if(i != 0)return i;
            return key.compareTo(o.key);
//            if(i != 0)return i;
//            return key.compareTo(o.key);
        }
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        Scanner sc = new Scanner(new FileInputStream(inputFileName));
        List<String> lines = new ArrayList<String>();
        while(sc.hasNextLine()){
            lines.add(sc.nextLine().toLowerCase());
        }
        Integer[] indexes = getIndexes();
        Map<String, Long> map = new HashMap<String, Long>();
        for (Integer index : indexes) {
            String line = lines.get(index);
            StringTokenizer tokenizer = new StringTokenizer(line, delimiters);
            while(tokenizer.hasMoreTokens()){
                String s = tokenizer.nextToken().trim();
                if(!contains(s, stopWordsArray)){
                    Long aLong = map.get(s);
                    Long count = aLong == null ? 1L : aLong +1;
                    map.put(s, count);
                }
            }
        }
        List<Item> items = new ArrayList<Item>();
        for(String str : map.keySet()){
            items.add(new Item(str, map.get(str)));
        }
        Collections.sort(items);
        for(int i = 0; i < ret.length; ++i){
            ret[i] = items.get(i).getKey();
        }

        //TODO

        return ret;
    }

    public static void main(String[] args) throws Exception {
//        args = new String[]{"33726"};
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
