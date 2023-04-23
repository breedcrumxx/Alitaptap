package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// Thread t = new Thread(new Runnable(){
		// 	@Override
		// 	public void run() {
		// 		while(true){
		// 			Scanner input = new Scanner(System.in);
		// 			String id = input.nextLine();
		
		// 			System.out.println("output" + id);
		// 		}
		// 	}
		// });

		SpringApplication.run(DemoApplication.class, args);
		// t.run();


	}

}
