package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.MathUtils;

public class RandomUtils {
    public static float randomBetween(float min, float max){
        return min + MathUtils.random() * (max - min);
    }
}
