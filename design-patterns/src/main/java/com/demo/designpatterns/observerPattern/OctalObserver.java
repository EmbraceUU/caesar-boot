package com.demo.designpatterns.observerPattern;

public class OctalObserver extends Observer {

    OctalObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("Octal Observer: " + Integer.toOctalString(subject.getState()));
    }
}
