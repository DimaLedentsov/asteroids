package com.dimka228.asteroids.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.objects.interfaces.GameObject;

public class Wall extends GameObjectImpl {
    public Wall(float x, float y, float w, float h) {
        // super(coords, velocity, accel, texture, type)
    
        super( Type.WALL);
        BodyDef def = new BodyDef();
        def.type = BodyType.StaticBody;
        def.position.set(x, y);
        body = Game.getInstance().getWorld().createBody(def);
        PolygonShape poly = new PolygonShape();

        poly.setAsBox(w, h);
        body.createFixture(poly,  0.1f);
        poly.dispose(); 
        
        body.setUserData(this);
        layer = 2;      
        
    }
    public void update(){
       
        
        //body.update();
        
    }
    
    public void collide(GameObject obj){
        die();
    }
    public void render(){

    }


    public void die(){
        setStatus(Status.DYING);
        
    }
    
}
