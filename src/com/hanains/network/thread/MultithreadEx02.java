package com.hanains.network.thread;

public class MultithreadEx02 {
	public static void main(String[] args) {
		Thread thread1 = new DigitThread();
		Thread thread2 = new LowerCaseAlphabetThread();
		
		try {
			thread1.start();
			thread2.start();
			new DigitThread().start();
		} catch (Exception e) {
		}
	}
}
