package reportinglistener;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class CustomWebDriverListener implements WebDriverListener {

    @Override
    public void beforeClick(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Attempting to click on the \"" + getElementDescription(element) + "\" element.");
    }

    @Override
    public void afterClick(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Successfully clicked on the \"" + getElementDescription(element) + "\" element.");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        ExtentManager.getTest().log(Status.INFO, "Entering text \"" + String.join(",", keysToSend) + "\" into the \"" + getElementDescription(element) + "\" field.");
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        ExtentManager.getTest().log(Status.INFO, "Successfully entered text into the \"" + getElementDescription(element) + "\" field.");
    }

    @Override
    public void beforeClear(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Clearing text from the \"" + getElementDescription(element) + "\" field.");
    }

    @Override
    public void afterClear(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Successfully cleared text from the \"" + getElementDescription(element) + "\" field.");
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        ExtentManager.getTest().log(Status.INFO, "Navigating to URL: " + url);
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
        ExtentManager.getTest().log(Status.INFO, "Successfully navigated to URL: " + url);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By by) {
        ExtentManager.getTest().log(Status.INFO, "Searching for element: " + by.toString());
    }

    @Override
    public void afterFindElement(WebDriver driver, By by, WebElement result) {
        if (result != null) {
            ExtentManager.getTest().log(Status.INFO, "Found element: \"" + getElementDescription(result) + ".\"");
        } else {
            ExtentManager.getTest().log(Status.WARNING, "Element not found: " + by.toString());
        }
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        ExtentManager.getTest().log(Status.INFO, "Closing the browser.");
    }

    @Override
    public void afterQuit(WebDriver driver) {
        ExtentManager.getTest().log(Status.INFO, "Browser closed successfully.");
    }

    // Helper method to get a descriptive name for the element
    private String getElementDescription(WebElement element) {
        String description = element.getTagName();
        if (element.getAttribute("id") != null && !element.getAttribute("id").isEmpty()) {
            description += " (ID: " + element.getAttribute("id") + ")";
        } else if (element.getAttribute("name") != null && !element.getAttribute("name").isEmpty()) {
            description += " (Name: " + element.getAttribute("name") + ")";
        } else if (element.getAttribute("class") != null && !element.getAttribute("class").isEmpty()) {
            description += " (Class: " + element.getAttribute("class") + ")";
        } else if (element.getText() != null && !element.getText().isEmpty()) {
            description += " (Text: \"" + element.getText() + "\")";
        }
        return description;
    }
}