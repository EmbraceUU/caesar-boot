package com.demo.designpatterns.dynamicProxy.test_two;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类必须实现InvocationHandler接口
 * @author ND
 *
 */
public class DynamicProxy implements InvocationHandler {
	
	// 要代理的真实对象
	private Object subject;
	
	// 将真实对象交给动态代理类
	public DynamicProxy(Object subject){
		this.subject = subject;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// 执行方法之前的预处理
		System.out.println("before rent house"); 
        System.out.println("Method:" + method);
        // 当代理对象调用真实对象的方法时,会自动跳转到关联的handler对象的invoke方法进行调用
        method.invoke(subject, args);
        // 执行方法后的处理
        System.out.println("after rent house");
		return null;
	}

}
