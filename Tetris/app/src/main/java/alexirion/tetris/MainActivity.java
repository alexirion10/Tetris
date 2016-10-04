package alexirion.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static int columns = 10;
    private static int rows = 22;
    private boolean gameFinished;
    private int timeDelay;

    private Handler handler = new Handler();    //for time delay between moves

    private TGrid tetrisGameGrid;   //grids to put within our custom view
    private TGrid nextPieceGrid;

    private Tetromino realBoardPiece;   //current piece moving on the board
    private Tetromino nextBoardPiece;   //the piece on the board next (queued up)

    private TetrisGridView tetrisGameView;  //view for the actual game
    private TetrisGridView nextPieceView;   //view for next up piece

    private int level;
    private TextView levelView;
    private int score;
    private TextView scoreView;
    private int rowsCleared;
    private TextView rowsClearedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initControls();
        initGameValues();
        runGame();
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tetris");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_gallery);
    }

    public void initControls() {
        Button moveLeft = (Button) findViewById(R.id.left);
        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBoardPiece.shiftLeft();
                tetrisGameView.invalidate();
            }
        });

        Button rotateLeft = (Button) findViewById(R.id.rotateLeft);
        rotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBoardPiece.rotateCounterClockwise();
                tetrisGameView.invalidate();
            }
        });

        Button down = (Button) findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBoardPiece.zoomDown();
                tetrisGameView.invalidate();
            }
        });

        Button rotateRight = (Button) findViewById(R.id.rotateRight);
        rotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBoardPiece.rotateClockwise();
                tetrisGameView.invalidate();
            }
        });

        Button moveRight = (Button) findViewById(R.id.right);
        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBoardPiece.shiftRight();
                tetrisGameView.invalidate();
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //restart the game somehow!!!
                initGameValues();
            }
        });

        tetrisGameView = (TetrisGridView) findViewById(R.id.gameView);
        nextPieceView = (TetrisGridView) findViewById(R.id.nextPieceView);
        levelView = (TextView) findViewById(R.id.levelTextEditView);
        scoreView = (TextView) findViewById(R.id.scoreTextEditView);
        rowsClearedView = (TextView) findViewById(R.id.rowsTextEditView);

    }

    public void initGameValues() {
        //set values for a new game
        tetrisGameGrid = new TGrid(columns, rows);
        nextPieceGrid = new TGrid(4, 6);
        timeDelay = 1000;
        level = 1;
        rowsCleared = 0;
        score = 0;
        gameFinished = false;
        updateTextViews();

        //add our created grids to the custom view
        tetrisGameView.addTGrid(tetrisGameGrid);
        //invalidate the view to update it with the changed underlying grid
        tetrisGameView.invalidate();

        nextPieceView.addTGrid(nextPieceGrid);
        nextPieceView.invalidate();

        realBoardPiece = TetrominoBuilder.Random();
        nextBoardPiece = TetrominoBuilder.Random();

        realBoardPiece.insertIntoGrid(4, 0, tetrisGameGrid);
        nextBoardPiece.insertIntoGrid(0, 2, nextPieceGrid);

    }

    public void runGame() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!gameFinished) {
                    //shift the piece down
                    if (!realBoardPiece.shiftDown()) {
                        //we are unable to move the piece down anymore
                        while (tetrisGameGrid.getFirstFullRow() != -1) {
                            //a row is full... remove it and add a level plus points to score
                            tetrisGameGrid.deleteRow(tetrisGameGrid.getFirstFullRow());
                            clearRow();
                        }

                        //move next board piece to the board, and get a new next
                        nextBoardPiece.removeFromGrid();
                        realBoardPiece = nextBoardPiece;
                        nextBoardPiece = TetrominoBuilder.Random();

                        //see if we can insert our piece to the board still
                        if (!realBoardPiece.insertIntoGrid(4, 0, tetrisGameGrid)) {
                            gameFinished = true;
                            Toast.makeText(getApplicationContext(), "GAME OVER!", Toast.LENGTH_LONG).show();
                        }
                    }

                    nextBoardPiece.insertIntoGrid(0, 2, nextPieceGrid);

                    //update both grids
                    nextPieceView.invalidate();
                    tetrisGameView.invalidate();

                    //starts at 1 second, decreased by 20% at each level
                    handler.postDelayed(this, timeDelay);
                }
                else {
                    //stop the thread??
                    Toast.makeText(getApplicationContext(), "RESET THE GAME!", Toast.LENGTH_LONG).show();
                    handler.postDelayed(this, 10000); //15 seconds
                }
            }
        });
    }

    public void updateTextViews() {
        levelView.setText(level + "");
        scoreView.setText(score + "");
        rowsClearedView.setText(rowsCleared + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.level_up) {
            level++;
            levelChanged();
            updateTextViews();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void clearRow() {
        rowsCleared++;
        //Every time a row is cleared, add a number of points equal to the current level to the
        // score. At level 1 add one point for each row, at level 2 two points
        score += level;

        //every time 5 rows have been cleared increment the level
        if(rowsCleared % 5 == 0) {
            level++;
            levelChanged();
        }
        updateTextViews();

    }

    public void levelChanged() {
        //time should be about 1 second between movement to start game
        //everytime the level is changed decrease the time between each block movement by 20%
        timeDelay *= .8;
    }
}
