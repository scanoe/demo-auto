package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Representation of the item detail page
 */
public class ItemDetailPage {

    private WebDriver driver;
    private By productName = By.xpath("//*[@class='inventory_details_name']");
    private By addToCartButton = By.xpath("//*[@class='btn_primary btn_inventory']");
    private By cartIcon = By.xpath("//*[@id='shopping_cart_container']");

    /**
     * Initializes an instance of the item detail page
     *
     * @param driver used to initialize the instance of the page
     */
    public ItemDetailPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * returns the contents on the label which includes the product name present in the page
     *
     * @return returns an string containing the name of the product the page taks about
     */
    public String getProductName() {
        return driver.findElement(productName).getText();
    }

    /**
     * clicks on the add to cart button for the product detail page
     */
    public void addToCart() {
        driver.findElement(addToCartButton).click();
    }

    /**
     * Clicks on the Cart icon inside the page returning an instannce of the shopping car page
     *
     * @return isntance of the Shopping cart page
     */
    public ShoppingCartPage clickOnCartIcon() {
        driver.findElement(cartIcon).click();
        return new ShoppingCartPage(driver);
    }
}
