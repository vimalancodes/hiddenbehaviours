package com.qa.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.util.Properties;
G
public class BaseClass {

	public static AndroidDriver driver;
	public Properties configProperties;
	public static DesiredCapabilities capabilities = new DesiredCapabilities();
	protected static String adb;
	@BeforeClass
	public void setup() throws Exception {

		configProperties = new Properties();
		try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
			configProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();

		}

		capabilities.setCapability("deviceName", (configProperties.getProperty("deviceName")));
		capabilities.setCapability("platformVersion", (configProperties.getProperty("platformVersion")));
		capabilities.setCapability("platformName", (configProperties.getProperty("platformName")));
		capabilities.setCapability("udid", (configProperties.getProperty("udid")));
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("automationName", "UiAutomator2");
		capabilities.setCapability("autoAcceptAlerts", true);
		capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS,true);
		adb= configProperties.getProperty("adbPath");

	}

	@AfterClass
	public void tearDown() {
//		if (driver != null) {
			driver.quit();
//		}
	}
}
