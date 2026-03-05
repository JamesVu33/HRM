"""Appium tests for LoginScreen (LoginScreen.kt + LoginViewModel.kt)."""
import logging
import pytest
from appium.webdriver.common.appiumby import AppiumBy
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from config import EXPLICIT_WAIT
from pages.login_page import LoginPage

log = logging.getLogger("appium.ihrm")

# Valid test credentials (ViewModel: email format, password 8-16 chars + upper, lower, digit, special)
VALID_EMAIL = "test@example.com"
VALID_PASSWORD = "Test@1234"


def _is_on_employee_list(driver) -> bool:
    """EmployeeListScreen shows 'No employees found' or 'Employee List Screen'."""
    wait = WebDriverWait(driver, EXPLICIT_WAIT)
    try:
        wait.until(
            EC.presence_of_element_located(
                (
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    'new UiSelector().textContains("No employees found")',
                )
            )
        )
        return True
    except Exception:
        pass
    try:
        wait.until(
            EC.presence_of_element_located(
                (
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    'new UiSelector().textContains("Employee List Screen")',
                )
            )
        )
        return True
    except Exception:
        pass
    return False


def _log_step(msg: str) -> None:
    log.info("STEP: %s", msg)


def _log_input(field: str, value: str, masked: bool = False) -> None:
    display = "****" if masked else value
    log.info("INPUT: %s = %s", field, display)


def _log_result(msg: str) -> None:
    log.info("RESULT: %s", msg + "\n\n")


@pytest.mark.usefixtures("driver")
class TestLogin:
    """Login flow: success, validation errors, Forgot Password."""

    def test_login_success_navigates_to_employee_list(self, driver):
        """Valid email + password -> Login -> EmployeeList."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        _log_input("email", VALID_EMAIL)
        _log_input("password", VALID_PASSWORD, masked=True)
        _log_step("Tap Login button")
        page.login(VALID_EMAIL, VALID_PASSWORD)
        on_list = _is_on_employee_list(driver)
        _log_result("Landed on Employee List" if on_list else "Did not reach Employee List")
        assert on_list, "Expected to land on Employee List after login"

    def test_login_empty_email_shows_error(self, driver):
        """Tap Login with empty email -> 'Email is required'."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        _log_input("email", "(empty)")
        _log_input("password", "(empty)")
        _log_step("Tap Login with empty fields")
        page.tap_login()
        err = page.get_error_text()
        _log_result(f"Error shown on UI: {err}" if err else "No error shown")
        assert err == LoginPage.ERR_EMAIL_REQUIRED, f"Expected '{LoginPage.ERR_EMAIL_REQUIRED}', got {err}"

    def test_login_empty_password_shows_error(self, driver):
        """Email filled, password empty, tap Login -> 'Password is required'."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        _log_input("email", VALID_EMAIL)
        _log_input("password", "(empty)")
        _log_step("Enter email, leave password empty, tap Login")
        page.enter_email(VALID_EMAIL)
        page.tap_login()
        err = page.get_error_text()
        _log_result(f"Error shown on UI: {err}" if err else "No error shown")
        assert err == LoginPage.ERR_PASSWORD_REQUIRED, f"Expected '{LoginPage.ERR_PASSWORD_REQUIRED}', got {err}"

    def test_login_invalid_email_shows_error(self, driver):
        """Invalid email format -> 'Please enter a valid email address'."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        _log_input("email", "invalid-email")
        _log_input("password", VALID_PASSWORD, masked=True)
        _log_step("Tap Login with invalid email format")
        page.login("invalid-email", VALID_PASSWORD)
        err = page.get_error_text()
        _log_result(f"Error shown on UI: {err}" if err else "No error shown")
        assert err == LoginPage.ERR_EMAIL_INVALID, f"Expected '{LoginPage.ERR_EMAIL_INVALID}', got {err}"

    def test_login_invalid_password_shows_error(self, driver):
        """Password fails validation (no upper/lower/digit/special) -> password error."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        _log_input("email", VALID_EMAIL)
        _log_input("password", "short")
        _log_step("Tap Login with invalid password (too short)")
        page.login(VALID_EMAIL, "short")
        err = page.get_error_text()
        _log_result(f"Error shown on UI: {err}" if err else "No error shown")
        assert err == LoginPage.ERR_PASSWORD_INVALID, f"Expected '{LoginPage.ERR_PASSWORD_INVALID}', got {err}"

    def test_login_screen_displays_welcome_and_forgot_password(self, driver):
        """Login UI shows 'Welcome' and 'Forgot your password?'."""
        page = LoginPage(driver)
        _log_step("Wait for login screen")
        page.wait_for_login_screen()
        welcome_ok = page.is_welcome_visible()
        forgot_ok = page.is_forgot_password_visible()
        _log_result(f"Welcome visible: {welcome_ok}, Forgot password visible: {forgot_ok}")
        assert welcome_ok, "Welcome should be visible"
        assert forgot_ok, "Forgot your password? should be visible"
