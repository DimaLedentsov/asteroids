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

    public static void processCollusion(RigidBody b1, RigidBody b2){
        
	    float dx = b1.getPosition().x - b2.getPosition().x;
	    float dy = b1.getPosition().y - b2.getPosition().y;///////////////
        
	    /*float angle = (float) Math.atan2((double)dy,(double)dx);
	    float dirx = (float)Math.cos(angle);
	    float diry = (float)Math.sin(angle);*/
        float angle = MathUtils.atan2(dy,dx);
        float dirx = MathUtils.cos(angle);
	    float diry = MathUtils.sin(angle);
        /*vel.rotateDeg(180).scl(0.5f);
        if(vel.len()<2){
            vel.scl(2);
        }*/
    
        Vector2 impulse1 = b1.getVelocity().scl(b1.getMass()),
                impulse2 = b2.getVelocity().scl(b2.getMass());
        


        Vector2 v = CollisionUtils.getCollusionPoint(b1.getShape(), b2.getShape());
        if(v==null) return;

        float xp = (b2.getPosition().x - b1.getPosition().x)*(v.y - b1.getPosition().y) - (v.x - b1.getPosition().x)*(b2.getPosition().x - b1.getPosition().y);

        float rot = 3;
        if(b1.getType()==Type.DYNAMIC) b1.setRotation(xp>0?rot:-rot);
        if(b2.getType()==Type.DYNAMIC) b2.setRotation(xp>0?-rot:rot);

        
	    while(CollisionUtils.collides(b1.getShape(), b2.getShape())){
	    	b1.addPosition(new Vector2(dirx, diry));
            b2.addPosition(new Vector2(-dirx, -diry));
	    }
    }
}
