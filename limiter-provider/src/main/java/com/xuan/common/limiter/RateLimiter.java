package com.xuan.common.limiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

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

	ReentrantLock lock = new ReentrantLock();

	public RateLimiter(double rate) {
		this(rate, TimeUnit.SECONDS);
	}

	public RateLimiter(double rate, String timeUnit) {
		this.timeUnit = timeUnit;
		this.refreshTime = System.currentTimeMillis();
		this.capacity = rate;

		if (TimeUnit.HOURS.equals(timeUnit)) {
			this.rate = rate / TimeUnit.CONVERSIONUNIT / TimeUnit.CONVERSIONUNIT;
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

	public synchronized boolean tryAcquire(long permits) {
		addToken();//距离上一次时间增加令牌;
		if (nowCapacity < permits) {
			return false;
		} else {
			removeToken(permits);//消耗令牌;
			return true;
		}
	}

  public void addToken(){
		long nowTime = System.currentTimeMillis();
		this.nowCapacity = Math.min(this.capacity, this.nowCapacity + (nowTime - this.refreshTime) * this.rate / 1000);
		this.refreshTime = nowTime;
	}

	public void removeToken(long permits) {
		this.nowCapacity -= permits;
	}

	public static void main(String[] args) throws InterruptedException {

		AtomicLong total = new AtomicLong(0);
		AtomicLong pass = new AtomicLong(0);
		AtomicLong bolck = new AtomicLong(0);
		long start = System.currentTimeMillis();
		CountDownLatch countDownLatch = new CountDownLatch(50000);
		RateLimiter l = new RateLimiter(10000,TimeUnit.SECONDS);
		for (int i = 0; i < 50000; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1; j++) {
					boolean flag;
					if (flag = l.tryAcquire()){
						count++;
						pass.getAndAdd(1);
					}else
						bolck.getAndAdd(1);
					System.out.println(flag);
				}
				countDownLatch.countDown();
			}).start();
		}

		countDownLatch.await();
		System.out.println("pass:" + pass +"  block:" + bolck);
		System.out.println("耗时"+(System.currentTimeMillis() - start) + "ms");
	}
}
