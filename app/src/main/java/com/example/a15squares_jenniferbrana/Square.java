package com.example.a15squares_jenniferbrana;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//COMMENT
public class Square {
    private float left;
    private float top;
    private float size;
    private String num;
    private Paint color;
    private Paint textColor;

    //COMMENT
    public Square(float t, float l, float s, String n){
        left = l;
        top = t;
        size = s;
        num = n;

        color = new Paint();
        color.setColor(Color.WHITE);

        textColor = new Paint();
        textColor.setColor(Color.BLACK);
        textColor.setTextSize(75.0f);
    }

    //Copy constructor for square
    public Square(Square sq){
        this.left = sq.left;
        this.top = sq.top;
        this.size = sq.size;
        this.num = sq.num;
        this.color = new Paint(sq.color);
        this.textColor = new Paint(sq.textColor);
    }

    //COMMENT
    public void draw(Canvas canvas){
        canvas.drawRect(left+10, top+10, left+size-10, top+size-10, color);

        //measure the length of the text so that it is centered
        float lText = textColor.measureText(num);

        //measure height of text
        Paint.FontMetrics fm = textColor.getFontMetrics();
        float height = fm.descent - fm.ascent;

        //draw text on the square
        canvas.drawText(num, (left+size/2 - lText/2), (top+size/2 + height/2 - 10), textColor);
    }

    public String getNum(){
        return num;
    }

    public void setNum(String n){
        num = n;
    }
}
