package com.honda.olympus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honda.olympus.vo.ResponseVO;

@RestController
public class MonitorController {
	@Value("${service.success.message}")
	private String responseMessage;
	
	@GetMapping(path = "/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<ResponseVO> monitorFiles() {
		System.out.println(responseMessage);
		
		return new ResponseEntity<>(new ResponseVO(responseMessage, null), HttpStatus.OK);
	}

}
