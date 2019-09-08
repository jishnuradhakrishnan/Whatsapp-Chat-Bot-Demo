package com.wtsp.bot.util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.wtsp.bot.dto.WtspMessages;

public class WtspMessagesReader {
	


	public void readNewMessages(WebDriver driver) {
		try {
			if (driver != null) {
				List<WebElement> messageItems = driver.findElement(By.className(ApplicationConstants.messagesDiv))
						.findElements(By.className(ApplicationConstants.messageItemDiv));
				messageItems.forEach(obj -> {

					String contactInfo = obj.findElement(By.className(ApplicationConstants.contactInfoDiv)).getText();
					String lastSeen = obj.findElements(By.className(ApplicationConstants.lastSeenDiv)).get(0).getText();
					String msg = null;
					WtspMessages wtspMessages = null;

					if (ApplicationConstants.wtspMessages.containsKey(contactInfo)) {
						wtspMessages = ApplicationConstants.wtspMessages.get(contactInfo);
					} else {
						wtspMessages = new WtspMessages();
					}

					try {
						obj.findElement(By.className(ApplicationConstants.newMsgPopDiv));
						msg = obj.findElement(By.className(ApplicationConstants.messageDiv)).getText();
					} catch (NoSuchElementException exc) {
					}

					wtspMessages.setContactInfo(contactInfo);
					wtspMessages.setLastSeen(lastSeen);
					wtspMessages.insertMessage(msg);
					ApplicationConstants.wtspMessages.put(contactInfo, wtspMessages);
				});
			}
		} catch (NullPointerException nullPointerException) {
			nullPointerException.printStackTrace();
		}
	}

}
