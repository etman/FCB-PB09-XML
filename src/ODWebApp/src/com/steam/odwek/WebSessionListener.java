package com.steam.odwek;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class WebSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent e) {

	}

	public void sessionDestroyed(HttpSessionEvent e) {
		try {
			System.out.println("[SessionEvent]release ODServer connection.");
			ODHelper.release(e.getSession());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
