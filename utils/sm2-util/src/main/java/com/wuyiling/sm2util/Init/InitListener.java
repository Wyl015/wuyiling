package com.wuyiling.sm2util.Init;

import com.wuyiling.sm2util.Domain.SM2KsyPair;
import com.wuyiling.sm2util.SM2Utils.SM2Util;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

@Log4j2
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(InitListener.class);

    @Autowired
    private com.wuyiling.sm2util.SM2Utils.SM2Util SM2Util;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("---------------------系统初始化开始------------------------");
        logger.info("=========初始化SM2加密算法公钥与私钥====开始=========");
        try {
            SM2Util sm2 = new SM2Util();
            KeyPair keyPair = sm2.generateSm2KeyPair();
            BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
            BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
            // 拿到密钥
            SM2KsyPair.pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(false)));
            SM2KsyPair.prvKey = privateKey.getD().toString(16);
        } catch (Exception e) {
            log.error("Exception:"+e);
        }
        logger.info("=========初始化SM2加密算法公钥与私钥====结束=========");
        logger.info("pubKey:  " + SM2KsyPair.pubKey);
        logger.info("prvKey:  " + SM2KsyPair.prvKey);

    }
}
