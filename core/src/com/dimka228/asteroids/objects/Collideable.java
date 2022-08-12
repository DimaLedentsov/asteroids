package com.dimka228.asteroids.objects;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public interface Collideable {
    public void collide(Collideable c);
    public Vector2 getPosition();
    public Polygon getShape();
}
