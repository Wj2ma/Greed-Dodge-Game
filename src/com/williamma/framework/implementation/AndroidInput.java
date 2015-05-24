package com.williamma.framework.implementation;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.williamma.framework.Input;

public class AndroidInput implements Input {    
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
       	touchHandler = new SingleTouchHandler(view, scaleX, scaleY);      
    }


    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public void setScaleX(float x) {
    	touchHandler.setScaleX(x);
    }
    
    public void setScaleY(float y) {
    	touchHandler.setScaleY(y);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
}
