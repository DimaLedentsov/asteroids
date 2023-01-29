package com.dimka228.asteroids.ai;

public class AISimpleTask implements AITask{
    private Runnable action;
    protected boolean isEnabled;
    protected boolean isFinished;
    public AISimpleTask(Runnable a){
        action = a;
        isEnabled = true;
        isFinished = false;
    }
    public void run(){
        if(!isEnabled()) return;
        action.run();
    }

    public void setEnabled(boolean f){
        isEnabled = f;
    }

    public boolean isEnabled(){
        return isEnabled;
    }
    public boolean isFinished(){
        return isFinished;
    }
    public void finish(){
        isFinished = true;
    }
}
