package com.dimka228.asteroids.ai;

public class AIConditionalTask  extends AISimpleTask{
    private Condition condition;
    public AIConditionalTask(Runnable a, Condition c){
        super(a);
        condition = c;
    }
    @Override
    public boolean isEnabled(){
        return super.isEnabled() && condition.check();
    }


}
