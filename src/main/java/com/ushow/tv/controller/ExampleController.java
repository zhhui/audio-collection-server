package com.ushow.tv.controller;


import com.google.common.collect.Maps;
import com.ushow.tv.controller.response.ApiResponse;
import com.ushow.tv.socket.SocketServer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ushow/api/v1/audio-collection-server")
@Slf4j
public class ExampleController extends AbstractController{
	private static final Logger LOG = Logger.getLogger(ExampleController.class);

  @GetMapping("/start")
	public ApiResponse start() throws Exception {

	  Map<String, Object> map = Maps.newHashMap();

    return ApiResponse.createSuccess(map);
	}

  @GetMapping("/stop")
  public ApiResponse stop() throws Exception {

    Map<String, Object> map = Maps.newHashMap();

    return ApiResponse.createSuccess(map);
  }


}
