package com.aviroc.studentmanagement.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

@Configuration
@EnableWebFlux
public class WebsocketConfig {

	@Bean
	public SimpleUrlHandlerMapping handlerMapping(WebSocketHandler wsh) {
		
		SimpleUrlHandlerMapping urlMapping =  new SimpleUrlHandlerMapping(); 
		urlMapping.setUrlMap(Collections.singletonMap("/ws/result", wsh));
		urlMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return urlMapping;
	}
}
