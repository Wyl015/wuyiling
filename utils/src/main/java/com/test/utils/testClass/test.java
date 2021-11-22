package com.test.utils.testClass;

public class test {
    public static void main(String[] args) {
        System.out.println(Parent.a + "***");
        System.out.println(Child.a + "***");

        System.out.println(Parent.a + "***");

        Parent parent = new Parent();
        Child child = new Child();

        System.out.println(Parent.a + "***");
    }
}
