package com.xuan.common.limiter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Leo xuan
 * @date 2018/10/24
 */
public class ManagerLimiter {

	private final Map<String, RateLimiter> limiters = new HashMap<>();

	private ParseLimiterConfig parseLimiterConfig;

	private LimiterFactory limiterFactory = new LimiterFactory();


	public ManagerLimiter() {
		init();
	}

	public ManagerLimiter(ParseLimiterConfig parseLimiterConfig) {
		this.parseLimiterConfig = parseLimiterConfig;
		init();
	}

	public boolean getPermit(String url) {
		boolean result = true;
		RateLimiter rateLimiter = limiters.get(url);
		if (rateLimiter == null)
			return result;
		result = rateLimiter.tryAcquire();
		return result;
	}

	public void putLimiter(String url, RateLimiter limiter) {
		if (url == null || url.equals("")) {
			throw new NullPointerException("ManagerLimiter#putLimiter(String url,RateLimiter limiter), url 为空");
		}
		limiters.put(url, limiter);
	}

	protected void init() {
		if (parseLimiterConfig == null) {
			parseLimiterConfig = new ParseLimiterConfig();
		}

		List<LimiterInfo> limiterInfos = parseLimiterConfig.getLimiterInfos();

		limiterInfos.forEach(e ->
				putLimiter(e.getUrl(), limiterFactory.build(e.getRate(), e.getTimeUnit())));
	}

	public ParseLimiterConfig getParseLimiterConfig() {
		return parseLimiterConfig;
	}

	public void setParseLimiterConfig(ParseLimiterConfig parseLimiterConfig) {
		this.parseLimiterConfig = parseLimiterConfig;
	}
}
