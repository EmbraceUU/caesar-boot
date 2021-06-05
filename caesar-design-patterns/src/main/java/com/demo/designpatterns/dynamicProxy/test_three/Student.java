package com.demo.designpatterns.dynamicProxy.test_three;

/**
 *	 实现类
 * @author ND
 *
 */
public class Student implements Person {
	
	private String name;
	
	public Student(String name) {
		this.name = name;
	}
	
	@Override
	public void giveMoney() {
		// 完成实际动作
		try {
			// 花费了一秒的时间
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(name + "上交班费50元");
	}
	
	@Override
	public void show() {
		System.out.println("Hello,I am " + name);
	}

}
