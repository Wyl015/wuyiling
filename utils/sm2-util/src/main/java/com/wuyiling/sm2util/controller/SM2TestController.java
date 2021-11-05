package com.wuyiling.sm2util.controller;

import com.wuyiling.sm2util.Domain.Query;
import com.wuyiling.sm2util.Domain.SM2KsyPair;
import com.wuyiling.sm2util.SM2Utils.SM2Util;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
@Log4j2
@RestController
public class SM2TestController {

    @Autowired
    private SM2Util SM2Util;


    @ApiOperation(value = "加密")
    @PostMapping("/SM2/encode")
    public String SM2Encode(@RequestBody Query query) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String encode = SM2Util.encode(query.getInput(), SM2KsyPair.pubKey);
        return encode;
    }

    @ApiOperation(value = "解密")
    @PostMapping("/SM2/decode")
    public String SM2Decode(@RequestBody Query query) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] decodeByte = SM2Util.decode(query.getInput(), SM2KsyPair.prvKey);
        return new String(decodeByte);
    }

    @ApiOperation(value = "公钥")
    @GetMapping("/SM2/pubkey")
    public String SM2PubKey() {
        return SM2KsyPair.pubKey;
    }

    @ApiOperation(value = "私钥")
    @GetMapping("/SM2/prvkey")
    public String SM2PrvKey() {
        return SM2KsyPair.prvKey;
    }

}
