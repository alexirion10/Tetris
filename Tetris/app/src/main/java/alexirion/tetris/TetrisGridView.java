package alexirion.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Canvas;

/**
 * Created by alexirion on 9/26/16.
 */
public class TetrisGridView extends View {

    private TGrid grid;
    private int cellWidth;
    private int cellHeight;
    private int gridWidth;
    private int gridHeight;
    private Paint borderPaint = new Paint();
    private Paint cellPaint = new Paint();

    public TetrisGridView(Context context){
        super(context);
    }

    public TetrisGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addTGrid(TGrid tg) {
        grid = tg;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //determine the size of the each cell
        gridWidth = grid.getWidth();
        gridHeight = grid.getHeight();
        cellWidth = getWidth() / gridWidth;
        cellHeight = getHeight() / (gridHeight - 2);

        //set our Paint values
        canvas.drawColor(Color.WHITE);

        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);

        cellPaint.setStyle(Paint.Style.FILL);

        //iterate over the entire board, fill in the color of the shapes
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 2; y < gridHeight; y++) {
                TCell currentCell = grid.getCellAt(x, y);

                if(currentCell != null) {
                    //update our color for each different cell we encounter
                    cellPaint.setColor(currentCell.getColor());
                    canvas.drawRect(x * cellWidth, (y-2) * cellHeight, (x + 1) * cellWidth, (y - 1) * cellHeight, cellPaint);
                    canvas.drawRect(x * cellWidth, (y-2) * cellHeight, (x + 1) * cellWidth, (y - 1) * cellHeight, borderPaint);

                }
            }
        }

    }
}
