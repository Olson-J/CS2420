import java.io.File;
import java.util.Scanner;

public class WritePoetry {

    public String WritePoem(String filename, String startWord, int num, boolean printHash){
        HashTable<String> Hash = new HashTable<>( );
        long startTime = System.currentTimeMillis( );

        File file = new File(filename);
        System.out.println("filename: " + filename);
        int count = 0;
        try (Scanner input = new Scanner(file)) {
            StringBuilder longString = new StringBuilder();
            while(input.hasNextLine()){
                longString.append(" " + input.nextLine());
            }
                String word = longString.toString();
                String[] words = word.split(" ");
                for (int i = 0; i < words.length - 1; i++) {
                    words[i] = words[i].toLowerCase();
                    count++;
                    Hash.insert(words[i], words[i + 1].toLowerCase());
                }
        } catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the file: " + ex);
        }

        // Because GAP and NUMS are mutally prime, this inserts all numbers between
        // 0 and 1999
        System.out.println( "HashTable size is: " + Hash.size( ) );

        if (printHash) {
            System.out.println(Hash.toString(Hash.capacity()));
        }

        StringBuilder newPoem = new StringBuilder();
        newPoem.append(startWord + " ");
        String currentWord = startWord;
        for ( int i = 0; i < num; i++) {
            currentWord = Hash.randFollow(currentWord);
            if ((currentWord.compareTo(".") == 0 )|| (currentWord.compareTo(",") == 0)
                    || (currentWord.compareTo("?") == 0) || (currentWord.compareTo("!") == 0)) {
                newPoem.append(currentWord + " ");
                newPoem.append('\n');
            } else{
                newPoem.append(currentWord + " ");
            }
        }
        if (currentWord.compareTo(".") != 0){
            newPoem.append(".");
        }
        System.out.println(newPoem);

        long endTime = System.currentTimeMillis( );
        System.out.println( "Elapsed time: " + (endTime - startTime) );
        System.out.println( "Hash size is: " + Hash.size( ) );
        System.out.println( "Array size is: " + Hash.capacity( ) );
        Hash.makeEmpty();
        return "\n";

    }

}
