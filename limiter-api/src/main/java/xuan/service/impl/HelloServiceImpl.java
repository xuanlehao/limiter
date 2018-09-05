package xuan.service.impl;

import org.springframework.stereotype.Service;
import xuan.service.HelloService;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */

@Service("helloService")
public class HelloServiceImpl implements HelloService {
	@Override
	public String hello() {
		return "hello world!";
	}
}
