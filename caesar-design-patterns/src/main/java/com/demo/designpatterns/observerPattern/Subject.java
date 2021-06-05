package com.demo.designpatterns.observerPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式-目标类
 */
class Subject {
    private List<Observer> observers = new ArrayList<>();
    private int state;


    int getState() {
        return state;
    }

    // 更新状态后, 遍历的方式通知观察者
    void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    void attach(Observer observer){
        observers.add(observer);
    }

    // 通知所有观察者
    private void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
