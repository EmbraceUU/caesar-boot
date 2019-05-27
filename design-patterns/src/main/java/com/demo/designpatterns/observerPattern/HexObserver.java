package com.demo.designpatterns.observerPattern;

public class HexObserver extends Observer {

    HexObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("Hex Observer: " + Integer.toHexString(subject.getState()));
    }
}
