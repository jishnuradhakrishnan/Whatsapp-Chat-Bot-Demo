package com.wtsp.bot.util;

import java.util.HashMap;
import java.util.Map;

import com.wtsp.bot.dto.WtspMessages;

public class ApplicationConstants {
	public static Map<String, WtspMessages> wtspMessages = new HashMap<String, WtspMessages>();

	public static Map<String, String> questionAnswer = new HashMap<String, String>() {
		public static final long serialVersionUID = 1L;

		{
			put("greetings", "hello sir \n welcome to syndicate bank's whatsapp services \n how can i help you today?");
			put("goodMorning", "good morning");
			put("conversation-complete", "Nice chatting with you. Bbye.");
			put("who-am-i", "I am your Virtual Assistant");
			put("weather-info", "Sorry i am not Alexa or Siri");
			put("account-balance", "Sir balance in your a/c XXXXXXXXXX5432 is ₹100");
			put("angry-emoji", "Why are you angry");

		}
	};

	public static String switchChat = "Sample group";
	public static String wtspChatBoxDiv = "_13mgZ";
	public static String wtspSendMsgBtnDiv = "_3M-N-";
	public static String chromeDriverPath = "/home/alrais/Desktop/chromedriver";
	public static String wtspWebUrl = "https://web.whatsapp.com";
	public static String qrCodeDiv = "_1Fl07";
	public static final String messagesDiv = "_3La1s";
	// there will be multiple div's
	public static final String messageItemDiv = "X7YrQ";
	public static final String contactInfoDiv = "_3H4MS";
	// if an unread message is there there will one more div with the same name
	public static final String lastSeenDiv = "_0LqQ";
	public static final String newMsgPopDiv = "P6z4j";
	// get title from this element
	public static final String messageDiv = "_1Wn_k";
}
