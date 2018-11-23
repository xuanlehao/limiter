package com.xuan.common.limiter;

/**
 * @author Leo xuan
 * @date 2018/10/25
 */
public class LimiterFactory {

	public RateLimiter build(double rate) {
		return build(rate, TimeUnit.SECONDS);
	}

	public RateLimiter build(double rate, String timeUnit) {
		return new RateLimiter(rate, timeUnit);
	}
}
