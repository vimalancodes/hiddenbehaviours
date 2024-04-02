package com.qa.tests;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

public class VerifyHideApp extends BaseClass {

	@Test
	public void hideTest() throws MalformedURLException, Throwable {
		capabilities.setCapability("appPackage", (configProperties.getProperty("appPackageAirBnb")));
		capabilities.setCapability("appActivity", (configProperties.getProperty("appActivityAirBnb")));
		URL appiumServerURL = new URL(configProperties.getProperty("appiumURL"));
		driver = new AndroidDriver(appiumServerURL, capabilities);
		driver.launchApp();
		System.out.println("App is Launched Successfully");

	// get package name
		String packageName = driver.getCapabilities().getCapability("appPackage").toString();
		System.out.println("Package Name: " + packageName);
		ApplicationState Appstate = driver.queryAppState(packageName);
		System.out.println("Current app state afer launch " + Appstate);
		
	// Kill the  by adb command
		  String[] command = {adb, "shell", "pm", "clear", packageName};
          ProcessBuilder processBuilder = new ProcessBuilder(command);          
          Process process = processBuilder.start();
          int exitCode = process.waitFor();
          if (exitCode == 0) {
              System.out.println("App killed with package name: " + packageName);
          } else {
              System.err.println("Error occurred while killing app");
          }
          
    // Retrieve the list of activity names associated with the app using ADB.	
		 String[] runningActivities = getRunningActivities();
		 String isActivityHidden = "Not hidden";	
	        System.out.println("List of running activities:");
	        
	// Verify if the activity is hidden by checking the list of activity names using Appium method.        
	        for (String activity : runningActivities) {
	            System.out.println(activity);
	            if (activity.contains(packageName)) {
					isActivityHidden = "hidden"; //  activity found, hidden
					break;
				}
				else{
					isActivityHidden = "Not hidden";
				}
				
			}
			if (isActivityHidden.equals("hidden")) {
				System.err.println("App activity is hidden.");
			} else {
				System.out.println("App activity is not hidden.");		
	        }
			Assert.assertEquals(isActivityHidden, "Not hidden");
	    }

	    public static String[] getRunningActivities() {
	        try {
	            Process process = Runtime.getRuntime().exec(adb+" shell dumpsys activity");
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line.contains("realActivity=")) {
	                    String activityName = line.split("=")[1].split(" ")[0];
	                    return new String[]{activityName};
	                }
	            }

	            process.waitFor();
	            process.destroy();
	        } catch (Exception  e) {
	            e.printStackTrace();
	        }

	        return new String[0];
	    }

	}
