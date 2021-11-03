package com.test.utils;

import com.test.utils.cryptology.SM2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class UtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UtilsApplication.class, args);
    }

}
