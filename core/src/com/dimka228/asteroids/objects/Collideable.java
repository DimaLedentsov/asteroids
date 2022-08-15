package com.dimka228.asteroids.objects;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.physics.RigidBody;

public interface Collideable {
    public void collide(Collideable c);
    public RigidBody getBody();
}
