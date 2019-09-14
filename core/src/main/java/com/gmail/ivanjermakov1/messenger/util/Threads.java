package com.gmail.ivanjermakov1.messenger.util;

public class Threads {

	public static void suspend(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void timeout(Runnable runnable, long millis) {
		new Thread(() -> {
			suspend(millis);
			runnable.run();
		}).start();
	}

}
