package com.wuyiling.agent;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    // spring 在实现AOP： 在bean放入容器前生成代理类
    // 当方法为 private 时，AOP 无法增强

    // java agent 在 JVM 层面 实现了AOP增强
    // JVM中类的加载： 加载 -> (修改二进制信息，.class | 网络) -> 验证 -> 准备 -> 解析 -> 初始化

    // Java agent和instrumentation
    // eg: java -javaagent:the-agent-demo.jar HelloWorld

//    包含Pre-Main参数的MENIFEST.MF
//    Manifest-Version: 1.0
//    Premain-Class: cn.kobelee.test.TestJavaAgent

    // java -javaagent:xxx.jar HelloWorld指定代理的jar，里面有premain. 我们需要在premain里面对instrumentation添加ClassTransformer. 而这个classTransformer的实现就是你要怎么修改这个类。

    public static void premain(String agentArgument,                // 传递参数 java -javaagent:the-agent-demo.jar=theAgentArumentDemo HelloWorld
                               Instrumentation instrumentation){    // Instrumentation是一个接口，定义了字节码修改的规范
        System.out.println("Java Agent Demo");
        //implements ClassFileTransformer
        SimpleClassTransformer simpleClassTransformer = new SimpleClassTransformer();
        instrumentation.addTransformer(simpleClassTransformer);
    }

//    javassist jboss提供的一个方便我们修改这个byte[]的工具包 改二进制文件

}
