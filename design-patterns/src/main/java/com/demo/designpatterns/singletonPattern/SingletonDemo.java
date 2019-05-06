package com.demo.designpatterns.singletonPattern;

public class SingletonDemo {
	public static void main(String[] args) {
		
		SingleObject so = SingleObject.getInstance();
		
		so.showMessage();
	}
}
