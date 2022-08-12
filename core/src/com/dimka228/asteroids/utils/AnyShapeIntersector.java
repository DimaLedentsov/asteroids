package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public class AnyShapeIntersector {
    public static boolean overlaps(Shape2D first, Shape2D second){
        if(first instanceof Rectangle && second instanceof Circle){
            return Intersector.overlaps((Circle)second, (Rectangle)first);
        }
        if(second instanceof Rectangle && first instanceof Circle){
            return Intersector.overlaps((Circle)first, (Rectangle)second);
        }
        if(first instanceof Rectangle && second instanceof Rectangle){
            return Intersector.overlaps((Rectangle)first, (Rectangle)second);
        }
        return false;
    }
}
