package com.test.utils.controller;

import com.test.utils.cryptology.SM2Util;
import org.springframework.web.bind.annotation.*;

@RestController
public class SM2TestController {

    private SM2Util sm2 = new SM2Util();

    @PostMapping(value = "/sm2decode")
    public String SM2Test(@RequestBody String SM2psd, @RequestBody String pryKey) {

        String decoder = new String(sm2.decode2(SM2psd, pryKey));
        return decoder;
    }
}

