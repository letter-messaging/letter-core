package com.gmail.ivanjermakov1.messenger.messaging.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("sse")
public class SseController {
	
	private SseEmitter emitter = new SseEmitter();
	
	@GetMapping("get")
	public ResponseBodyEmitter get() {
		System.out.println("get");
		emitter = new SseEmitter();
		
		emitter.onTimeout(() -> {
			System.out.println("timeout");
			emitter.complete();
		});
		
		return emitter;
	}
	
	@Scheduled(fixedRate = 2000)
	public void send() throws IOException {
		System.out.println("new notification");
		emitter.send("New message! " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh mm ss")));
	}
	
	
}
