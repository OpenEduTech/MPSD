package indi.zyang.neev;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"indi.zyang.neev"})
@EntityScan(basePackages = "indi.zyang.neev.entity")
@MapperScan(
        basePackages = "indi.zyang.neev.dao",
        annotationClass = Repository.class
)
@EnableTransactionManagement
public class NeevApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeevApplication.class, args);
    }

}
