package com.honda.olympus.scheduller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honda.olympus.exception.MonitorException;
import com.honda.olympus.service.MonitorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MonitorScheduller {

	@Autowired
	MonitorService monitorService;
	
	@Value("${monitor.timelapse}")
	Long timeLapse;

	@Scheduled(fixedDelayString = "${monitor.timelapse}")
	public void monitorScheduledTask() throws IOException {
		log.info("Monitor:: Scheduller running - Start");

		try {
			Long startTime = System.nanoTime();
			monitorService.checkFiles();
			Long endTime = System.nanoTime();
			Long timeElapsed = endTime - startTime;
			
			log.info("Monitor:: Execution duration time in milliseconds: {}",timeElapsed / 1000000);
			
			Date expireDate = new Date(System.currentTimeMillis() + timeLapse);
			
			log.info("Monitor:: Scheduller End, Next service execution at: {}",expireDate.toString());
		} catch (MonitorException e) {

			e.printStackTrace();
		}
	}

}
