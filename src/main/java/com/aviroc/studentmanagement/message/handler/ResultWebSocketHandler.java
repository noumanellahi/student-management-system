package com.aviroc.studentmanagement.message.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.aviroc.studentmanagement.model.Result;
import com.aviroc.studentmanagement.service.ResultService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class ResultWebSocketHandler implements WebSocketHandler {

	@Autowired
	private ResultService resultService;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public Mono<Void> handle(WebSocketSession session) {

		Flux<WebSocketMessage> output = session.receive().map(message -> {
			Result result = parseResultJson(message.getPayloadAsText());
			if (result != null) {
				return resultService.updateResult(result).get(0);
			} else {
				return "NOT A VALID JSON";
			}
		}).map(e -> session.textMessage(e.toString()));

		return session.send(output);
	}

	/**
	 * Parse the string Result json to Result object.
	 * 
	 * @param message
	 * @return
	 */
	private Result parseResultJson(String message) {
		try {
			return objectMapper.readValue(message, Result.class);
		} catch (Exception ex) {
			log.error(ex);
		}
		return null;
	}
}
