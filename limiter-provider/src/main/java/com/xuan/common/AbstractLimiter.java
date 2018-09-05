package com.xuan.common;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
public abstract class AbstractLimiter implements Limiter{

	final int rate;

	final int capacity;

	volatile int nowCapacity;

	protected AbstractLimiter(int rate,int capacity){
		this.rate = rate;
		this.capacity = capacity;
		nowCapacity = capacity;
	}

	private AbstractLimiter(int rate){
		this.rate = rate;
		this.capacity = rate;
		nowCapacity = capacity;
	}
	abstract void addToken();

	abstract void removeToken();
}
