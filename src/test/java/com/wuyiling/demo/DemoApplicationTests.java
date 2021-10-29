package com.wuyiling.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {

        //判断是否整点
        //使用Date

        Date d = new Date();
        //格式化时间串,HH:MM:SS分别是时分秒，这里只判断分钟，有mm就够了
        //需要秒的话，就mm：ss，下面就判断是否 00:00
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String timestr=sdf.format(d);
        System.out.println(timestr);


        LocalDateTime now = LocalDateTime.now();

        System.out.println(now.getMinute());
    }

}
