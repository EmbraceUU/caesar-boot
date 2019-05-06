package com.demo.designpatterns.dynamicProxy.test_three;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import Util.MonitorUtil;

/**
 * 	实现了InvocationHandler接口
 * 	这个类不是生成的动态代理类本身&&没有和实体类有共同接口
 * @target 持有被代理对象
 * @invoke() 所有target中的方法会被替换成执行invoke方法
 * @author ND
 * @param <T>
 *
 */
public class StuInvocationHandler<T> implements InvocationHandler {

	// Invocationhandler 持有的被代理对象
	T target;

	public StuInvocationHandler(T target) {
		this.target = target;
	}

	/**
	 * @param proxy  代表动态代理对象
	 * @param method 代表正在执行的方法
	 * @param arg    代表执行目标方法时传入的实参
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] arg) throws Throwable {

		System.out.println("代理执行" + method.getName() + "方法");
		// 在invoke中可以加入预处理和后处理
		MonitorUtil.start(); 
		// invoke的参数是被代理对象
		Object result = method.invoke(target, arg); 
		MonitorUtil.finish(method.getName());
		return result;
	}

}
