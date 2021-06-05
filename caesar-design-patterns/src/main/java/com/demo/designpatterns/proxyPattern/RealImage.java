package com.demo.designpatterns.proxyPattern;

/**
 * 实现接口的实体类
 * @author ND
 *
 */
public class RealImage implements Image {
	
	private String fileName;
	
	public RealImage(String fileName) {
		this.fileName = fileName;
		loadFromDisk(fileName);
	}
	
	@Override
	public void display() {
		// 实现具体的行为动作
		System.out.println("Displaying" + fileName);
	}
	
	private void loadFromDisk(String fileName) {
		System.out.println("Loading" + fileName);
	}
	
}
