package com.qa.Incubyte.Base;

import java.util.Iterator;
import java.util.Set;
import java.time.Duration;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.By;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.Incubyte.Utility.Gmail_IAutoConst;

import java.util.Properties;
import org.openqa.selenium.WebDriver;

public class GmailInitiation implements Gmail_IAutoConst {
	public static WebDriver driver;
	public static Properties prop;
	public static WebDriverWait wait;

	protected GmailInitiation() {
		final File file = new File(CONFIG_PROPERTIES_PATH1);
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		prop = new Properties();
		try {
			prop.load(fileInput);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public static boolean retryingFindClick(final By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 3) {
			try {
				driver.findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException ex) {
				++attempts;
			}
		}
		return result;
	}

	public static boolean retryingFindSend(final String Xpath, final String data) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 3) {
			try {
				driver.findElement(By.xpath(prop.getProperty(Xpath))).sendKeys(new CharSequence[] { data });
				result = true;
				break;
			} catch (StaleElementReferenceException ex) {
				++attempts;
			}
		}
		return result;
	}

	public static void SignInGmail() throws InterruptedException {
		driver.findElement(By.xpath(prop.getProperty("Enter_Email")))
				.sendKeys(new CharSequence[] { prop.getProperty("email") });
		driver.findElement(By.xpath(prop.getProperty("Next_Btn"))).click();
		driver.findElement(By.xpath(prop.getProperty("Next_Btn"))).isDisplayed();
		driver.findElement(By.xpath(prop.getProperty("Next_Btn"))).isEnabled();
		driver.findElement(By.xpath(prop.getProperty("Enter_password")))
				.sendKeys(new CharSequence[] { prop.getProperty("password") });
		retryingFindClick(By.xpath(prop.getProperty("Next_Btn")));
	}

	public static void ScrollToElemet(final WebElement Element) {
		final JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", new Object[] { Element });
	}

	public static void setUp() {
		final String browserName = prop.getProperty("Browser_Name");
		if (browserName.equals("Chrome")) {
			System.setProperty("webdriver.chrome.driver", ChromeDriver_Path);
			driver = (WebDriver) new ChromeDriver();
		}
		if (browserName.equals("FireFox")) {
			System.setProperty("webdriver.chrome.driver", GeckoDriver_Path);
			driver = (WebDriver) new FirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50L));
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2L));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50L));
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
	}

	public static void HandleWinowPopUP() {
		final Set<String> handler = (Set<String>) driver.getWindowHandles();
		final Iterator<String> Itr = handler.iterator();
		final String ParentWindowID = Itr.next();
		System.out.println("Parent Window ID is:" + ParentWindowID);
		final String ChildWindowID = Itr.next();
		System.out.println("Parent Window ID is:" + ChildWindowID);
		driver.switchTo().window(ChildWindowID);
		driver.close();
		driver.switchTo().window(ParentWindowID);
	}

	public static void CheckPaymentDone() {
		final WebElement ele = driver.findElement(By.xpath("//div[contains(text(),'Permite las notificaciones')]"));
		if (ele.isDisplayed()) {
			System.out.println("Payment Successfuly done");
		} else {
			System.out.println(
					"Due to some interrupstion payment got failed, Check the screenshot for the excat root cause");
		}
	}
}
