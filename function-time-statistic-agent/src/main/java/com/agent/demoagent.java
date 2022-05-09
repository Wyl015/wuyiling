package com.agent;

import java.lang.instrument.Instrumentation;

public class demoagent {
    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     */
    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("agent的premain(String arg, Instrumentation instrumentation)方法");

        instrumentation.addTransformer(new MyTransformer());
    }

    /**
     * 若不存在 premain(String agentArgs, Instrumentation inst)，
     * 则会执行 premain(String agentArgs)
     */
    public static void premain(String arg) {
        System.out.println("agent的premain(String arg)方法");
    }

}
