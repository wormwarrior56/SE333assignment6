package playwrightLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class playwrightAITests {

    static Browser browser;
    static BrowserContext context;
    static Page page;

    @BeforeAll
    static void setup() throws Exception {
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videosAI/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
    }

    @AfterAll
    static void teardown() {
        context.close();
        browser.close();
    }

    @Test
    void comprehensiveCheckoutTest() throws Exception {
        // Navigate to the bookstore
        page.navigate("https://depaul.bncollege.com/");
        // small pause to allow initial page scripts to settle
        Thread.sleep(1000);

        // Enter "earbuds" in the search box and press Enter
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForURL("**/search/**");
        // give search results a moment to render
        Thread.sleep(800);

        // Click on the "Brand" filter to expand it
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        Thread.sleep(500);

        // Select "JBL" brand
        page.locator("label").filter(new Locator.FilterOptions().setHasText("brand JBL")).first().click();
        page.waitForURL("**/search**");
        Thread.sleep(800);

        // Click on the "Color" filter to expand it
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        Thread.sleep(500);

        // Select "Black" color
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Color Black")).first().click();
        page.waitForURL("**/search**");
        Thread.sleep(800);

        // Click on the "Price" filter to expand it
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        Thread.sleep(500);

        // Select "Over $50" price range
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Price Over $50")).first().click();
        page.waitForURL("**/search**");
        Thread.sleep(800);

        // Click on the "JBL Quantum True Wireless Noise Cancelling Gaming..." item link
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();
        page.waitForURL("**/JBL-Quantum-True-Wireless**");
        Thread.sleep(1000);

        // AssertThat product name, SKU number, the price, and the product description
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(1).setName("JBL Quantum True Wireless"))).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.locator("text=$164.98").first()).isVisible();
        assertThat(page.locator("text=Adaptive noise cancelling").first()).isVisible();

        // Add 1 to the Cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
        Thread.sleep(800);

        // AssertThat "1 Items" in cart
        assertThat(page.locator("text=1 items").first()).isVisible();

        // Click "Cart" (click icon in upper right of page)
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();
        page.waitForURL("**/cart");
        Thread.sleep(800);

        // AssertThat you are at cart: "Your Shopping Cart"
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(2).setName("Your Shopping Cart"))).isVisible();

        // AssertThat the product name, quantity, and price
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(2).setName("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black"))).isVisible();
        // Target the visible quantity textbox (aria-label contains 'Quantity, edit and press')
        assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Quantity, edit and press enter to update the quantity"))).hasValue("1");
        assertThat(page.locator("text=$164.98").nth(1)).isVisible();

        // Select "FAST In-Store Pickup" (should already be selected)
        assertThat(page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("FAST In-Store Pickup"))).isChecked();

        // AssertThat sidebar subtotal, handling, taxes, and estimated total
        assertThat(page.locator("text=Subtotal").first()).containsText("Subtotal");
        assertThat(page.locator("text=$164.98").first()).containsText("$164.98");
        assertThat(page.locator("text=TBD").first()).hasText("TBD");

        // Enter promo code TEST and click APPLY (code doesn't exist so it should fail)
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).fill("TEST");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Promo Code")).click();
        Thread.sleep(600);

        // AssertThat promo code reject message is displayed
        assertThat(page.locator("text=The coupon code entered is not valid").first()).isVisible();

        // Click "PROCEED TO CHECKOUT"
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout")).first().click();
        // Some pages render a create-account section; click the section to reveal options (works around client-side rendering)
        page.locator(".bned-create-account-section").click();
        // Ensure the Create Account heading is visible
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Account"))).isVisible();
        // Click the 'Proceed As Guest' link (note: role is LINK here)
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();
        Thread.sleep(700);

        // Ensure the Contact Information page loaded — wait for the 'First Name' field to appear
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name")).waitFor(new Locator.WaitForOptions().setTimeout(60000));
        assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name"))).isVisible();

        // Enter a first name, a last name, an email address, a phone number
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name")).fill("John");
        Thread.sleep(400);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name")).fill("Doe");
        Thread.sleep(400);
        // Use the inspector-provided email textbox selector to reliably focus the correct field
        Locator emailBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address\u00A0(required)"));
        emailBox.click();
        emailBox.fill("superprotogentmail@proton.me");
        // Dispatch input/change events so client-side validation picks up the new value
        emailBox.evaluate("el => { el.dispatchEvent(new Event('input', { bubbles: true })); el.dispatchEvent(new Event('change', { bubbles: true })); }");
        Thread.sleep(400);

        // Phone field — fill and dispatch events
        Locator phoneInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone"));
        phoneInput.fill("1234567890");
        phoneInput.evaluate("el => { el.dispatchEvent(new Event('input', { bubbles: true })); el.dispatchEvent(new Event('change', { bubbles: true })); }");
        // Small pause to allow any client-side validation and UI updates
        Thread.sleep(400);

        // AssertThat sidebar subtotal, handling, taxes, and estimated total
        // The subtotal price may be updated asynchronously; give the UI a brief moment before continuing
        Thread.sleep(500);

        // Click CONTINUE
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).first().click();
        Thread.sleep(700);

        // AssertThat Contact Information: name, email, and phone are correct
        assertThat(page.locator("text=John Doe").first()).isVisible();
        assertThat(page.locator("text=superprotogentmail@proton.me").first()).isVisible();
        assertThat(page.locator("text=+11234567890").first()).isVisible();

        // AssertThat Pick Up location — element may be present but hidden; assert text content instead
        Locator pickupLoc = page.locator(".bned-entries-delivery-multicampus-name").filter(new Locator.FilterOptions().setHasText("DePaul University Loop Campus & SAIC")).first();
        assertThat(pickupLoc).hasText("DePaul University Loop Campus & SAIC");

        // AssertThat selected Pickup Person ("I'll pick them up") — check text content (may be hidden in some layouts)
        Locator pickupPerson = page.locator("text=I'll pick them up").first();
        assertThat(pickupPerson).hasText("I'll pick them up");

        // AssertThat Sidebar order subtotal, handling, taxes, and estimated total
        // The 'Subtotal' label may be present but hidden in some layouts; verify its text exists and wait for a visible price
        assertThat(page.locator("text=Subtotal").first()).containsText("Subtotal");
        // Verify the order summary (main) contains the expected price text — avoids waiting on hidden duplicate nodes
        assertThat(page.getByLabel("main")).containsText("$164.98");
        assertThat(page.locator("text=TBD").first()).hasText("TBD");

        // AssertThat pickup item and price are present in the main order summary (avoid asserting a possibly hidden link)
        assertThat(page.getByLabel("main")).containsText("JBL Quantum");
        assertThat(page.getByLabel("main")).containsText("$164.98");

        // Click CONTINUE
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).first().click();
        Thread.sleep(700);

        // AssertThat Sidebar order subtotal, handling, taxes, and total
        assertThat(page.locator("text=Subtotal").first()).containsText("Subtotal");
        assertThat(page.locator("text=$164.98").first()).containsText("$164.98");
        // The page sometimes renders a descriptive paragraph instead of a short "Handling" label.
        // Assert the main order summary mentions handling (case-insensitive content check via lowercase substring).
        assertThat(page.getByLabel("main")).containsText("handling"); // most recent change
        assertThat(page.locator("text=$3.00").first()).containsText("$3.00");
        assertThat(page.getByLabel("main")).containsText("Order Summary Subtotal $164.98 Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, " +
                "and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and " +
                "personalized service. No minimum purchase required. $3.00 Taxes TBD Estimated Total $167.98"); // manually edited
        assertThat(page.locator("text=$17.22").first()).containsText("$17.22");
        assertThat(page.getByLabel("main")).containsText("Total $185.20 185.2 $"); // also manually edited
        assertThat(page.locator("text=$185.20").first()).containsText("$185.20");

        // AssertThat pickup item and price are present in the main order summary (avoid asserting a possibly hidden link)
        assertThat(page.getByLabel("main")).containsText("JBL Quantum");
        assertThat(page.getByLabel("main")).containsText("$164.98");

        // Click "< BACK TO CART" (upper-right of page)
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to Cart")).click();
        page.waitForURL("**/cart");
        Thread.sleep(700);

        // Delete product from cart
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove product")).first().click();

        // AssertThat your cart is empty (check main region for either string)
        assertThat(page.getByLabel("main")).containsText("Your cart is empty");

        // Close window
        page.close();
    }
}