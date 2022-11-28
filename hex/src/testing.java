public class testing {
    public static void main(String[] args){
        HexGame game = new HexGame();
        // to print compression tests/steps change second argument to true
        game.CreateBoard("moves.txt", true);
        game.CreateBoard("moves2.txt", false);
    }
}
