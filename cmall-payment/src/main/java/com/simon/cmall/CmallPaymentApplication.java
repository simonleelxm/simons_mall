package com.simon.cmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.simon.cmall.payment.mapper")
public class CmallPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmallPaymentApplication.class, args);
    }

}
