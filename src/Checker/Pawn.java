package Checker;

public class Pawn {
    char x;
    int y;
    boolean king;
    char color;
    /*
     * There are three types of colors I use " " to represent that there's no pawn in
     * this box, " " == empty
     */

    public Pawn(char x, int y, boolean king, char color) {
        this.x = x;
        this.y = y;
        this.king = king;
        this.color = color;
    }

    public char getx() {
        return x;
    }

    public void setx(char x) {
        this.x = x;
    }

    public int gety() {
        return y;
    }

    public void sety(int y) {
        this.y = y;
    }

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    
}