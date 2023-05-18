package com.honda.olympus.scheduller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorScheduller {
	
	@Scheduled(fixedDelay = 5000)
	public void scheduleFixedDelayTask() {
	    System.out.println(
	      "Fixed delay task - " + System.currentTimeMillis() / 1000);
	}

}
