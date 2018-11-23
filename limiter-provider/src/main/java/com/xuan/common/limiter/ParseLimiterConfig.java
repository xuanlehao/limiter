package com.xuan.common.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Leo xuan
 * @date 2018/10/24
 */
public class ParseLimiterConfig {

	private static Logger log = LoggerFactory.getLogger(ParseLimiterConfig.class);

	private List<LimiterInfo> limiterInfos = new ArrayList<>();

	public String configPath;

	protected ParseLimiterConfig() {
		this.configPath = "G:/limiter.properties";
		parse();
	}

	protected ParseLimiterConfig(String configPath) {
		this.configPath = configPath;
		parse();
	}

	private void parse() {
		Properties properties = new Properties();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(configPath));
			properties.load(bufferedReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//解析配置url的参数
		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			LimiterInfo li = new LimiterInfo();
			String key = (String) en.nextElement();
			li.setUrl(key);
			String value = properties.getProperty(key);
			if (value == null) {
				continue;
			}
			/**待改进（容错）**/
			String[] strs = value.split(",");
			try {
				if (strs.length < 3) {
					throw new LimiterConfigException("配置的url解析异常！url : " + key);
				}
				li.setRate(Double.valueOf(strs[0]));
				li.setTimeUnit(strs[1]);
				li.setType(strs[2]);
				limiterInfos.add(li);
			} catch (Exception e) {
				log.error("配置的url解析异常！url : " + key);
				log.error(e.getMessage());
			}
			System.out.println(key + "=" + value);
		}
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public List<LimiterInfo> getLimiterInfos() {
		return limiterInfos;
	}
}
