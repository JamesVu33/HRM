"""Page object for LoginScreen (LoginScreen.kt)."""

# NOTE: This file implements a "Page Object" for Appium: 
# It abstracts/simplifies test automation interactions with the mobile login screen in Python.
# If you have no experience with Appium and Python, read the comments below each method for clarity.

import time
from typing import Optional

from appium.webdriver import WebElement
from appium.webdriver.common.appiumby import AppiumBy
from appium.webdriver.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from config import EXPLICIT_WAIT, SPLASH_DURATION_SEC


class LoginPage:
    """Page object model for interacting with the LoginScreen (as defined in the Android/Kotlin app)."""

    # App texts for locating elements or checking messages (from LoginScreen.kt + LoginViewModel.kt)
    WELCOME = "Welcome"
    SUBTITLE = "Enter your credentials to access your account"
    LOGIN_BTN = "Login"
    FORGOT_PWD = "Forgot your password?"
    ERR_EMAIL_REQUIRED = "Email is required"
    ERR_PASSWORD_REQUIRED = "Password is required"
    ERR_EMAIL_INVALID = "Please enter a valid email address"
    ERR_PASSWORD_INVALID = "Password must be 8-16 characters and include uppercase, lowercase, number, and special character."

    def __init__(self, driver: WebDriver, wait: int = EXPLICIT_WAIT):
        """
        :param driver: Appium WebDriver instance connected to the app/emulator
        :param wait: How long to wait (seconds) for UI elements to appear
        """
        self.driver = driver
        self.wait = WebDriverWait(driver, wait)

    def wait_for_login_screen(self) -> None:
        """
        Waits for the splash to finish, then for the Login UI ('Welcome' text).
        This is called at the start to make sure we're on the login page before trying to interact.
        """
        time.sleep(SPLASH_DURATION_SEC + 0.5)  # Wait for splash screen to disappear
        self.wait.until(
            EC.presence_of_element_located(
                (AppiumBy.ANDROID_UIAUTOMATOR, f'new UiSelector().text("{self.WELCOME}")')
            )
        )

    def _email_field(self) -> WebElement:
        """
        Returns the email EditText input element.
        Many Compose screens expose EditText fields this way.
        Raises error if not found.
        """
        els = self.driver.find_elements(AppiumBy.CLASS_NAME, "android.widget.EditText")
        if len(els) < 2:
            raise AssertionError("Expected at least 2 EditText (Email, Password)")
        return els[0]

    def _password_field(self) -> WebElement:
        """
        Returns the password EditText input element.
        Raises error if not found.
        """
        els = self.driver.find_elements(AppiumBy.CLASS_NAME, "android.widget.EditText")
        if len(els) < 2:
            raise AssertionError("Expected at least 2 EditText (Email, Password)")
        return els[1]

    def _login_button(self) -> WebElement:
        """
        Returns the Login button element, waiting for it to become clickable.
        """
        return self.wait.until(
            EC.element_to_be_clickable(
                (
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    f'new UiSelector().text("{self.LOGIN_BTN}")',
                )
            )
        )

    def _forgot_password_button(self) -> WebElement:
        """
        Returns the 'Forgot Password?' link/button element.
        """
        return self.wait.until(
            EC.presence_of_element_located(
                (
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    f'new UiSelector().text("{self.FORGOT_PWD}")',
                )
            )
        )

    def enter_email(self, value: str) -> None:
        """
        Types the provided email in the Email field.
        """
        self._email_field().clear()
        self._email_field().send_keys(value)

    def enter_password(self, value: str) -> None:
        """
        Types the provided password in the Password field.
        """
        self._password_field().clear()
        self._password_field().send_keys(value)

    def tap_login(self) -> None:
        """
        Clicks the Login button.
        """
        self._login_button().click()

    def tap_forgot_password(self) -> None:
        """
        Clicks the 'Forgot Password?' link/button.
        """
        self._forgot_password_button().click()

    def login(self, email: str, password: str) -> None:
        """
        High-level: fills in both fields and taps Login.
        """
        self.enter_email(email)
        self.enter_password(password)
        self.tap_login()

    def is_welcome_visible(self) -> bool:
        """
        Checks if the welcome message is visible.
        Returns True if found, False otherwise.
        """
        try:
            self.driver.find_element(
                AppiumBy.ANDROID_UIAUTOMATOR,
                f'new UiSelector().text("{self.WELCOME}")',
            )
            return True
        except Exception:
            return False

    def get_error_text(self) -> Optional[str]:
        """
        Checks if any known (validation) error messages are shown.
        Returns the text, or None if not present.
        """
        for msg in (
            self.ERR_EMAIL_REQUIRED,
            self.ERR_PASSWORD_REQUIRED,
            self.ERR_EMAIL_INVALID,
            self.ERR_PASSWORD_INVALID,
        ):
            try:
                el = self.driver.find_element(
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    f'new UiSelector().text("{msg}")',
                )
                return el.text
            except Exception:
                continue
        return None

    def is_forgot_password_visible(self) -> bool:
        """
        Checks if the 'Forgot Password?' element is visible.
        """
        try:
            self._forgot_password_button()
            return True
        except Exception:
            return False

# If you are new to Python + Appium:
# - 'WebDriver' is the session for automating your mobile app.
# - 'find_element' and 'find_elements' let you locate onscreen views by criteria.
# - Methods starting with '_' are "private" helpers (convention); public methods interact with the screen.
# - Always wrap UI checks/interactions in try/except to handle timing issues.