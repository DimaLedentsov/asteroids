package com.dimka228.asteroids.objects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.physics.RigidBody;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObjectImpl implements GameObject{
    protected GameObject.Type type;
    protected Status status;
    protected Game game;
    protected int layer;
    protected RigidBody body;
    public GameObjectImpl(Game game, Type type){

        //sprite = new Sprite(tex);
        this.type = type;
        status = Status.ALIVE;
        this.game = game;
        layer=0;
    }
    public void update(){
        
        //sprite.setPosition(pos.x, pos.y);
        /*shape.setX(coords.x);
        shape.setY(coords.y);*/
    }

    public void collide(Collideable obj){
        
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

    public RigidBody getBody(){
        return body;
    }
}
