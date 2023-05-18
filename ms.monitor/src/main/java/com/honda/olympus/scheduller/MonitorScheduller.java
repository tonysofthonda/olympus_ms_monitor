package com.honda.olympus.scheduller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honda.olympus.service.MonitorService;

@Component
public class MonitorScheduller {

	@Autowired
	MonitorService monitorService;

	@Scheduled(fixedDelay = 10000)
	public void scheduleFixedDelayTask() {
		System.out.println("Monitor Scheduller running - " + System.currentTimeMillis() / 1000);

		try {
			monitorService.listFiles();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
