package reportinglistener;

import base.BrowserManager;
import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class ExtentReportListener extends TestListenerAdapter {
    private ExtentReports extentReports;

    @Override
    public void onStart(ITestContext testContext) {
        File screenshotDir = new File("test-output/screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        ExtentReporter htmlReporter = new ExtentHtmlReporter("test-output/ExtentReport.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
        extentReports.setSystemInfo("Test Environment", "Dev");
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Java Runtime Version", System.getProperty("java.runtime.version"));
        extentReports.setSystemInfo("Java Specification Version", System.getProperty("java.specification.version"));
        extentReports.setSystemInfo("Java Class Version", System.getProperty("java.class.version"));
        extentReports.setSystemInfo("Operating System Name", System.getProperty("os.name"));
        extentReports.setSystemInfo("Operating System Version", System.getProperty("os.version"));
        extentReports.setSystemInfo("Operating System Architecture", System.getProperty("os.arch"));
        extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
        extentReports.setSystemInfo("User Directory", System.getProperty("user.dir"));
        extentReports.setSystemInfo("User Country", System.getProperty("user.country"));
        extentReports.setSystemInfo("User Language", System.getProperty("user.language"));
        extentReports.setSystemInfo("User Timezone", System.getProperty("user.timezone"));
    }

    @Override
    public void onFinish(ITestContext testContext) {
        extentReports.flush();
        ExtentManager.removeTest();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getConstructorOrMethod()
                .getMethod().getDeclaredAnnotation(Test.class)
                .testName();
        if (testName.isEmpty())
            testName = result.getMethod().getMethodName();
        ExtentTest extentTest = extentReports.createTest(testName);
        ExtentManager.setTest(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().log(Status.FAIL, "Test failed");
        ExtentManager.getTest().log(Status.FAIL, result.getThrowable());

        try {
            WebDriver driver = BrowserManager.getDriver();
            if (driver != null) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotName = result.getName() + ".png";
                File destFile = new File("test-output/screenshots/" + screenshotName);
                FileUtils.copyFile(screenshot, destFile);
                ExtentManager.getTest().addScreenCaptureFromPath(destFile.getAbsolutePath());
            } else {
                ExtentManager.getTest().log(Status.WARNING, "Could not capture screenshot: WebDriver instance is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ExtentManager.getTest().log(Status.ERROR, "Error capturing screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped");
    }
}
