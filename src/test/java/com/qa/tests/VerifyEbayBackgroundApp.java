package com.qa.tests;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

public class VerifyEbayBackgroundApp extends BaseClass {

	@Test
	public void backgroundTest() throws MalformedURLException, Throwable {

		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageEbay")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityEbay")));
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();
		System.out.println("App Launched Successfully");
		String packageName = driver.getCapabilities().getCapability("appPackage").toString();

		// Get the package name of the  app using Appium method
		Thread.sleep(5000);
		System.out.println(driver.getCurrentPackage());
		System.out.println("Package name of the running app: " + packageName);
		ApplicationState Appstate = driver.queryAppState(configProperties.getProperty("appPackageEbay"));
		System.out.println("Current app state afer launch " + Appstate);
		Thread.sleep(100);

		// Stop the  app's process using the package name by ADB command.
		
		String adbCommand = adb+" shell am force-stop " + packageName;
		try {
			Process process = Runtime.getRuntime().exec(adbCommand);
			process.waitFor();
			System.out.println("App's process stopped successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApplicationState Appstate1 = driver.queryAppState(configProperties.getProperty("appPackageEbay"));
		System.out.println("Current app state afer the app got stopped " + Appstate1);
		Thread.sleep(5000);

		// Verify if the  app is running in the background by checking the app's state using Appium method.

		boolean isAppRunningInBackground = driver.getPageSource().contains(configProperties.getProperty("appPackageEbay"));
		if (isAppRunningInBackground) {
			System.err.println("APP is running in background");
		} else {
			System.out.println("APP is not running in background");
		}

		if (Appstate1.equals(configProperties.getProperty("appPackageEbay"))) {

			System.err.println("APP is running in background");
		} else {
			System.out.println("APP is not running in background");
		}

		Assert.assertFalse(isAppRunningInBackground, "APP is running in background");
		Assert.assertEquals(Appstate1, ApplicationState.NOT_RUNNING , "APP is running in background");
	}
}