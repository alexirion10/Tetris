package alexirion.tetris;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cody on 9/23/15.
 */
public class TGrid {

    protected ArrayList<ArrayList<TCell>> grid;

    protected int columns;
    protected int rows;

    public TGrid(int columns, int rows) {

        this.columns = columns;
        this.rows = rows;

        //Generate an empty grid
        this.grid = new ArrayList<>();
        for(int iii = 0; iii < this.columns; iii++) {
            ArrayList<TCell> nextColumn = new ArrayList<>();
            for(int jjj = 0; jjj < this.rows; jjj++) {
                nextColumn.add(null);
            }
            this.grid.add(nextColumn);
        }

    }

    public int getWidth() {
        return this.columns;
    }

    public int getHeight() {
        return this.rows;
    }

    //return a pointer to the cell at a position (X,Y)
    public TCell getCellAt(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return null;
        }
        return this.grid.get(X).get(Y);
    }

    //same as getCellAt(X,Y), except this function also removes it from the grid
    public TCell extractCellAt(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return null;
        }
        TCell result = this.grid.get(X).get(Y);
        this.grid.get(X).set(Y, null);
        return result;
    }

    //insert a cell into the grid
    public void putCell(int X, int Y, TCell cell) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return;
        }
        cell.setXPosition(X);
        cell.setYPosition(Y);
        this.grid.get(X).set(Y, cell);
    }

    //remove a cell from the grid
    public void removeCell(int X, int Y) {
        if(X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return;
        }
        TCell theCell = this.grid.get(X).get(Y);
        if(theCell != null) {
            this.grid.get(X).set(Y, null);
            theCell.setYPosition(-1);
            theCell.setXPosition(-1);
        }
    }

    //look for the first row that is full
    //returns -1 if not found
    public int getFirstFullRow() {
        for(int jjj = 0; jjj < this.rows; jjj++) {
            boolean fullRow = true;
            for(int iii = 0; iii < this.columns; iii++) {
                if(this.grid.get(iii).get(jjj) == null) {
                    fullRow = false;
                    break;
                }
            }
            if(fullRow) {
                return jjj;
            }
        }
        return -1;
    }

    //deletes a row, shifts everything down
    public void deleteRow(int row) {
        for(int iii = 0; iii < this.columns; iii++) {
            this.removeCell(iii, row);
        }
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = row; jjj >= 0; jjj--) {
                TCell cellToMove = this.extractCellAt(iii, jjj - 1);
                if(cellToMove != null) {
                    this.putCell(iii, jjj, cellToMove);
                }
            }
        }
    }

    //delete all cells
    public void clear() {
        for(int iii = 0; iii < this.columns; iii++) {
            for(int jjj = 0; jjj < this.rows; jjj++) {
                this.grid.get(iii).set(jjj, null);
            }
        }
    }

}
