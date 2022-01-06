package com.wuyiling.worktest.Utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kanx
 * @date 2021/10/14 17:52
 * @descrip 1.0
 */
public class GeneratorSqlmap {
    public void generator() throws Exception{

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
       String path=Thread.currentThread().getContextClassLoader().getResource("generatorConfig.xml").getPath();
        File configFile = new File(path);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }
    public static void main(String[] args) throws Exception {
        try {
            com.yuuwei.faceview.util.GeneratorSqlmap generatorSqlmap = new com.yuuwei.faceview.util.GeneratorSqlmap();
            generatorSqlmap.generator();
            System.out.println("生成文件成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
