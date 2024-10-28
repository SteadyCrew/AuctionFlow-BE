package org.example.auctionflowbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AuctionFlowBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionFlowBeApplication.class, args);
    }

}
