package com.wuyiling.worktest;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.wuyiling.worktest.mapper.User;
import com.wuyiling.worktest.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Wrapper;
import java.util.ArrayList;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseSpringTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1() {
        User user = User.builder().name("zhangsan").email("123@123.com").build();
        userMapper.insert(user);
    }

    @Test
    public void test2() {
        User user = userMapper.selectById(1);
        user.setName("11");

//        User user2 = userMapper.selectById(1);
//        user.setName("22");
//        userMapper.updateById(user2);
//        System.out.println(userMapper.selectById(1).getName());

        userMapper.updateById(user);
        System.out.println(userMapper.selectById(1).getName());
    }

    @Test
    public void test3() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(User.class,info ->
                !info.getColumn().equals("name")&&
                !info.getColumn().equals("id")
        ).orderByAsc("id");

    }


//    代码自动生成器 mybatis
    @Test
    public void test4() {
        // 需要构建一个 代码自动生成器 对象
        AutoGenerator mpg = new AutoGenerator();
        // 配置策略
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("狂神说");
        gc.setOpen(false);
        gc.setFileOverride(false);// 是否覆盖
        gc.setServiceName("%sService");// 去Service的I前缀
        gc.setIdType(IdType.ID_WORKER);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/kuang_community? useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        //3、包的配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("blog");
        pc.setParent("com.kuang");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("blog_tags","course","links","sys_settings","user_record"," user_say"); // 设置要映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);// 自动lombok；
        strategy.setLogicDeleteFieldName("deleted");   // 自动填充配置
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);
        // 乐观锁
        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        // localhost:8080/hello_id_2
        mpg.setStrategy(strategy);
        mpg.execute();
        //执行
//————————————————
//        版权声明：本文为CSDN博主「?Handsome?」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/zdsg45/article/details/105138493

    }

    @Test
    public void test5() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();

    }

}
