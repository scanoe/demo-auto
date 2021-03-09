package Runners;

import Pages.*;
import Utils.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static Utils.Constants.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestRunner {

    private WebDriver driver;
    private String user;

    /**
     * Initializes the setup for the different webdrivers
     */
    @BeforeSuite
    public void setDrivers() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
    }

    /**
     * Given a browser name initializes the webdriver and opens the browser
     *
     * @param browser browser to be used in the test default as Chrome
     */
    @BeforeTest
    @Parameters({"BROWSER", "USER"})

    public void setUp(@Optional("FIRE_FOX") String browser, @Optional("STANDARD") String user) {
        if (browser.equals(Constants.CHROME)) driver = new ChromeDriver();
        else if (browser.equals(Constants.FIRE_FOX)) driver = new FirefoxDriver();
        else if (browser.equals(Constants.EDGE)) driver = new EdgeDriver();
        else driver = new ChromeDriver();

        if (user.equals("STANDARD")) this.user = STANDARD_USER;
        else if (user.equals("PERFORMANCE_GLITCHED_USER")) this.user = PERFORMANCE_GLITCH_USER;
        else if (user.equals("PROBLEM_USER")) this.user = PROBLEM_USER;
        else if (USER_FIRST_NAME.equals("LOCKED_OUT_USER")) this.user = BLOCKED_OUT_USER;

    }

    /**
     * Basic login page with default user
     */
    @Test
    public void loginAsNormalUser() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        assertThat(productpage.getBannerText(), equalTo(PRODUCTS_BANNER_TEXT));
    }

    /**
     * Checks the log out option from the hamburger menu
     */
    @Test(priority = 4)
    public void logOut() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.clickOnPageOptions();
        login = productpage.ClickLogOut();
        assertTrue(login.LoginEnabled());

    }

    /**
     * Locked out user message test
     */
    @Test
    public void logInAsBlockedUser() {
        LoginPage login = new LoginPage(driver);
        login.loginWith(BLOCKED_OUT_USER, LOGIN_PASSWORD);
        assertThat(login.fetchFailedLoginMessage(), equalTo(LOCKED_OUT_MESSAGE));

    }

    /**
     * Wrong username password combination
     */
    @Test
    public void wrongPasswordLogin() {
        LoginPage login = new LoginPage(driver);
        login.loginWith(BLOCKED_OUT_USER, INCORRECT_PASSWORD);
        assertThat(login.fetchFailedLoginMessage(), equalTo(INCORRECT_PASSWORD_OR_USER_MESSAGE));
    }

    /**
     * Tries to open the items list page directly without logging
     */
    @Test(dependsOnMethods = "logOut")
    public void openItemsPageDirectly() {
        ProductPage productpage = new ProductPage(driver);
        LoginPage login = productpage.openPage();
        assertThat(login.fetchFailedLoginMessage(), equalTo(MESSAGE_MUST_LOGIN_BEFORE_INVENTORY));
    }

    /**
     * Checks the functionality of adding products to the cart
     */
    @Test(priority = 2)
    public void addProductsToCart() {
        LoginPage login = new LoginPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        softAssert.assertEquals(productpage.getBannerText(), PRODUCTS_BANNER_TEXT);

        productpage.addToCart(Constants.ITEMS_TO_BUY);
        softAssert.assertEquals(productpage.getNumberOfItemsOnCartIcon(), ITEMS_TO_BUY.size());
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        assertThat(cartPage.getBannerText(), equalTo(Constants.CART_PAGE_BANNER));
    }

    /**
     * Checks the button to remove objects from the cart
     */
    @Test(priority = 1)
    public void removeProductsFromCart() {
        LoginPage login = new LoginPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.addToCart(Constants.ITEMS_TO_BUY);
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        cartPage.removeItemsFromCart(ITEMS_TO_REMOVE_FROM_CART);
        int currentElementsInCar = ITEMS_TO_BUY.size() - ITEMS_TO_REMOVE_FROM_CART.size();
        softAssert.assertEquals(cartPage.GetNumberOfElementsFromCarIcon(), currentElementsInCar);
        softAssert.assertEquals(cartPage.getNamesOfElementsOnCart().size(), ITEMS_TO_BUY.size() - ITEMS_TO_REMOVE_FROM_CART.size());
        assertThat(cartPage.getNamesOfElementsOnCart(), not(containsInAnyOrder(ITEMS_TO_REMOVE_FROM_CART)));

    }

    /**
     * Checks the complete flow in which the user adds items to the cart and the proceeds with the payment
     */
    @Test
    public void buyCartItems() {
        LoginPage login = new LoginPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.addToCart(Constants.ITEMS_TO_BUY);
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        ShipmentInformationPage shipmentInformationPage = cartPage.clickCheckout();
        shipmentInformationPage.fillUserName(USER_FIRST_NAME);
        shipmentInformationPage.fillLastName(USER_LAST_NAME);
        shipmentInformationPage.fillPostalCode(USER_POSTAL_CODE);
        PaymentConfirmationPage confirmation = shipmentInformationPage.clickOnContinue();
        softAssert.assertEquals(confirmation.getNamesOfShoppingList(), ITEMS_TO_BUY);
        OrderSentPage orderSentPage = confirmation.clickOnFinish();
        assertThat(orderSentPage.getThankYoumessage(), equalTo(THANK_YOU_CONFIRMATION_MESSAGE));
    }

    /**
     * checks that the sum of the prices of the elements inside the shopping cart is the same as the one that appears as subtotal
     */
    @Test(priority = 2)
    public void checkPriceTax() {
        LoginPage login = new LoginPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.addToCart(ITEMS_TO_BUY);
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        ShipmentInformationPage shipmentInformationPage = cartPage.clickCheckout();
        shipmentInformationPage.fillUserName(USER_FIRST_NAME);
        shipmentInformationPage.fillLastName(USER_LAST_NAME);
        shipmentInformationPage.fillPostalCode(USER_POSTAL_CODE);
        PaymentConfirmationPage confirmation = shipmentInformationPage.clickOnContinue();
        float calculatedSubtotal = confirmation.calculateTotalWithoutTax();
        float calculatedTax = confirmation.calculateTax();
        softAssert.assertEquals(calculatedTax, confirmation.getTax());
        softAssert.assertEquals(calculatedSubtotal, confirmation.getPriceWihthoutTax());
        assertThat(calculatedSubtotal + calculatedTax, equalTo(confirmation.getTotalPrice()));
    }

    /**
     * checks the option to access the items description page from the inventory
     */
    @Test
    public void enterItemDescription() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        ItemDetailPage itemDetail = productpage.openItemDescription(Constants.ITEM_FOR_DESCRIPTION);
        assertThat(itemDetail.getProductName(), equalTo(ITEM_FOR_DESCRIPTION));
    }

    /**
     * checks the functionality of adding an item to the shopping cart from the item description
     */
    @Test(priority = 2)
    public void addItemFromDescription() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        ItemDetailPage itemDetail = productpage.openItemDescription(Constants.ITEM_FOR_DESCRIPTION);
        itemDetail.addToCart();
        ShoppingCartPage cartPage = itemDetail.clickOnCartIcon();
        assertThat(cartPage.getNamesOfElementsOnCart(), hasItems(ITEM_FOR_DESCRIPTION));
    }

    /**
     * checks the functionality to order items present in the inventory
     */
    @Test
    public void orderElementsInIProducstsPage() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.orderPageItemsUsing(REVERSE_ALPHABETICAL_ORDER);
        assertThat(productpage.getNameOfItems(), equalTo(Constants.REVERSE_ALPHABETICAL_ORDERED_LIST_OF_ITEMS));
    }

    @Test(priority = 3)
    public void shipmentInformationEmpty() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.addToCart(ITEMS_TO_BUY);
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        ShipmentInformationPage shipmentInformationPage = cartPage.clickCheckout();
        shipmentInformationPage.clickOnContinue();
        assertThat(shipmentInformationPage.getErrorMessage(), equalTo(ERROR_FIRST_NAME_REQUIRED));
    }

    /**
     * Checks the cancellation of the process of entering shipping information
     */
    @Test(priority = 3)
    public void cancelProcessAtShipmentInformation() {
        LoginPage login = new LoginPage(driver);
        ProductPage productpage = login.loginWith(user, LOGIN_PASSWORD);
        productpage.addToCart(ITEMS_TO_BUY);
        ShoppingCartPage cartPage = productpage.ClickOnBuyCart();
        ShipmentInformationPage shipmentInformationPage = cartPage.clickCheckout();
        cartPage = shipmentInformationPage.clickOnCancel();
        assertThat(cartPage.getBannerText(), equalTo(CART_PAGE_BANNER));
    }

    /**
     * tear downs procedure
     */
    @AfterTest
    public void TearDown() {
        driver.close();
        driver = null;
    }


}
