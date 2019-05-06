package com.demo.designpatterns.dynamicProxy.test_one;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类, 实现了InvocationHandler接口
 * @author ND
 *
 */
public class Invoker implements InvocationHandler {
	
	private AbstractClass as;
	
	Invoker(AbstractClass as) {
		this.as = as;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] arg) throws Throwable {
		//在调用之前做处理
		method.invoke(as, arg);
		//在调用之后做处理
		return null;
	}

}
