package reportinglistener;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class CustomWebDriverListener implements WebDriverListener {

    @Override
    public void afterClick(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Clicked on: " + getElementDescription(element));
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        ExtentManager.getTest().log(Status.INFO, "Entered text into: " + getElementDescription(element));
    }

    @Override
    public void afterClear(WebElement element) {
        ExtentManager.getTest().log(Status.INFO, "Cleared text from: " + getElementDescription(element));
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
        ExtentManager.getTest().log(Status.INFO, "Navigated to URL: " + url);
    }

    @Override
    public void afterFindElement(WebDriver driver, By by, WebElement result) {
        if (result != null) {
            ExtentManager.getTest().log(Status.INFO, "Found element: " + getElementDescription(result));
        } else {
            ExtentManager.getTest().log(Status.WARNING, "Element not found by: " + by.toString());
        }
    }

    @Override
    public void afterQuit(WebDriver driver) {
        ExtentManager.getTest().log(Status.INFO, "Browser session ended.");
    }

    // Helper method to get a descriptive name for the element
    private String getElementDescription(WebElement element) {
        String description = element.getTagName();
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        String className = element.getAttribute("class");
        String placeholder = element.getAttribute("placeholder");
        String value = element.getAttribute("value");
        String ariaLabel = element.getAttribute("aria-label");
        String text = element.getText();

        if (ariaLabel != null && !ariaLabel.isEmpty()) {
            description = ariaLabel;
        } else if (placeholder != null && !placeholder.isEmpty()) {
            description = placeholder;
        } else if (value != null && !value.isEmpty()) {
            description = value;
        } else if (id != null && !id.isEmpty()) {
            description += " (id: " + id + ")";
        } else if (name != null && !name.isEmpty()) {
            description += " (name: " + name + ")";
        } else if (className != null && !className.isEmpty()) {
            description += " (class: " + className + ")";
        } else if (text != null && !text.isEmpty()) {
            description += " (text: \"" + text + "\")";
        }
        return description;
    }
}