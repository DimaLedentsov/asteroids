package com.dimka228.asteroids.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.objects.Updateable;
import com.dimka228.asteroids.utils.CollisionUtils;

public final class RigidBody implements Forceable, Updateable{
    private Polygon shape;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float angle;
    private Vector2 position;
    private float rotation;
    private float rotAcceleration;
    private float mass;
    private Type type;
    public boolean overlapping;
    float moi;
    public void update(){
        velocity.add(acceleration);
        velocity.limit(5);
        rotation += rotAcceleration;
        angle += rotation;
        position.add(velocity);
        shape.setPosition(position.x, position.y);
        shape.setRotation(angle);
    }

    public void updateAngle(){
        angle+=rotation;
    }

    public RigidBody(float[] vertices, Vector2 velocity, Vector2 acceleration, float angle, Vector2 position, float rotation, float mass) {
        this.shape = new Polygon(vertices);
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.angle = angle;
        this.position = position;
        this.rotation = rotation;
        this.mass = mass;
        this.type = Type.DYNAMIC;
        rotAcceleration =0;
        //moi = 
    }

    public void rotate(float a){
        setAngle(angle+a);
    }

    public void setRotationAcceleration(float a){
        rotAcceleration=a;
    }
    public float getRotationAcceleration(){
        return rotAcceleration;
    }
    public Polygon getShape() {
        return this.shape;
    }

    public void setShape(Polygon shape) {
        this.shape = shape;
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        shape.setPosition(position.x, position.y);
    }
    public void addPosition(Vector2 d){
        position.add(d);
        shape.setPosition(position.x, position.y);
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        shape.setRotation(rotation);
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void applyForce(Vector2 f){
        acceleration.set(f.scl(mass));
    }

    public enum Type{
        STATIC,
        DYNAMIC,
        GHOST
    }

    public Type getType(){
        return type;
    }
    public void setType(Type t){
        type = t;
    }


    public void collide(RigidBody b2){
        RigidBody b1 = this;
	    float dx = b1.getPosition().x - b2.getPosition().x;
	    float dy = b1.getPosition().y - b2.getPosition().y;///////////////
        
	    /*float angle = (float) Math.atan2((double)dy,(double)dx);
	    float dirx = (float)Math.cos(angle);
	    float diry = (float)Math.sin(angle);*/
        float angle = MathUtils.atan2(dy,dx);
        float dirx = MathUtils.cos(angle);
	    float diry = MathUtils.sin(angle);

        Vector2 v1 = b1.getVelocity().cpy();
        Vector2 v2 = b2.getVelocity().cpy();
        float m1 = b1.getMass();
        float m2 = b2.getMass();

        

        
        
        b1.setVelocity(v1.scl(m1-m2).add(v2.scl(2*m2)).scl(1/(m1+m2)));

        Vector2 v = CollisionUtils.getCollusionPoint(b1.getShape(), b2.getShape());
        if(v==null) return;

        float xp = (b2.getPosition().x - b1.getPosition().x)*(v.y - b1.getPosition().y) - (v.x - b1.getPosition().x)*(b2.getPosition().x - b1.getPosition().y);

        float rot1 = 3;
        if(b1.getType()==Type.DYNAMIC) b1.setRotation(xp>0?rot1:-rot1);

        b1.addPosition(new Vector2(dirx, diry));
	    /*while(CollisionUtils.collides(b1.getShape(), b2.getShape())){
            b1.addPosition(new Vector2(dirx, diry));
            //else b2.addPosition(new Vector2(-dirx, -diry));
	    }*/
    
    }
    public RigidBody copy(){
        return new RigidBody(this.shape.getVertices().clone(), velocity.cpy(), acceleration.cpy(), angle, position.cpy(), rotation, mass);
    }


}
