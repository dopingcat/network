package com.hanains.network.test;

public class TypeTest {
	public static void main(String[] args) {
		Person p1 = new Student();
		Person p2 = new Professor();
		
		System.out.print("p1 instanceof Person\t: ");
		System.out.println(p1 instanceof Person);
		
		System.out.print("p1 instanceof Student\t: ");
		System.out.println(p1 instanceof Student);
		
		System.out.print("p1 instanceof Professor\t: ");
		System.out.println(p1 instanceof Professor);
		
		System.out.print("p2 instanceof Person\t: ");
		System.out.println(p2 instanceof Person);
		
		System.out.print("p2 instanceof Student\t: ");
		System.out.println(p2 instanceof Student);
		
		System.out.print("p2 instanceof Professor\t: ");
		System.out.println(p2 instanceof Professor);
	}
	
	static class Person {
	}
	
	static class Student extends Person {
	}
	
	static class Professor extends Person {
	}
}
