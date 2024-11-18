package com.kupstudio.bbarge.config;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JasyptConfig.class)
class jasyptConfigTest {

    @Test
    public void jasyptTest() throws Exception {
        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
        enc.setAlgorithm("PBEWithMD5AndDES");
        enc.setPassword("encryptCode");  //key값
        String chat = "jdbc:mysql://";

        System.out.println("@@@ chat db 암호화  :  " + enc.encrypt(chat));

    }
}