package com.example.a15squares_jenniferbrana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * BoardSurfaceView: class to create and draw the game board as well as perform moving of squares and checking correctness.
 *
 * @author Jennifer Brana
 * @version 27 September 2021
 */

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    //set constants that are used to determine the size the board on the tablet.
    public static final float boardSize = 1000; //width of board
    public static final float top = 200; //dimension of where the top of the board starts
    public static final float left = 100; //dimension of where the left of the board starts

    //instance variables for a BoardSurfaceView object
    private Paint color; //Paint to set the color of the background of the board
    private int numSquares; //square root of the number of squares on the board --> the width or height of the board.
    private float sqSize; //size of a square to be drawn on the board
    private ArrayList<Square> theSquares; //ArrayList of squares on the board.
    private boolean isCorrect; //boolean to track if the board is solved yet.
    private int blankRow, blankCol, blankIndex; //mark where in the board is the blank when the board is created.

    /** BoardSurfaceView
     *
     * Function: constructor for the BoardSurfaceView that initializes all instance variables and creates the initial board.
     *
     * @param context
     * @param attrs
     */
    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //enable drawing on the board
        setWillNotDraw(false);

        //init isCorrect to false because the board hasn't been solved yet.
        isCorrect = false;

        //create the Paint object for the color of the background
        color = new Paint();

        //init the background color to Black.
        color.setColor(Color.BLACK);

        numSquares = 4; //initialize the number of squares to 4 so 15 will be drawn when the game starts.

        //call help function createBoard() to build the initial board for the game.
        createBoard();

    }

    /** createBoard()
     *
     * Function: creates a new board with random entries the based on the number of squares and the dimensions.
     *          Stores the new board in the instance variable theSquares.
     *
     */
    public void createBoard(){
        sqSize = boardSize / numSquares; //set the size of each squares based on the number of squares needed for the board.

        //init the ArrayList of squares
        theSquares = new ArrayList<Square>();

        //generate arraylist with all the numbers
        ArrayList<String> numLabels = new ArrayList<String>();
        numLabels.add(""); //add empty string to list
        for(int n = 1; n < numSquares*numSquares; n++){
            numLabels.add(String.valueOf(n));
        }

        //generate random board by shuffling ArrayList of number labels
        Collections.shuffle(numLabels);

        //loop through the board and add Square objects to the board.
        for(int i = 0; i < numSquares; i++){
            for(int j = 0; j < numSquares; j++){
                int index = (j+(i*numSquares));
                Square newSquare = new Square(top+sqSize*i, left+sqSize*j, sqSize, numLabels.get(index));

                //get index of blank box
                if(numLabels.get(index).equals("")){
                    blankRow = i;
                    blankCol = j;
                    blankIndex = index;
                }

                theSquares.add(newSquare);
            }
        }

        //check if the board is solvable
        if(!isSolvable()){
            createBoard();
        }

        //check if the new board is correct so the background color will change if it is correct.
        checkCorrect();
    }

    /** onDraw()
     *
     * Function: draws the board on the tablet each time it is called.
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas){

        //draw the black background of the board first.
        canvas.drawRect(left, top, left+boardSize, top+boardSize, color);

        //loop through all the Squares in theSqaures ArrayList, drawing each one individual by calling using the draw function from the square class.
        for(Square sq: theSquares){
            sq.draw(canvas);
        }
    }

    /** onTouch()
     *
     * Function: record the touch events of the user attempting to move the squares.
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int first; //record the location of the first touch event to select the square to be moved.

        //get the x and y coordinates of the touch to find the square the user wants to move.
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            //use the x and y coordinates to calculate the square touched by calling getTouchedSquare()
            first = getTouchedSquare(x, y);

            //if the touch is not a square, the first value will be -1 to record a false move which is rejected.
            if(first == -1){
                return false;
            }

            //call method swapSquares() on the square the user wants to move and check if the selected square is able to be swapped with the empty square.
            boolean swapped = swapSquares(first);

            //if the swap completed, call checkCorrect() to check if the board is now solved
            //redraw the board with the users move by calling invalidate()
            if(swapped){
                checkCorrect();
                invalidate();
            }
        }

        return false;
    }

    /** getTouchedSquare()
     *
     * Function: Use the x & y coordinates of the square the user wants to select to find the square the user is trying to move.
     *
     * @param x     x coordinate of the touch on the board
     * @param y     y coordinate of the touch on the board
     * @return      index in theSquares ArrayList of the square touched by the user
     */
    public int getTouchedSquare(float x, float y){
        int xCoor = (int)((x-left) / sqSize);
        int yCoor = (int)((y-top) / sqSize);

        //make sure that the spot touched was on the board to ensure a touch that was out of bounds is rejected.
        if(xCoor >= numSquares | yCoor >= numSquares){
            return -1; //return null if it is out of bounds
        }

        return (xCoor+(yCoor*numSquares)); //return the index of the square that corresponds to the spot the user touched.
    }

    /** swapSquares()
     *
     * Function:
     *
     * @param sq    the index of the square that the user wants to swap with the empty square.
     * @return      return true if the square was swapped, return false if the square is not able to be swapped.
     */
    public boolean swapSquares(int sq){
        int max = numSquares * numSquares - 1; //max index of the theSquares ArrayList.

        //check the square directly above the selected square to see if it is the empty square.
        //      first make sure there is a square above.
        if(sq - numSquares >= 0){
            if(!((sq-numSquares)%numSquares == (numSquares-1) & (sq)%numSquares==0)) { //ensures the swapping squares are in the same row or column
                if (theSquares.get(sq - ((int) numSquares)).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq)); //create temp square to be used in the swap
                    theSquares.get(sq).setNum(theSquares.get(sq - ((int) numSquares)).getNum());
                    theSquares.get(sq - ((int) numSquares)).setNum(tempSquare.getNum());
                    return true; //return true if the squares swapped
                }
            }
        }

        //check the square directly below the selected square to see if it is the empty square.
        //      make sure first that there is a square below.
        if(sq+numSquares <= max){
            if(!((sq+numSquares)%numSquares == (numSquares-1) & (sq)%numSquares==0)){ //ensures the swapping squares are in the same row or column
                if(theSquares.get(sq+((int)numSquares)).getNum().equals("")){
                    Square tempSquare = new Square(theSquares.get(sq)); //create temp square to be used in the swap
                    theSquares.get(sq).setNum(theSquares.get(sq+((int)numSquares)).getNum());
                    theSquares.get(sq+((int)numSquares)).setNum(tempSquare.getNum());
                    return true; //return true if the squares swapped
                }
            }


        }

        //check the square to the left of the selected square to see if it is the empty square.
        //      make sure first that there is a square to the left.
        if(sq-1 >= 0){
            if(!((sq-1)%numSquares == (numSquares-1) & (sq)%numSquares==0)) { //ensures the swapping squares are in the same row
                if (theSquares.get(sq - 1).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq)); //create temp square to be used in the swap
                    theSquares.get(sq).setNum(theSquares.get(sq - 1).getNum());
                    theSquares.get(sq - 1).setNum(tempSquare.getNum());
                    return true; //return true if the squares swapped
                }
            }
        }

        //check the square directly to the right of the selected square to see if it is the empty square.
        //      make sure first that there is a square to the right.
        if(sq+1 <= max){
            if(!((sq+1)%numSquares == 0 & (sq)%numSquares==(numSquares-1))) { //ensures the swapping squares are in the same row
                if (theSquares.get(sq + 1).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq)); //create temp square to be used in the swap
                    theSquares.get(sq).setNum(theSquares.get(sq + 1).getNum());
                    theSquares.get(sq + 1).setNum(tempSquare.getNum());
                    return true; //return true if the squares swapped
                }
            }
        }


        return false; //return false if no squares were swapped
    }

    /** onProgressChanged()
     *
     * Function: record the changes of the seekBar that is used to set the number of squares on the board then change the numSquares instance variable
     *           for the BoardSurfaceView and build a new board for the selected size.
     *
     * Caveat: the minimum size of the board allowed is 2x2. The max is 10x10.
     *
     * @param seekBar   seekBar on the board
     * @param size      the size specified by the progress of the seekBar
     * @param b         boolean from user
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int size, boolean b) {
        //if the size is larger than or equal to 2, set the numSquares to the size specified and create a new board.
        if(size >= 2){
            numSquares = size;
        }
        else{
            numSquares = 2;
        }
        createBoard();
        invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /** onClick()
     *
     * Function: monitors the reset button so that when the reset button is clicked,
     * it creates a new board and invalidates the drawing so a new board is drawn
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        createBoard(); //generate a new board
        invalidate(); //invalidate the current view so it is redrawn
    }

    /** checkCorrect()
     *
     * Function: method checks if the board is correct and sets isCorrect field to true or false
     *
     */
    public void checkCorrect(){
        //check if the value in the square is the same as it's index
        for(int i = 0; i < numSquares*numSquares-1; i++){
            if(!theSquares.get(i).getNum().equals(String.valueOf(i+1))){
                isCorrect = false;
                color.setColor(Color.BLACK); //sets the color to black for false
                return;
            }
        }

        //check if the last square is empty
        if(!theSquares.get(numSquares*numSquares-1).getNum().equals("")){
            isCorrect = false;
            color.setColor(Color.BLACK); //sets the color to black for false
            return;
        }

        isCorrect = true; //if no false squares are found, set to correct
        color.setColor(Color.GRAY); //set the color of the background to gray if it is correct
    }

    /** isSolvable()
     *
     * Function: determine if the board arrangement can be solved.
     *
     * @return      returns a boolean that is true if the board can be solved and false otherwise.
     */
    public boolean isSolvable(){
        //check if the width of the grid is odd or even.
        boolean even = numSquares % 2 == 0;

        //get the number of inversions on the board
        int inversions = countInversions();

        //get if the blank square is in an even row or an odd row
        boolean rowOdd = isRowOdd();

        //True Conditions:
        //if the width is even
        if(even){
            //if the number of inversions is even, the blank must be in an odd row --> True Condition
            if(inversions % 2 == 0 && rowOdd){
                return true;
            }

            //if the number of inversions is odd, the blank must be in an even row --> True Condition
            if(inversions % 2 != 0 && !rowOdd){
                return true;
            }
        }

        //if the width is odd, the number of inversions must be even --> True Condition
        if(!even && inversions % 2 == 0){
            return true;
        }


        return false;
    }

    /** countInversions()
     *
     * Function: counts the number of inversions in the given board.
     *
     * @return  return the number of inversions in the board.
     */
    public int countInversions(){
        int count = 0; //init count variable to 0

        //loop through all the Squares in the theSquares ArrayList
        for(int i = 0; i < numSquares * numSquares - 1; i++){
            //loop through all the Squares in theSquares ArrayList after the current Square selected by i
            for(int j = i + 1; j < numSquares * numSquares; j++){
                //make sure it's not checking the blank square
                if(i == blankIndex || j == blankIndex){
                    continue;
                }

                //increment count if the value of i is greater than j
                //  do this because a pair is an inversion if i appears before j but the value of i > j
                int valI = Integer.valueOf(theSquares.get(i).getNum());
                int valJ = Integer.valueOf(theSquares.get(j).getNum());
                if(valI > valJ){
                    count += 1;
                }
            }
        }

        return count;
    }

    /** isRowOdd()
     *
     * Function: check if the row is even or odd from the bottom of the board
     *
     * @return  true if the row is odd, false if it is even
     */
    public boolean isRowOdd(){
        //check starting from the last row if the row of the blank is odd
        for(int i = numSquares-1; i >= 0; i-=2){
            if(blankRow == i){
                return true;
            }
        }

        return false;
    }

}





