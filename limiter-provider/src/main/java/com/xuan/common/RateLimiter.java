package com.xuan.common;

/**
 * @author Leo xuan
 * @date 2018/9/5
 */
public class RateLimiter {

	volatile long refreshTime;

	double rate;

	double capacity;

	volatile double nowCapacity;

	String timeUnit;

	static int count = 0;

	public RateLimiter(double rate) {
		this(rate, TimeUnit.SECONDS);
	}

	public RateLimiter(double rate, String timeUnit) {
		this.timeUnit = timeUnit;
		this.refreshTime = System.currentTimeMillis();
		this.capacity = rate;

		if (TimeUnit.HOURS.equals(timeUnit)) {
			this.rate = rate / TimeUnit.CONVERSIONUNIT/TimeUnit.CONVERSIONUNIT;
		} else if (TimeUnit.MINUTES.equals(timeUnit)) {
			this.rate = rate / TimeUnit.CONVERSIONUNIT;
		} else {
			this.rate = rate;
		}

		this.nowCapacity = capacity;
	}

	public boolean tryAcquire() {
		return tryAcquire(1);
	}

	public boolean tryAcquire(long permits) {
		addToken();//距离上一次时间增加令牌;
		if (nowCapacity < permits) {
			return false;
		} else {
			removeToken(permits);//消耗令牌;
			return true;
		}
	}

	public void addToken() {
		long nowTime = System.currentTimeMillis();
		this.nowCapacity = Math.min(this.capacity, this.nowCapacity + (nowTime - this.refreshTime) * this.rate / 1000);
		this.refreshTime = nowTime;
	}

	public void removeToken(long permits) {
		this.nowCapacity -= permits;
	}

	public static void main(String[] args) throws InterruptedException {

		RateLimiter l = new RateLimiter(100000);
		for (int i = 0; i < 1000; i++) {
			new Thread(() -> {
				synchronized (l) {
					for (int j = 0; j < 200; j++) {
						boolean flag;
						if (flag = l.tryAcquire())
							count++;
						System.out.println(flag);
					}
				}
			}).start();
		}
		Thread.sleep(3000);
		System.out.println(count);
	}
}
