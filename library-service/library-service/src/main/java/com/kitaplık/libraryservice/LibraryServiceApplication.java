package com.kitaplık.libraryservice;

import com.kitaplık.libraryservice.client.RetrieveErrorMessageDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class LibraryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryServiceApplication.class, args);
	}


	//Feign Client Error Handling
//	@Bean
//	public ErrorDecoder errorDecoder(){
//		return new RetrieveErrorMessageDecoder();
//	}
//
//	@Bean
//	Logger.Level feignLoggerLevel() {
//		return Logger.Level.FULL;
//	}

}
