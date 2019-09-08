package com.wtsp.bot.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.wtsp.bot.dto.WtspMessages;

public class WtspAutoReply {

	public void replyToMsg(WebDriver driver) {
		ApplicationConstants.wtspMessages.forEach((key, value) -> {
			wtspClickToChat(value, driver);
		});
	}

	private void wtspClickToChat(WtspMessages messages, WebDriver driver) {

		String msg = messages.popMsg();
		String reply = "oops something went wrong";

		if (msg != null) {
			OpenNLPChatBot nlpChatBot = new OpenNLPChatBot();
			try {
				reply = nlpChatBot.generateReply(msg);
			} catch (Exception exc) {
				exc.printStackTrace();
			}

			try {
				driver.findElement(By.xpath("//*[contains(text(), '" + messages.getContactInfo() + "')]")).click();
				WebElement el1 = driver.findElement(By.id("main"));
				el1.findElement(By.className(ApplicationConstants.wtspChatBoxDiv)).sendKeys(reply);
				el1.findElement(By.className(ApplicationConstants.wtspSendMsgBtnDiv)).click();

				WebElement el2 = driver.findElement(By.xpath("//*[contains(text(), '"+ApplicationConstants.switchChat+"')]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(el2).click().perform();
			} catch (Exception noSuchElementException) {
				noSuchElementException.printStackTrace();
			}

		}
	}
}
