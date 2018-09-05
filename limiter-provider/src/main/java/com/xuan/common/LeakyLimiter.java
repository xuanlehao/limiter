package com.xuan.common;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
public class LeakyLimiter {

	volatile long refreshTime;

	final double rate;

	final double capacity;

	double nowCapacity;

	private LeakyLimiter(int rate) {
		this.rate = rate;
		this.capacity = rate ;
		this.nowCapacity = 0;
		this.refreshTime = System.currentTimeMillis();
	}


	public boolean tryAcquire() {
		removeToken();//减去这段时间桶中处理掉的请求
		if (nowCapacity < capacity) {
			addToken();//把自己的请求加进桶里
			return true;
		}
		return false;
	}


	void addToken() {
		this.nowCapacity++;
	}


	void removeToken() {
		long nowTime = System.currentTimeMillis();
		this.nowCapacity = Math.max(0, this.nowCapacity - (nowTime - this.refreshTime) * (this.rate/1000));
		this.refreshTime = nowTime;
	}


	public static void main(String[] args) throws InterruptedException {
		LeakyLimiter l =new LeakyLimiter(10);
		for(int i=0;i<20;i++){
			System.out.println(l.tryAcquire());
		}
		Thread.sleep(1000);
		for(int i=0;i<20;i++){
			System.out.println(l.tryAcquire());
		}
	}
}
