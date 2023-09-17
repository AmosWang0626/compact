package cn.eyeo.compact;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot Starter
 *
 * @author Frank Zhang
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"cn.eyeo.compact", "com.alibaba.cola"})
@MapperScan("cn.eyeo.compact.gateway.impl.database.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
