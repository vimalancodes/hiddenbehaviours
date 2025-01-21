package com.qa.tests;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.AppPage;
import com.qa.base.BaseClass;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class GoogleMeetMuteCall extends BaseClass {
	@Test
	public void mutephoneTest() throws MalformedURLException, Throwable {
		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageGoogleMeet")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityGoogleMeet")));
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		executeADBCommand(" shell pm revoke "+configProperties.getProperty("appPackageGoogleMeet")+" android.permission.RECORD_AUDIO");
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();
		System.out.println("Google meet app is Launched Successfully");
		AppPage app = new AppPage();
		app.waitForElementVisibility(driver,By.xpath("//*[contains(@text, 'New meeting')]"));
		driver.findElementByXPath("//*[contains(@text, 'New meeting')]").click();
		Thread.sleep(2000);
		MobileElement startmeeting = (MobileElement) driver.findElementByXPath("//*[contains(@text,'Start an instant meeting')]");
		startmeeting.click();
		boolean flag= false;
		MobileElement muteButton;
		try {
		muteButton = (MobileElement) driver.findElementByXPath("//android.view.ViewGroup[@content-desc=\"Turn microphone off\"]");
		flag = muteButton.isDisplayed();
		}
		catch(Exception e) {
			flag=false;
		}

		Assert.assertFalse(flag, "mic muted");
		System.out.println("Meeting started Successfully");
		executeADBCommand(" shell pm grant "+configProperties.getProperty("appPackageGoogleMeet")+" android.permission.RECORD_AUDIO");
		driver.launchApp();
		System.out.println("Google meet app is Launched Successfully");
		app.waitForElementVisibility(driver,By.xpath("//*[contains(@text, 'New meeting')]"));
		driver.findElementByXPath("//*[contains(@text, 'New meeting')]").click();
		Thread.sleep(2000);
		MobileElement startmeeting1 = (MobileElement) driver.findElementByXPath("//*[contains(@text,'Start an instant meeting')]");
		startmeeting1.click();
		Thread.sleep(7000);
		muteButton = (MobileElement) driver.findElementByXPath("//android.view.ViewGroup[@content-desc=\"Turn microphone off\"]");
		muteButton.isDisplayed();
		muteButton.isEnabled();
		muteButton.click();
		System.out.println("Mute button got clicked");
		
		//verify if the phone is muted by checking the mic state using ADB
		
		String microphoneState = executeADBCommand(" shell dumpsys package "+configProperties.getProperty("appPackageGoogleMeet")+" | grep -i \"android.permission.RECORD_AUDIO\"");

        if (microphoneState.contains("granted=false")) {
            System.err.println("phone mic is muted.");
            Assert.fail();
        } else {
            System.out.println("phone mic is not muted.");
        }

    }

    private static String executeADBCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(adb+ command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }	

	}
