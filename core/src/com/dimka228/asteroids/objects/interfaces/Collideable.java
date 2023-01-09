package com.dimka228.asteroids.objects.interfaces;


import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface Collideable {
    public void collide(GameObject c);
    public Body getBody();
}
