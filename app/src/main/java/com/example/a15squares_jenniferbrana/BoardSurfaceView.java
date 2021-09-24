package com.example.a15squares_jenniferbrana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener {
    //COMMENT
    public static final float boardSize = 1000; //width of board
    public static final float top = 200;
    public static final float left = 100;

    //COMMENT
    private Paint color;
    private int numSquares;
    private float sqSize;
    private ArrayList<Square> theSquares;

    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //COMMENT
        setWillNotDraw(false);

        //COMMENT
        color = new Paint();
        color.setColor(Color.BLACK);

        numSquares = 4; //COMMENT
        sqSize = boardSize / numSquares; //COMMENT

        theSquares = new ArrayList<Square>();

        //generate arraylist with all the numbers
        ArrayList<String> numLabels = new ArrayList<String>();
        numLabels.add(""); //add empty string to list
        for(int n = 1; n < 16; n++){
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

            //COMMENT
            boolean swapped = swapSquares(first);
            if(swapped){
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
        if(sq - numSquares >= 0){
            if(theSquares.get(sq-((int)numSquares)).getNum().equals("")){
                Square tempSquare = new Square(theSquares.get(sq));
                theSquares.get(sq).setNum(theSquares.get(sq-((int)numSquares)).getNum());
                theSquares.get(sq-((int)numSquares)).setNum(tempSquare.getNum());
                return true;
            }
        }

        if(sq+numSquares <= 15){
            if(theSquares.get(sq+((int)numSquares)).getNum().equals("")){
                Square tempSquare = new Square(theSquares.get(sq));
                theSquares.get(sq).setNum(theSquares.get(sq+((int)numSquares)).getNum());
                theSquares.get(sq+((int)numSquares)).setNum(tempSquare.getNum());
                return true;
            }
        }


        return false;
    }
}
