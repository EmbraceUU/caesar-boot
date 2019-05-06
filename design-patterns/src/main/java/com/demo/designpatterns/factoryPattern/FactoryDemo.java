package com.demo.designpatterns.factoryPattern;

public class FactoryDemo {
	public static void main(String[] args) {
		ShapeFactory sf = new ShapeFactory();
		
		Shape shape = sf.getShapeType("Square");
		shape.draw();
		
		shape = sf.getShapeType("RECTANGLE");
		shape.draw();
	}
}
