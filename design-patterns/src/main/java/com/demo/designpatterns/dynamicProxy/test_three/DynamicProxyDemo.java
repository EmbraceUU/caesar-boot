package com.demo.designpatterns.dynamicProxy.test_three;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyDemo {
	public static void main(String[] args) {
		
		// 创建一个实体类
		// 可以创建不同的实体类*
		Person stu = new Student("Amy");
		
		// 创建一个与代理对象相关联的InvocationHandler 这个类是有特性的
		// 可以有不同的关联,做不同的处理*
		InvocationHandler handler = new StuInvocationHandler<Person>(stu);
		
		// 创建一个代理对象proxy来代理实体对象stu,代理对象的每个执行方法都会被替换成执行handler中的invoke方法
		// handler和Proxy动态创建了代理对象
		// 而且这个代理对象是针对其持有的实体类*
		// 可以动态生成不同的代理对象*
		Person stuProxy = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[] {Person.class}, handler);
		
		// invoke处理了所有的方法*
		stuProxy.giveMoney();
		stuProxy.show();
	}
}
