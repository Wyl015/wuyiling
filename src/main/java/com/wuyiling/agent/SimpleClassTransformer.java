package com.wuyiling.agent;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SimpleClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if(!className.contains("kobelee")){  //我只需要我定义的包路径下统计，当然这个也判断也可以删了
                return null;
            }
            CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classfileBuffer));      //创建了一个ctClass对象
            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            for (CtBehavior method : declaredMethods) {
                CtClass[] parameterTypes = method.getParameterTypes();
                StringBuilder sb = new StringBuilder("{");
                for (int i = 0; i< parameterTypes.length; i++) {
                    sb.append("StringBuilder code = new StringBuilder();");
                    sb.append("code.append(\""+method.getLongName()+" before.\");");
                    sb.append("code.append(\""+parameterTypes[i].getName()+"\");");
                    sb.append("code.append(\":\");");
                    sb.append("code.append($args["+i+"]);");
                    sb.append("System.out.println(code.toString());");
                }

                sb.append("}");
                method.insertBefore(sb.toString());
                method.insertAfter("System.out.println(\""+method.getLongName()+" end\");");
            }
            byte[] returnByte = ctClass.toBytecode();
            return returnByte;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }
}