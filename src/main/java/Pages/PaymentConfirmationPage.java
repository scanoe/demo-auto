package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;
import static Utils.Constants.BLANK_STRING;
import static Utils.Constants.ITEMS_TOTAL_TEXT;
import static Utils.Constants.TAX_PERCENTAGE;
import static Utils.Constants.TAX_TEXT;
import static Utils.Constants.TOTAL_TEXT;

/**
 * Representation of the payment confirmation page
 */
public class PaymentConfirmationPage {
    private final WebDriver driver;
    private By finishButton = By.xpath("//*[@class ='btn_action cart_button']");
    private By cancelButton = By.xpath("//*[@class ='cart_cancel_link btn_secondary']");
    private By namesOfItems = By.xpath("//*[@class='inventory_item_name']");
    private By listOfPrices = By.xpath("//*[@class='inventory_item_price']");
    private By priceWithoutTax = By.xpath("//*[@class='summary_subtotal_label']");
    private By tax = By.xpath("//*[@class='summary_tax_label']");
    private By Total = By.xpath("//*[@class='summary_total_label']");

    /**
     * returns an instance of the payment confirmation page using the given driver
     *
     * @param driver used to initialize the page element
     */
    public PaymentConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * clicks on the finish button of the page
     *
     * @return returns an instance of the order sent page
     */
    public OrderSentPage clickOnFinish() {
        driver.findElement(finishButton).click();
        return new OrderSentPage(driver);
    }

    /**
     * retrieves a list with the names of the elements in the shopping cart
     *
     * @return list of elements inside the shopping cart
     */
    public List<String> getNamesOfShoppingList() {
        return driver.findElements(namesOfItems).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * retrives  the summation of the prices of the objects listed
     *
     * @return the total of the prices listed in the page
     */
    public float calculateTotalWithoutTax() {
        return (driver.findElements(listOfPrices).stream().map(x -> Float.valueOf(x.getText().replace("$", BLANK_STRING))).reduce(0F, Float::sum));
    }

    /**
     * returns the subtotal as it appears in the payment confirmation page
     *
     * @return returns the subtotal as it appears in the payment confirmation page
     */
    public float getPriceWihthoutTax() {

        return Float.valueOf(driver.findElement(priceWithoutTax).getText().replace(ITEMS_TOTAL_TEXT, BLANK_STRING));
    }

    /**
     * calculates the tax of the products listed on the page
     *
     * @return returns the calculated tax for the prices of the objects inside the page
     */
    public float calculateTax() {

        return (Math.round(Float.valueOf(driver.findElement(priceWithoutTax).getText().replace(ITEMS_TOTAL_TEXT, BLANK_STRING))) * TAX_PERCENTAGE) / 100;
    }

    /**
     * returns the tax value as it appears in the page as a number
     *
     * @return returns the numeric value of the tax as it appears in the page
     */
    public float getTax() {
        return Float.valueOf(driver.findElement(tax).getText().replace(TAX_TEXT, BLANK_STRING));
    }

    /**
     * returns the total price as it appears in the page
     *
     * @return returns the total value as it appears in the page as a numeric value
     */
    public float getTotalPrice() {
        return Float.valueOf(driver.findElement(Total).getText().replace(TOTAL_TEXT, BLANK_STRING));
    }
}
