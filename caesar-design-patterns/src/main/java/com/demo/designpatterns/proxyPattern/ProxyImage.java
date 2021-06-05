package com.demo.designpatterns.proxyPattern;

/**
 * 代理类, 同样实现了Image接口, 并且持有一个Image的实现类
 * 可以代理这个对象的行为
 * @author ND
 *
 */
public class ProxyImage implements Image {
	// 被代理的委托对象
	private RealImage realImage; 
	
	public ProxyImage(Image realImage) { 
		// 确定代理的委托类
		if(realImage.getClass() == RealImage.class){
			this.realImage = (RealImage) realImage;
		}	
	}
	
	@Override
	public void display() { 
		realImage.display();
	}

}
