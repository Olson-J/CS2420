import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class LadderGame {
    AVLTree<Scored> AVL = new AVLTree<>();
    static int MaxWordSize = 15;  //Max length word allowed
    ArrayList<String>[] wordBook;  // Array of ArrayLists of words of each length.
    LinkedList<LadderInfo> queue = new LinkedList();    // list of ladders
    Random random;  // Random number generator
    /**
     *  Creates separate ArrayLists for words of each length
     * @param dictionaryFileName  Contains all words to be used in word ladder in 
    alpha order
     */
    public LadderGame(String dictionaryFileName) {
        random = new Random();
        wordBook = new ArrayList[MaxWordSize];
        for (int i = 0; i < MaxWordSize; i++)
            wordBook[i] = new ArrayList<String>();
        try {
            Scanner reader = new Scanner(new File(dictionaryFileName));
            while (reader.hasNext()) {
                String word = reader.next();
                if (word.length()< MaxWordSize) wordBook[word.length()].add(word);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Given starting and ending words, create a word ladder of minimal length using both brute force and
     * A* solutions. print out a report of both solutions once done
     * @param startWord  Beginning word of word ladder
     * @param endWord  Ending word on word ladder
     */
    public void play(String startWord, String endWord) {
        // check if input words are usable, stop if not
        if (startWord.length() != endWord.length()){
            System.out.println("Words are not the same length");
            return;
        }
        if (startWord.length()  >= MaxWordSize) {
            return;
        }

        // make two copies of the list of words that are the same length as the input so each solution type has its
        // own dictionary
        ArrayList<String> l = wordBook[startWord.length()];
        ArrayList dictionary = (ArrayList) l.clone();
        ArrayList AVLdictionary = (ArrayList) l.clone();

        // check that the start word is a valid word in dictionary
        if (!dictionary.contains(startWord)) {
            System.out.println("Start word is not in dictionary");
            return;
        }
        if (!dictionary.contains(endWord)) {
            System.out.println("End word is not in dictionary");
            return;
        }

        // once words are checked, remove startWord from dictionary to prevent re-usage
        dictionary.remove(startWord);

        // create a search tree, then remove start word
        AVL.insert(new Scored(endWord, startWord, startWord, 0));
        AVLdictionary.remove(startWord);

        System.out.println("Seeking a solution from " + startWord + " -> " + endWord
                + " Size of List " + dictionary.size());
        // start a new word ladder
        String currentWord = startWord;
        int currentMoves = 0;
        String currentLadder = startWord + " ";

        // brute force version
        int dictionaryLength = dictionary.size();
        boolean done = false;
        int bruteQueueCount = 0;
        StringBuilder bruteReport = new StringBuilder();
        // continue processing each word ladder until end word is found or no solution is possible
        while (!done) {
            int i = 0;
            while (i < dictionaryLength && !done) {
                // check if two words are one letter apart, if yes add to ladder
                if (testWords(currentWord, dictionary.get(i).toString())) {
                    LadderInfo newWord = new LadderInfo(dictionary.get(i).toString(), currentMoves + 1,
                            currentLadder);
                    queue.addLast(newWord);
                    bruteQueueCount++;
                    // check if word is end goal, if yes stop if not remove and move on
                    if (dictionary.get(i).toString().equals(endWord)) {
                        done = true;
                        // add info to print report
                        bruteReport.append("BRUTE SOLUTION: shortest ladder = [")
                                .append(newWord.ladder).append(newWord.word).append("]");
                    }
                    dictionary.remove(i);
                    dictionaryLength--;
                }
                else {i++; }
            }
            // if queue is empty, no complete ladders were found
            if (queue.size() == 0) {
                System.out.println("No possible ladders found");
                done = true;
            } else {
                // removes first ladder and starts on next ladder level
                currentWord = queue.get(0).word;
                currentMoves = queue.get(0).moves;
                currentLadder = queue.get(0).ladder + queue.get(0).word + " ";
                queue.remove(0);
            }
        }
        // add enqueue count to print report
        bruteReport.append(" enqueue count: ").append(bruteQueueCount);
        queue.clear();


        // AVLTree version
        int AVLdictionaryLength = AVLdictionary.size();
        done = false;
        int AVLQueueCount = 0;
        StringBuilder AVLReport = new StringBuilder();
        // continue processing each word ladder until end word is found or no solution is possible
        while (!done) {
            int i = 0;
            currentWord = AVL.findMin().word;
            String prevWords = AVL.findMin().prevWords;
            int wordCount = AVL.findMin().prevWordCount;
            AVL.deleteMin();

            while (i < AVLdictionaryLength && !done) {
                // check if two words are one letter apart, if yes add to ladder
                if (testWords(currentWord, AVLdictionary.get(i).toString())) {
                    // create comprehensive score
                    AVL.insert(new Scored(endWord, AVLdictionary.get(i).toString(),
                            prevWords + " " + AVLdictionary.get(i).toString(), wordCount + 1));
                    AVLQueueCount++;
                    // check if word is end goal, if yes stop if not remove and move on
                    if (AVLdictionary.get(i).equals(endWord)) {
                        done = true;
                        // add info to print report
                        AVLReport.append("A* SOLUTION: shortest ladder = [").append(prevWords).append(" ")
                                .append(endWord).append("]");
                    }
                    AVLdictionary.remove(i);
                    AVLdictionaryLength--;
                }else {i++; }
            }
            // if search tree is empty, no complete ladders were found
            if (AVL.isEmpty()) {
                System.out.println("No possible ladders found");
                done = true;
            }
        }
        // add enqueue count to the AVL print report
        AVLReport.append(" enqueue count: ").append(AVLQueueCount);
        AVL.makeEmpty();

        // check to see how the brute force and AVL solutions compare
        double sameCheck;
        // if no ladder was found, use 0 rather than crashing
        if (AVLQueueCount == 0 || bruteQueueCount == 0) {
            sameCheck = 0;
        } else {
            // find what percentage AVL solution is of brute force solution
            sameCheck = ((double) AVLQueueCount / (double) bruteQueueCount * 100);
        }

        // print out reports on both solutions and the comparison
        System.out.println(AVLReport);
        System.out.println(bruteReport);
        System.out.println("Same Length Work Compare " + AVLQueueCount + " " + bruteQueueCount + " "
                + (int) sameCheck + "%");
        System.out.println();
    }

    /**
     * Find a word ladder between random words of length len
     * @param len  Length of words in desired word ladder
     */
    public void play(int len) {
        if (len >= MaxWordSize) return;
        ArrayList<String> list = wordBook[len];
        String firstWord = list.get(random.nextInt(list.size()));
        String lastWord = list.get(random.nextInt(list.size()));
        play(firstWord, lastWord );
    }
    /**
     * compare two words to see if they are one letter apart
     * and return the results
     * @param word1 last word on word ladder
     * @param test word from dictionary being tested
     * @return true or false
     * @author Julie Olson a02363064
     */
    public boolean testWords(String word1, String test) {
        String[] word1Chars = word1.split("");
        String[] testChars = test.split("");
        int count = 0;
        for(int i =0; i < word1.length(); i++) {
            if (!word1Chars[i].equals(testChars[i])) count++;
        }
        return count == 1;
    }
}
