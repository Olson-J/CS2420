public class Scored implements Comparable<Scored>{
    int score;
    String word;
    String prevWords;
    int prevWordCount;

    /**
     * initialize class variables using the given values
     * @param endWord the 'goal' word
     * @param word1 current word
     * @param prevWords1 a record of past words on the ladder
     * @param prevWordCount1 number of previous words on ladder
     * */
    public Scored(String endWord, String word1, String prevWords1, int prevWordCount1){
        word = word1;
        prevWords = prevWords1;
        prevWordCount = prevWordCount1;
        score = compareScore(endWord) + prevWordCount;
    }

    /**
     * compare the scores of two nodes and return an integer value
     * @param newNode the new node that is being compared to this.score
     * */
    public int compareTo(Scored newNode){return Integer.compare(this.score, newNode.score);}

    /**
     * compare two words letter by letter and find out how many letters are different
     * @param searchWord the word that is being compared to word (current word)
     * @return count, number of letters different
     * */
    private int compareScore(String searchWord) {
        String[] word1Chars = searchWord.split("");
        String[] testChars = word.split("");
        int count = 0;
        for(int i =0; i < searchWord.length(); i++) {
            if (!word1Chars[i].equals(testChars[i])) count++;
        }
        return count;
    }
}
