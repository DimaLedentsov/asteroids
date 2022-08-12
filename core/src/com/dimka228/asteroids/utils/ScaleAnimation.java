package com.dimka228.asteroids.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScaleAnimation {

    float scaleX = 1;
    float scaleY = 1;
    float startX, startY = 0;
    Sprite sprite;
    /* ...duplicate and call through to super constructors here... */

    public ScaleAnimation(Sprite sp, float x, float y){
        sprite = sp;
        scaleX = x;
        scaleY = y;
    }
    
    public void apply(){
        sprite.setScale(sprite.getScaleX() + scaleX, sprite.getScaleX() + scaleY);
    }
}
