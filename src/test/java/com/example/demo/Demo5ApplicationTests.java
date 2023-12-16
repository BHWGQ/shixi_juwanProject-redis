package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class Demo5ApplicationTests {

    @Test
    void contextLoads() {
        String input = "2003";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(input.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : md5Bytes) {
                sb.append(String.format("%02x", b));
            }

            String md5Hash = sb.toString();
            System.out.println("MD5 Hash for '2003': " + md5Hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
