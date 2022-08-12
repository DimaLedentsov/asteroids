package com.dimka228.asteroids.utils;

public class Animation {
    private int duration;
    private boolean finished;

    private Callback callback;
    public Animation(int d, Callback c){
        duration = d;
        callback = c;
    }
    public void animate(){
        callback.animate();
        duration--;
        if(duration<=0) finished=true;
    }
    public boolean isFinished(){
        return finished;
    }
    public interface Callback{
        public void animate();
    }

}
