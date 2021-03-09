package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Representation on the page for entering shipment information
 */
public class ShipmentInformationPage {

    private WebDriver driver;
    private By firstnameTestBox = By.xpath("//*[@id='first-name']");
    private By lastNameTextBox = By.xpath("//*[@id='last-name']");
    private By zipCodeTextBox = By.xpath("//*[@id='postal-code']");
    private By continueButton = By.xpath("//*[@class='btn_primary cart_button']");
    private By cancelButton = By.xpath("//*[@class='cart_cancel_link btn_secondary']");
    private By errorMessage = By.xpath("//*[@data-test='error']");

    /**
     * Generates an instance of the Shipment information page
     *
     * @param driver used for the operations inside the page
     */
    public ShipmentInformationPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * uses the given firstname to fill the corresponding textbox
     *
     * @param firstName used to fill the information text box
     */
    public void fillUserName(String firstName) {
        driver.findElement(firstnameTestBox).sendKeys(firstName);
    }

    /**
     * uses the given last used for  name for filling the information
     *
     * @param lastname used to fill the text box
     */
    public void fillLastName(String lastname) {
        driver.findElement(lastNameTextBox).sendKeys(lastname);
    }

    /**
     * uses the given postal code to fill the corresponding text box
     *
     * @param postalCode used to fill the text box
     */
    public void fillPostalCode(String postalCode) {
        driver.findElement(zipCodeTextBox).sendKeys(postalCode);
    }

    /**
     * Clicks on the continue button inside the shipment information page
     *
     * @return an instance of the payment confirmation page
     */
    public PaymentConfirmationPage clickOnContinue() {
        driver.findElement(continueButton).click();
        return new PaymentConfirmationPage(driver);
    }

    /**
     * Retrieves the error message that appears on the page
     *
     * @return con text content of the error as it appears on the page
     */
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    /**
     * Clicks on the cancel option inside the shipping information page
     *
     * @return an instance of the shopping cart Page
     */
    public ShoppingCartPage clickOnCancel() {
        driver.findElement(cancelButton).click();
        return new ShoppingCartPage(driver);
    }
}
