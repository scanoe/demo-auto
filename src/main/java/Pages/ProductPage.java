package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * page to represent the products list page
 */
public class ProductPage {
    private String url = "https://www.saucedemo.com/inventory.html";
    private WebDriver driver;
    private By productPageLabel = By.xpath("//div[@class ='product_label']");
    private By numberOfElementsCartIcon = By.xpath("//div[@id='shopping_cart_container']//span");
    private By pageOptions = By.xpath("//*[@id='react-burger-menu-btn']");
    private By logOut = By.xpath("//*[@id='logout_sidebar_link']");
    private By selectOrder = By.xpath("//*[@class ='product_sort_container']");
    private By itemNames = By.xpath("//*[@class='inventory_item_name']");

    /**
     * initiates and returns an instance of the products pages
     *
     * @param driver the driver instance for performing different actions inside the page
     */
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * retirves the text inside the general banner
     *
     * @return text content of the banner
     */
    public String getBannerText() {
        return driver.findElement(productPageLabel).getText();
    }

    /**
     * adds indicated elements by name to the shopping cart
     *
     * @param items items for adding to the shopping cart
     */
    public void addToCart(List<String> items) {
        for (String item : items) {
            driver.findElement(By.xpath("//*[text() ='" + item + "']/ancestor::div[@class='inventory_item_label']/following-sibling::div/button")).click();
        }
    }

    /**
     * retrieves the number of elements as it appears in the shopping cart icon
     *
     * @return number of elements inside the shopping cart icon
     */
    public int getNumberOfItemsOnCartIcon() {
        return Integer.valueOf(driver.findElement(numberOfElementsCartIcon).getText().trim());
    }

    /**
     * clicks on the shopping cart icon
     *
     * @return an instance of the shopping page
     */
    public ShoppingCartPage ClickOnBuyCart() {
        driver.findElement(numberOfElementsCartIcon).click();
        return new ShoppingCartPage(driver);
    }

    /**
     * tries to open the items page but redirects to login page
     *
     * @return an instance of the logging page
     */
    public LoginPage openPage() {
        driver.get(url);
        return new LoginPage(driver);
    }

    /**
     * Clicks on the title for the product whose name is passed as a parameter
     *
     * @param itemForDescription name of the item to open its description
     * @return returns an instance of the item description page
     */
    public ItemDetailPage openItemDescription(String itemForDescription) {
        driver.findElement(By.xpath("//*[@id='inventory_container']//div[text()='" + itemForDescription + "']")).click();
        return new ItemDetailPage(driver);
    }

    /**
     * Clicks on the page options hamburger menu
     */
    public void clickOnPageOptions() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.findElement(pageOptions).click();
        wait.until(ExpectedConditions.elementToBeClickable(logOut));
    }

    /**
     * Clicks on the log out option inside the opened hamburger menu
     *
     * @return an instance of the logging page
     */
    public LoginPage ClickLogOut() {
        driver.findElement(logOut).click();
        return new LoginPage(driver);
    }

    /**
     * Selects an option from the dropdown menu to order the items present on the screen
     *
     * @param order the text as it appears on the dropdown menu used to sort the products
     */
    public void orderPageItemsUsing(String order) {
        Select dropdown = new Select(driver.findElement(selectOrder));
        dropdown.selectByVisibleText(order);
    }

    /**
     * retirves the name of the items that appear on the screen
     *
     * @return list the items that appear on the screen
     */
    public List<String> getNameOfItems() {
        return driver.findElements(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
