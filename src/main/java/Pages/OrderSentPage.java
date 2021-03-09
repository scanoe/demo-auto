package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Representation of the order sent page
 */
public class OrderSentPage {
    private final WebDriver driver;
    private By thankYouMessage = By.xpath("//*[@class='complete-header']");

    /**
     * initializes an instance of the order sent page
     *
     * @param driver used for initializing the page instance
     */
    public OrderSentPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * retrieves the thank you message at the banner of the page
     *
     * @return a String containing the thank you message
     */
    public String getThankYoumessage() {
        return driver.findElement(thankYouMessage).getText();
    }
}
