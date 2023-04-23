package com.example.demo;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.Models.RFID;
import com.example.demo.Repositories.RFIDRepository;
import com.example.demo.Services.RFIDService;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

	@Autowired
	public RFIDService rfidService;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ConfigurableApplicationContext appContext = SpringApplication.run(DemoApplication.class, args);

		RFIDService service = appContext.getBean(RFIDService.class);

		// Thread t = new Thread(new Runnable(){
		// 	@Override
		// 	public void run() {
		// 		while(true){

		// 			String id = input.nextLine();
		
		// 			rfidService.saveRFID(id);
		// 		}
		// 	}
		// });
		// t.run();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		rfidService.trySaveRFID();
		throw new UnsupportedOperationException("Unimplemented method 'run'");
	}

}
