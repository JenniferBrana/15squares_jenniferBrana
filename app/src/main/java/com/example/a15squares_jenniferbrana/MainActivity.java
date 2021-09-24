package com.example.a15squares_jenniferbrana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        BoardSurfaceView boardView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        //set OnTouchListener
        boardView.setOnTouchListener(boardView);

        //COMMENT
        SeekBar sizeSeekBar = (SeekBar) findViewById(R.id.SizeSeekBar);
        sizeSeekBar.setOnSeekBarChangeListener(boardView);

        //setting up reset button
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(boardView);
    }
}