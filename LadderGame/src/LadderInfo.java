
/**
 * This class creates WordInfo objects which allows the program
 * to keep track of words, their partial ladder, and number of moves in the word 
 ladder.
 */
public class LadderInfo {
    public String word;  //last word in the word ladder
    public int moves;    //number of in the word ladder
    public String ladder;// printable representation of ladder
    public LadderInfo(String word, int moves, String ladder){
        this.word = word;
        this.moves = moves;
        this.ladder = ladder;
    }
    public String toString(){
        return "Word " + word    + " Moves " +moves  + " Ladder ["+ ladder +"]";
    }
}