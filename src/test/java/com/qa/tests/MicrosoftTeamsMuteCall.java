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

public class MicrosoftTeamsMuteCall extends BaseClass {
	@Test
	public void mutephoneTest() throws MalformedURLException, Throwable {
		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageMicrosoftTeams")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityMicrosoftTeams")));
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();
		System.out.println("MicrosoftTeams app is Launched Successfully");
		AppPage app = new AppPage();
		//Find the option to mute the microphone within the app.
		app.waitForElementVisibility(driver,By.id("com.microsoft.teams:id/meet_now_button"));
		MobileElement meetingItem = (MobileElement) driver.findElementById("com.microsoft.teams:id/meet_now_button");
		meetingItem.click();

		MobileElement joinButton = (MobileElement)  driver.findElementByXPath("//*[contains(@text,'Meet now')]");
		joinButton.click();
		
		MobileElement startmeeting = (MobileElement)  driver.findElementByXPath("//*[contains(@text,'Start meeting')]");
		startmeeting.click();
		System.out.println("Meeting started Successfully");
	
		MobileElement muteButton = (MobileElement) driver.findElementByXPath("//android.widget.FrameLayout[@content-desc=\"Mic muted\"]/android.widget.ImageView");
		muteButton.isDisplayed();
		muteButton.isEnabled();
		muteButton.click();
		System.out.println("Mute button got clicked");
		
		//Mute the phone using the app.
	
		muteButton.click();
		System.out.println("Muted successfully");
		
		//verify if the phone ismuted by checing the mic state using ADB
		
		String microphoneState = executeADBCommand(" shell \"dumpsys audio | grep -m 1 'Microphone: MicMute=true'\"");

        if (microphoneState.contains("MicMute=true")) {
            System.err.println("phone mic is muted.");
            Assert.fail();
        } else {
            System.out.println("phone mic is not muted.");
        }

        driver.quit();
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
