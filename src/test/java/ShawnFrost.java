import base.Browser;
import base.BrowserManager;
import base.DesktopBrowserType;
import downloader.Downloader;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.common.GoodJob;
import pages.obstacles.AndCounting;
import pages.obstacles.FindTheChangedCell;
import pages.obstacles.NotATable;
import pages.obstacles.ToscaBotCanFly;
import utils.XMLParser;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static utils.ElementOperations.*;

public class ShawnFrost {
    

    @BeforeMethod
    void initObstaclePage() {
        System.setProperty("SE_MSEDGEDRIVER_MIRROR_URL", "https://msedgedriver.microsoft.com");
        WebDriver driver = new Browser().openBrowser("edge");
        BrowserManager.setDriver(driver);
    }

    @Test(testName = "Not a table")
    void NotATable() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/64161");
        NotATable notATable = new NotATable(BrowserManager.getDriver());
        notATable.clickGenerateOrderId();
        String orderId = notATable.getOrderID();
        notATable.enterOrderId(orderId);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "And Counting")
    void AndCounting() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/24499");
        AndCounting andCounting = new AndCounting(BrowserManager.getDriver());
        String chars = andCounting.getTypeThisText();
        long countOfOptions = andCounting.dropDown().getOptions()
                .stream().filter(option -> option.getAttribute("innerText").startsWith(chars))
                .count();
        andCounting.enterCount(String.valueOf(countOfOptions));
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Todo List")
    void Todolist() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/23292");
        List<WebElement> todoTable = BrowserManager.getDriver().findElements(By.xpath("//table[@id='todo-tasks']//tr"))
                .stream().filter(row -> row.getAttribute("task") != null)
                .collect(Collectors.toList());
        WebElement completedTasksTable = BrowserManager.getDriver().findElement(By.id("completed-tasks"));
        Actions actions = new Actions(BrowserManager.getDriver());
        for (int i = 1; i <= todoTable.size(); i++) {
            int finalI = i;
            actions.dragAndDrop(
                            todoTable.stream()
                                    .filter(row -> Integer.parseInt(row.getAttribute("task")) == finalI)
                                    .findFirst().get(), completedTasksTable)
                    .perform();
        }
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Red Stripe")
    void RedStripe() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/30034");
        BrowserManager.getDriver().findElement(By.id("generate")).click();
        BrowserManager.getDriver().findElement(By.xpath("//div[@id='number']//following-sibling::div")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Future Christmas")
    void FutureChristmas() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/21269");
        String dayName = LocalDate.of(LocalDate.now().getYear(), 12, 25)
                .plusYears(2)
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        BrowserManager.getDriver().findElement(By.id("christmasday")).sendKeys(dayName);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Be Fast - Automate")
    void BeFastAutomate() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/87912");
        BrowserManager.getDriver().findElement(By.id("loadbooks")).click();
        String xmlString = BrowserManager.getDriver().findElement(By.id("books")).getAttribute("value");
        String isbn = new XMLParser()
                .fromXMLString(xmlString)
                .getStringValueByXPath("//title[text()='Testing Computer Software']/following-sibling::isbn");
        BrowserManager.getDriver().findElement(By.id("isbn")).sendKeys(isbn);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Tricentis Tosca Olympics")
    void TricentisToscaOlympics() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/82018");
        WebElement startButton = BrowserManager.getDriver().findElement(By.id("start"));
        startButton.click();
        WebElement instructions = BrowserManager.getDriver().findElement(By.className("instructions"));
        String winMessage = "You did it!";
        String crashMessage = "Crash!";
        while (!instructions.getText().trim().equals(winMessage)) {
            String op = instructions.getText().trim();
            switch (op) {
                case "Go right!":
                    new Actions(BrowserManager.getDriver()).sendKeys(Keys.ARROW_RIGHT).perform();
                    break;
                case "Go left!":
                    new Actions(BrowserManager.getDriver()).sendKeys(Keys.ARROW_LEFT).perform();
                    break;
            }
            if (op.equals(crashMessage)) startButton.click();
        }
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Find The Changed Cell", enabled = false)
    void FindTheChangedCell() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/73591");
        FindTheChangedCell findTheChangedCell = new FindTheChangedCell(BrowserManager.getDriver());

        List<List<String>> originalTable = findTheChangedCell.getTableData();
        findTheChangedCell.clickChangeTableButton();
        List<List<String>> changedTable = findTheChangedCell.getTableData();

        for (int i = 0; i < originalTable.size(); i++) {
            for (int j = 0; j < originalTable.get(0).size(); j++) {
                String originalStr = originalTable.get(i).get(j);
                String changedStr = changedTable.get(i).get(j);
                if (!originalStr.equals(changedStr)) {
                    findTheChangedCell.enterRowNumber(i + 1);
                    findTheChangedCell.enterColumnNumber(j + 1);
                    findTheChangedCell.enterOriginalValue(originalStr);
                    findTheChangedCell.enterChangedValue(changedStr);
                    break;
                }
            }
        }

        findTheChangedCell.clickSubmit();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Find The Changed Cell Using Inner HTML")
    void FindTheChangedCellUsingInnerHTML() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/73591");
        FindTheChangedCell findTheChangedCell = new FindTheChangedCell(BrowserManager.getDriver());

        Function<WebElement, HashMap<String, String>> extractTableData = (WebElement tableElement) -> {
            String tableHTML = tableElement.getAttribute("innerHTML");
            List<String> cells = regexMultiTextExtractor(tableHTML, "<td id=\"\\d+_\\d+\">[\\w|\\d]{4}<\\/td>");
            HashMap<String, String> tableDataMap = new HashMap<>();
            for (String cell : cells) {
                Matcher matcher = regexExtractor(cell, "<td id=\"?(?<key>\\d+_\\d+)\"?>(?<cell>[\\w|\\d]{4})<\\/td>");
                tableDataMap.put(matcher.group("key"), matcher.group("cell"));
            }
            return tableDataMap;
        };

        WebElement table = BrowserManager.getDriver().findElement(By.xpath("//div[@id='tableContent']"));
        HashMap<String, String> originalTableData = extractTableData.apply(table);
        findTheChangedCell.clickChangeTableButton();
        HashMap<String, String> newTableData = extractTableData.apply(table);

        for (String key : originalTableData.keySet()) {
            String originalData = originalTableData.get(key);
            String newData = newTableData.get(key);

            if (!originalData.equals(newData)) {
                System.out.println("New Implementation");
                String[] rowCol = key.split("_");
                System.out.printf("Row: %s\nCol: %s\nActual Value: %s\nExpected Value: %s%n",
                        rowCol[0], rowCol[1], originalData, newData);
                findTheChangedCell.enterRowNumber(rowCol[0]);
                findTheChangedCell.enterColumnNumber(rowCol[1]);
                findTheChangedCell.enterOriginalValue(originalData);
                findTheChangedCell.enterChangedValue(newData);
            }
        }

        findTheChangedCell.clickSubmit();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Halfway")
    void Halfway() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41038");
        WebElement halfButton = BrowserManager.getDriver().findElement(By.id("halfButton"));
        clickElementByOffSet(BrowserManager.getDriver(), halfButton, 75, 50);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Tomorrow")
    void Tomorrow() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/19875");
        String tomorrowsDate = LocalDate.now()
                .plusDays(1)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        BrowserManager.getDriver().findElement(By.id("datefield")).sendKeys(tomorrowsDate);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Math")
    void Math() {
        Map<String, BinaryOperator<Integer>> operatorMap = new HashMap<>();
        operatorMap.put("+", Integer::sum);
        operatorMap.put("-", (a, b) -> a - b);
        operatorMap.put("*", (a, b) -> a * b);
        operatorMap.put("%", (a, b) -> a % b);

        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/32403");
        int number1 = Integer.parseInt(BrowserManager.getDriver().findElement(By.id("no1")).getText().trim());
        int number2 = Integer.parseInt(BrowserManager.getDriver().findElement(By.id("no2")).getText().trim());
        String operator = BrowserManager.getDriver().findElement(By.id("symbol1")).getText().trim();
        int result = operatorMap.get(operator).apply(number1, number2);
        BrowserManager.getDriver().findElement(By.id("result")).sendKeys(String.valueOf(result));
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Extracting Text")
    void ExtractingText() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/81012");
        String successMessage = BrowserManager.getDriver().findElement(By.id("alerttext")).getText().trim();
        String amount = regexExtractor(successMessage, "\\$(?<Amount>\\d+\\.\\d+)")
                .group("Amount");
        BrowserManager.getDriver().findElement(By.id("totalamountText")).sendKeys(amount);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Tough Cookie")
    void ToughCookie() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/45618");
        WebElement txtBox = BrowserManager.getDriver().findElement(By.id("generated"));
        txtBox.click();
        String randomText = txtBox.getAttribute("value").trim();
        List<String> numbers = regexMultiTextExtractor(randomText, "\\d+");
        BrowserManager.getDriver().findElement(By.id("firstNumber")).sendKeys(numbers.get(0));
        BrowserManager.getDriver().findElement(By.id("secondNumber")).sendKeys(numbers.get(1));
        BrowserManager.getDriver().findElement(By.id("thirdNumber")).sendKeys(numbers.get(2));
        txtBox.click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Hidden Element")
    void HiddenElement() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/66666");
        BrowserManager.getDriver().findElement(By.id("clickthis")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Addition")
    void Addition() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/78264");
        int number1 = Integer.parseInt(BrowserManager.getDriver().findElement(By.id("no1")).getText().trim());
        int number2 = Integer.parseInt(BrowserManager.getDriver().findElement(By.id("no2")).getText().trim());
        BrowserManager.getDriver().findElement(By.id("result")).sendKeys(String.valueOf(number1 + number2));
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Empty")
    void Empty() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/66667");
        BrowserManager.getDriver().findElement(By.id("generate")).click();
        List<WebElement> checkpoints = BrowserManager.getDriver().findElements(By.className("checkpoint"));
        for (WebElement checkpoint : checkpoints) {
            checkpoint.click();
        }
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "ToscaBot Can Fly")
    void ToscaBotCanFly() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/60469");
        ToscaBotCanFly toscaBotCanFly = new ToscaBotCanFly(BrowserManager.getDriver());
        new Actions(BrowserManager.getDriver())
                .dragAndDrop(toscaBotCanFly.getToscaBot(), toscaBotCanFly.getTargetLocation())
                .perform();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Bubble Sort")
    void BubbleSort() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/73589");
        String xpath = "(//div[@id='array']/div[@class='bubble']/div[@class='num'])[%d]";
        try {
            for (int i = 1; i <= 9; i++) {
                for (int j = 1; j <= 9; j++) {
                    staticWait(1);
                    int number1 = Integer.parseInt(BrowserManager.getDriver().findElement(By.xpath(String.format(xpath, 1))).getText());
                    int number2 = Integer.parseInt(BrowserManager.getDriver().findElement(By.xpath(String.format(xpath, 2))).getText());
                    if (number1 > number2) {
                        BrowserManager.getDriver().findElement(By.id("button1")).click();
                    }
                    BrowserManager.getDriver().findElement(By.id("button2")).click();
                }
            }
        } catch (Exception ignored) {}
        finally {
            Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
        }
    }

    @Test(testName = "Testing Methods")
    void TestingMethods() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/94441");
        String[] testingTypes = {
                "Functional", "End2End", "GUI", "Exploratory"
        };
        Select multiSelect = new Select(BrowserManager.getDriver().findElement(By.id("multiselect")));
        for (String testingType : testingTypes) {
            multiSelect.selectByVisibleText(testingType.concat(" testing"));
        }
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "DropDown Table")
    void DropDownTable() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/14090");
        BrowserManager.getDriver().findElement(By.id("generate")).click();
        List<WebElement> tasks = BrowserManager.getDriver().findElements(By.className("task"));
        tasks.remove(0);
        List<WebElement> values = BrowserManager.getDriver().findElements(By.xpath("//select[@class='tableselect value']"));
        for (int i = 0; i < tasks.size(); i++) {
            String firstLetter = tasks.get(i).getText().split(":")[1].trim();
            Select select = new Select(values.get(i));
            select.getOptions().stream()
                    .filter(element -> element.getText().startsWith(firstLetter))
                    .findFirst()
                    .ifPresent(option -> select.selectByVisibleText(option.getText()));
        }
        BrowserManager.getDriver().findElement(By.id("submit")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Click Me If You Can")
    void ClickMeIfYouCan() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41040");
        WebElement button = BrowserManager.getDriver().findElement(By.id("buttontoclick"));
        triggerClick(BrowserManager.getDriver(), button);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "TestData In A Service")
    void TestDataInAService() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/16384");
        WebElement createDataButton = BrowserManager.getDriver().findElement(By.id("createTDS"));
        createDataButton.click();
        WebElement keyTextBox = BrowserManager.getDriver().findElement(By.id("key"));
        WebElement attributeTextBox = BrowserManager.getDriver().findElement(By.id("attribute"));
        String key = keyTextBox.getText().trim();
        String attribute = attributeTextBox.getText().trim();

        //staticWait(3);
        // Steps to get the value from TDS (Tricentis Test Data Service)
        String tdsUri = "https://tdsservice.azurewebsites.net";
        String repository = "data";
        String tdsType = "obstacle";

        Response response = given()
                .baseUri(tdsUri)
                .get(repository + "/" + tdsType);

        List<Map<String, Object>> data = response.body().jsonPath().getList("$");
        Map<String, Object> r = data.stream()
                .filter(e -> ((Map<String, Object>) e.get("data")).get("key").equals(key))
                .findFirst()
                .orElse(null);
        String result = ((Map<String, Object>) r.get("data")).get(attribute).toString();
        System.out.printf("key => %s\nattribute => %s\nresult => %s", key, attribute, result);

        WebElement resultTextBox = BrowserManager.getDriver().findElement(By.id("result"));
        resultTextBox.sendKeys(result);
        WebElement submitButton = BrowserManager.getDriver().findElement(By.id("submit"));
        submitButton.click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Scroll Into View")
    void ScrollIntoView() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/99999");

        WebElement frame = BrowserManager.getDriver().findElement(By.id("container"));
        BrowserManager.getDriver().switchTo().frame(frame);

        WebElement textBox = BrowserManager.getDriver().findElement(By.id("textfield"));
        scrollIntoView(BrowserManager.getDriver(), textBox);
        staticWait(2);
        Assert.assertTrue(textBox.isEnabled(), "Text box is not enabled");
        textBox.sendKeys("Tosca");

        BrowserManager.getDriver().switchTo().defaultContent();

        new Actions(BrowserManager.getDriver()).scrollByAmount(0, -500).build().perform();
        BrowserManager.getDriver().findElement(By.id("submit")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Fun With Tables")
    void FunWithTables() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/92248");

        WebElement desiredRow = BrowserManager.getDriver().findElement(By.xpath("//table[@id='persons']//tr[td[1][text()='John'] and td[2][text()='Doe']]"));
        WebElement editButton = desiredRow.findElement(By.xpath(".//td[4]//button[@name='edit']"));
        editButton.click();

        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Again and Again and Again")
    void AgainAndAgainAndAgain() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/81121");
        WebElement button = BrowserManager.getDriver().findElement(By.id("button"));

        while (!button.getText().trim().equalsIgnoreCase("enough")) {
            button.click();
        }
        button.click();

        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Lots Of Rows")
    void LotsOfRows() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41032");
        int countOfRows = BrowserManager.getDriver().findElement(By.id("rowCountTable"))
                .findElements(By.tagName("tr"))
                .size();
        BrowserManager.getDriver().findElement(By.id("rowcount")).sendKeys(String.valueOf(countOfRows));
        BrowserManager.getDriver().findElement(By.id("sample")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "The Last Row")
    void TheLastRow() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/70310");
        List<WebElement> amounts = BrowserManager.getDriver().findElements(By.xpath("//table[@id='orderTable']//tr[td[1][text()='Order Value']]/td[2]"));
        String amount = amounts.get(amounts.size() - 1).getText();
        BrowserManager.getDriver().findElement(By.id("ordervalue")).sendKeys(amount);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Table Search")
    void TableSearch() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41036");
        boolean cellIsPresent = BrowserManager.getDriver().findElements(By.xpath("//table[@id='randomTable']/tr/td"))
                .stream().anyMatch(cell -> cell.getText().equals("15"));
        BrowserManager.getDriver().findElement(By.id("resulttext")).sendKeys(cellIsPresent ? "True" : "False");
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Meeting Scheduler")
    void MeetingScheduler() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41037");

        WebElement timeTable = BrowserManager.getDriver().findElement(By.id("timeTable"));
        WebElement headerCell = timeTable.findElement(By.xpath("//th[text()='Thursday']"));
        WebElement timeSlot = timeTable.findElement(By.xpath("//tr[td[1][text()='11:00 - 13:00']]"));

        int cellIndex = Integer.parseInt(headerCell.getAttribute("cellIndex"));
        String roomStatus = timeSlot.findElement(By.xpath(String.format("./td[%d]", cellIndex + 1))).getText();
        //String roomStatus = timeSlot.findElement(By.xpath("./td[" + (cellIndex + 1) + "]")).getText();

        BrowserManager.getDriver().findElement(By.id("resulttext")).sendKeys(roomStatus);

        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Escape")
    void Escape() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/41041");
        BrowserManager.getDriver().findElement(By.id("resulttext")).sendKeys("{Click}");
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "The Obvious")
    void TheObvious() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/73588");
        BrowserManager.getDriver().findElement(By.id("clickme")).click();
        String randomText = BrowserManager.getDriver().findElement(By.id("randomtext")).getAttribute("value").trim();
        Select options = new Select(BrowserManager.getDriver().findElement(By.id("selectlink")));
        options.selectByVisibleText(randomText);
        BrowserManager.getDriver().findElement(By.id("submitanswer")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Find And Fill")
    void FindAndFill() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/73590");
        BrowserManager.getDriver().findElement(By.id("pass")).click();
        BrowserManager.getDriver().findElement(By.id("actual")).sendKeys("ABC");
        BrowserManager.getDriver().findElement(By.id("sample")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Errors Occur")
    void ErrorsOccur() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/70924");
        WebElement button = BrowserManager.getDriver().findElement(By.xpath(".//button[@id='tech']/preceding-sibling::button"));
        do {
            button.click();
            if (button.getText().equals("ERROR"))
                BrowserManager.getDriver().findElement(By.id("tech")).click();
        } while (Integer.parseInt(button.getText().trim()) != 10);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Confusing Dates")
    void ConfusingDates() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/57683");
        BrowserManager.getDriver().findElement(By.id("generate")).click();
        String generatedDate = BrowserManager.getDriver().findElement(By.id("dateGenerated")).getAttribute("value");
        String dateSolution = LocalDate.parse(generatedDate, DateTimeFormatter.ofPattern("M/d/yyyy"))
                .plusMonths(2)
                .withDayOfMonth(1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
        BrowserManager.getDriver().findElement(By.id("dateSolution")).sendKeys(dateSolution);
        BrowserManager.getDriver().findElement(By.id("done")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Popup Windows")
    void PopupWindows() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/51130");
        BrowserManager.getDriver().findElement(By.id("button")).click();
        String mainWindowHandle = BrowserManager.getDriver().getWindowHandle();
        for (String windowHandle : BrowserManager.getDriver().getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                BrowserManager.getDriver().switchTo().window(windowHandle);
                break;
            }
        }
        BrowserManager.getDriver().close();
        BrowserManager.getDriver().switchTo().window(mainWindowHandle);
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Ids Are Not Everything")
    void IdsAreNotEverything() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/22505");
        BrowserManager.getDriver().findElement(By.linkText("Click me!".toUpperCase())).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Twins")
    void Twins() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/12952");
        BrowserManager.getDriver().findElement(By.id("thisoneistheright"))
                .findElement(By.tagName("a")).click();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Two Times")
    void TwoTimes() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/72954");
        new Actions(BrowserManager.getDriver())
                .doubleClick(BrowserManager.getDriver().findElement(By.partialLinkText("Click me".toUpperCase())))
                .build()
                .perform();
        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Wait A Moment")
    void WaitAMoment() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/33678");

        BrowserManager.getDriver().findElement(By.id("one")).click();

        WebDriverWait webDriverWait = new WebDriverWait(BrowserManager.getDriver(), Duration.ofSeconds(60));
        webDriverWait.until(ExpectedConditions.textToBe(By.id("label"), "100%"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("two")))
                .click();

        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @Test(testName = "Get Sue's Number")
    void GetSuesNumber() {
        BrowserManager.getDriver().get("https://obstaclecourse.tricentis.com/Obstacles/72946");

        WebElement catalogLink = BrowserManager.getDriver().findElement(By.id("downloadSolution"));

        String downloadPath = System.getProperty("java.io.tmpdir");
        Downloader fileDownloader = new Downloader(BrowserManager.getDriver())
                .setDownloadPath(downloadPath);
        String downloadedXMLPath = fileDownloader.startDownload(catalogLink);

        XMLParser xmlParser = new XMLParser()
                .fromXMLFile(downloadedXMLPath);

        String prefix = xmlParser.getStringValueByXPath("//number[@id='Sue']/prefix");
        String number = xmlParser.getStringValueByXPath("//number[@id='Sue']/number");
        String value = prefix + number;
        System.out.println(value);
        BrowserManager.getDriver().findElement(By.id("NumberSue")).sendKeys(value);

        Assert.assertTrue(new GoodJob(BrowserManager.getDriver()).isSuccessMessageShowed(), "Problem Not Solved");
    }

    @AfterMethod(alwaysRun = true)
    void tearDownSession() {
        BrowserManager.quitDriver();
    }
}
