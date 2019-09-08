package com.wtsp.bot.util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WtspWebHandler {
	private final WebDriver driver;
	private static WtspWebHandler instance = null;
	// private String wtspLandingFinder = "Open WhatsApp on your phone";
	public static boolean isLoggedIn = false;

	private WtspWebHandler() {
		System.setProperty("webdriver.chrome.driver", ApplicationConstants.chromeDriverPath);
		driver = new ChromeDriver();
		driver.get(ApplicationConstants.wtspWebUrl);
        WtspMessagesReader messagesReader = new WtspMessagesReader();
        WtspAutoReply autoReply = new WtspAutoReply();
        
		  new Thread() {
			public void run() {
				while (true) {
					isLoggedIn = isLoggedIn();
                    if(isLoggedIn) {
                    	messagesReader.readNewMessages(driver);
                    	autoReply.replyToMsg(driver);
                    }
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.run();
	}

	public static WtspWebHandler getInstance() {
		if (instance == null) {
			instance = new WtspWebHandler();
		}

		return instance;
	}

	private boolean isLoggedIn() {
		try {
			driver.findElement(By.className(ApplicationConstants.qrCodeDiv));
			return false;
		} catch (NoSuchElementException exc) {
			return true;
		}

	}

}
