package com.dimka228.asteroids.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.objects.interfaces.Ship;
import com.dimka228.asteroids.objects.interfaces.Shootable;
import com.dimka228.asteroids.objects.particles.ExplosionParticle;
import com.dimka228.asteroids.objects.particles.ThrustParticle;
import com.dimka228.asteroids.physics.BodyCategories;
import com.dimka228.asteroids.utils.Random;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;
//TODO: TEAMS!

public abstract class AbstractPlayer extends GameObjectImpl implements Ship{
    protected boolean isRotating;
    protected Vector2 shootingPoint;
    protected Vector2 thrustPoint;
    protected double hp;
    protected float rotation;
    protected float ammo;
    protected float reload;
    static final float maxVel  = 1.8f;
    protected boolean isRotatingRight;
    public double getHp() {
        return this.hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }
    public AbstractPlayer(float x, float y) {
 
        // super(coords, velocity, accel, texture, type)
        super( Type.PLAYER);
        isRotating = false;

        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        def.position.set(x,y);
        body = Game.getInstance().getWorld().createBody(def);
        PolygonShape poly = new PolygonShape();

        poly.set(new Vector2[] {
            new Vector2(-1,-1),
            new Vector2(1,-1),
            new Vector2(0,2)
        });
        shootingPoint = new Vector2();
        thrustPoint = new Vector2();
        poly.getVertex(2, shootingPoint);
        
        FixtureDef fix = new FixtureDef();
        fix.filter.categoryBits= BodyCategories.SCENERY;
        fix.filter.maskBits=BodyCategories.SCENERY;
        fix.shape=poly;
        fix.density=0.1f;

        body.createFixture(fix);
        poly.dispose(); 
        
        body.setUserData(this);
        layer = 2;    
        hp = 1;  
        rotation = 0.2f;
        ammo=1;
        reload= 0.1f;
        
      
        
    }
   
    

    public void update(){
        ammo+=reload;
        Vector2 vel = body.getLinearVelocity();
        if(vel.len()>maxVel) body.setLinearVelocity(vel.nor().scl(maxVel));
        if(hp<=0) die();
    }
    public void collide(GameObject obj){
    
        
    }


    public void die(){
        setStatus(Status.DESTROYED);
        explode();
        
    }

    public void shoot(float  a){
        if(ammo<1) return;
        //shootingPoint = body.getPosition();
        //body.getTransform().mul(shootingPoint);
        ((PolygonShape)(body.getFixtureList().first().getShape())).getVertex(1, shootingPoint);
        body.getTransform().mul(shootingPoint);
        Bullet bullet = new  Bullet(this,shootingPoint.x, shootingPoint.y, a);
        ammo = 0;

        
        Game.getInstance().addObject(bullet);
    }

    public void rotate(float angle){
        body.setAngularVelocity(angle);
    }

    public void rotateRight(){
        if(!isRotating) {
            rotate(rotation);//body.setTransform(body.getPosition(), body.getAngle()-0.1f);
            isRotatingRight = true;
        }

       
    }
    public void rotateLeft(){
        if(!isRotating) {
            rotate(-rotation);
            isRotatingRight=false;
        }
    }
    void shootForward(){
        shoot(body.getAngle());
    }

    public void thrust(){
        float mod = 0.1f;
        
        Vector2 force = (new Vector2(Vector2.Y)).scl(body.getMass()).scl(mod).rotateRad(body.getAngle());//VectorUtils.fromAngle(angle, mod);
        //Vector2 pvel = VectorUtils.neg(force);
        //Vector2 enginePoint = new Vector2(0,-1);
        //body.getTransform().mul(enginePoint);
        //Particle particle = new  Particle(this,enginePoint.x, enginePoint.y, body.getAngle());
        //
        //Game.getInstance().addObject(particle);
        //System.out.println(angle);
        //force.rotateDeg(body.getRo());
        //System.out.println(force.angleDeg());
        //Game.getInstance().addObject(new ThrustParticle(body.getPosition().x, body.getPosition().y, body.getAngle()));
        thrustPoint = new Vector2(0, -1);
        body.getTransform().mul(thrustPoint);
        Game.getInstance().addObject(new ThrustParticle(thrustPoint.x, thrustPoint.y, body.getAngle()));
    
    
        body.applyForceToCenter(force, true);
    }

    public void stopRotation(){
        body.setAngularVelocity(0);
        isRotating = false;
    }

    public void takeHp(double d){
        hp-=d;
    }

    public void explode(){
        Game.getInstance().addObject(new ExplosionParticle(body.getPosition(), Vector2.Zero, 0.1f, 0.1f, 0.01f, Color.YELLOW));
        for(int i=0; i<10;i++){
            Game.getInstance().addObject(new ExplosionParticle(body.getPosition(), VectorUtils.randomVector(0.5f, 2),0.1f, 0.5f, -0.02f, 0.01f, Color.ORANGE));
        }

    }



    
}
