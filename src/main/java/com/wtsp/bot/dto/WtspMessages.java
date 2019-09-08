package com.wtsp.bot.dto;

import java.util.ArrayList;
import java.util.List;

public class WtspMessages {
	// this should be a phone number
	private String contactInfo;
	private String lastSeen;
	private List<String> messagesQue = new ArrayList<String>();
	private int elementIndex = 0;

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(String lastSeen) {
		this.lastSeen = lastSeen;
	}

	public void insertMessage(String msg) {
		if (msg != null) {
			messagesQue.add(msg);
		}
	}

	public String popMsg() {
		String msg = null;
		if (messagesQue.size() > 0) {
			msg = messagesQue.get(0);
			messagesQue.remove(0);
		}

		return msg;
	}

	public int getElementIndex() {
		return elementIndex;
	}

	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	@Override
	public String toString() {
		int listSize = messagesQue.size();
		String lastMsg = null;
		if (listSize > 0) {
			lastMsg = messagesQue.get(listSize - 1);
		}
		return "contactInfo:" + contactInfo + "  lastSeen:" + lastSeen + "  messages" + messagesQue.size()
				+ " last message:" + lastMsg;
	}
}
