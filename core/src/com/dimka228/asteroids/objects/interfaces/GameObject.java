package com.dimka228.asteroids.objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;


public interface GameObject extends Renderable, Updateable, Destroyable, Collideable{

    public enum Type{
        WALL,
        SHIP,
        BULLET,
        BACKGROUND,
        OBSTACLE,
        PARTICLE
    }
    public enum Status{
        ALIVE,
        DESTROYED,
        DYING
    }
    Type getType();
    //com.badlogic.gdx.physics.box2d.Body getBody();
    public int getLayer();
    public void setStatus(Status s);
    public Status getStatus();
    public class SortingComparator implements Comparator<GameObject>{
        public int compare(GameObject a,GameObject b){
            return a.getLayer()>b.getLayer()?0:-1;
        }
    }
}
