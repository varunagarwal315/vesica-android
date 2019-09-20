package com.example.varun.vesica.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.varun.vesica.R;

/**
 * Custom button to allow for various fonts to be added to it using xml. Makes typecasting fonts much easier
 */
public class MyButton extends Button {


    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyButton(Context context) {
        super(context);
        init(null);
    }

    public void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyButton);
            String fontName = a.getString(R.styleable.MyButton_fontNameButton);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+"Roboto-Regular");
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }






}
