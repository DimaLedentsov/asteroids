package com.dimka228.asteroids.objects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class AbstractGameObject implements GameObject{
    protected GameObject.Type type;
    protected Status status;

    protected int layer;
    protected Color color;
    protected float lineWidth;
    public AbstractGameObject(Type type){

        //sprite = new Sprite(tex);
        this.type = type;
        status = Status.ALIVE;
        color = Color.WHITE;
        layer=0;
        lineWidth = 3;
    }
    

    public Type getType(){
        return type;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status s){
        status = s;
    }


    public int getLayer(){
        return layer;
    }




   
}
