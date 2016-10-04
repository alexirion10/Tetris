package alexirion.tetris;

/**
 * Created by cody on 9/23/15.
 *
 * This class represents a single box on the Tetris screen.
 */
public class TCell {

    protected int myColor;
    protected int xPos;
    protected int yPos;

    public TCell(int myColor) {
        this.myColor = myColor;
        this.xPos = -1;
        this.yPos = -1;
    }

    public String toString() {
        String result = "(";
        result += this.xPos;
        result += ",";
        result += ")";
        return result;
    }

    public int getColor() {
        return this.myColor;
    }

    public void setColor(int newColor) {
        this.myColor = newColor;
    }

    public int getXPosition() {
        return xPos;
    }

    public void setXPosition(int xPos) {
        this.xPos = xPos;
    }

    public int getYPosition() {
        return yPos;
    }

    public void setYPosition(int yPos) {
        this.yPos = yPos;
    }
}
