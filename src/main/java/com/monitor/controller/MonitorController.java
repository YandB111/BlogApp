package com.monitor.controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.bean.MonitorBean;
import com.monitor.service.MonitorService;



@RestController
public class MonitorController {

	
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
	
	
	
	@Autowired
	MonitorService monitorService;
	
	@Autowired
	MonitorBean monitorBean;
	
	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public String monitor() {
		//logger.info("Welcome home! The client locale is {}.", locale);

		monitorService.MonitorCheck();
		return "Hello";
		
	}

	
	@CrossOrigin
	@RequestMapping(value = "/monitoring", method = RequestMethod.GET)
	public ResponseEntity<MonitorBean> monitoring() throws IOException {
		//logger.info("Welcome home! The client locale is {}.", locale);

		//monitorService.MonitorCheck();
		
		
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            monitorService.MonitorCheck();
        }, 0, 5, TimeUnit.SECONDS);
    
		
		return new ResponseEntity<MonitorBean>(monitorBean, HttpStatus.OK);
		
	}
}
