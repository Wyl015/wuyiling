package com.test.utils.testClass;

public class Child extends Parent {
    static String a;
    static {
        a = "static-child-1";
        System.out.println(a);
    }

    static {
        a = "static-child-2";
        System.out.println(a);
    }


}
