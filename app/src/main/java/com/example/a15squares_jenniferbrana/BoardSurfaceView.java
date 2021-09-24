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

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    //COMMENT
    public static final float boardSize = 1000; //width of board
    public static final float top = 200;
    public static final float left = 100;

    //COMMENT
    private Paint color;
    private int numSquares;
    private float sqSize;
    private ArrayList<Square> theSquares;
    private boolean isCorrect;

    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //COMMENT
        setWillNotDraw(false);

        //init isCorrect to false
        isCorrect = false;

        //COMMENT
        color = new Paint();
        color.setColor(Color.BLACK);

        numSquares = 4; //COMMENT

        theSquares = new ArrayList<Square>();

        createBoard();

    }

    //COMMENT
    public void createBoard(){
        sqSize = boardSize / numSquares; //COMMENT

        theSquares = new ArrayList<Square>();

        //generate arraylist with all the numbers
        ArrayList<String> numLabels = new ArrayList<String>();
        numLabels.add(""); //add empty string to list
        for(int n = 1; n < numSquares*numSquares; n++){
            numLabels.add(String.valueOf(n));
        }


        //generate random board by shuffling ArrayList of number labels
        Collections.shuffle(numLabels);

        //COMMENT
        for(int i = 0; i < numSquares; i++){
            for(int j = 0; j < numSquares; j++){
                int index = (j+(i*numSquares));
                Square newSquare = new Square(top+sqSize*i, left+sqSize*j, sqSize, numLabels.get(index));
                theSquares.add(newSquare);
            }
        }

        checkCorrect();
    }

    //COMMENT
    @Override
    public void onDraw(Canvas canvas){

        canvas.drawRect(left, top, left+boardSize, top+boardSize, color);

        //COMMENT
        for(Square sq: theSquares){
            sq.draw(canvas);
        }
    }

    //COMMENT
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int first; //COMMENT

        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            first = getTouchedSquare(x, y);
            if(first == -1){
                return false;
            }

            //COMMENT
            boolean swapped = swapSquares(first);
            if(swapped){
                checkCorrect();
                invalidate();
            }
        }

        return false;
    }

    //COMMENT
    public int getTouchedSquare(float x, float y){
        int xCoor = (int)((x-left) / sqSize);
        int yCoor = (int)((y-top) / sqSize);

        //COMMENT
        if(xCoor >= numSquares | yCoor >= numSquares){
            return -1; //return null if it is out of bounds
        }

        return (xCoor+(yCoor*numSquares)); //COMMENT
    }

    //COMMENT
    public boolean swapSquares(int sq){
        int max = numSquares * numSquares - 1;

        if(sq - numSquares >= 0){
            if(!((sq-numSquares)%numSquares == (numSquares-1) & (sq)%numSquares==0)) {
                if (theSquares.get(sq - ((int) numSquares)).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq));
                    theSquares.get(sq).setNum(theSquares.get(sq - ((int) numSquares)).getNum());
                    theSquares.get(sq - ((int) numSquares)).setNum(tempSquare.getNum());
                    return true;
                }
            }
        }

        if(sq+numSquares <= max){
            if(!((sq+numSquares)%numSquares == (numSquares-1) & (sq)%numSquares==0)){
                if(theSquares.get(sq+((int)numSquares)).getNum().equals("")){
                    Square tempSquare = new Square(theSquares.get(sq));
                    theSquares.get(sq).setNum(theSquares.get(sq+((int)numSquares)).getNum());
                    theSquares.get(sq+((int)numSquares)).setNum(tempSquare.getNum());
                    return true;
                }
            }


        }

        if(sq-1 >= 0){
            if(!((sq-1)%numSquares == (numSquares-1) & (sq)%numSquares==0)) {
                if (theSquares.get(sq - 1).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq));
                    theSquares.get(sq).setNum(theSquares.get(sq - 1).getNum());
                    theSquares.get(sq - 1).setNum(tempSquare.getNum());
                    return true;
                }
            }
        }

        if(sq+1 <= max){
            if(!((sq+1)%numSquares == 0 & (sq)%numSquares==(numSquares-1))) {
                if (theSquares.get(sq + 1).getNum().equals("")) {
                    Square tempSquare = new Square(theSquares.get(sq));
                    theSquares.get(sq).setNum(theSquares.get(sq + 1).getNum());
                    theSquares.get(sq + 1).setNum(tempSquare.getNum());
                    return true;
                }
            }
        }


        return false;
    }

    //COMMENT
    @Override
    public void onProgressChanged(SeekBar seekBar, int size, boolean b) {
        //COMMENT
        if(size >= 2){
            numSquares = size;
            createBoard();
            invalidate();
        }
        else{
            numSquares = 2;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //when the reset button is clicked, it creates a new board and invalidates the drawing'
    //so a new board is drawn
    @Override
    public void onClick(View view) {
        createBoard();
        invalidate();
    }

    //checkCorrect: method checks if the board is correct.
    //sets isCorrect field to true or false
    //COMMENT
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
}
