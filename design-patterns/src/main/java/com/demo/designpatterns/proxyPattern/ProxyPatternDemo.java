package com.demo.designpatterns.proxyPattern;

public class ProxyPatternDemo {
	public static void main(String[] args) {
		// 生成被代理对象
		Image image = new RealImage("myImage.jpg");
		// 将此对象传给代理对象
		Image proxy = new ProxyImage(image);
		// 代理对象代理被代理对象
		proxy.display(); 
	}
}
