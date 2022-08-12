package com.dimka228.asteroids.utils;

public class Random {
    public static float between(float min, float max){
        return min + (float)(Math.random() * ((max - min) + 1));
    }
}
