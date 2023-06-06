package com.honda.olympus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.honda.olympus.vo.TransferFileVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TransferFileService {

	@Value("${transfer.file.service.url}")
	private String transferFileURI;

	public void sendTransferFileEvent(TransferFileVO message) {
		try {
			log.debug("Calling transferFile service");
			log.debug(message.toString());
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<TransferFileVO> requestEntity = new HttpEntity<>(message, headers);

			ResponseEntity<String> responseEntity = restTemplate.postForEntity(transferFileURI, requestEntity,
					String.class);

			log.debug("Transfer file called with Status Code: {}",responseEntity.getStatusCode());
			log.debug("Message: " + responseEntity.getBody());
		} catch (Exception e) {
			log.info("Monitor:: Error calling transferFile service {}",e.getLocalizedMessage());
		}

	}

}
