package com.xuan.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.xuan.common.DisconfTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
@Controller
public class TestController {

	@Autowired
	DisconfTest disconfTest;

	@RequestMapping("/test")
	@ResponseBody
	public String testController(){
		return "hello";
	}

}
