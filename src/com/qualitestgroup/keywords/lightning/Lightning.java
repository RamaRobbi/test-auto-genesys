package com.qualitestgroup.keywords.lightning;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.kdt.exceptions.KDTValidationException;
import com.qualitestgroup.keywords.common.ElementOperation;

public class Lightning extends KeywordGroup {

	private static final String CURR_APP = "lightning";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	private static String accountName;
	private static String contactName;
	private static String opportunityName;
	private static String opportunityStage;
	private static String mandatoryFieldValue[];
	private static String contactFieldValue[];
	private static String accountProfiling[];

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>LoginLightningApplication</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to login into the
	 * application.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Username(Mandatory):Username</li>
	 * <li>Password(Mandatory):Password</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class LoginToApplication extends Keyword {

		private String url;
		private String userName;
		private String password;
		private String browser = "";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("URL", "Username", "Password");
				url = args.get("URL");
				userName = args.get("Username");
				password = args.get("Password");
			} catch (Exception e) {
				this.addComment("Error while initializing LoginToApplication");
				throw new KDTKeywordInitException("Error while initializing LoginToApplication", e);
			}

			if (hasArgs("Browser")) {
				browser = args.get("Browser");
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			if (!(browser.isEmpty())) {
				try {
					Keyword.run("Browser", "Launch", "Browser", browser);
				} catch (KDTException e) {
					throw new KDTValidationException("Unable to launch browser");
				}
			}

			WebDriver driver = context.getWebDriver();
			driver.get(url);
			eo.enterText(driver, "id", "usernameTextbox", userName, "SFDC Homepage - Username", CURR_APP);
			eo.enterText(driver, "id", "passwordTextbox", password, "SFDC Homepage - Password", CURR_APP);
			eo.clickElement(driver, "id", "loginButton", CURR_APP);
			eo.wait(10);
			if (!driver.getCurrentUrl().contains("lightning")) {
				eo.waitForWebElementVisible(driver, "xpath", "profileImage", waitTime, "Website URL - Lightning Page",
						CURR_APP);
				eo.clickElement(driver, "xpath", "profileImage", CURR_APP);

			}
			eo.waitForWebElementVisible(driver, "xpath", "lightPageValidation", waitTime, "Lightning Home Page",
					CURR_APP);
			this.addComment("User navigated to " + eo.getText(driver, "xpath", "homeTab", CURR_APP) + " Page.");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>LogoutOfApplication</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to log out of
	 * application.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */

	public static class LogoutOfApplication extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.waitForPageLoad(driver, waitTime);
			eo.switchToDefaultContent(driver);
			eo.waitForWebElementVisible(driver, "xpath", "userProfileLink", waitTime, "Global Profile Link", CURR_APP);
			eo.actionClick(driver, "xpath", "userProfileLink", " - Header part, Profile Link", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "logoutLink", waitTime, "Logout link", CURR_APP);
			eo.clickElement(driver, "xpath", "logoutLink", CURR_APP);
			eo.waitForWebElementVisible(driver, "id", "passwordTextbox", waitTime, "After Logout - Login Page - ",
					CURR_APP);
			this.addComment("User logged out of application.");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>CancelAccountCreation</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to cancel Account
	 * creation.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */

	public static class CancelAccountCreation extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.waitForWebElementVisible(driver, "xpath", "newPopupComponent", waitTime, "New Account Creation Pop up",
					CURR_APP);
			eo.clickElement(driver, "xpath", "cancelButton", CURR_APP);

			if (!eo.isDisplayed(driver, "xpath", "newPopupComponent", CURR_APP)) {
				this.addComment("Account Creation Process - Cancelled");
			} else {
				this.addFailMessage(
						"Account Creation Process - In Progress, Expected: Account Creation Process should be Cancelled");
				throw new KDTValidationException(
						"Account Creation Process - In Progress, Expected: Account Creation Process should be Cancelled");
			}
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SelectAccountRecordType</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to select Account record
	 * type.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>recordType(Mandatory):Record type of Account to be selected</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SelectAccountRecordType extends Keyword {

		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("RecordType");
				recordType = args.get("RecordType");
			} catch (Exception e) {
				this.addComment("Error while initializing SelectAccountRecordType");
				throw new KDTKeywordInitException("Error while initializing SelectAccountRecordType", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.waitForWebElementVisible(driver, "xpath", "accountsTab", waitTime, "Accounts Tab", CURR_APP);
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "accountsHomePage", waitTime, "Accounts Home Page", CURR_APP);
			eo.clickElement(driver, "xpath", "newButton", CURR_APP);

			eo.waitForWebElementVisible(driver, "xpath", "newPopupComponent", waitTime,
					"New Account Creation - Record Selection Popup", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "selectARecordText", waitTime, "Account Name Text box",
					CURR_APP);

			driver.findElement(By.xpath("//span[@class='slds-form-element__label'][text()='" + recordType + "']"))
					.click();
			eo.clickElement(driver, "xpath", "nextButton", CURR_APP);
			eo.switchToFrameByXPath(driver, "xpath", "frameSearchAccount", waitTime, "Account Search Section",
					CURR_APP);
			// driver.switchTo().frame(driver.findElement(By.xpath(".//iframe[@title='accessibility
			// title']")));
			eo.waitForWebElementVisible(driver, "cssselector", "searchAccountButton", waitTime, "Search Account Button",
					CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>AccountSearchByNameAndClickNew</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to search Account by its
	 * Name.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>accountName(Mandatory):Account Name to search and click on New
	 * button</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class AccountSearchByNameAndClickNew extends Keyword {

		private String accountName;
		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("AccountName", "RecordType");
				accountName = args.get("AccountName");
				recordType = args.get("RecordType");
			} catch (Exception e) {
				this.addComment("Error while initializing AccountSearchByNameAndClickNew");
				throw new KDTKeywordInitException("Error while initializing AccountSearchByNameAndClickNew", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			eo.waitForWebElementVisible(driver, "xpath", "accountNameTitle", waitTime, "Account Home Page Title",
					CURR_APP);
			eo.enterText(driver, "xpath", "accountNameTextbox", accountName, "Account Creation Page, Account Name",
					CURR_APP);
			eo.clickElement(driver, "cssselector", "searchAccountButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "newAccountCreateButton", waitTime,
					"New Account Creation Button", CURR_APP);
			eo.clickElement(driver, "cssselector", "newAccountCreateButton", CURR_APP);
			eo.switchToDefaultContent(driver);
			eo.waitForWebElementVisible(driver, "xpath", "newPopupComponent", waitTime, "New Account Creation Popup",
					CURR_APP);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					waitTime, CURR_APP);
			String popupTitleTxt = eo.getText(driver, "xpath", "popUpTitle", CURR_APP);
			if (("New Account: " + recordType).equalsIgnoreCase(popupTitleTxt)) {
				this.addComment("Account Creation Process - Initiated for " + recordType);
			} else {
				this.addFailMessage("Account Creation Process - Not Initiated for " + recordType);
				throw new KDTValidationException("Account Creation Process - Not Initiated for " + recordType);
			}
			this.addComment("Landed in Account Creation Popup for the Account Record Type " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>ValidateErrorMessageForNewAccountCreation</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to validate Error Message
	 * when mandatory fields are not entered.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>errorMessages(Mandatory):Error message descriptions</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class ValidateErrorMessageForNewAccountCreation extends Keyword {

		private String errorMessages;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("ErrorMessages");
				errorMessages = args.get("ErrorMessages");
			} catch (Exception e) {
				this.addComment("Error while initializing ValidateErrorMessageForNewAccountCreation");
				throw new KDTKeywordInitException("Error while initializing ValidateErrorMessageForNewAccountCreation",
						e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			eo.clickElement(driver, "xpath", "saveButton", CURR_APP);
			String errorMsg = eo.getText(driver, "xpath", "pageErrorMessage", CURR_APP).trim();
			if (errorMsg.equals(errorMessages)) {
				this.addComment("Valid Error message displayed at page level");
			} else {
				this.addFailMessage("Valid Error message was NOT displayed at page level" + "Expected <b>"
						+ errorMessages + "</b> Acutal <b>" + errorMsg + "</b>");
				throw new KDTValidationException("Valid Error message was NOT displayed at page level" + "Expected <b>"
						+ errorMessages + "</b> Acutal <b>" + errorMsg + "</b>");
			}

			eo.clickElement(driver, "xpath", "closePopup", CURR_APP);
			eo.invisibilityOfElement(driver, "xpath", "closePopup", CURR_APP, waitTime);
			this.addComment("Edit Account Pop up is closed");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EnterAccountMandatoryDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to enter mandatory
	 * details of Account.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>mandatoryFieldValue(Mandatory): Account mandatory field values</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterAccountMandatoryDetails extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			mandatoryFieldValue = args.get("AccountFieldValues").split("\\|");
			contactFieldValue = args.get("ContactFieldValues").split("\\|");
			accountProfiling = args.get("AccountProfiling").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			accountName = mandatoryFieldValue[0] + "_" + eo.gnerateRandomNo(5);
			eo.wait(5);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					accountName, CURR_APP);
			eo.wait(1);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledTextAreaXpath", "fieldName",
					"Account Comments", mandatoryFieldValue[1], CURR_APP);
			// Status
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Status",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					mandatoryFieldValue[2], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					mandatoryFieldValue[2], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "GARN Account Code",
					mandatoryFieldValue[4], CURR_APP);

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Intent Status",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					mandatoryFieldValue[3], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					mandatoryFieldValue[3], CURR_APP);

			// Contact Information
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 1",
					contactFieldValue[0], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 2",
					contactFieldValue[1], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 3",
					contactFieldValue[2], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "City",
					contactFieldValue[3], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Zip Code",
					contactFieldValue[4], CURR_APP);
			// country lookup
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "generalLookup", "fieldName", "Country",
					contactFieldValue[5], CURR_APP);
			eo.wait(2);
			eo.actionClickAfterReplacingKeyValue(driver, "xpath", "lookupDiv", "searchText", contactFieldValue[5],
					CURR_APP);

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "State/Province",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					contactFieldValue[6], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					contactFieldValue[6], CURR_APP);

			// Account profiling
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Vertical",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[0], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[0], CURR_APP);
			
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Industry",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[1], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[1], CURR_APP);
			
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Sector",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[2], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[2], CURR_APP);
			
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Type of Commerce",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[3], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[3], CURR_APP);
			
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "WW Employee Range",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[4], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[4], CURR_APP);
			
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName", "Account Segmentation",
					CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[5], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dropdownSelectText", "visibleText",
					accountProfiling[5], CURR_APP);
	
			this.addComment("Entered Account details");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveAccountDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to save Account
	 * details.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveAccountDetails extends Keyword {
		
		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "xpath", "saveButton", CURR_APP);
			//driver.navigate().to("https://genesys--qa.lightning.force.com/lightning/r/Account/0011100001yyIGVAA2/view");		
			//accountName="Automation_23572";
			eo.wait(10);
			eo.waitForWebElementVisible(driver, "xpath", "editButton", waitTime, "Accounts Details Page", CURR_APP);
			System.out.println("Entered Account Name is " + accountName);
			String createdAccount = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName","Account Name",CURR_APP);
					
			System.out.println("Created Account Name: " + createdAccount);
			if (!accountName.equals(createdAccount)) {
				throw new KDTValidationException("Account was not Saved");
			}
			

			System.out.println("Entered Status is: " + mandatoryFieldValue[2]);
			String savedStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Status", CURR_APP);
			System.out.println("Saved Status: " + savedStatus);
			if (!mandatoryFieldValue[2].equals(savedStatus)) {
				throw new KDTValidationException("Status is not same as Entered");
			}

			System.out.println("Entered GARN code: " + mandatoryFieldValue[4]);
			String savedGarnCode = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"GARN Account Code", CURR_APP);
			System.out.println("Saved GARN code: " + savedGarnCode);
			if (!mandatoryFieldValue[4].equals(savedGarnCode)) {
				throw new KDTValidationException("GARN code is not same as entered");
			}
			/*
			 * System.out.println("Entered Intent Status: " + mandatoryFieldValue[3]);
			 * String intentStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath",
			 * "savedFieldText", "fieldName", "Intent Status", CURR_APP);
			 * System.out.println("Saved Intent Status: " + intentStatus); if
			 * (!mandatoryFieldValue[3].equals(intentStatus)) { throw new
			 * KDTValidationException("Intent Status is not same as entered"); }
			 */

			System.out.println("Entered Address1: " + contactFieldValue[0]);
			String address1 = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Address 1", CURR_APP);
			System.out.println("Saved Address1: " + address1);
			if (!contactFieldValue[0].equals(address1)) {
				throw new KDTValidationException("Address 1 is not same as entered");
			}

			System.out.println("Entered Address2: " + contactFieldValue[1]);
			String address2 = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Address 2", CURR_APP);
			System.out.println("Saved Address2: " + address2);
			if (!contactFieldValue[1].equals(address2)) {
				throw new KDTValidationException("Address 2 is not same as entered");
			}

			System.out.println("Entered Address3: " + contactFieldValue[2]);
			String address3 = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Address 3", CURR_APP);
			System.out.println("Saved Address3: " + address3);
			if (!contactFieldValue[2].equals(address3)) {
				throw new KDTValidationException("Address 3 is not same as entered");
			}

			System.out.println("Entered City " + contactFieldValue[3]);
			String city = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName", "City",
					CURR_APP);
			System.out.println("Saved City: " + city);
			if (!contactFieldValue[3].equals(city)) {
				throw new KDTValidationException("City is not same as entered");
			}

			System.out.println("Entered ZipCode: " + contactFieldValue[4]);
			String zipCode = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Zip Code", CURR_APP);
			System.out.println("Saved ZipCode: " + zipCode);
			if (!contactFieldValue[4].equals(zipCode)) {
				throw new KDTValidationException("Zip Code is not same as entered");
			}

			System.out.println("Entered Country: " + contactFieldValue[5]);
			String country = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedLinkText", "fieldName",
					"Country", CURR_APP);
			System.out.println("Saved Country: " + country);
			if (!contactFieldValue[5].equals(country)) {
				throw new KDTValidationException("Country is not same as entered");
			}

			System.out.println("Entered State/Province: " + contactFieldValue[6]);
			String stateProvince = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"State/Province", CURR_APP);
			System.out.println("Saved State/Province: " + stateProvince);
			if (!contactFieldValue[6].equals(stateProvince)) {
				throw new KDTValidationException("State/Province is not same as entered");
			}
			this.addComment("New Account was Saved successfully");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveAndNewAccount</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to save Account by
	 * clicking Save and New.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveAndNewAccount extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveAndNewButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "continueButton", waitTime, "Continue button", CURR_APP);
			String currentTab = eo.getText(driver, "cssselector", "breadCrumbText", CURR_APP);
			System.out.println("Current Tab: " + currentTab);
			if (!currentTab.trim().equals("New Account")) {
				throw new KDTValidationException("User was not navigated to New Account Page");
			}
			this.addComment("New Account was Saved successfully and New Account Page was displayed");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EditAccount</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to edit Account.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>accountNameToEdit(Mandatory): Account Name to edit</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EditAccount extends Keyword {

		private String accountNameToEdit;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			accountNameToEdit = args.get("AccountName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts tab", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", accountNameToEdit, CURR_APP);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					CURR_APP);
			accountName = "Update_" + accountNameToEdit + "_" + eo.gnerateRandomNo(3);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					accountName, CURR_APP);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SelectContactRecordType</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to select record type of
	 * Contact.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>recordType(Mandatory): Record type to be selected</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SelectContactRecordType extends Keyword {

		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			recordType = args.get("RecordType");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "contactsTab", "Contacts tab", CURR_APP);
			eo.clickElement(driver, "cssselector", "newButton", CURR_APP);
			eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
			eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>CancelContactCreation</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to cancel Contact
	 * creation.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class CancelContactCreation extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "cssselector", "cancelButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "newButton", waitTime, "New Contact Creation Button",
					CURR_APP);
			String currentTab = eo.getText(driver, "cssselector", "breadCrumbText", CURR_APP);
			System.out.println("Current Tab: " + currentTab);
			if (!currentTab.equals("Contacts")) {
				throw new KDTValidationException("User is NOT navigated to Contacts Home page");
			}
			this.addComment("User is navigated to Contacts Home page and Account is not created");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EnterContactMandatoryDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to enter mandatory
	 * details of Contact.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>mandatoryFieldValue(Mandatory): Mandatory filed values of Contact</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterContactMandatoryDetails extends Keyword {

		private String mandatoryFieldValue[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			mandatoryFieldValue = args.get("ContactFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			contactName = mandatoryFieldValue[0] + " " + mandatoryFieldValue[1];
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name",
					mandatoryFieldValue[0], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Last Name",
					mandatoryFieldValue[1], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Email",
					mandatoryFieldValue[2], CURR_APP);
			eo.javaScriptScrollToViewElement(driver, "xpath", "countryLookupImage", CURR_APP);
			eo.clickElement(driver, "xpath", "accountNameLookupImage", CURR_APP);
			String parentWindow = eo.switchWindow(driver);
			eo.switchToFrameByIdentifier(driver, "id", "resultsFrame", waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", mandatoryFieldValue[3],
					CURR_APP);
			System.out.println("Selected Account Name");
			eo.wait(1);
			driver.switchTo().window(parentWindow);
			eo.wait(1);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Job Title",
					CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Buyer Role", mandatoryFieldValue[5], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Job Title",
					mandatoryFieldValue[4], CURR_APP);
			this.addComment("Entered Contact details");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveAndNewContact</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Contact using
	 * Save and New option.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveAndNewContact extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveAndNewButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "continueButton", waitTime, "Continue Button", CURR_APP);
			String currentTab = eo.getText(driver, "cssselector", "breadCrumbText", CURR_APP);
			System.out.println("Current Tab: " + currentTab);
			if (!currentTab.trim().equals("New Contact")) {
				throw new KDTValidationException("User was not navigated to New Contact Page");
			}
			this.addComment("New Contact was Saved successfully and New Contact Page was displayed");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveContactDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Contact
	 * details.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveContactDetails extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit button", CURR_APP);
			System.out.println("Entered Contact Name is " + contactName);
			String createdContact = eo.getText(driver, "xpath", "savedContactNameText", CURR_APP).trim();
			System.out.println("Created Contact Name: " + createdContact);
			if (!contactName.equals(createdContact)) {
				throw new KDTValidationException("Contact was not Saved");
			}
			this.addComment("New Contact was Saved successfully");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EditContact</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to edit Contact
	 * details.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>contactNameToEdit(Mandatory): Contact Name to edit</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EditContact extends Keyword {

		private String contactNameToEdit;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			contactNameToEdit = args.get("ContactName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.actionClick(driver, "xpath", "contactsTab", "Contacts tab", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", contactNameToEdit, CURR_APP);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			String[] name = contactNameToEdit.split(",");
			contactName = name[1].trim() + " Updated";
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name",
					contactName, CURR_APP);
			contactName = contactName + " " + name[0].trim();
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Last Name", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Last Name",
					name[0].trim(), CURR_APP);
			this.addComment("Edited the Contact details.");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SelectOpportunitiesRecordType</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to select record type of
	 * Opportunity.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>recordType(Mandatory): Record type to be selected</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SelectOpportunitiesRecordType extends Keyword {

		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			recordType = args.get("RecordType");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "opportunitiesTab", "Opportunity tab", CURR_APP);
			eo.clickElement(driver, "cssselector", "newButton", CURR_APP);
			eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
			eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "saveAndAddProductButton", waitTime,
					"Save and Product Selection button", CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EnterOpportunitiesMandatoryDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to enter mandatory
	 * details of Opportunity.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>mandatoryFieldValue(Mandatory): Mandatory field values of
	 * Opportunities</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterOpportunitiesMandatoryDetails extends Keyword {

		private String mandatoryFieldValue[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			mandatoryFieldValue = args.get("OpportunitiesFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			opportunityName = mandatoryFieldValue[0] + "_" + eo.gnerateRandomNo(3);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Opportunity Name",
					opportunityName, CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Close Date",
					mandatoryFieldValue[1], CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Opportunity Name",
					CURR_APP);
			opportunityStage = mandatoryFieldValue[2];
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Stage", opportunityStage, CURR_APP);
			eo.javaScriptScrollToViewElement(driver, "xpath", "countryLookupImage", CURR_APP);
			eo.clickElement(driver, "xpath", "accountNameLookupImage", CURR_APP);
			String parentWindow = eo.switchWindow(driver);
			eo.switchToFrameByIdentifier(driver, "id", "resultsFrame", waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", mandatoryFieldValue[3],
					CURR_APP);
			System.out.println("Selected Account Name");
			eo.wait(1);
			driver.switchTo().window(parentWindow);
			eo.wait(1);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Opportunity Source", mandatoryFieldValue[4], CURR_APP);
			this.addComment("Entered Opportunities details");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveOpportunitiesDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Opportunities
	 * details.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveOpportunitiesDetails extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "cssselector", "saveAndAddProductButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "id", "priceBookDDL", waitTime, "PriceBook", CURR_APP);
			eo.clickElement(driver, "cssselector", "cancelButton", CURR_APP);
			this.addComment("Cancel Price Book");
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit button", CURR_APP);
			System.out.println("Entered Opportunity Name is " + opportunityName);
			String createdOpportunity = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Opportunity Name", CURR_APP).trim();
			System.out.println("Created Opportunity Name: " + createdOpportunity);
			if (!opportunityName.equals(createdOpportunity)) {
				throw new KDTValidationException("Opportunity Name did not match");
			}
			System.out.println("Entered Stage is " + opportunityStage);
			String createdStage = eo
					.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName", "Stage", CURR_APP)
					.trim();
			System.out.println("Created Opportunity Name: " + createdStage);
			if (!opportunityStage.equals(createdStage)) {
				throw new KDTValidationException("Stage Value did not match");
			}
			this.addComment("New Opportunity was Saved successfully");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EditOpportunity</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to edit Opportunity.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>opportunityNameToEdit(Mandatory): Opportunity Name to edit</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EditOpportunity extends Keyword {

		private String opportunityNameToEdit;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			opportunityNameToEdit = args.get("OpportunityName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.actionClick(driver, "xpath", "opportunitiesTab", "Opportunity tab", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", opportunityNameToEdit,
					CURR_APP);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Opportunity Name",
					CURR_APP);
			opportunityName = "Updated_" + opportunityNameToEdit + "_" + eo.gnerateRandomNo(3);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Opportunity Name",
					opportunityName, CURR_APP);
			this.addComment("Edited Opportunity details");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveEditedOpportunity</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Edited
	 * Opportunity.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveEditedOpportunity extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit button", CURR_APP);
			System.out.println("Entered Opportunity Name is " + opportunityName);
			String createdOpportunity = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Opportunity Name", CURR_APP).trim();
			System.out.println("Updated Opportunity Name: " + createdOpportunity);
			if (!opportunityName.equals(createdOpportunity)) {
				throw new KDTValidationException("Opportunity Name did not match");
			}
			this.addComment("Opportunity was Updated successfully");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SelectQuoteRecordType</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to select record type for
	 * Quote.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>recordType(Mandatory): record type to be selected</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SelectQuoteRecordType extends Keyword {

		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			recordType = args.get("RecordType");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
			eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>AddQuoteToOpportunity</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to add Quote to
	 * Opportunity.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>opportunityNameToEdit(Mandatory): Opportunity name for which Quote needs
	 * to be added</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class AddQuoteToOpportunity extends Keyword {

		private String opportunityNameToEdit;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			opportunityNameToEdit = args.get("OpportunityName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.actionClick(driver, "xpath", "opportunitiesTab", "Opportunity Tab", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", opportunityNameToEdit,
					CURR_APP);
			eo.clickElement(driver, "xpath", "newQuoteButton", CURR_APP);
			this.addComment("Adding Quote to Opportunity");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EnterQuoteMandatoryDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to enter mandatory
	 * details of Quote.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>mandatoryFieldValue(Mandatory): All the mandatory field of Quote</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterQuoteMandatoryDetails extends Keyword {

		private String mandatoryFieldValue[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			mandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownFieldWithInfoXpath", "fieldName",
					"Billing Period", CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Billing Period", mandatoryFieldValue[0], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Initial Subscription Term", mandatoryFieldValue[1], CURR_APP);
			this.addComment("Entered Quote details");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>SaveQuoteDetails</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Quote
	 * details.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveQuoteDetails extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.wait(2);
			if (!eo.isExists(driver, "xpath", "addProductsButton", CURR_APP)) {
				throw new KDTValidationException("Quote was not Saved");
			}
			this.addComment("New Quote was Saved successfully");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>AddFirstProductToQuote</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to add first Product to
	 * Quote.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class AddFirstProductToQuote extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(2);
			if (eo.isDisplayed(driver, "xpath", "savePriceBook", CURR_APP)) {
				System.out.println("Saving Price book!");
				eo.clickElement(driver, "xpath", "savePriceBook", CURR_APP);
				eo.invisibilityOfElement(driver, "cssselector", "loadingImage", CURR_APP, waitTime);
			}
			eo.clickElement(driver, "xpath", "addProductsButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "firstProductCheckbox", waitTime, "First Product Checkbox",
					CURR_APP);
			eo.clickElement(driver, "xpath", "firstProductCheckbox", CURR_APP);
			eo.clickElement(driver, "xpath", "selectButton", CURR_APP);
			eo.clickElement(driver, "xpath", "saveQuoteLineButton", CURR_APP);
			eo.wait(2);
			if (!eo.isExists(driver, "xpath", "editQuoteLinesLink", CURR_APP)) {
				throw new KDTValidationException("Quote Lines are not added");
			}
			this.addComment("Quote Lines are added successfully.");
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>EditQuoteAddedToOpportunity</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Edit Quote Added To
	 * Opportunity.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>opportunityNameToEdit(Mandatory): Opportunity Name for which Quote needs
	 * to be added</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EditQuoteAddedToOpportunity extends Keyword {

		private String opportunityNameToEdit;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			opportunityNameToEdit = args.get("OpportunityName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "opportunitiesTab", "Opportunity tab", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", opportunityNameToEdit,
					CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "newQuoteButton", waitTime, "New Quote Button", CURR_APP);
			if (!eo.isExists(driver, "xpath", "firstQuoteEditLink", CURR_APP)) {
				throw new KDTValidationException("Opportunity does not have Quote to Edit.");
			}
			eo.javaScriptScrollToViewElement(driver, "xpath", "newQuoteButton", CURR_APP);
			String primaryCheckbox = eo.getAttribute(driver, "xpath", "firstQuotePrimaryCheckbox", "title", CURR_APP);
			System.out.println("Before " + eo.getAttribute(driver, "xpath", "firstQuoteEditLink", "title", CURR_APP)
					+ " Primary checkbox was:  " + primaryCheckbox);
			eo.clickElement(driver, "xpath", "firstQuoteEditLink", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName", "Primary",
					CURR_APP);
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "newQuoteButton", waitTime, "New Quote Creation Button",
					CURR_APP);
			eo.javaScriptScrollToViewElement(driver, "xpath", "newQuoteButton", CURR_APP);
			String primaryCheckboxAfterEdit = eo.getAttribute(driver, "xpath", "firstQuotePrimaryCheckbox", "title",
					CURR_APP);
			System.out.println("After Edit Primary checkbox is:  " + primaryCheckboxAfterEdit);
			if (primaryCheckboxAfterEdit.equals(primaryCheckbox)) {
				throw new KDTValidationException("Primary Checkbox was not updated in Opportunity");
			}
			this.addComment(
					eo.getAttribute(driver, "xpath", "firstQuoteEditLink", "title", CURR_APP) + " was successful");
		}
	}
}