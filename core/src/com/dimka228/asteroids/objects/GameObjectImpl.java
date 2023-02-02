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
import com.dimka228.asteroids.objects.interfaces.Collideable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObjectImpl extends AbstractGameObject implements GameObject, Collideable{

    protected Body body;
    
    public GameObjectImpl(Type type){
        
        //sprite = new Sprite(tex);
        super(type);
 
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

    public Body getBody(){
        return body;
    }

    public void destroy(){
        Game.getInstance().getWorld().destroyBody(body);
    }
    public void render(){
        ShapeDrawer drawer = Game.getInstance().getDrawer();
  

        PolygonShape p = (PolygonShape)body.getFixtureList().get(0).getShape();
        /*Vector2 v1=new Vector2();
        p.getVertex(p.getVertexCount()-1, v1);
        body.getTransform().mul(v1);
        v1.scl(Game.WORLD_TO_VIEW);
        Vector2 v2=new Vector2();
        p.getVertex(0, v2);
        body.getTransform().mul(v2);
        v2.scl(Game.WORLD_TO_VIEW);
        drawer.line(v2, v1);
        for(int i=1;i<p.getVertexCount();i++){
             v1=new Vector2();
            p.getVertex(i-1, v1);
            body.getTransform().mul(v1);
            v1.scl(Game.WORLD_TO_VIEW);
             v2=new Vector2();
            p.getVertex(i, v2);
            body.getTransform().mul(v2);
            v2.scl(Game.WORLD_TO_VIEW);
            drawer.line(v1, v2);
        }*/
        
        float vertices[] = new float [p.getVertexCount()*2] ;
        for(int i=0; i<p.getVertexCount();i++){
            Vector2 v1=new Vector2();
            p.getVertex(i, v1);
            
            body.getTransform().mul(v1);
            v1.scl(Game.WORLD_TO_VIEW);
            vertices[i*2] = v1.x;
            vertices[i*2+1] = v1.y;
        }
        
        //vertices = new float[]{1,1,5,3,4,0};
        drawer.setColor(color);
        
        drawer.polygon(vertices, lineWidth, JoinType.SMOOTH);
   
        //drawer.circle(getViewPosition().x, getViewPosition().y, 5);
    }

    public Vector2 getViewPosition(){
        return body.getPosition().scl(Game.WORLD_TO_VIEW);
    }
    public Vector2 getPosition(){
        return body.getPosition();
    }
}
