package com.xuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
@Controller
public class TestController {


	@RequestMapping("/test")
	@ResponseBody
	public String testController(){
		return "hello";
	}

}
