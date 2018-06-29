package com.ushow.tv;

import com.ushow.tv.socket.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

    SocketServer socketServer = new SocketServer();
    try {
      log.info("Start SocketServer at {} ..." , DateTime.now().toString("yyyy-MM-dd HH:ss:mm"));
      socketServer.startSocketServer(50005);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("SocketServer start error : {}", e.getMessage());
    }

  }

}
