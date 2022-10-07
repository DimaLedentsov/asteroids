package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {
    public static Vector2 fromAngle(float angle, float m) {
        return new Vector2((float) Math.cos(angle) * m, (float) Math.sin(angle) * m);
    }

    public static void limitLow(Vector2 v, float l) {
        if (v.len() < l) {
            float angle = v.angleRad();
            // v.set(Vector2.Y).setAn
        }
    }
/*/
    public Vector2 normalize(Vector2 v) {
        return v.cpy().scl(1 / v.len());
    }*/

    public static float dot(Vector2 v1, Vector2 v2) {
        return (v1.x * v2.x + v1.y * v2.y);
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2 mult(Vector2 v, float val) {
        return new Vector2(v.x * val, v.y * val);
    }
    

    public static Vector2 neg(Vector2 v){
        return new Vector2(-v.x, -v.y);
    }
    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }
    public static Vector2 cross(Vector2 v, float z){
        return new Vector2(-v.y * z, v.x * z);
      }
      
      public static float cross(Vector2 v1, Vector2 v2){
        return v1.x * v2.y - v1.y * v2.x;
      }
      
    public static Vector2 tripleProduct(Vector2 a, Vector2 b, Vector2 c){
        Vector2 r = new Vector2();
        float dot = a.x * b.y - b.x * a.y;
        r.x = -c.y * dot;
        r.y = c.x * dot;
        return r;
    }
    public static Vector2 div(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x / v2.x, v1.y / v2.y);
    }
    
    public static Vector2 div(Vector2 v, float val) {
        return new Vector2(v.x / val, v.y / val);
    }
    public static  float mag(Vector2 v){
        return (float) Math.sqrt(v.x*v.x + v.y*v.y);
    }
      
    public static Vector2 normalize(Vector2 v){
        return (div(v, mag(v)));
    }
    
      

    // Get the vertex which is furthest along the given direction vector
    public static Vector2 getSupport(Polygon p, Vector2 direction) {
        float furthestDistance = -999999;
        Vector2 furthestVertex = new Vector2();

        for (int i = 0; i < p.getVertexCount(); i++) {
            float distance = dot(p.getVertex(i, new Vector2()), direction);
            if (distance > furthestDistance) {
                furthestDistance = distance;
                furthestVertex = new Vector2(p.getTransformedVertices()[i*2], p.getTransformedVertices()[i*2+1]);
            }
        }

        return furthestVertex;
    }

}
