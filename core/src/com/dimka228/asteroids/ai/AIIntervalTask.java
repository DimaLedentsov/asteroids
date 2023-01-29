package com.dimka228.asteroids.ai;

public class AIIntervalTask extends AISimpleTask{
    private final static int DEFAULT_COUNT = 1000;
    private int counter;
    private int interval;
    public AIIntervalTask(Runnable a){
        super(a);
        counter = DEFAULT_COUNT;
    }
    public AIIntervalTask(Runnable a, int i){
        super(a);
        counter = 0;
        interval = i;
    }

    public void run(){
        if(counter<0) counter = interval;
        super.run();
        counter-=1;
    }

    public boolean isEnabled(){
        return super.isFinished() && counter==0;
    }
}
