/**
 * TestLadder is the main class where a LadderGame is played using different word 
 inputs. The play function can be
 * called given a starting and ending words or an integer which will lead to random
 words being selected. It also
 * prints out the elapsed time for the game.
 */
public class TestLadder {
    public static void main(String[] args) {
        String[] source ={ "jowl", "oops", "slow", "kiss", "cock", "jura", "stet", "rums", "stylus",
                "herded", "stone"};
        String[] dest   ={"pond", "tots", "fast", "woof", "numb", "such", "whey", "numb", "swives",
                "raffia", "money"};
// Since constructor has the dictionary, it can read one and used // to solve many 
        //problems.
                LadderGame g = new LadderGame("dictionary.txt");
        for (int i=0; i < source.length; i++){
            g.play(source[i], dest[i]);
        }
//        int RANDOMCT = 7;
//        for (int i = 3; i < RANDOMCT; i++)
//            g.play(i);
    }
}