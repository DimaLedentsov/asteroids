package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.physics.RigidBody;
import com.dimka228.asteroids.physics.RigidBody.Type;

public class CollisionUtils {
    public static boolean collides(Polygon p1, Polygon p2){
        return Intersector.overlapConvexPolygons(p1, p2);
    }
    public static Vector2 getCollusionPoint(Polygon p1, Polygon p2){
        Vector2 p = null;
        float[] vertices = p1.getTransformedVertices();
        int count = vertices.length;

        for(int i = 0; i<count-1;i++){
            float x =vertices[i], y= vertices[i+1];
            if(Intersector.isPointInPolygon(p2.getTransformedVertices(), 0, p2.getTransformedVertices().length, x,y)){
                p = new Vector2(x,y);
                break;
            }
        }
        if(p==null){
            vertices = p2.getTransformedVertices();
            count = vertices.length;
            for(int i=0; i<count-1;i++){
                float x = vertices[i], y= vertices[i+1];
                if(Intersector.isPointInPolygon(p1.getTransformedVertices(), 0, p1.getTransformedVertices().length, x,y)){
                    p = new Vector2(x,y);
                    break;
                }
            }
        }
        return p;
    }

}