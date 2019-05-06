package com.demo.designpatterns.factoryPattern;

/**
 * 根据给定信息，生成对应的类
 * @author Administrator
 *
 */
public class ShapeFactory {
	public Shape getShapeType(String type){
		if(null == type){
			return null;
		}
		if (type.equalsIgnoreCase("RECTANGLE")) {
			return new Rectangle();
		}else if(type.equalsIgnoreCase("SQUARE")){
			return new Square();
		}
		return null;
	}
}
