package com.kvote.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
//		exclude = { SecurityAutoConfiguration.class } // springSecurity 로그인 창 자동생성 방지 -> 나중에 인증기능이 추가되면 해당 커맨드 없애야함.
)
@EnableScheduling
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
