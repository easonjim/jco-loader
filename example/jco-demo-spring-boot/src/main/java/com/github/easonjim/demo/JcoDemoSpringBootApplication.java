package com.github.easonjim.demo;

import com.github.easonjim.JcoProvisioner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JcoDemoSpringBootApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JcoDemoSpringBootApplication.class, args);
        JcoProvisioner.provision();
        SapConn con = new SapConn(
                "127.0.0.1",
                "123",
                "456",
                "abc",
                "abc",
                "abc",
                "123",
                "321",
                "123456"
        );

        // 测试连接
        SapConnUtils.connect(con);
    }

}
