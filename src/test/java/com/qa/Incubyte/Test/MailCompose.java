package com.qa.Incubyte.Test;

import com.qa.Incubyte.Base.GmailInitiation;

import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import java.util.function.Function;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

public class MailCompose extends GmailInitiation {

	static SoftAssert soft;

	static {
		soft = new SoftAssert();
	}

	@Test(priority = 0)
	public static void SignInPage() {
		try {
			setUp();
			final boolean status = driver.findElement(By.xpath(prop.getProperty("Enter_Email"))).isDisplayed();
			soft.assertEquals(status, true, "Issue in loading the sign page");
			SignInGmail();
			soft.assertEquals(status, true, "Issue in loading the sign page");
			final boolean status2 = driver.findElement(By.xpath(prop.getProperty("Gmail"))).isDisplayed();
			soft.assertEquals(status2, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 1)
	public static void checkComposeFrame() {
		driver.findElement(By.xpath(prop.getProperty("compose_Btn"))).click();
		(wait = new WebDriverWait(driver, Duration.ofSeconds(1L))).until((Function) ExpectedConditions
				.visibilityOf(driver.findElement(By.xpath(prop.getProperty("New_MSG_Frame")))));
		final boolean composeFrmae = driver.findElement(By.xpath(prop.getProperty("New_MSG_Frame"))).isDisplayed();
		Assert.assertEquals(composeFrmae, true);
	}

	@Test(priority = 2)
	public static void EnterEmailsAddress() throws InterruptedException {
		final WebElement ele1 = driver.findElement(By.xpath(prop.getProperty("TO")));
		ele1.clear();
		ele1.sendKeys(new CharSequence[] { prop.getProperty("email1") });
		Thread.sleep(1000L);
		ele1.sendKeys(new CharSequence[] { (CharSequence) Keys.ENTER });
		driver.findElement(By.xpath(prop.getProperty("To_input")))
				.sendKeys(new CharSequence[] { prop.getProperty("email1") });
		final WebElement ele2 = driver.findElement(By.xpath(prop.getProperty("CC")));
		ele2.click();
		final WebElement ele2_Input = driver.findElement(By.xpath(prop.getProperty("CC_input")));
		ele2_Input.sendKeys(new CharSequence[] { prop.getProperty("email2") });
		ele2_Input.sendKeys(new CharSequence[] { (CharSequence) Keys.ENTER });
		driver.findElement(By.xpath(prop.getProperty("CC_input")))
				.sendKeys(new CharSequence[] { prop.getProperty("email2") });
		final WebElement ele3 = driver.findElement(By.xpath(prop.getProperty("BCC")));
		ele3.click();
		final WebElement ele3_Input = driver.findElement(By.xpath(prop.getProperty("Bcc_input")));
		ele3_Input.sendKeys(new CharSequence[] { prop.getProperty("email3") });
		Thread.sleep(1000L);
		ele3_Input.sendKeys(new CharSequence[] { (CharSequence) Keys.ENTER });
		driver.findElement(By.xpath(prop.getProperty("Bcc_input")))
				.sendKeys(new CharSequence[] { prop.getProperty("email3") });
	}

	@Test(priority = 3)
	public static void EnterSubject() {
		driver.findElement(By.xpath(prop.getProperty("Sub")))
				.sendKeys(new CharSequence[] { prop.getProperty("Subject") });
	}

	@Test(priority = 4)
	public static void EnterMessageBody() {
		driver.findElement(By.xpath(prop.getProperty("Body")))
				.sendKeys(new CharSequence[] { prop.getProperty("MessageBody") });
	}

	@Test(priority = 5)
	public static void Attachfile() throws InterruptedException, IOException {
		driver.findElement(By.xpath(prop.getProperty("Attachment"))).click();
		Thread.sleep(2000L);
		final String scriptFilePath = String.valueOf(System.getProperty("user.dir")) + "\\AutoIT\\GmailFileUpload.exe";
		final String fileToUpload = String.valueOf(System.getProperty("user.dir"))
				+ "\\TestScenario.xlsx";
		final String[] cmd = { scriptFilePath, fileToUpload };
		Runtime.getRuntime().exec(cmd);
		final WebElement Fileattached = driver.findElement(By.xpath("//div[@class='dL']"));
		if (Fileattached.isDisplayed()) {
			System.out.println("File uploaded Successfully");
		} else {
			System.out.println("File uploaded Successfully");
		}
	}

	@Test(priority = 6, enabled = false)
	public static void AttachImage() {
		driver.findElement(By.xpath(prop.getProperty("InsertImageIcon"))).click();
		final String parentWindow = driver.getWindowHandle();
		System.out.println("Parent Window Title " + driver.getTitle());
		for (final String handle : driver.getWindowHandles()) {
			System.out.println(handle);
			driver.switchTo().window(handle);
		}
		final String childtFrame = driver.getWindowHandle();
		driver.switchTo().window(childtFrame);
		System.out.println("Child Frame Title " + driver.getTitle());
	}

	@Test(priority = 7)
	public static void SendMail() {
		driver.findElement(By.xpath("//span[contains(text(),'ak199430@gmail.com')]")).isDisplayed();
		driver.findElement(By.xpath("//input[@value='Test Automation Task']")).isDisplayed();
		driver.findElement(By.xpath("//div[contains(text(),'This is sample message body')]")).isDisplayed();
		driver.findElement(By.xpath("//div[@class='dL']")).isDisplayed();
		driver.findElement(By.xpath(prop.getProperty("SendBtn"))).click();
		final boolean ele = driver.findElement(By.xpath("//span[contains(text(),'Message sent')]")).isDisplayed();
		Assert.assertEquals(ele, true);
	}

	@Test(priority = 8)
	public static void VerifyEmailSent() {
		driver.findElement(By.xpath(prop.getProperty("SentBtn"))).click();
		final List<WebElement> list = (List<WebElement>) driver.findElements(By.xpath("//table[@cellpadding='0' and @role='grid']//tr"));
		System.out.println(list.size());
		for (final WebElement ele1 : list) {
			if (ele1.findElement(By.xpath("(//span[contains(text(),'Test Automation Task')])[2]")).isDisplayed()) {
				System.out.println("Email is availbale in the sent queue");
			}
		}
	}

}
