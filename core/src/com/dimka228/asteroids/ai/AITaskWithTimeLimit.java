package com.dimka228.asteroids.ai;

public class AITaskWithTimeLimit extends AISimpleTask{
    private final static int DEFAULT_COUNT = 10000;
    private int counter;
    public AITaskWithTimeLimit(Runnable a){
        super(a);
        counter = DEFAULT_COUNT;
    }
    public AITaskWithTimeLimit(Runnable a, int i){
        super(a);
        counter = i;
    }

    public void run(){
        super.run();
        counter-=1;
    }

    public boolean isFinished(){
        return super.isFinished() && counter>0;
    }
}
