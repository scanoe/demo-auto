package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * representation of the logging page for the exercise
 */
public class LoginPage {
    private WebDriver driver;
    private final String url = "https://www.saucedemo.com/";
    private By userName = By.xpath("//*[@id='user-name']");
    private By password = By.xpath("//*[@id='password']");
    private By loginButton = By.xpath("//*[@id='login-button']");
    private By lockOutMessage = By.xpath("//*[@data-test='error']");

    /**
     * Initializes the login page object
     *
     * @param driver driver used to initialize the page instance
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        driver.get(url);
    }

    /**
     * Performs the login action withing the login page with the credentials passed as parameters
     *
     * @param user username for the log-in
     * @param pass password for the log-in
     * @return an instance of the product page
     */
    public ProductPage loginWith(String user, String pass) {
        WebElement userText = driver.findElement(userName);
        WebElement passwordText = driver.findElement(password);
        WebElement button = driver.findElement(loginButton);

        userText.sendKeys(user);
        passwordText.sendKeys(pass);
        button.click();

        return new ProductPage(driver);
    }

    /**
     * Returns the content on the message that appears in case a loging was not successfully
     *
     * @return the content of the text explaining the login failure
     */
    public String fetchFailedLoginMessage() {
        return driver.findElement(lockOutMessage).getText();
    }

    /**
     * Checks if the logging button is enabled to be used
     *
     * @return a boolean reflecting the state of the login button
     */
    public boolean LoginEnabled() {
        return driver.findElement(loginButton).isEnabled();
    }
}
