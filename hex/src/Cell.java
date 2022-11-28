/*******
 * class and constructor for the individual cells of the game board
 */
public class Cell {
    public int group;
    public String color;
    public int size;
    public Cell() {
        this.group = 0;
        this.size = 1;
        this.color = "W";
    }

    public Cell(int group, String color, int size) {
        this.group = group;
        this.color = color;
        this.size = size;
    }
}
