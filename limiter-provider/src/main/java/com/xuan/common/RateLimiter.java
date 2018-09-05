package com.xuan.common;

/**
 * @author Leo xuan
 * @date 2018/9/5
 */
public class RateLimiter {

	volatile long refreshTime;

	final double rate;

	final double capacity;

	double nowCapacity;

	public RateLimiter(double rate) {
		this.rate = rate;
		this.capacity = rate;
		this.nowCapacity = capacity;
		this.refreshTime = System.currentTimeMillis();
	}

	public synchronized boolean tryAcquire() {
		addToken();//距离上一次时间增加令牌;
		if (nowCapacity < 1) {
			return false;
		} else {
			removeToken();//消耗令牌;
			return true;
		}
	}

	public void addToken() {
		long nowTime = System.currentTimeMillis();
		this.nowCapacity = Math.min(this.capacity, this.nowCapacity + (nowTime - this.refreshTime) * this.rate / 1000);
		this.refreshTime = nowTime;
	}

	public void removeToken() {
		this.nowCapacity -= 1;
	}

	public static void main(String[] args) throws InterruptedException {
		RateLimiter l = new RateLimiter(10);
		new Thread(() -> {
			for (int i = 0; i < 50; i++) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread one " + i + " : " + l.tryAcquire());
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 50; i++) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread two " + i + " : " + l.tryAcquire());
			}
		}).start();
	}
}
