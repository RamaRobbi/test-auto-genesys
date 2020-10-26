package com.qualitestgroup.keywords.classic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.itextpdf.text.log.SysoCounter;
import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.kdt.exceptions.KDTValidationException;
import com.qualitestgroup.keywords.common.Common.GetCurrentDateTime;
import com.qualitestgroup.keywords.common.ElementOperation;

public class Classic extends KeywordGroup {

	private static final String CURR_APP = "classic";
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
	private static String contactInfo[];
	private static String oppMandatoryFieldValue[];
	private static String quoteMandatoryFieldValue[];
	private static Map<String, String> singleProducts = new HashMap<String, String>();
	private static Map<String, String> wrapperProducts = new HashMap<String, String>();
	private static Map<String, String> products = new HashMap<String, String>();
	private static List<String> productsList = new ArrayList<String>();
	private static String productsData[];
	private static String[] subProducts;
	private static String productMandatoryFieldValue[];
	private static String quoteNumber;
	private static String orderNumber;
	private static String orderHeader;

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>LoginToApplication</b>
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i> Modified by: siva surya prasand Arivalagan</i></b>
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
			eo.wait(2);
			if (driver.getCurrentUrl().contains("lightning")) {
				eo.waitForWebElementVisible(driver, "cssselector", "profileImage", waitTime,
						"Website URL - Lightning Page", CURR_APP);
				eo.clickElement(driver, "cssselector", "profileImage", CURR_APP);
				eo.clickElement(driver, "cssselector", "switchToClassicLink", CURR_APP);
			}

			String appSwitcher = eo.getText(driver, "xpath", "txt_appSwitcher", CURR_APP);
			System.out.println("the used app is " + appSwitcher);

			if (!appSwitcher.equalsIgnoreCase("Salesforce CPQ")) {
				eo.actionClick(driver, "xpath", "txt_appSwitcher", " - Header part, App Switcher Link", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "list_appSwitcher", waitTime,
						"List of Apps avaiable to be Switched - ", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", "Salesforce CPQ",
						CURR_APP);
				System.out.println("User navigated to Salesforce CPQ Page");
				this.addComment("User navigated to Salesforce CPQ Page.");
			}

