package com.qa.tests;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

public class Truecaller extends BaseClass {

	@Test
	public void truecallerTest() throws MalformedURLException, Throwable {

		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageTruecaller")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityTruecller")));
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();
		System.out.println("Truecaller app is Launched Successfully");
		Thread.sleep(5000);
		
     // Find and click on the option to delete call logs
		
        MobileElement callLog = (MobileElement) driver.findElement(By.xpath("//*[contains(@text,'Calls')]"));
        callLog.click();
        Thread.sleep(2000);
        MobileElement deleteCallLogsOption = (MobileElement) driver.findElementById("com.truecaller:id/moreButton");
        deleteCallLogsOption.click();
        Thread.sleep(2000);
        MobileElement deleteAllCalls = (MobileElement) driver.findElement(By.xpath("//*[contains(@text,'Delete all calls')]"));
        deleteAllCalls.click();
        Thread.sleep(2000);
    // Delete the call logs using the Truecaller  app
        MobileElement deleteAllCallsConfirmation = (MobileElement) driver.findElementById("android:id/button1"); 
        deleteAllCallsConfirmation.click();      
        driver.closeApp();
        
    //launching the phone app
        
        capabilities.setCapability("appPackage", (configProperties.getProperty("appPackagephone")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityphone")));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		System.out.println("phone app launched Successfully");
		
   // Verify if the call logs are deleted by checking the call log history in the device
        Process process = Runtime.getRuntime().exec(adb +" shell content query --uri content://call_log/calls | grep -c \"content://call_log/calls\"");
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         String output = reader.readLine();
         int numberOfCalls=0;
         try{
        	 numberOfCalls = Integer.parseInt(output.trim());
         }catch(Exception e) {
        	 
         }
         System.out.println("Number of calls in call log: " + numberOfCalls);
         reader.close();
		if(!(numberOfCalls==0))
        {
        	System.out.println("Call logs were not deleted.");
        }
            else {
            System.err.println("Call logs were deleted");
        }
        Assert.assertTrue(numberOfCalls!=0, "Call logs were deleted.");


    }
}