package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of the shopping cart page
 */
public class ShoppingCartPage {
    private final WebDriver driver;
    private By cartPageBanner = By.xpath("//*[@class='subheader']");
    private By numberElementsCartIcon = By.xpath("//div[@id='shopping_cart_container']//span");
    private By nameOfElementsInCart = By.xpath("//*[@class ='inventory_item_name']");
    private By checkoutButton = By.xpath("//*[@class='btn_action checkout_button']");

    /**
     * Generates an instance of the shopping cart page
     *
     * @param driver used to initialize the shopping cart instance
     */
    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Retrieves the text on the cart banner
     *
     * @return the content on the cart page banner as an string
     */
    public String getBannerText() {
        return driver.findElement(cartPageBanner).getText();
    }

    /**
     * Clicks on the remove from cart button for the items passed as parameter
     *
     * @param itemsToRemoveFromCart list of items to be removed from
     */
    public void removeItemsFromCart(List<String> itemsToRemoveFromCart) {
        for (String item : itemsToRemoveFromCart) {
            driver.findElement(By.xpath("//div[text()='" + item + "']/ancestor::div[@class='cart_item_label']//following-sibling::div[@class='item_pricebar']/button")).click();
        }
    }

    /**
     * Returns the number of items that appear on the cart icon
     *
     * @return returns the numeric value of the number of items present in the cart icon
     */
    public int GetNumberOfElementsFromCarIcon() {
        return Integer.valueOf(driver.findElement(numberElementsCartIcon).getText());
    }

    /**
     * Returns a list with the names of  the elements on the shopping cart
     *
     * @return a list of names of the objects inside the page
     */
    public List<String> getNamesOfElementsOnCart() {
        System.out.println(Arrays.toString(driver.findElements(nameOfElementsInCart).stream().map(WebElement::getText).collect(Collectors.toList()).toArray()));
        return driver.findElements(nameOfElementsInCart).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Clicks on the Checkout Button inside the shopping cart page
     *
     * @return an instance of the Shipment information page
     */
    public ShipmentInformationPage clickCheckout() {
        driver.findElement(checkoutButton).click();
        return new ShipmentInformationPage(driver);
    }
}