			this.addComment("User is in Salesforce CPQ Page.");
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			eo.wait(10);
			eo.actionClick(driver, "id", "userProfileLink", " - Header part, Profile Link", CURR_APP);
			eo.clickElement(driver, "cssselector", "logoutLink", CURR_APP);
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			eo.clickElement(driver, "cssselector", "cancelButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "newButton", waitTime, "New Account Creation Pop up",
					CURR_APP);
			String currentTab = eo.getText(driver, "cssselector", "breadCrumbText", CURR_APP);
			System.out.println("Current Tab: " + currentTab);
			if (currentTab.equalsIgnoreCase("ACCOUNTS")) {
				this.addComment("User is navigated to Account Home page and Account is not created");
			} else {
				this.addFailMessage("User is NOT navigated to Account Home page");
				throw new KDTValidationException("User is NOT navigated to Account Home page");
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i> Modified by: siva surya prasand Arivalagan</i></b>
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
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.clickElement(driver, "cssselector", "newButton", CURR_APP);
			eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
			eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "searchAccountButton", waitTime, "Account Name Text box",
					CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	public static class SelectBusinessAccountRecordType extends Keyword {

		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("BusinessRecordType");
				recordType = args.get("BusinessRecordType");
			} catch (Exception e) {
				this.addComment("Error while initializing SelectAccountRecordType");
				throw new KDTKeywordInitException("Error while initializing SelectAccountRecordType", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.clickElement(driver, "cssselector", "newButton", CURR_APP);
			eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
			eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "searchAccountButton", waitTime, "Account Name Text box",
					CURR_APP);
			this.addComment("Selected Record Type as " + recordType);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>AccountSearchByName</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to search Account by its
	 * Name.</i>
	 * </p>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>accountName(Mandatory):Account Name to search</li>
	 * </ul>
	 * <p>
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i> Modified by: siva surya prasand Arivalagan</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class AccountSearchByName extends Keyword {

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
			String selectedRecordTypeValue;
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.enterText(driver, "xpath", "accountNameTextbox", accountName, "Account Creation Page, Account Name",
					CURR_APP);
			eo.clickElement(driver, "cssselector", "searchAccountButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "newAccountCreateButton", waitTime,
					"New Account Creation Button", CURR_APP);
			this.addComment("Searched for Account by Name: " + accountName);
			eo.clickElement(driver, "cssselector", "newAccountCreateButton", CURR_APP);

			try {
				eo.waitForWebElementVisible(driver, "xpath", "recordTypeSelected2", waitTime,
						"Record Type : " + recordType, CURR_APP);
				selectedRecordTypeValue = eo.getText(driver, "xpath", "recordTypeSelected2", CURR_APP);
			} catch (Exception e) {

				eo.waitForWebElementVisible(driver, "xpath", "recordTypeSelected", waitTime,
						"Record Type : " + recordType, CURR_APP);
				selectedRecordTypeValue = eo.getText(driver, "xpath", "recordTypeSelected", CURR_APP);
			}

			if ((recordType).equalsIgnoreCase(selectedRecordTypeValue)) {
				this.addComment("Account Creation Process - Initiated for " + recordType);
			} else {
				this.addFailMessage("Account Creation Process - Not Initiated for " + recordType);
				throw new KDTValidationException("Account Creation Process - Not Initiated for " + recordType);
			}
			this.addComment("Landed in Account Creation Popup for the Account Record Type " + recordType);
		}
	}

	public static class AccountSearchByNameForBusinnessPartner extends Keyword {

		private String accountName;
		private String recordType;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("AccountName", "BusinessRecordType");
				accountName = args.get("AccountName");
				recordType = args.get("BusinessRecordType");
			} catch (Exception e) {
				this.addComment("Error while initializing AccountSearchByNameAndClickNew");
				throw new KDTKeywordInitException("Error while initializing AccountSearchByNameAndClickNew", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			String selectedRecordTypeValue;
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.enterText(driver, "xpath", "accountNameTextbox", accountName, "Account Creation Page, Account Name",
					CURR_APP);
			eo.clickElement(driver, "cssselector", "searchAccountButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "cssselector", "newAccountCreateButton", waitTime,
					"New Account Creation Button", CURR_APP);
			this.addComment("Searched for Account by Name: " + accountName);
			eo.clickElement(driver, "cssselector", "newAccountCreateButton", CURR_APP);

			try {
				eo.waitForWebElementVisible(driver, "xpath", "recordTypeSelected2", waitTime,
						"Record Type : " + recordType, CURR_APP);
				selectedRecordTypeValue = eo.getText(driver, "xpath", "recordTypeSelected2", CURR_APP);
			} catch (Exception e) {

				eo.waitForWebElementVisible(driver, "xpath", "recordTypeSelected", waitTime,
						"Record Type : " + recordType, CURR_APP);
				selectedRecordTypeValue = eo.getText(driver, "xpath", "recordTypeSelected", CURR_APP);
			}

			if ((recordType).equalsIgnoreCase(selectedRecordTypeValue)) {
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
	 * <b><i>Keyword Name: </i>ValidateErrorMessage</b>
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class ValidateErrorMessage extends Keyword {

		private String errorMessages[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			errorMessages = args.get("ErrorMessages").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			String errorMsg = eo.getText(driver, "id", "pageErrorMessage", CURR_APP).trim();
			if (!errorMsg.equals(errorMessages[0])) {
				this.addFailMessage("Valid Error message was NOT displayed at page level" + "Expected <b>"
						+ errorMessages[0] + "</b> Acutal <b>" + errorMsg + "</b>");
				throw new KDTValidationException("Valid Error message was NOT displayed at page level" + "Expected <b>"
						+ errorMessages[0] + "</b> Acutal <b>" + errorMsg + "</b>");
			}
			List<WebElement> errorMsgs = eo.getListOfWebElements(driver, "cssselector", "fieldErrorMessage", CURR_APP);
			for (WebElement err : errorMsgs) {
				if (!err.getText().trim().equals(errorMessages[1])) {
					this.addFailMessage("Valid Error message was NOT displayed at field level" + "Expected <b>"
							+ errorMessages[1] + "</b> Acutal <b>" + err.getText() + "</b>");
					throw new KDTValidationException("Valid Error message was NOT displayed at field level"
							+ "Expected <b>" + errorMessages[1] + "</b> Acutal <b>" + err.getText() + "</b>");
				}
			}
			this.addComment("Valid Error messages were displayed.");
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterAccountMandatoryDetails extends Keyword {

		// private String mandatoryFieldValue[];
		// private String contactFieldValue[];
		// private String accountProfiling[];

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

			eo.waitForPageload(driver);
			eo.wait(3);

			accountName = mandatoryFieldValue[0] + "_" + eo.gnerateRandomNo(5);

			// Account Information
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					accountName, CURR_APP);
			eo.wait(1);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Account Name (Native)", accountName, CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithHelpTextAreaXpath", "fieldName",
					"Account Comments", mandatoryFieldValue[1], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Status", mandatoryFieldValue[2], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Intent Status", mandatoryFieldValue[3], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "GARN Account Code",
					mandatoryFieldValue[4], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldNonHiddenXpath", "fieldName", "Subsidiary",
					mandatoryFieldValue[5], CURR_APP);
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

			eo.javaScriptScrollToViewElement(driver, "xpath", "countryLookupImage", CURR_APP);
			eo.clickElement(driver, "xpath", "countryLookupImage", CURR_APP);

			String parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			List<WebElement> listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			// driver.switchTo().frame("searchFrame");

			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", contactFieldValue[5], "Country Search", CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);

			eo.wait(3);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(listFrames.get(1));
			// driver.switchTo().frame("resultsFrame");
			eo.wait(1);

			eo.clickElement(driver, "xpath", "selectFirstItem", CURR_APP);
			eo.wait(2);
			System.out.println("Selected Country");
			eo.wait(1);
			driver.switchTo().window(parentWindow);

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "State/Province", contactFieldValue[6], CURR_APP);

			// Account profiling

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Vertical", accountProfiling[0], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Industry", accountProfiling[1], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Sector", accountProfiling[2], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Type of Commerce", accountProfiling[3], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"WW Employee Range", accountProfiling[4], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Account Segmentation", accountProfiling[5], CURR_APP);
			this.addComment("Entered Account details");
		}
	}

	public static class EnterAccountMandatoryDetailsForBusinessAccount extends Keyword {

		// private String mandatoryFieldValue[];
		// private String contactFieldValue[];
		// private String accountProfiling[];

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

			eo.waitForPageload(driver);
			eo.wait(3);

			accountName = mandatoryFieldValue[0] + "_" + eo.gnerateRandomNo(5);

			// Account Information
			// Partner Success Manager lookup
			/*
			 * eo.javaScriptClickAfterReplacingKeyValue(driver, "xpath",
			 * "generalLookupImage", "fieldName", "Partner Success Manager", CURR_APP);
			 */
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName",
					"Partner Success Manager", CURR_APP);

			String parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			List<WebElement> listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));

			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", mandatoryFieldValue[6], "Partner Success Manager	",
					CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);

			eo.wait(3);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(listFrames.get(1));
			eo.wait(1);

			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			eo.wait(2);
			System.out.println("Selected Country");
			eo.wait(1);
			driver.switchTo().window(parentWindow);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					accountName, CURR_APP);
			eo.wait(1);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Account Name (Native)", accountName, CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Status", mandatoryFieldValue[2], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "GARN Account Code",
					mandatoryFieldValue[4], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldNonHiddenXpath", "fieldName", "Subsidiary",
					mandatoryFieldValue[5], CURR_APP);

			// Contact Information
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 1",
					contactFieldValue[0], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 2",
					contactFieldValue[1], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Address 3",
					contactFieldValue[2], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Zip Code",
					contactFieldValue[4], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "City",
					contactFieldValue[3], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "State/Province", contactFieldValue[6], CURR_APP);

			// country lookup
			eo.javaScriptScrollToViewElement(driver, "xpath", "countryLookupImage", CURR_APP);
			eo.clickElement(driver, "xpath", "countryLookupImage", CURR_APP);

			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			// driver.switchTo().frame("searchFrame");

			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", contactFieldValue[5], "Country Search", CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);

			eo.wait(3);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(listFrames.get(1));
			// driver.switchTo().frame("resultsFrame");
			eo.wait(1);

			eo.clickElement(driver, "xpath", "selectFirstItem", CURR_APP);
			eo.wait(2);
			System.out.println("Selected Country");
			eo.wait(1);
			driver.switchTo().window(parentWindow);

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Type of Commerce", accountProfiling[3], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithHelpTextAreaXpath", "fieldName",
					"Account Comments", mandatoryFieldValue[1], CURR_APP);

			// Account profiling

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Vertical", accountProfiling[0], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Industry", accountProfiling[1], CURR_APP);

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SaveBusinessPartnerAccountDetails extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Accounts Details Page", CURR_APP);
			// Validation
			System.out.println("Entered Account Name is: " + accountName);
			String createdAccount = eo.getText(driver, "xpath", "savedAccountNameText", CURR_APP)
					.replace(" [View Hierarchy]", "");
			System.out.println("Created Account Name: " + createdAccount);
			if (!accountName.contains(createdAccount)) {
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

			System.out.println("Entered Subsidary: " + mandatoryFieldValue[5]);
			String savedSubsidary = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Subsidiary", CURR_APP);
			System.out.println("Saved GARN code: " + savedSubsidary);
			if (!mandatoryFieldValue[5].equals(savedSubsidary)) {
				throw new KDTValidationException("Subsidary is not same as entered");
			}

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
			String country = eo.getText(driver, "xpath", "countryLink", CURR_APP);
			System.out.println("Saved Country: " + country);
			if (!contactFieldValue[5].equals(country)) {
				throw new KDTValidationException("Country is not same as entered");
			}

			System.out.println("Entered State/Province: " + contactFieldValue[6]);
			String stateProvince = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo", "fieldName",
					"State/Province", CURR_APP);
			System.out.println("Saved State/Province: " + stateProvince);
			if (!contactFieldValue[6].equals(stateProvince)) {
				throw new KDTValidationException("State/Province is not same as entered");
			}

			this.addComment("New Account was Saved successfully");
		}
	}

	public static class SaveAccountDetails extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Accounts Details Page", CURR_APP);
			// Validation
			System.out.println("Entered Account Name is: " + accountName);
			String createdAccount = eo.getText(driver, "xpath", "savedAccountNameText", CURR_APP)
					.replace(" [View Hierarchy]", "");
			System.out.println("Created Account Name: " + createdAccount);
			if (!accountName.contains(createdAccount)) {
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

			System.out.println("Entered Subsidary: " + mandatoryFieldValue[5]);
			String savedSubsidary = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Subsidiary", CURR_APP);
			System.out.println("Saved GARN code: " + savedSubsidary);
			if (!mandatoryFieldValue[5].equals(savedSubsidary)) {
				throw new KDTValidationException("Subsidary is not same as entered");
			}

			System.out.println("Entered Intent Status: " + mandatoryFieldValue[3]);
			String intentStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo", "fieldName",
					"Intent Status", CURR_APP);
			System.out.println("Saved Intent Status: " + intentStatus);
			if (!mandatoryFieldValue[3].equals(intentStatus)) {
				throw new KDTValidationException("Intent Status is not same as entered");
			}

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
			String country = eo.getText(driver, "xpath", "countryLink", CURR_APP);
			System.out.println("Saved Country: " + country);
			if (!contactFieldValue[5].equals(country)) {
				throw new KDTValidationException("Country is not same as entered");
			}

			System.out.println("Entered State/Province: " + contactFieldValue[6]);
			String stateProvince = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo", "fieldName",
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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

	public static class CreateContactFromExistingAccount extends Keyword {

		private String accountNameToCreateContact;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			accountNameToCreateContact = args.get("AccountName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			accountName = accountNameToCreateContact;
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(2);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickAccountLinkFromTable", CURR_APP);
			eo.wait(20);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Contacts", waitTime,
					CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Contacts", CURR_APP);

			eo.clickElement(driver, "xpath", "NewContactButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);

		}
	}

	public static class CreateContactFromAccount extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(2);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickAccountLinkFromTable", CURR_APP);
			eo.wait(5);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Contacts", waitTime,
					CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Contacts", CURR_APP);

			eo.clickElement(driver, "xpath", "NewContactButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts tab", CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "nameLink", "linkText", accountName, waitTime,
					CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "nameLink", "linkText", accountName, CURR_APP);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Account Name",
					CURR_APP);
			accountName = "Update_" + accountName;
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i> Modified by: siva surya prasand Arivalagan</i></b>
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
			if (eo.isDisplayed(driver, "xpath", "contactsTab", CURR_APP)) {
				eo.actionClick(driver, "xpath", "contactsTab", "Contacts tab", CURR_APP);
			} else {
				eo.actionClick(driver, "xpath", "allTab", "All Tab", CURR_APP);
				eo.actionClick(driver, "xpath", "allTab_contactsTab", "Contacts tab", CURR_APP);

			}
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Contacts", waitTime,
					CURR_APP);
			/*
			 * String currentTab = eo.getText(driver, "cssselector", "cancelContactHeader",
			 * CURR_APP); System.out.println("Current Tab: " + currentTab); if
			 * (!currentTab.equals("Contacts")) { throw new
			 * KDTValidationException("User is NOT navigated to Contacts Home page"); }
			 */
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterContactMandatoryDetails extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			contactInfo = args.get("ContactInfo").split("\\|");
			contactFieldValue = args.get("ContactFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			contactName = contactInfo[0] + " " + contactInfo[1];
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name",
					contactInfo[0], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Last Name",
					contactInfo[1], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Relationship", contactInfo[6], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Email", contactInfo[2],
					CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Mobile",
					contactInfo[7], CURR_APP);
			eo.wait(1);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Job Title",
					contactInfo[4], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Buyer Role", contactInfo[5], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Persona", contactInfo[8], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithTextAreaXpath", "fieldName",
					"Status / Next Actions", contactInfo[9], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Primary offering", contactInfo[10], CURR_APP);

			// validation of Address details inside contact page

			System.out.println("Account Name parsed from Account: " + accountName);
			String account = eo.getAttributeAfterReplacingValue(driver, "xpath", "InputFieldWithMandateXpath",
					"fieldName", "Account Name", "value", CURR_APP);
			System.out.println("Accountname displaying: " + account);
			if (!accountName.equals(account)) {
				throw new KDTValidationException("Account name is not same as entered");
			}

			System.out.println("Parsed Address1: " + contactFieldValue[0]);
			String address1 = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Address 1", "value", CURR_APP);
			System.out.println("Displayed Address1: " + address1);
			if (!contactFieldValue[0].equals(address1)) {
				throw new KDTValidationException("Address 1 is not same as entered");
			}

			System.out.println("Parsed Address2: " + contactFieldValue[1]);
			String address2 = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Address 2", "value", CURR_APP);
			System.out.println("Displayed Address2: " + address2);
			if (!contactFieldValue[1].equals(address2)) {
				throw new KDTValidationException("Address 2 is not same as entered");
			}

			System.out.println("Parsed Address3: " + contactFieldValue[2]);
			String address3 = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Address 3", "value", CURR_APP);
			System.out.println("Displayed Address3: " + address3);
			if (!contactFieldValue[2].equals(address3)) {
				throw new KDTValidationException("Address 3 is not same as entered");
			}

			System.out.println("Parsed City " + contactFieldValue[3]);
			String city = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName", "City",
					"value", CURR_APP);
			System.out.println("Displayed City: " + city);
			if (!contactFieldValue[3].equals(city)) {
				throw new KDTValidationException("City is not same as entered");
			}

			System.out.println("Parsed ZipCode: " + contactFieldValue[4]);
			String zipCode = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Zip Code", "value", CURR_APP);
			System.out.println("Displayed ZipCode: " + zipCode);
			if (!contactFieldValue[4].equals(zipCode)) {
				throw new KDTValidationException("ZipCode is not same as entered");
			}

			System.out.println("Parsed Country: " + contactFieldValue[5]);
			String country = eo.getAttributeAfterReplacingValue(driver, "xpath", "InputFieldWithMandateXpath",
					"fieldName", "Country", "value", CURR_APP);
			System.out.println("Displayed Country: " + country);
			if (!contactFieldValue[5].equals(country)) {
				throw new KDTValidationException("Country is not same as entered");
			}

			System.out.println("Parsed State/Province: " + contactFieldValue[6]);
			String stateProvince = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Mailing State/Province", "value", CURR_APP);
			System.out.println("Saved State/Province: " + stateProvince);
			if (!contactFieldValue[6].equals(stateProvince)) {
				throw new KDTValidationException("State/Province is not same as entered");
			}

		}
	}

	public static class EnterContactMandatoryDetailsForExistingAccount extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			contactInfo = args.get("ContactInfo").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			contactName = contactInfo[0] + " " + contactInfo[1];
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name",
					contactInfo[0], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Last Name",
					contactInfo[1], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Relationship", contactInfo[6], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Email", contactInfo[2],
					CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Mobile",
					contactInfo[7], CURR_APP);
			eo.wait(1);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Job Title",
					contactInfo[4], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Buyer Role", contactInfo[5], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Persona", contactInfo[8], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithTextAreaXpath", "fieldName",
					"Status / Next Actions", contactInfo[9], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Primary offering", contactInfo[10], CURR_APP);

			// validation of Address details inside contact page

			System.out.println("Account Name parsed from Account: " + accountName);
			String account = eo.getAttributeAfterReplacingValue(driver, "xpath", "InputFieldWithMandateXpath",
					"fieldName", "Account Name", "value", CURR_APP);
			System.out.println("Accountname displaying: " + account);
			if (!accountName.equals(account)) {
				throw new KDTValidationException("Account name is not same as entered");
			}
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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

			// validations after saving contact details
			System.out.println("Entered Relationship is: " + contactInfo[6]);
			String relationship = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Relationship", CURR_APP);
			System.out.println("Saved Relationship: " + relationship);
			if (!contactInfo[6].equals(relationship)) {
				throw new KDTValidationException("Relationship is not same as entered");
			}

			System.out.println("Entered Email is: " + contactInfo[2]);
			String email = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName", "Email",
					CURR_APP);
			System.out.println("Saved Email: " + email);
			if (!contactInfo[2].equals(email)) {
				throw new KDTValidationException("Email is not same as entered");
			}

			System.out.println("Entered Mobile is: " + contactInfo[7]);
			String mobile = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName", "Mobile",
					CURR_APP);
			System.out.println("Saved Mobile: " + mobile);
			/*
			 * if (!contactInfo[7].equals(mobile)) { throw new
			 * KDTValidationException("Mobile is not same as entered"); }
			 */

			System.out.println("Entered Job Title is: " + contactInfo[4]);
			String jobTitle = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Job Title", CURR_APP);
			System.out.println("Saved Job Title: " + jobTitle);
			if (!contactInfo[4].equals(jobTitle)) {
				throw new KDTValidationException("Job Title is not same as entered");
			}

			System.out.println("Entered Buyer Role is: " + contactInfo[5]);
			String buyerRole = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Buyer Role", CURR_APP);
			System.out.println("Saved Buyer Role: " + buyerRole);
			if (!contactInfo[5].equals(buyerRole)) {
				throw new KDTValidationException("Buyer Role  is not same as entered");
			}

			System.out.println("Entered Persona: " + contactInfo[8]);
			String persona = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo", "fieldName",
					"Persona", CURR_APP);
			System.out.println("Saved Persona: " + persona);
			if (!contactInfo[8].equals(persona)) {
				throw new KDTValidationException("Persona is not same as entered");
			}

			System.out.println("Entered Status / Next Actions is: " + contactInfo[9]);
			String statusNextActions = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Status / Next Actions", CURR_APP);
			System.out.println("Saved Status / Next Actions: " + statusNextActions);
			if (!contactInfo[9].equals(statusNextActions)) {
				throw new KDTValidationException("Status / Next Actions is not same as entered");
			}

			System.out.println("Entered Primary offering: " + contactInfo[10]);
			String primaryOffering = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Primary offering", CURR_APP);
			System.out.println("Saved Primary offering: " + primaryOffering);
			if (!contactInfo[10].equals(primaryOffering)) {
				throw new KDTValidationException("Primary Offering is not same as entered");
			}

			this.addComment("Entered Contact details");

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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

			if (eo.isDisplayed(driver, "xpath", "contactsTab", CURR_APP)) {
				eo.actionClick(driver, "xpath", "contactsTab", "Contacts tab", CURR_APP);
			} else {
				eo.actionClick(driver, "xpath", "allTab", "All Tab", CURR_APP);
				eo.actionClick(driver, "xpath", "allTab_contactsTab", "Contacts tab", CURR_APP);

			}

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

	public static class EditContactFromAccount extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Contacts",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Contacts",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickContactLinkFromTable", CURR_APP);
			eo.wait(5);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			String[] name = contactName.split(" ");
			contactName = name[0].trim() + "Updated";
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "First Name",
					contactName, CURR_APP);
			contactName = contactName + " " + name[1].trim();
			this.addComment("Edited the Contact details.");
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SelectOpportunitiesRecordType extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			// accountName = "Automation_14400";
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(3);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Accounts",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickAccountLinkFromTable", CURR_APP);
			eo.wait(15);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Opportunities",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Opportunities",
					CURR_APP);

			eo.clickElement(driver, "xpath", "newOpportunityButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);
			/*
			 * eo.waitForWebElementVisible(driver, "cssselector", "saveAndAddProductButton",
			 * waitTime, "Save and Product Selection button", CURR_APP);
			 */
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterOpportunitiesMandatoryDetails extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			oppMandatoryFieldValue = args.get("OpportunitiesFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			opportunityName = eo.getAttributeAfterReplacingValue(driver, "xpath", "inputFiledXpath", "fieldName",
					"Opportunity Name", "value", CURR_APP);

			driver.navigate().to(
					"https://genesys--qa.my.salesforce.com/0061100000LqRoEAAV/e?id=0061100000LqRoEAAV&nooverride=1&retURL=%2F0061100000LqRoEAAV");
			eo.wait(5);
			// End User Country Lookup
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName",
					"End User Country", CURR_APP);
			String parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			List<WebElement> listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", oppMandatoryFieldValue[5],
					"Searching End User Country", CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);
			eo.wait(3);
			driver.switchTo().defaultContent();

			driver.switchTo().frame(listFrames.get(1));
			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			driver.switchTo().window(parentWindow);

			// Sold To/Business Partner Lookup
			/*
			 * eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage",
			 * "fieldName", "Sold To/Business Partner", CURR_APP); String parentWindow =
			 * eo.switchWindow(driver); System.out.println(parentWindow +
			 * "is the Parent window");
			 * 
			 * List<WebElement> listFrames = driver.findElements(By.tagName("frame"));
			 * System.out.println("number of frames" + listFrames.size());
			 * driver.switchTo().frame(listFrames.get(0)); eo.wait(1); eo.enterText(driver,
			 * "cssselector", "searchCountry", accountName,
			 * "Searching Sold To Business Partner", CURR_APP); eo.clickElement(driver,
			 * "xpath", "goButton", CURR_APP); eo.wait(3);
			 * driver.switchTo().defaultContent();
			 * 
			 * driver.switchTo().frame(listFrames.get(1)); eo.clickElement(driver, "xpath",
			 * "showAllResultsLink", CURR_APP); eo.clickElement(driver, "xpath",
			 * "selectFirstItemLink", CURR_APP); driver.switchTo().window(parentWindow);
			 */
			// Sold to country lookup

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName", "Sold To Country",
					CURR_APP);
			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", oppMandatoryFieldValue[5], "Searching Country",
					CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);
			eo.wait(3);
			driver.switchTo().defaultContent();

			driver.switchTo().frame(listFrames.get(1));
			eo.clickElement(driver, "xpath", "selectFirstItem", CURR_APP);
			driver.switchTo().window(parentWindow);

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Opportunity Source", oppMandatoryFieldValue[4], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Direct/Indirect Sale", oppMandatoryFieldValue[6], CURR_APP);

			// PriceBook Lookup
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName", "Price Book",
					CURR_APP);
			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", oppMandatoryFieldValue[11], "Searching Country",
					CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);
			eo.wait(3);
			driver.switchTo().defaultContent();

			driver.switchTo().frame(listFrames.get(1));
			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			driver.switchTo().window(parentWindow);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Close Date",
					calanderHandlerPickCurrentDate(), CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Stage", oppMandatoryFieldValue[2], CURR_APP);

			// Primary Contact Lookup
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "generalLookupImage", "fieldName",
					"Primary Contact Lookup", CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName",
					"Primary Contact Lookup", CURR_APP);
			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(1));
			eo.waitForWebElementVisible(driver, "xpath", "selectFirstItemLink", waitTime, "Primary contact lookup",
					CURR_APP);
			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			driver.switchTo().window(parentWindow);

			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Type", oppMandatoryFieldValue[7], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Lead Platform", oppMandatoryFieldValue[8], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Offering Detail", oppMandatoryFieldValue[10], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Platform Type", oppMandatoryFieldValue[9], CURR_APP);
			// Bill To Look up
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName", "Bill To Account",
					CURR_APP);
			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");
			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", accountName, "Searching Account", CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);
			eo.wait(3);
			driver.switchTo().defaultContent();

			driver.switchTo().frame(listFrames.get(1));
			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			driver.switchTo().window(parentWindow);

			// Ship To Lookup
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "generalLookupImage", "fieldName", "Ship To Account",
					CURR_APP);
			parentWindow = eo.switchWindow(driver);
			System.out.println(parentWindow + "is the Parent window");

			listFrames = driver.findElements(By.tagName("frame"));
			System.out.println("number of frames" + listFrames.size());
			driver.switchTo().frame(listFrames.get(0));
			eo.wait(1);
			eo.enterText(driver, "cssselector", "searchCountry", accountName, "Searching Account", CURR_APP);
			eo.clickElement(driver, "xpath", "goButton", CURR_APP);
			eo.wait(3);
			driver.switchTo().defaultContent();

			driver.switchTo().frame(listFrames.get(1));
			eo.clickElement(driver, "xpath", "selectFirstItemLink", CURR_APP);
			driver.switchTo().window(parentWindow);

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			/*
			 * eo.waitForWebElementVisible(driver, "id", "priceBookDDL", waitTime,
			 * "PriceBook", CURR_APP); eo.clickElement(driver, "cssselector",
			 * "cancelButton", CURR_APP); this.addComment("Cancel Price Book");
			 */
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit button", CURR_APP);
			System.out.println("Entered Opportunity Name is " + opportunityName);
			String createdOpportunity = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Opportunity Name", CURR_APP).trim();
			System.out.println("Created Opportunity Name: " + createdOpportunity);
			if (!opportunityName.equals(createdOpportunity)) {
				throw new KDTValidationException("Opportunity Name did not match");
			}
			System.out.println("Entered Stage is " + oppMandatoryFieldValue[2]);
			String createdStage = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Stage", CURR_APP);
			System.out.println("Created Stage: " + createdStage);
			if (!oppMandatoryFieldValue[2].equals(createdStage)) {
				throw new KDTValidationException("Stage Value did not match");
			}

			System.out.println("Selected Sold To/Business Partner is: " + accountName);
			String soldToBusinessPartner = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo",
					"fieldName", "Sold To/Business Partner", CURR_APP).trim();
			System.out.println("Saved Sold To/Business Partner is: " + soldToBusinessPartner);
			if (!accountName.equals(soldToBusinessPartner)) {
				throw new KDTValidationException("Sold To/Business Partner Value did not match");
			}

			System.out.println("Selected Sold To Country: " + oppMandatoryFieldValue[5]);
			String soldToCountry = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Sold To Country", CURR_APP);
			System.out.println("Saved Sold To/Business Partner is: " + soldToCountry);
			if (!oppMandatoryFieldValue[5].equals(soldToCountry)) {
				throw new KDTValidationException("Sold To Country Value did not match");
			}

			System.out.println("Selected Opportunity Source is: " + oppMandatoryFieldValue[4]);
			String opportunitySource = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo",
					"fieldName", "Opportunity Source", CURR_APP);
			System.out.println("Saved Opportunity Source is: " + opportunitySource);
			if (!oppMandatoryFieldValue[4].equals(opportunitySource)) {
				throw new KDTValidationException("Opportunity Source Value did not match");
			}

			System.out.println("Selected Direct/Indirect Sale: " + oppMandatoryFieldValue[6]);
			String directIndirectSale = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Direct/Indirect Sale", CURR_APP);
			System.out.println("Saved Direct/Indirect Sale is: " + directIndirectSale);
			if (!oppMandatoryFieldValue[6].equals(directIndirectSale)) {
				throw new KDTValidationException("Direct/Indirect Sale value did not match");
			}

			System.out.println("Selected Type is: " + oppMandatoryFieldValue[7]);
			String type = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName", "Type",
					CURR_APP);
			System.out.println("Saved Type is: " + type);
			if (!oppMandatoryFieldValue[7].equals(type)) {
				throw new KDTValidationException("Type value did not match");
			}

			System.out.println("Selected Bill To is: " + accountName);
			String billTo = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Bill To Account", CURR_APP);
			System.out.println("Saved Bill To is: " + billTo);
			if (!accountName.equals(billTo)) {
				throw new KDTValidationException("Bill To value did not match");
			}

			System.out.println("Selected Ship To Name is: " + accountName);
			String shipToName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Ship To Account", CURR_APP);
			System.out.println("Saved Ship To Name is: " + shipToName);
			if (!accountName.equals(shipToName)) {
				throw new KDTValidationException("Ship To Name value did not match");
			}

			System.out.println("Selected Lead Platform is: " + oppMandatoryFieldValue[8]);
			String leadPlatform = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo", "fieldName",
					"Lead Platform", CURR_APP);
			System.out.println("Saved Lead Platform is: " + leadPlatform);
			if (!oppMandatoryFieldValue[8].equals(leadPlatform)) {
				throw new KDTValidationException("Lead Platform Value did not match");
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			// accountName = "Update_Update_Automation_16220";
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(3);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Opportunities",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Opportunities",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickOpportunitiesLinkFromTable", CURR_APP);
			eo.wait(5);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);

			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Opportunity Name",
					CURR_APP);
			opportunityName = opportunityName + " Updated";
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
					"Opportunity Name", CURR_APP);
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			if (eo.isDisplayed(driver, "id", "recordTypeDDL", CURR_APP)) {
				eo.selectComboBoxByVisibleText(driver, "id", "recordTypeDDL", recordType, CURR_APP);
				eo.clickElement(driver, "cssselector", "continueButton", CURR_APP);
				eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);
				this.addComment("Selected Record Type as " + recordType);
			}
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			// accountName = "Automation_14400";
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", opportunityName, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Opportunities",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Opportunities",
					CURR_APP);
			eo.clickElement(driver, "xpath", "clickOpportunitiesLinkFromTable", CURR_APP);
			eo.wait(7);
			eo.clickElement(driver, "xpath", "newCPQQuoteButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save button", CURR_APP);

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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class EnterQuoteMandatoryDetails extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			quoteMandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithTextAreaXpath", "fieldName",
					"Description", quoteMandatoryFieldValue[2], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Order Type",
					quoteMandatoryFieldValue[3], CURR_APP);
			eo.checkCheckBox(driver, "xpath", "primaryCheckBox", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithHelpTextAreaXpath", "fieldName",
					"Deal Justification", quoteMandatoryFieldValue[9], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Initial Subscription Term", quoteMandatoryFieldValue[1], CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName", "Start Date",
					CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName", "Start Date",
					calanderHandlerPickCurrentDate(), CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term", quoteMandatoryFieldValue[4], CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term Start Date", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term Start Date", calanderHandlerPickCurrentDate(), CURR_APP);
			eo.checkCheckBox(driver, "xpath", "docusignCompletedCheckBox", CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Billing Category", CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Billing Category", quoteMandatoryFieldValue[5], CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownFieldWithInfoXpath", "fieldName",
					"Billing Period", CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Billing Period", quoteMandatoryFieldValue[0], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Payment Terms", quoteMandatoryFieldValue[6], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithMandateXpath",
					"fieldName", "Purchase Order is Required", quoteMandatoryFieldValue[7], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Is This Customer Tax Exempt?", quoteMandatoryFieldValue[8], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO#",
					eo.gnerateRandomNo(6), CURR_APP);
			eo.wait(2);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO Date",
					calanderHandlerPickCurrentDate(), CURR_APP);

			eo.wait(2);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO Received Date",
					calanderHandlerPickCurrentDate(), CURR_APP);
			eo.wait(2);
			this.addComment("Entered Quote details");
		}
	}

	public static class EnterQuoteMandatoryDetailsForRAMP extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			quoteMandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			driver.navigate().to("https://genesys--qa.my.salesforce.com/aQX110000004rOL/e?retURL=%2FaQX110000004rOL");
			eo.wait(3);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithTextAreaXpath", "fieldName",
					"Description", quoteMandatoryFieldValue[2], CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "Order Type",
					quoteMandatoryFieldValue[3], CURR_APP);
			eo.checkCheckBox(driver, "xpath", "primaryCheckBox", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithHelpTextAreaXpath", "fieldName",
					"Deal Justification", quoteMandatoryFieldValue[9], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Initial Subscription Term", quoteMandatoryFieldValue[1], CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName", "Start Date",
					CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName", "Start Date",
					calanderHandlerPickCurrentDate(), CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term", quoteMandatoryFieldValue[4], CURR_APP);
			eo.clearDataAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term Start Date", CURR_APP);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFieldWithInfoXpath", "fieldName",
					"Subscription Term Start Date",
					calanderHandlerPickFuture(Integer.parseInt(quoteMandatoryFieldValue[2])), CURR_APP);
			eo.checkCheckBox(driver, "xpath", "docusignCompletedCheckBox", CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Billing Category", CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Billing Category", quoteMandatoryFieldValue[5], CURR_APP);
			eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "dropdownFieldWithInfoXpath", "fieldName",
					"Billing Period", CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Billing Period", quoteMandatoryFieldValue[0], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithInfoXpath",
					"fieldName", "Payment Terms", quoteMandatoryFieldValue[6], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldWithMandateXpath",
					"fieldName", "Purchase Order is Required", quoteMandatoryFieldValue[7], CURR_APP);
			eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
					"Is This Customer Tax Exempt?", quoteMandatoryFieldValue[8], CURR_APP);

			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO#",
					eo.gnerateRandomNo(6), CURR_APP);
			eo.wait(2);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO Date",
					calanderHandlerPickCurrentDate(), CURR_APP);

			eo.wait(2);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledXpath", "fieldName", "PO Received Date",
					calanderHandlerPickCurrentDate(), CURR_APP);
			eo.wait(2);
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			if (!eo.isExists(driver, "xpath", "quoteHeader", CURR_APP)) {
				throw new KDTValidationException("Quote was not Saved");
			}
			this.addComment("New Quote was Saved successfully");
		}
	}

	public static class AddAccountOrderAddressForQuote extends Keyword {
		private String addressLines[];
		private String aoaValues[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			addressLines = args.get("AddressTypeValues").split("\\|");
			aoaValues = args.get("AOAValues").split("\\|");
		}

		public List<String> getElementsByXPath(WebDriver driver, String xpath) {
			try {
				List<WebElement> eles = driver.findElements(By.xpath(xpath));

				if (null != eles) {
					return eles.stream().map(ele -> ele.getText()).collect(Collectors.toList());
				}
			} catch (Exception e) {
				System.out.print("No Elements found" + xpath);
			}
			return null;
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			driver.navigate().to("https://genesys--qa.my.salesforce.com/aQX110000004rOL");
			// accountName="Update_Update_Automation_10143";
			eo.wait(10);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText",
					"Account - Order Addresses", CURR_APP);
			eo.clickElement(driver, "xpath", "createNewAddressButton", CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "buttonInAOA", "buttonName", "Add Selected",
					waitTime, CURR_APP);
			List<String> existingRecords = getElementsByXPath(driver, "//table[@class='list']/tbody/tr/td[2]/label");
			for (int i = 0; i < addressLines.length; i++) {
				if (existingRecords != null && existingRecords.contains(addressLines[i])) {
					continue;
				}
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAOA", "buttonName",
						"Create New Address", CURR_APP);
				eo.selectRadioButton(driver, "xpath", "endUserRadioButton", CURR_APP);
				eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath", "dropdownFieldXpath", "fieldName",
						"Type", addressLines[i], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "Address Line 1",
						aoaValues[0], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "Address Line 2",
						aoaValues[1], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "City",
						aoaValues[2], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "State",
						aoaValues[3], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "Country",
						aoaValues[4], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "controlsInAOAPage", "fieldName", "Zipcode",
						aoaValues[5], CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "bottomButton", "buttonName",
						"Create New Address", CURR_APP);

				Alert alert = driver.switchTo().alert();
				alert.accept();

				try {
					String error = eo.getText(driver, "xpath", "errorMessage", CURR_APP);

					if (error != null) {
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "bottomButton", "buttonName", "Cancel",
								CURR_APP);
						eo.wait(2);
						continue;
					}
				} catch (KDTKeywordExecException kdt) {
					System.out.print("Address Type added succssfully! so no error message!!");
				}
				eo.wait(2);
			}

			List<String> accountTypeList = new ArrayList<>();

			List<WebElement> existingAddressRows = eo.getListOfWebElements(driver, "xpath", "allCheckBoxes", CURR_APP);
			List<String> emailMandatory = new ArrayList<>();
			emailMandatory.add("Bill To");
			emailMandatory.add("Ship To");
			emailMandatory.add("Activation Email");
			emailMandatory.add("Order Acknowledgement");
			List<String> nameMandatory = new ArrayList<>();
			nameMandatory.add("Bill To");
			nameMandatory.add("Ship To");
			nameMandatory.add("End User");
			for (int row = existingAddressRows.size(); row > 0; row--) {

				try {
					String addressType = eo.getTextAfterReplacingKeyValue(driver, "xpath", "addressType", "index",
							String.valueOf(row), CURR_APP);

					if (!accountTypeList.contains(addressType)) {

						if (nameMandatory.contains(addressType)) {
							eo.enterTextAfterReplacingKeyValue(driver, "xpath", "mandatoryFirstName", "index",
									String.valueOf(row), "John", CURR_APP);
							eo.wait(2);
							eo.enterTextAfterReplacingKeyValue(driver, "xpath", "mandatoryLastName", "index",
									String.valueOf(row), "Doe", CURR_APP);
							eo.wait(2);
						}

						if (emailMandatory.contains(addressType)) {
							eo.enterTextAfterReplacingKeyValue(driver, "xpath", "mandatoryEmail", "index",
									String.valueOf(row), aoaValues[6], CURR_APP);
							eo.wait(2);
						}

						if ("Ship To".equals(addressType)) {
							eo.selectComboBoxByVisibleTextAfterReplacingKeyValue(driver, "xpath",
									"shippingMethodDropdown", "index", String.valueOf(row), aoaValues[7], CURR_APP);
							eo.wait(2);
						}
						accountTypeList.add(addressType);
						eo.checkCheckBoxAfterReplacingKeyValue(driver, "xpath", "selectedCheckbox", "index",
								String.valueOf(row), CURR_APP);

						eo.wait(2);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAOA", "buttonName", "Add Selected",
					CURR_APP);
			eo.wait(5);

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAOA", "buttonName", "Return to Quote",
					CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText",
					"Account - Order Addresses", waitTime, CURR_APP);
			this.addComment("Entered Address details ");
		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name: </i>NavigateToEditLineItemsPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to Save Quote
	 * details.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class NavigateToEditLineItemsPage extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.wait(5);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.clickElement(driver, "xpath", "editLinesBtn", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "buttonInAddProdcut", "buttonName", "Add Products",
					waitTime, CURR_APP);
			this.addComment("User navigated to Edit Line items page of the Quote");
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
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
			/*
			 * eo.wait(2); if (eo.isDisplayed(driver, "xpath", "savePriceBook", CURR_APP)) {
			 * System.out.println("Saving Price book!"); eo.clickElement(driver, "xpath",
			 * "savePriceBook", CURR_APP); eo.invisibilityOfElement(driver, "cssselector",
			 * "loadingImage", CURR_APP, waitTime); }
			 */
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName", "Add Products",
					CURR_APP);
			eo.wait(2);
			eo.clickElement(driver, "xpath", "closeGuidedSellingIcon", CURR_APP);
			eo.wait(1);
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
	 * <b><i>Keyword Name: </i>SearchAndAddProduct</b>
	 * </p>
	 * <p>
	 * <b><i>Description: </i></b> <i>This Keyword is used to add first Product to
	 * Quote.</i>
	 * </p>
	 * <p>
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */
	public static class SearchAndAddOneHardwareProduct extends Keyword {
		private String subscriptionTerm;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			subscriptionTerm = args.get("QuoteFieldValues").split("\\|")[5];
			productsData = args.get("HardwareProd1").split("\\|");

			for (String product : productsData) {
				String[] prd = product.split("@");
				products.put(prd[0], prd[1]);
			}
			productMandatoryFieldValue = args.get("HardwareProd1MandateFieldValues").split("\\|");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(15);
			// Adding Multiple products
			for (Map.Entry<String, String> entry : products.entrySet()) {
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName",
						"Add Products", CURR_APP);
				eo.wait(11);
				eo.waitForWebElementVisible(driver, "xpath", "firstProductCheckbox", waitTime, "First Product Checkbox",
						CURR_APP);
				eo.enterText(driver, "xpath", "searchTextbox", entry.getKey(),
						"Search product Page - '" + entry.getKey() + "' - ", CURR_APP);
				eo.clickElement(driver, "xpath", "searchButton", CURR_APP);
				eo.wait(10);

				/*
				 * if (eo.isDisplayed(driver, "xpath", "foundMoreThanOneSearchResult", CURR_APP)
				 * == false) { eo.clickElement(driver, "xpath", "selectSearchedProductCheckbox",
				 * CURR_APP); }
				 */
				eo.clickElement(driver, "xpath", "selectSearchedProductCheckbox", CURR_APP);
				eo.wait(2);

				eo.clickElement(driver, "xpath", "selectButton", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
				eo.wait(6);
			}

			List<WebElement> productrows = eo.returnWebElements(driver, "xpath", "allProductRows", CURR_APP);
			// Enter Start date, quantity, additional discount and subscription term
			for (int i = 0; i < productrows.size(); i++) {

				String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "productName", "index",
						String.valueOf(i + 1), CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
				eo.actionMoveToElementAfterReplace(driver, "xpath", "startDateBox", "index", String.valueOf(i + 1),
						CURR_APP);
				String date = eo.getTextAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
						String.valueOf(i + 1), CURR_APP);

				if (null == date || "".equals(date)) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "startDateTextBox", calanderHandlerPickCurrentDate(), "Start Date - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "quantityBox", "index", String.valueOf(i + 1),
							CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.wait(1);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "quanitytTextbox", products.get(productName), "Quantity - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "additionalProdDiscTextbox", productMandatoryFieldValue[0],
							"Additional Product Discount - ", CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "subscriptionTextbox", subscriptionTerm, "Subscription - ", CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);
				}
				eo.wait(2);
			}
			eo.clickElement(driver, "xpath", "saveQuoteLineButton", CURR_APP);
			eo.wait(10);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Quote Lines",
					waitTime, CURR_APP);
			this.addComment("Quote Lines are added successfully.");
		}
	}

	public static class SearchAndAddProducts extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");

			for (String product : productsData) {
				String[] prd = product.split("@");
				products.put(prd[0], prd[1]);
				singleProducts.put(prd[0], prd[1]);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			driver.navigate().to(
					"https://genesys--qa--sbqq.visualforce.com/apex/sb?scontrolCaching=1&id=aQX110000004rOL#quote/le?qId=aQX110000004rOL");
			eo.wait(11);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName", "Add Products",
					CURR_APP);
			eo.wait(11);
			eo.waitForWebElementVisible(driver, "xpath", "firstProductCheckbox", waitTime, "First Product Checkbox",
					CURR_APP);
			// Adding Multiple products
			for (Map.Entry<String, String> entry : singleProducts.entrySet()) {

				eo.enterText(driver, "xpath", "searchTextbox", entry.getKey(),
						"Search product Page - '" + entry.getKey() + "' - ", CURR_APP);
				eo.clickElement(driver, "xpath", "searchButton", CURR_APP);
				eo.wait(10);
				eo.clickElement(driver, "xpath", "selectSearchedProductCheckbox", CURR_APP);
				eo.wait(2);

				eo.clickElement(driver, "xpath", "selectAndAddMoreButton", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "selectButton", waitTime, "Select Button", CURR_APP);
				eo.clearData(driver, "xpath", "searchTextbox", CURR_APP);
			}

			eo.clickElement(driver, "xpath", "selectButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
			eo.wait(6);
			this.addComment("Products added successfully.");
		}
	}

	public static class SearchAndAddWrapperProduct extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("BundleProduct1").split("\\|");

			for (String product : productsData) {
				String[] prd = product.split("@");
				wrapperProducts.put(prd[0], prd[1]);
				products.put(prd[0], prd[1]);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(15);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName", "Add Products",
					CURR_APP);
			eo.wait(11);
			eo.waitForWebElementVisible(driver, "xpath", "firstProductCheckbox", waitTime, "First Product Checkbox",
					CURR_APP);
			// Adding Multiple products
			for (Map.Entry<String, String> entry : wrapperProducts.entrySet()) {

				eo.enterText(driver, "xpath", "searchTextbox", entry.getKey(),
						"Search product Page - '" + entry.getKey() + "' - ", CURR_APP);
				eo.wait(2);
				eo.actionClickAfterReplacingKeyValue(driver, "xpath", "productDiv", "wrapperProductName",
						entry.getKey(), CURR_APP);
				// eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productDiv",
				// "wrapperProductName", entry.getKey(), CURR_APP);
				eo.clickElement(driver, "xpath", "selectSearchedProductCheckbox", CURR_APP);
				eo.wait(2);

				eo.clickElement(driver, "xpath", "selectAndAddMoreButton", CURR_APP);
				eo.wait(10);

				if (eo.isDisplayed(driver, "xpath", "inlineSaveButton", CURR_APP)) {
					eo.clickElement(driver, "xpath", "inlineSaveButton", CURR_APP);
					eo.waitForWebElementVisible(driver, "xpath", "selectButton", waitTime, "Select Button", CURR_APP);
				}

				eo.clearData(driver, "xpath", "searchTextbox", CURR_APP);
			}
			eo.wait(7);
			eo.clickElement(driver, "xpath", "selectButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
			eo.wait(6);
			this.addComment("Wrapper products added successfully.");
		}
	}

	public static class EnterValuesToTheProducts extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");

			for (String product : productsData) {
				String[] prd = product.split("@");
				wrapperProducts.put(prd[0], prd[1]);
				products.put(prd[0], prd[1]);
			}
			quoteMandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
			productMandatoryFieldValue = args.get("HardwareProd1MandateFieldValues").split("\\|");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(15);
			// Adding Multiple products

			List<WebElement> productrows = eo.returnWebElements(driver, "xpath", "allProductRows", CURR_APP);
			// Enter Start date, quantity, additional discount and subscription term
			for (int i = 0; i < productrows.size(); i++) {

				String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "productName", "index",
						String.valueOf(i + 1), CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
				eo.actionMoveToElementAfterReplace(driver, "xpath", "startDateBox", "index", String.valueOf(i + 1),
						CURR_APP);
				String date = eo.getTextAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
						String.valueOf(i + 1), CURR_APP);

				if (null == date || "".equals(date)) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "startDateTextBox", calanderHandlerPickCurrentDate(), "Start Date - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "quantityBox", "index", String.valueOf(i + 1),
							CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.wait(1);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "quanitytTextbox", products.get(productName), "Quantity - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "additionalProdDiscTextbox", productMandatoryFieldValue[0],
							"Additional Product Discount - ", CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "subscriptionTextbox", quoteMandatoryFieldValue[4], "Subscription - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);
				}
				eo.wait(2);
			}
			eo.clickElement(driver, "xpath", "saveQuoteLineButton", CURR_APP);
			eo.wait(10);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Quote Lines",
					waitTime, CURR_APP);
			this.addComment("Quote Lines are added successfully.");
		}
	}

	public static class EnterValuesToTheProductsForRamp extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");

			for (String product : productsData) {
				String[] prd = product.split("@");
				wrapperProducts.put(prd[0], prd[1]);
				products.put(prd[0], prd[1]);
			}
			quoteMandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
			productMandatoryFieldValue = args.get("HardwareProd1MandateFieldValues").split("\\|");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(15);
			// Adding Multiple products

			List<WebElement> productrows = eo.returnWebElements(driver, "xpath", "allProductRows", CURR_APP);
			// Enter Start date, quantity, additional discount and subscription term
			for (int i = 0; i < productrows.size(); i++) {

				String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "productName", "index",
						String.valueOf(i + 1), CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save button", CURR_APP);
				eo.actionMoveToElementAfterReplace(driver, "xpath", "startDateBox", "index", String.valueOf(i + 1),
						CURR_APP);
				String date = eo.getTextAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
						String.valueOf(i + 1), CURR_APP);

				if (null == date || "".equals(date)) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "startDateBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "startDateTextBox",
							calanderHandlerPickFuture(Integer.parseInt(productMandatoryFieldValue[2])), "Start Date - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "quantityBox", "index", String.valueOf(i + 1),
							CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.wait(1);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "quantityBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "quanitytTextbox", products.get(productName), "Quantity - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "additionalProdDiscBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "additionalProdDiscTextbox", productMandatoryFieldValue[0],
							"Additional Product Discount - ", CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "subscriptionBox", "index",
							String.valueOf(i + 1), CURR_APP);
					eo.enterText(driver, "xpath", "subscriptionTextbox", quoteMandatoryFieldValue[4], "Subscription - ",
							CURR_APP);
					eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);
				}
				eo.wait(2);
			}
			eo.clickElement(driver, "xpath", "saveQuoteLineButton", CURR_APP);
			eo.wait(10);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Quote Lines",
					waitTime, CURR_APP);
			this.addComment("Quote Lines are added successfully.");
		}
	}

	public static class ConfigureProducts extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");
			for (String product : productsData) {
				String[] prd = product.split("@");
				wrapperProducts.put(prd[0], prd[1]);
				products.put(prd[0], prd[1]);
			}
			quoteMandatoryFieldValue = args.get("QuoteFieldValues").split("\\|");
			productMandatoryFieldValue = args.get("HardwareProd1MandateFieldValues").split("\\|");
			subProducts = args.get("WrapperSubProducts").split("\\|");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(15);

			eo.clickElement(driver, "xpath", "configureIcon", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "inlineSaveButton", waitTime, "Save Button", CURR_APP);

			List<WebElement> checkBoxEnabledRows = eo.returnWebElements(driver, "xpath", "enabledCheckboxRows",
					CURR_APP);

			for (int i = 1; i <= checkBoxEnabledRows.size(); i++) {
				// eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath",
				// "enabledCheckBox", "index",
				// String.valueOf(i), CURR_APP);
				eo.checkCheckBoxAfterReplacingKeyValue(driver, "xpath", "enabledCheckBox", "index", String.valueOf(i),
						CURR_APP);
				eo.wait(1);
			}

			eo.clickElement(driver, "xpath", "inlineSaveButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "saveQuoteLineButton", waitTime, "Save Button", CURR_APP);

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "expandButton", "wrapperProduct",
					"PureCloud 2 User - Wrapper", CURR_APP);
			eo.wait(2);

			for (int i = 0; i < subProducts.length; i++) {
				eo.actionMoveToElementAfterReplace(driver, "xpath", "subProductQuantityDiv", "productName",
						subProducts[i], CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subProductQuantityDiv", "productName",
						subProducts[i], CURR_APP);
				eo.wait(1);
				eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "subProductQuantityDiv", "productName",
						subProducts[i], CURR_APP);
				eo.clearDataAfterReplacingKeyValue(driver, "xpath", "subProductQuantityTextBox", "productName",
						subProducts[i], CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "editQuantityIcon", "productName",
						subProducts[i], CURR_APP);
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "subProductQuantityTextBox", "productName",
						subProducts[i], productMandatoryFieldValue[3], CURR_APP);
				eo.clickElement(driver, "xpath", "quoteTitleText", CURR_APP);
			}
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "expandButton", "wrapperProduct",
					"PureCloud 2 User - Wrapper", CURR_APP);
			/*
			 * eo.clickElement(driver, "xpath", "saveQuoteLineButton", CURR_APP);
			 * eo.wait(10);
			 */
			this.addComment("Product Configuration done successfully.");
		}
	}

	public static class ValidateProductsInQuoteLines extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");

			if (products.isEmpty()) {
				for (String product : productsData) {
					String[] data = product.split("@");
					products.put(data[0], data[1]);
				}
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException, KDTValidationException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			// driver.navigate().to("https://genesys--qa.my.salesforce.com/aQX110000004jHR?srPos=0&srKp=aQX");
			eo.wait(6);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Quote Lines", CURR_APP);
			eo.wait(2);

			if (products == null || products.isEmpty()) {
				throw new KDTValidationException("Quote Lines should not be empty or null");
			}

			Map<String, String> productNames = new HashMap<String, String>();

			if (!eo.isDisplayedAfterReplace(driver, "xpath", "gotoListLinkForAllLists", "headerText", "Quote Lines",
					CURR_APP)) {

				List<WebElement> quoteOrderProducts = eo.getListOfWebElements(driver, "xpath",
						"totalQuoteLinesWithoutGotoLink", CURR_APP);
				for (int i = 2; i <= quoteOrderProducts.size(); i++) {

					String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"productNameWithoutGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quoteLineNameWithoutGotoLinkInQuoteLines",
							"index", String.valueOf(i), CURR_APP);
					eo.wait(2);
					String productQty = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo",
							"fieldName", "Quantity", CURR_APP);
					productNames.put(productName, productQty);
					driver.navigate().back();
					String unitPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"unitPriceWithoutGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);
					unitPrice = unitPrice.split(" ")[1];
					String netTotal = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"netTotalWithoutGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);
					netTotal = netTotal.split(" ")[1];
					netTotal = netTotal.replace(",", "");
					Double calculatedTotal = Double.parseDouble(unitPrice) * Double.parseDouble(productQty);
					String calculatedTotalPrice = String.format("%.2f", calculatedTotal);
					System.out.println("Product Name: " + productName + " and Net Total: " + netTotal);
					if (!netTotal.equals(calculatedTotalPrice)) {
						throw new KDTValidationException("Net Total Price didnot match");

					}

				}

			} else {

				// clicking on go to list of order products //
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "gotoListLinkForAllLists", "headerText",
						"Quote Lines", CURR_APP);
				eo.wait(2);

				if (products == null || products.isEmpty()) {
					throw new KDTValidationException("Quote Lines should not be empty or null");
				}

				List<WebElement> orderProducts = eo.getListOfWebElements(driver, "xpath",
						"totalQuoteLinesWithGotoLinkInQuoteLines", CURR_APP);
				for (int i = 2; i <= orderProducts.size(); i++) {

					String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"productNameWithGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quoteLineNameWithGotoLinkInQuoteLines",
							"index", String.valueOf(i), CURR_APP);
					eo.wait(2);
					String productQty = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldTextInfo",
							"fieldName", "Quantity", CURR_APP);
					productNames.put(productName, productQty);
					driver.navigate().back();
					String unitPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"unitPriceWithGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);
					unitPrice = unitPrice.split(" ")[1];
					String netTotal = eo.getTextAfterReplacingKeyValue(driver, "xpath",
							"netTotalWithGotoLinkInQuoteLines", "index", String.valueOf(i), CURR_APP);
					netTotal = netTotal.split(" ")[1];
					netTotal = netTotal.replace(",", "");
					Double calculatedTotal = Double.parseDouble(unitPrice) * Double.parseDouble(productQty);
					String calculatedTotalPrice = String.format("%.2f", calculatedTotal);
					System.out.println("Product Name: " + productName + " and Net Total: " + netTotal);
					if (!netTotal.equals(calculatedTotalPrice)) {
						throw new KDTValidationException("Net Total Price didnot match");

					}

				}

			}

			for (Entry<String, String> obj : products.entrySet()) {

				if (productNames.containsKey(obj.getKey())) {
					System.out.println("Actual Product is: " + obj.getKey());
					System.out.println("Actual Quantity is: " + obj.getValue());
					System.out.println("Quantity in Quote lines is:" + productNames.get(obj.getKey()));

					if (!productNames.get(obj.getKey()).equals(obj.getValue())) {
						throw new KDTValidationException("Quantity didnot match");
					}

				} else {

					throw new KDTValidationException("Quote line didnot match");
				}
			}
			driver.navigate().back();
			eo.waitForWebElementVisible(driver, "xpath", "submitForApprovalButton", waitTime,
					"Submit For Approval button", CURR_APP);
		}

	}

	public static class ValidateTotalAmountAfterCalculate extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.actionMoveToElement(driver, "xpath", "quantityBox", CURR_APP);
			eo.clickElement(driver, "xpath", "quantityBox", CURR_APP);
			eo.actionDoubleClick(driver, "xpath", "quantityBox", CURR_APP);
			eo.enterText(driver, "xpath", "quanitytTextbox", mandatoryFieldValue[1], "Quantity - ", CURR_APP);

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName", "Calculate",
					CURR_APP);
			// Net total= qyantity*unit price
			String unitPrice = eo.getText(driver, "xpath", "netUnitPriceBox", CURR_APP);
			String quantity = eo.getText(driver, "xpath", "quantityBox", CURR_APP);
			float total = Float.parseFloat(unitPrice) * Float.parseFloat(quantity);

			String netTotal = eo.getText(driver, "xpath", "netTotalBox", CURR_APP);
			if (Float.toString(total).equals(netTotal)) {
				this.addComment("Amount calculated correctly");
			}

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "buttonInAddProdcut", "buttonName", "Quick Save",
					CURR_APP);
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
	 * <b><i> Authur: Vinay Kumar B M</i></b>
	 * </p>
	 * <p>
	 * <b><i>Version: </i></b> 0.1
	 * </p>
	 * </div>
	 */

	public static class SubmitQuoteForApproval extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			String status = "Approved";
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "quoteLinkFromGlobalSearch", waitTime,
					"Searching for Quotes link", CURR_APP);
			eo.clickElement(driver, "xpath", "quoteLinkFromGlobalSearch", CURR_APP);
			eo.clickElement(driver, "xpath", "clickQuoteLinkFromTable", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "submitForApprovalButton", waitTime,
					"Submit for approval button", CURR_APP);
			eo.clickElement(driver, "xpath", "submitForApprovalButton", CURR_APP);
			eo.wait(10);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Approvals", waitTime,
					CURR_APP);
			System.out.println("Status is " + status);
			String approvalStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Approval Status", CURR_APP);
			System.out.println("Saved Status: " + approvalStatus);
			if (!status.equalsIgnoreCase(approvalStatus)) {
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Approvals",
						CURR_APP);
				eo.wait(1);
				if (!eo.isDisplayedAfterReplace(driver, "xpath", "gotoListLinkForAllLists", "headerText", "Approvals",
						CURR_APP)) {

					// handling table wiithout go to list
					while (true) {
						Boolean noPendingRequests = false;
						List<WebElement> rows = eo.getListOfWebElements(driver, "xpath", "approvalRowsWithoutGotoLink",
								CURR_APP);
						for (int i = 2; i <= rows.size(); i++) {
							String quoteStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath",
									"approvalStatusWithoutGotolink", "index", String.valueOf(i), CURR_APP);

							if (quoteStatus.equals("Requested")) {
								noPendingRequests = true;
								eo.clickElementAfterReplacingKeyValue(driver, "xpath", "approvalNumberWithoutGotoLink",
										"index", String.valueOf(i), CURR_APP);
								eo.waitForWebElementVisible(driver, "xpath", "approveButton", waitTime,
										"Approve button", CURR_APP);
								eo.clickElement(driver, "xpath", "approveButton", CURR_APP);
								eo.waitForWebElementVisible(driver, "xpath", "commentsTextbox", waitTime,
										"Comments text box", CURR_APP);
								eo.enterText(driver, "xpath", "commentsTextbox", "Test", "Comments text box", CURR_APP);
								eo.clickElement(driver, "xpath", "approveButtonInSubmission", CURR_APP);
								eo.wait(10);
								break;
							}
						}

						if (noPendingRequests) {
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText",
									"Approvals", CURR_APP);
							eo.wait(1);
						} else {
							break;
						}
					}

					eo.wait(3);
				} else {
					// handling table with go to list
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "gotoListLinkForAllLists", "headerText",
							"Approvals", CURR_APP);
					while (true) {
						Boolean noPendingRequests = false;
						List<WebElement> rows = eo.getListOfWebElements(driver, "xpath", "approvalRowsWithGotoLink",
								CURR_APP);
						for (int i = 2; i <= rows.size(); i++) {
							String quoteStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath",
									"approvalStatusWithGotolink", "index", String.valueOf(i), CURR_APP);

							if (quoteStatus.equals("Requested")) {
								noPendingRequests = true;
								eo.clickElementAfterReplacingKeyValue(driver, "xpath", "approvalNumberWithGotoLink",
										"index", String.valueOf(i), CURR_APP);
								eo.waitForWebElementVisible(driver, "xpath", "approveButton", waitTime,
										"Approve button", CURR_APP);
								eo.clickElement(driver, "xpath", "approveButton", CURR_APP);
								eo.waitForWebElementVisible(driver, "xpath", "commentsTextbox", waitTime,
										"Comments text box", CURR_APP);
								eo.enterText(driver, "xpath", "commentsTextbox", "Test", "Comments text box", CURR_APP);
								eo.clickElement(driver, "xpath", "approveButtonInSubmission", CURR_APP);
								eo.wait(10);
								break;
							}
						}

						if (noPendingRequests) {
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText",
									"Approvals", CURR_APP);
							eo.wait(1);
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "gotoListLinkForAllLists",
									"headerText", "Approvals", CURR_APP);
						} else {
							driver.navigate().back();
							break;
						}
					}
				}
			}

			approvalStatus = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Approval Status", CURR_APP);
			System.out.println("Final status of the Quote is: " + approvalStatus);
			if (!status.equalsIgnoreCase(approvalStatus)) {
				throw new KDTValidationException("Quote is not Approved");
			}

			this.addComment("Quote Approved successfully");

		}
	}

	public static class GenerateAndActivateOrderForTheQuote extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			// accountName = "Automation_11394";
			eo.wait(3);
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "quoteLinkFromGlobalSearch", waitTime,
					"Searching for Quotes link", CURR_APP);
			eo.clickElement(driver, "xpath", "quoteLinkFromGlobalSearch", CURR_APP);
			eo.clickElement(driver, "xpath", "clickQuoteLinkFromTable", CURR_APP);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Approvals", waitTime,
					CURR_APP);
			quoteNumber = eo.getText(driver, "xpath", "quoteNumber", CURR_APP);

			eo.actionDoubleClickAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Order Management Status", CURR_APP);

			eo.wait(1);
			eo.selectComboBoxByVisibleText(driver, "xpath", "InlineDropdownFieldXpath", "Approved", CURR_APP);

			eo.waitForWebElementVisible(driver, "name", "InlineSaveButton", waitTime, "Save Button", CURR_APP);
			eo.clickElement(driver, "name", "InlineSaveButton", CURR_APP);

			if (eo.isDisplayed(driver, "xpath", "InlineErrorMessage", CURR_APP)) {
				driver.navigate().refresh();
				eo.clickElement(driver, "name", "InlineSaveButton", CURR_APP);
			}
			eo.wait(30);
			driver.navigate().refresh();

			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Orders", CURR_APP);
			eo.wait(2);
			// clicking on Order number from Orders table
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "clickOnLinkFromTheTable", "headerText", "Orders",
					CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Order Products",
					waitTime, CURR_APP);
			// Get Order number
			orderHeader = eo.getText(driver, "xpath", "orderHeader", CURR_APP);
			orderNumber = orderHeader.split(" ")[1];
			System.out.println("Order number is: " + orderNumber);
			// validate quote number on order
			System.out.println("Actual Quote Number is: " + quoteNumber);
			String quoteNumberOnOrder = eo.getText(driver, "xpath", "quoteNumberOnOrder", CURR_APP);
			System.out.println("Quote Number on Order is: " + quoteNumberOnOrder);

			if (!quoteNumber.equals(quoteNumberOnOrder)) {
				throw new KDTValidationException("Quote number on order didnot match");
			}

			this.addComment("Order Generated and Activated successfully");

		}
	}

	public static class ValidateOrderLinesInOrderProducts extends Keyword {
		private String totalPrice, calculatedTotalPrice;
		private Double calculatedTotal;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			productsData = args.get("HardwareProd1").split("\\|");

			if (products.isEmpty()) {
				for (String product : productsData) {
					String[] data = product.split("@");
					products.put(data[0], data[1]);
				}
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException, KDTValidationException {
			WebDriver driver = context.getWebDriver();
			// driver.navigate().to("https://genesys--qa.my.salesforce.com/80111000002imyF");
			eo.wait(6);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Order Products",
					CURR_APP);
			eo.wait(2);

			if (products == null || products.isEmpty()) {
				throw new KDTValidationException("Order Products should not be empty or null");
			}

			Map<String, String> productNames = new HashMap<String, String>();
			if (!eo.isDisplayedAfterReplace(driver, "xpath", "gotoListLinkForAllLists", "headerText", "Order Products",
					CURR_APP)) {

				List<WebElement> quoteOrderProducts = eo.getListOfWebElements(driver, "xpath",
						"totalOrdeProductsWithoutGotoLink", CURR_APP);
				for (int i = 2; i <= quoteOrderProducts.size(); i++) {

					String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "productNameWithoutGotoLink",
							"index", String.valueOf(i), CURR_APP);
					String productQty = eo.getTextAfterReplacingKeyValue(driver, "xpath", "quantityWithoutGotoLink",
							"index", String.valueOf(i), CURR_APP);
					productNames.put(productName, productQty.trim());
					String unitPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath", "unitPriceWithoutGotoLink",
							"index", String.valueOf(i), CURR_APP);
					unitPrice = unitPrice.split(" ")[1];
					totalPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath", "totalPriceWithoutGotoLink", "index",
							String.valueOf(i), CURR_APP);
					totalPrice = totalPrice.split(" ")[1];
					totalPrice = totalPrice.replace(",", "");
					calculatedTotal = Double.parseDouble(unitPrice) * Double.parseDouble(productQty);
					calculatedTotalPrice = String.format("%.2f", calculatedTotal);
					System.out.println("Product Name: " + productName + " and Total Price: " + totalPrice);
					if (!totalPrice.equals(calculatedTotalPrice)) {
						throw new KDTValidationException("Total Price didnot match");

					}
				}

			} else {

				// clicking on go to list of order products //
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "gotoListLinkForAllLists", "headerText",
						"Order Products", CURR_APP);
				eo.wait(2);

				if (products == null || products.isEmpty()) {
					throw new KDTValidationException("Order Products should not be empty or null");
				}

				List<WebElement> orderProducts = eo.getListOfWebElements(driver, "xpath",
						"totalOrdeProductsWithGotoLink", CURR_APP);
				for (int i = 2; i <= orderProducts.size(); i++) {

					String productName = eo.getTextAfterReplacingKeyValue(driver, "xpath", "productNameWithGotoLink",
							"index", String.valueOf(i), CURR_APP);
					String productQty = eo.getTextAfterReplacingKeyValue(driver, "xpath", "quantityWithGotoLink",
							"index", String.valueOf(i), CURR_APP);
					productNames.put(productName, productQty.trim());
					String unitPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath", "unitPriceWithGotoLink",
							"index", String.valueOf(i), CURR_APP);
					unitPrice = unitPrice.split(" ")[1];
					totalPrice = eo.getTextAfterReplacingKeyValue(driver, "xpath", "totalPriceWithGotoLink", "index",
							String.valueOf(i), CURR_APP);
					totalPrice = totalPrice.split(" ")[1];
					totalPrice = totalPrice.replace(",", "");
					calculatedTotal = Double.parseDouble(unitPrice) * Double.parseDouble(productQty);
					calculatedTotalPrice = String.format("%.2f", calculatedTotal);
					System.out.println("Product Name: " + productName + " and Total Price: " + totalPrice);
					if (!totalPrice.equals(calculatedTotalPrice)) {
						throw new KDTValidationException("Total Price didnot match");

					}
				}

			}

			for (Entry<String, String> obj : products.entrySet()) {

				if (productNames.containsKey(obj.getKey())) {
					System.out.println("Actual Product is: " + obj.getKey());
					System.out.println("Actual Quantity is: " + obj.getValue());
					System.out.println("Quantity in Order Products is:" + productNames.get(obj.getKey()));

					if (!productNames.get(obj.getKey()).equals(obj.getValue())) {
						throw new KDTValidationException("Quantity didnot match");
					}

				} else {

					throw new KDTValidationException("Product line didnot match");
				}
			}
			driver.navigate().back();
		}

	}

	public static class GenerateAndActivateContractForTheQuote extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.wait(3);
			/*
			 * quoteNumber = "Q-72004"; orderNumber = "00028110"; accountName =
			 * "Automation_18233"; orderHeader = "Order 00028110";
			 */

			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", orderHeader, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "linkInGlobalSearch", "linkText", "Orders",
					waitTime, CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "linkInGlobalSearch", "linkText", "Orders",
					CURR_APP);

			eo.clickElement(driver, "xpath", "clickOrderNumberFromTable", CURR_APP);

			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Contracts", waitTime,
					CURR_APP);
			eo.wait(20);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Contracts", CURR_APP);
			eo.wait(2);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "clickOnLinkFromTheTable", "headerText", "Contracts",
					CURR_APP);
			eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "relativeLink", "linkText", "Cloud Deployments",
					waitTime, CURR_APP);

			// validate account name
			System.out.println("Entered Account Name is: " + accountName);
			String accountNameOnContract = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText",
					"fieldName", "Account Name", CURR_APP);
			System.out.println("Account name on contract is: " + accountNameOnContract);

			if (!accountName.contains(accountNameOnContract)) {
				throw new KDTValidationException("Account didnot match");
			}

			// validate order number

			System.out.println("Order number is: " + orderNumber);
			String orderNumberOnContract = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText",
					"fieldName", "Order", CURR_APP);
			System.out.println("Order number on contract is: " + orderNumberOnContract);

			if (!orderNumber.equals(orderNumberOnContract)) {
				throw new KDTValidationException("Order number didnot match");
			}

			// validate quote number
			System.out.println("Actual Quote Number is " + quoteNumber);
			String quoteNumberOnContract = eo.getText(driver, "xpath", "quoteNumberOnOrder", CURR_APP);
			System.out.println("Quote Number on contract is: " + quoteNumberOnContract);

			if (!quoteNumber.equals(quoteNumberOnContract)) {
				throw new KDTValidationException("Quote number on contract didnot match");
			}

			this.addComment("Contract generated and activated successfully");

			eo.wait(10);
			driver.navigate().refresh();
			// cloud deployment
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "relativeLink", "linkText", "Cloud Deployments",
					CURR_APP);
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "clickOnLinkFromTheTable", "headerText",
					"Cloud Deployments", CURR_APP);
			// validate account number on cloud
			System.out.println("Entered Account Name is: " + accountName);
			String accountNameOnCloud = eo.getTextAfterReplacingKeyValue(driver, "xpath", "savedFieldText", "fieldName",
					"Cloud Deployment Name", CURR_APP);
			System.out.println("Account name on Cloud is: " + accountNameOnCloud);

			if (!accountName.contains(accountNameOnCloud)) {
				throw new KDTValidationException("Account didnot match");
			}

		}

	}

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
			eo.actionClick(driver, "xpath", "accountsTab", "Accounts Tab", CURR_APP);
			eo.enterText(driver, "xpath", "homePageSearch", accountName, "Account Name Search", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "homeSearchButton", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "quoteLinkFromGlobalSearch", waitTime,
					"Searching for Quotes link", CURR_APP);
			eo.clickElement(driver, "xpath", "quoteLinkFromGlobalSearch", CURR_APP);
			eo.clickElement(driver, "xpath", "clickQuoteLinkFromTable", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit Button", CURR_APP);
			eo.clickElement(driver, "name", "editButton", CURR_APP);
			eo.wait(2);
			eo.enterTextAfterReplacingKeyValue(driver, "xpath", "inputFiledWithHelpTextAreaXpath", "fieldName",
					"Deal Justification", " updated", CURR_APP);
			eo.waitForWebElementVisible(driver, "name", "saveButton", waitTime, "Save Button", CURR_APP);
			eo.clickElement(driver, "name", "saveButton", CURR_APP);
			eo.wait(5);
			eo.waitForWebElementVisible(driver, "name", "editButton", waitTime, "Edit Button", CURR_APP);
			this.addComment("Quote Edited successfully");
		}

	}

	/**
	 * Function select the Calender date - Current date
	 */
	public static String calanderHandlerPickCurrentDate() {

		LocalDate date = LocalDate.now();

		int month = date.getMonthValue();
		int year = date.getYear();
		int day = date.getDayOfMonth();
		return (month + "/" + day + "/" + year);

	}

	/**
	 * Function select the Calender date in Future
	 */
	public static String calanderHandlerPickFuture(int days) {

		LocalDate date = LocalDate.now().plusDays(days);

		int month = date.getMonthValue();
		int year = date.getYear();
		int day = date.getDayOfMonth();
		return (month + "/" + day + "/" + year);

	}
}
