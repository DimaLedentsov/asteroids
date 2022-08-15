package com.dimka228.asteroids.physics;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public final class RigidBody {
    private Polygon shape;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float angle;
    private Vector2 position;
    private float rotation;
    private float mass;
    public void update(){
        velocity.add(acceleration);
        velocity.limit(5);
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


}
