package com.qa.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;

import com.qa.base.AppPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

public class Wynk extends BaseClass {
	static String song;
	@Test
	public void WynkTest() throws MalformedURLException, Throwable {
		song = configProperties.getProperty("song");
		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageWynk")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityWynk")));
//		capabilities.setCapability("appium:resetApp", true);
		capabilities.setCapability("noReset", false);
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();		
		System.out.println("Wynk app is Launched Successfully");
		playMusicAndCheckNotification(driver);
	}

	public static void playMusicAndCheckNotification(AppiumDriver<MobileElement> driver) throws Exception {
		// Play music in Wynk app
		AppPage app = new AppPage();
		app.playWynkMusic(driver, song);

		Thread.sleep(5000);

		// Verify if there is a music notification from Wynk
		boolean isNotificationVisible = isNotificationVisible(song);
		System.out.println("Wynk music notification visible for song "+song+": " + isNotificationVisible);

		// Hide the notification using ADB command
		if (isNotificationVisible) {
			hideNotification(song);
		}

		// Verify the music notification is not visible
		boolean isNotificationHidden = !isNotificationVisible(song);
		if(isNotificationHidden)
		System.out.println("Wynk music notification hidden: " + isNotificationHidden);
		else {
			System.err.println("Wynk music notification hidden: " + isNotificationHidden);
		}
	}

	// Method to check if a notification with a given text is visible
	private static boolean isNotificationVisible(String notificationText) {
		String command = adb +" shell dumpsys notification | grep ticker | cut -d= -f2";
		try {
			Process process = Runtime.getRuntime().exec(command);
			Scanner scanner = new Scanner(process.getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains(notificationText)) {
					return true;
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Method to hide a notification with a given text using ADB
	private static void hideNotification(String notificationText) {
		String command = adb+" shell pm revoke com.bsbportal.music android.permission.POST_NOTIFICATIONS";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}