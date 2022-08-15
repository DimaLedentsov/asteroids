package com.dimka228.asteroids.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.physics.RigidBody;

import java.util.Comparator;
public interface GameObject extends Renderable, Updateable, Collideable{

    public enum Type{
        WALL,
        PLAYER,
        BULLET,
        ENEMY,
        BACKGROUND
    }
    public enum Status{
        ALIVE,
        DEAD,
        DYING
    }
    Type getType();
    RigidBody getBody();
    public int getLayer();
    public void setStatus(Status s);
    public Status getStatus();
    public class SortingComparator implements Comparator<GameObject>{
        public int compare(GameObject a,GameObject b){
            return a.getLayer()>b.getLayer()?0:-1;
        }
    }
}
