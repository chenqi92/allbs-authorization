package cn.allbs.allbsjwt;

import cn.allbs.annotation.EnableAllbsSwagger3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ChenQi
 */
@EnableAllbsSwagger3
@SpringBootApplication
public class AllbsJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllbsJwtApplication.class, args);
    }

}
