package com.test.utils.testClass;

public class Parent {
    static String a;
    static {
        a = "static-parent-1";
        System.out.println(a);
    }

    {
        a = "static-parent-2";
        System.out.println(a);
    }



}
