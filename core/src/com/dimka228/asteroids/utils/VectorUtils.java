package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.Vector2;

public class VectorUtils {
    public static Vector2 fromAngle(float angle, float m){
        return new Vector2((float)Math.cos(angle)*m, (float)Math.sin(angle)*m);
    }
    public static void limitLow(Vector2 v, float l){
        if(v.len()<l){
            float angle = v.angleRad();
           // v.set(Vector2.Y).setAn
        }
    }
}
