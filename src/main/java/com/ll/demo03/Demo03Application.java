package com.ll.demo03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // @EntityListeners(AuditingEntityListener.class) 이 작동되게 하기 위해 작성(BaseTime.class)에 위치
public class Demo03Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo03Application.class, args);
	}

}
