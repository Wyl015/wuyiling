package com.wuyiling.worktest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static void main(String[] args) {
        TimeUtils timeUtils = new TimeUtils();
        timeUtils.testString2Date();
    }

    public void testString2Date() {
        String startDate = "2017-08-15 18:00:51";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long startDay = 0L;
        try {
            Date dateStart = format.parse(startDate);
            startDay = dateStart.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(startDay);

    }
}
