package com.demo.designpatterns.observerPattern;

public class ObserverPatternDemo {
    public static void main(String[] args) {

        // 初始化目标
        Subject subject = new Subject();

        // 初始化观察者, 并且将观察者与观察目标绑定
        new BinaryObserver(subject);
        new OctalObserver(subject);
        new HexObserver(subject);

        // 目标更新后, 通知所有的观察者
        System.out.println("First state change: 15");
        subject.setState(15);
        System.out.println("Second state change: 10");
        subject.setState(10);
    }
}
