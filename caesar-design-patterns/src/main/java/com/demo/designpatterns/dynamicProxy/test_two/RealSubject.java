package com.demo.designpatterns.dynamicProxy.test_two;

/**
 * 实现类,真是对象
 * @author ND
 *
 */
public class RealSubject implements Subject {
	
	// 重写两个方法,实现具体动作
	@Override
	public void rent() {
		System.out.println("I want to rent my house");
	}

	@Override
	public void hello(String str) {
		System.out.println("hello: " + str);
	}

}
