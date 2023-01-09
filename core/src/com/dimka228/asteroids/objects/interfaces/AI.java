package com.dimka228.asteroids.objects.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface AI {
    void rotateTo(Vector2 t);
    void setTarget(GameObject o);
}
