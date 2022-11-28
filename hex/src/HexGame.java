import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/*******
 * class that manages the functionality of the hex game
 */
public class HexGame {
    public Cell[] board = new Cell[126];
    public int rightEdge = 122;
    public int leftEdge = 123;
    public int topEdge = 124;
    public int bottomEdge = 125;

    /*******
     * create the game board and manage the creation and placement
     * of each game piece
     * @param filename name of the file that contains the list of turns
     * @param testing boolean that determines whether ot print test information
     */
    public void CreateBoard(String filename, boolean testing){
        try{
            int blueMoves = 0;
            int redMoves = 0;

            for (int i = 0 ; i < board.length ; i++) {
                board[i] = new Cell();
            }

            board[leftEdge].color = "B";
            board[leftEdge].group = leftEdge;
            board[rightEdge].color = "B";
            board[rightEdge].group = rightEdge;
            board[topEdge].color = "R";
            board[topEdge].group = topEdge;
            board[bottomEdge].color = "R";
            board[bottomEdge].group = bottomEdge;

            if (testing) {
                System.out.println("Test constructors, print empty game board");
                printBoard(true);
                System.out.println("left bound group: " + board[leftEdge].group);
                System.out.println("right bound group: " + board[rightEdge].group);
                System.out.println("top bound group: " + board[topEdge].group);
                System.out.println("bottom bound group: " + board[bottomEdge].group);
            }

            File file = new File(filename);
            Scanner reader = new Scanner(file);
            int turn = 0;
            while (reader.hasNext()) {
                turn++;
                // get position on board of move
                int spot = reader.nextInt();
                // assign piece a color based on turn
                if (turn % 2 == 1) {
                    // blue turn
                    board[spot].color = "B";
                    blueMoves++;
                } else if (turn % 2 == 0){
                    // red turn
                    board[spot].color = "R";
                    redMoves++;
                }
                // Look for similar colors nearby
                UnionFind(spot);
                if (testing && turn % 2 == 1) {
                    System.out.println("Intermittent snapshots of every other turn to show the progression of the " +
                            "path compression grouping/union-ing. Each group is represented by the 'root' cell number " +
                            "to clarify separate groups");
                    printBoard(true);
                }

                // after each turn check if someone won
                if (find(board[topEdge]) == find(board[bottomEdge])) {
                    System.out.println("");
                    System.out.println("---------------> Red player has won after " + redMoves + " attempted moves!");
                    System.out.println("Here is the final board");
                    printBoard(false);
                    break;
                } else if (find(board[leftEdge]) == find(board[rightEdge])) {
                    System.out.println("");
                    System.out.println("---------------> Blue player has won after " + blueMoves + " attempted moves!");
                    System.out.println("Here is the final board");
                    printBoard(false);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******
     * determine which group/union, if any, the new piece should join and check
     * any neighboring cells for other pieces that need to join the same group
     * @param place location of cell/piece being placed
     */
    public void UnionFind(int place){
        Cell current = board[place];
        // Check if it has been played, if not assign default group
        if (current.group == 0){
            // once played set individual index as group
            board[place].group = place;
        } else {
            // notify if cell has already been played
            System.out.println("Cell has already been taken");
            return;
        }
        // check neighbors for cells to be added to group
        ArrayList<Integer> neighbors = getNeighbors(place);

        // connect neighbors with union function
        for (Integer neighbor : neighbors) {
            union(place, board[neighbor].group);
        }
    }

    /*******
     * determine which group is the largest and should absorb the other
     * @param cell1 cell belonging to one group
     * @param cell2 cell belonging to a different group
     */
    public void union(int cell1, int cell2){
        int g1 = board[cell1].group;
        int g2 = board[cell2].group;

        if (board[g1].size > board[g2].size){
            board[g2].size = 0;
            for (int i = 1; i < 126 ; i++) {
                if (board[i].group == g2) {
                    board[i].group = g1;
                    board[g1].size++;
                }
            }
        } else {
            board[g1].size = 0;
            for (int i = 1; i < 126 ; i++) {
                if (board[i].group == g1) {
                    board[i].group = g2;
                    board[g2].size++;
                }
            }
        }
    }

    /*******
     * @param search cell being inspected
     * @return the group the cell belongs to
     */
    public int find(Cell search) {
        return search.group;
    }

    /*******
     * private helper function to determine the vlid neighbors of a cell
     * based on cell color and group
     * @param spot location of 'base' cell whose neighbors are being found
     * @return arraylist of viable neighbors
     */
    private ArrayList<Integer> getNeighbors(int spot) {
        // neighbors listed clockwise starting from left
        // check for neighbors of same color but not in group, to be added
        ArrayList<Integer> buddies = new ArrayList<Integer>();

        if (spot == 1) {
            // upper left corner
            // check i+1, and i+11, and lefEdge, and topEdge
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color,board[leftEdge].color) &&
                    board[spot].group != board[leftEdge].group){
                buddies.add(leftEdge);
            } else if (Objects.equals(board[spot].color,board[topEdge].color) &&
                    board[spot].group != board[topEdge].group) {
                buddies.add(topEdge);
            }
        } else if (spot == 11) {
            // upper right corner
            // check i-1, i+10, and i+11, and rightEdge, and topEdge
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 10].color) &&
                    board[spot].group != board[spot + 10].group){
                buddies.add(spot + 10);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color,board[rightEdge].color) &&
                    board[spot].group != board[rightEdge].group){
                buddies.add(rightEdge);
            } else if (Objects.equals(board[spot].color,board[topEdge].color) &&
                    board[spot].group != board[topEdge].group) {
                buddies.add(topEdge);
            }
        } else if (spot == 111) {
            // bottom left corner
            // check i-11, i-10, i+1, leftEdge, bottomEdge
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color, board[spot - 10].color) &&
                    board[spot].group != board[spot - 10].group){
                buddies.add(spot - 10);
            }
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color,board[leftEdge].color) &&
                    board[spot].group != board[leftEdge].group){
                buddies.add(leftEdge);
            } else if (Objects.equals(board[spot].color,board[bottomEdge].color) &&
                    board[spot].group != board[bottomEdge].group) {
                buddies.add(bottomEdge);
            }
        } else if (spot == 121) {
            // bottom right corner
            // check i-1, i-11, rightEdge, bottomEdge
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color,board[rightEdge].color) &&
                    board[spot].group != board[rightEdge].group){
                buddies.add(rightEdge);
            } else if (Objects.equals(board[spot].color,board[bottomEdge].color) &&
                    board[spot].group != board[bottomEdge].group) {
                buddies.add(bottomEdge);
            }
        } else if (spot % 11 == 1) {
            // left edge
            // check i-11, i-10, i+1, i+11, leftEdge
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color, board[spot - 10].color) &&
                    board[spot].group != board[spot - 10].group){
                buddies.add(spot - 10);
            }
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color,board[leftEdge].color) &&
                    board[spot].group != board[leftEdge].group){
                buddies.add(leftEdge);
            }
        } else if (spot % 11 == 0) {
            // right edge
            // check i-1, i-11, i+11, i+10, rightEdge
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color, board[spot + 10].color) &&
                    board[spot].group != board[spot + 10].group){
                buddies.add(spot + 10);
            }
            if (Objects.equals(board[spot].color,board[rightEdge].color) &&
                    board[spot].group != board[rightEdge].group){
                buddies.add(rightEdge);
            }
        } else if (spot <= 11) {
            // top edge
            // check i-1, i+1, i+11, i+10, topEdge
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color, board[spot + 10].color) &&
                    board[spot].group != board[spot + 10].group){
                buddies.add(spot + 10);
            }
            if (Objects.equals(board[spot].color,board[topEdge].color) &&
                    board[spot].group != board[topEdge].group){
                buddies.add(topEdge);
            }
        } else if (spot >= 111 && spot <= 121) {
            // bottom edge
            // check i-1, i-11, i-10, i+1, bottomEdge
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color, board[spot - 10].color) &&
                    board[spot].group != board[spot - 10].group){
                buddies.add(spot - 10);
            }
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color,board[bottomEdge].color) &&
                    board[spot].group != board[bottomEdge].group){
                buddies.add(bottomEdge);
            }
        } else {
            // center
            // check i-1, i-11, i-10, i+1, i+11, i+10
            if (Objects.equals(board[spot].color, board[spot - 1].color) &&
                    board[spot].group != board[spot - 1].group){
                buddies.add(spot - 1);
            }
            if (Objects.equals(board[spot].color, board[spot - 11].color) &&
                    board[spot].group != board[spot - 11].group){
                buddies.add(spot - 11);
            }
            if (Objects.equals(board[spot].color, board[spot - 10].color) &&
                    board[spot].group != board[spot - 10].group){
                buddies.add(spot - 10);
            }
            if (Objects.equals(board[spot].color, board[spot + 1].color) &&
                    board[spot].group != board[spot + 1].group){
                buddies.add(spot + 1);
            }
            if (Objects.equals(board[spot].color, board[spot + 11].color) &&
                    board[spot].group != board[spot + 11].group){
                buddies.add(spot + 11);
            }
            if (Objects.equals(board[spot].color, board[spot + 10].color) &&
                    board[spot].group != board[spot + 10].group){
                buddies.add(spot + 10);
            }
        }
        return buddies;
    }

    /*******
     * print the game board to screen, with pieces shown either by color
     * or group number
     * @param testing determines whether to print group numbers or just colors
     */
    public void printBoard(boolean testing) {
        StringBuilder spacing = new StringBuilder("");
        StringBuilder row = new StringBuilder("");
        String ANSI_RED = "\u001B[31m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_RESET =   "\u001B[0m";
        String value;
        for (int i = 1; i < 12; i++) {
            spacing.append("  ");
            row.append(spacing);
            for (int j = 1 ; j < 12 ; j++){
                int test = (i-1) * 11 + j;
                if (testing) {
                    value = String.valueOf(board[test].group);
                } else {
                    value = board[test].color;
                }
                if (Objects.equals(board[test].color, "R")){
                    row.append(ANSI_RED + value + ANSI_RESET);
                } else if (Objects.equals(board[test].color, "B")){
                    row.append(ANSI_BLUE + value + ANSI_RESET);
                } else {
                    row.append(value + ANSI_RESET);
                }
                // for spacing between spots
                if (!testing){
                    row.append("  ");
                } else {
                    if (board[test].group > 99) {
                        row.append(" ");
                    } else if (board[test].group > 9) {
                        row.append("  ");
                    } else {
                        row.append("   ");
                    }
                }
            }
            System.out.println(row);
            row.delete(0,1000);
        }
    }
}