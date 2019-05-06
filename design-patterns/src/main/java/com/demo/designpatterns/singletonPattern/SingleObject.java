package com.demo.designpatterns.singletonPattern;

/**
 * 单例模式示例
 * 1.保证一个类只有一个实例，提供一个访问他的全局访问点
 * 2.构造函数私有化
 * @author ND
 *
 */
public class SingleObject {
	
	// 创建一个SingleObect的对象
	private static SingleObject instance = new SingleObject();
	
	// 将构造函数为private,这样该类就不能被实例化
	private SingleObject(){};
	
	// 获得唯一可用的对象
	public static SingleObject getInstance(){
		return instance;
	}
	
	// 方法
	public void showMessage(){
		System.out.println("Singleton Pattern !");
	}
}
