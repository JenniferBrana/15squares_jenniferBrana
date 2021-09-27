package com.example.a15squares_jenniferbrana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * MainActivity
 *
 * This is the main activity for the 15squares game.
 *
 * Enhancements:
 *          1. Size of the puzzle can be changed using seekBar at bottom of the board.
 *          2. Randomly initializes board so that it is solvable.
 *
 * @author Jennifer Brana
 * @version 27 September 2021
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Set the screen orientation to portrait
        setContentView(R.layout.activity_main);

        //Setup a BoardSurfaceView to draw the board and respond to user clicks
        BoardSurfaceView boardView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        //set OnTouchListener to monitor the board for user clicks
        boardView.setOnTouchListener(boardView);

        //COMMENT
        SeekBar sizeSeekBar = (SeekBar) findViewById(R.id.SizeSeekBar);
        sizeSeekBar.setOnSeekBarChangeListener(boardView);

        //setting up reset button
        Button resetButton = (Button) findViewById(R.id.resetButton);

        //set listener for reset button
        resetButton.setOnClickListener(boardView);
    }
}