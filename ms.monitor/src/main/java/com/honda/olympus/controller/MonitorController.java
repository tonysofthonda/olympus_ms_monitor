package com.honda.olympus.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honda.olympus.exception.MonitorException;
import com.honda.olympus.service.MonitorService;
import com.honda.olympus.vo.ResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MonitorController {
	@Value("${service.success.message}")
	private String responseMessage;

	@Value("${service.name}")
	private String serviceName;

	@Value("${service.version}")
	private String version;

	@Value("${service.profile}")
	private String profile;

	@Autowired
	private MonitorService monitorService;

	@Operation(summary = "Force Monitor execute once")
	@PostMapping(path = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseVO> monitorFiles() throws MonitorException, IOException {
		
		try {
		log.info("Monitor:: Calling FORCE monitor event:: start");

		monitorService.checkFiles();

		}catch(Exception e) {
		 e.printStackTrace();  
		}
		return new ResponseEntity<>(new ResponseVO(serviceName, 1L, responseMessage, ""), HttpStatus.OK);
	}

}
