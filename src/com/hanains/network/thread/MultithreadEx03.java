package com.hanains.network.thread;

public class MultithreadEx03 {
	public static void main(String[] args) {
		Thread thread = new Thread(new DigitRunnunableImpl());
		thread.start();
	}
}
