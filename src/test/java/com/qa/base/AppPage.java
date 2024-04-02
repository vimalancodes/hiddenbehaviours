package com.qa.base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class AppPage extends BaseClass{

	By searchField = By.xpath("//android.widget.EditText");
	By spotifyLoginBtn = By.xpath("//*[contains(@text,'Log in')]");
	By spotifyUsername = By.xpath("//*[contains(@text,'username')]/..//android.widget.EditText");
	By spotifySearchIcon = By.xpath("//*[contains(@text,'Search')]");
	By spotifySearch = By.xpath("//android.widget.TextView[@content-desc=\"Search for something to listen to\"]");
	By spotifySongs = By.xpath("//*[@text='Songs']");
	By spotifyCard = By.xpath("//*[@resource-id='com.spotify.music:id/row_root']");
	By spotifyPlayButton = By.xpath("//*[@resource-id='com.spotify.music:id/button_play_and_pause']");
	By spotifyBottomBar = By.xpath("//*[@resource-id='com.spotify.music:id/anchor_bar_bottom']");
	By wynkTry = By.xpath("//*[@text='Try it first']");
	By wynkIcon = By.xpath("//android.widget.ImageView[@content-desc=\"SEARCH\"]");
	By wynkSearch = By.xpath("//*[@resource-id='com.bsbportal.music:id/searchBar']");
	By wynkPlayMusic = By.xpath("//*[@resource-id='com.bsbportal.music:id/play_icon']");

	public void loginToSpotify(AppiumDriver<MobileElement> driver,String username, String pass) {
		click(driver, spotifyLoginBtn);
		List<MobileElement> edit = driver.findElements(spotifyUsername);
		ArrayList<MobileElement> fields = new ArrayList<MobileElement>();
		for (int i = 0; i < edit.size(); i++) {
			fields.add(edit.get(i));
		}
		fields.get(0).sendKeys(username);
		fields.get(1).sendKeys(pass);
		click(driver, spotifyLoginBtn);
	}

	public void waitForElementClickability(AppiumDriver<MobileElement> driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForElementVisibility(AppiumDriver<MobileElement> driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public boolean isElementPresent(AppiumDriver<MobileElement> driver, By locators) {
		boolean status = false;
		try {
			driver.findElement(locators).isDisplayed();
			status = true;
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	public void hardWait(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void enterData(AppiumDriver<MobileElement> driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		hardWait(1000);
		driver.findElement(locator).click();
		hardWait(1000);
		driver.findElement(locator).clear();
		hardWait(2000);
		driver.findElement(locator).sendKeys(value);
	}

	public void click(AppiumDriver<MobileElement> driver, By locator) {
		try {
			waitForElementVisibility(driver, locator);
			waitForElementClickability(driver, locator);
			driver.findElement(locator).click();
		} catch (TimeoutException e) {
			hardWait(2000);
			driver.findElement(locator).click();
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			hardWait(2000);
			driver.findElement(locator).click();
			e.printStackTrace();
		} catch (StaleElementReferenceException e) {
			hardWait(2000);
			driver.findElement(locator).click();
			e.printStackTrace();
		}
	}

	public void verticalScrollByLocationUntilElementVisibility(AppiumDriver<MobileElement> driver, double x,
			double startOfY, double endOfY, By locator) {
		@SuppressWarnings("rawtypes")
		TouchAction action = new TouchAction(driver);
		Dimension size = driver.manage().window().getSize();
		int width = size.width;
		int height = size.height;
		int X = (int) (width / x);
		int startYCoordinate = (int) (height * startOfY);
		int endYCoordinate = (int) (height * endOfY);
		while (isElementPresent(driver, locator) == false) {
			action.press(PointOption.point(X, startYCoordinate))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(PointOption.point(X, endYCoordinate)).release().perform();
		}
	}

	public void playMusic(AppiumDriver<MobileElement> driver, String name, String user, String pass) throws Exception {
//		loginToSpotify(driver, user, pass);
		click(driver, spotifySearchIcon);
		click(driver, spotifySearch);
		enterData(driver, searchField, name);
		click(driver, spotifySongs);
		click(driver, spotifyCard);
//		verticalScrollByLocationUntilElementVisibility(driver, 2, 0.9, 0.5, spotifyPlayButton);
//		click(driver, spotifyPlayButton);
//		click(driver, spotifyBottomBar);
//		hardWait(Integer.parseInt(Config.RECORD_TIME) * 1000);
	}
	
	public void playWynkMusic(AppiumDriver<MobileElement> driver, String name) throws Exception {
		click(driver, wynkTry);
		click(driver, wynkIcon);
		click(driver, wynkSearch);
		enterData(driver, searchField, name);
		click(driver, wynkPlayMusic);
	}

}
