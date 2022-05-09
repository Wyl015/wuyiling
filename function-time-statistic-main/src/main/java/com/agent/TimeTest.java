package com.agent;

public class TimeTest {
    public void test(){
        System.out.println("test start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("test stop");
    }
}
