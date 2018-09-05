package com.xuan.common;

/**
 * @author Leo xuan
 * @date 2018/9/4
 */
public interface Limiter {

	boolean tryAcquire();
}
