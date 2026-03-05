"""
Pytest fixtures to automate launching and controlling the IHRM Android app using Appium during UI tests.

- Provides utilities for consistent driver creation and environment-based configuration.
"""

import logging
import os
import pytest
from appium import webdriver
from appium.options.android import UiAutomator2Options

from config import (
    APP_ACTIVITY,
    APP_PACKAGE,
    APPIUM_URL,
    DEFAULT_APK,
    EXPLICIT_WAIT,
    IMPLICIT_WAIT,
)

# Logger for test steps, inputs, and results (see pytest.ini log_cli for console output)
logger = logging.getLogger("appium.ihrm")

def _build_capabilities(use_apk: bool) -> UiAutomator2Options:
    """
    Creates an options object needed by Appium for Android automation.

    - Reads device, app, and automation settings from environment variables or config.
    - APK path can be dynamically overridden for CI or custom builds.
    """
    opts = UiAutomator2Options()
    opts.platform_name = "Android"                       # Must be 'Android' for Android test automation
    opts.automation_name = "UiAutomator2"                # Most robust engine for modern Android automation
    opts.app_package = APP_PACKAGE                       # e.g., "com.example.ihrm"
    opts.app_activity = APP_ACTIVITY                     # e.g., ".MainActivity"
    opts.device_name = os.environ.get("ANDROID_DEVICE", "Android Emulator")  # Use an emulator or device name from env
    opts.no_reset = False                                # Ensures the app launches with cleared data
    opts.full_reset = False                              # Avoids uninstalling the app for every test run

    # APK to install: can override via IHRM_APK env var for testing different builds/apks
    apk = os.environ.get("IHRM_APK", DEFAULT_APK)
    if use_apk and apk and os.path.isfile(apk):
        opts.app = apk                                   # Path to the .apk file to be installed
    return opts

@pytest.fixture(scope="session")
def appium_url() -> str:
    """
    Get the Appium server URL for this test session.
    Allows overriding via APPIUM_URL environment variable (useful for Docker, CI, remote devices).
    """
    return os.environ.get("APPIUM_URL", APPIUM_URL)

@pytest.fixture
def driver(appium_url):
    """
    Main pytest fixture for test cases needing the Android driver.

    - Instantiates an Appium driver for the IHRM app.
    - Decides whether to (re)install a custom APK build based on IHRM_USE_APK env var.
    - Yields the configured driver for test(s).
    - Handles clean driver teardown after each use.
    """
    use_apk = os.environ.get("IHRM_USE_APK", "1") == "1"
    caps = _build_capabilities(use_apk=use_apk)
    _driver = webdriver.Remote(appium_url, options=caps)
    _driver.implicitly_wait(IMPLICIT_WAIT)  # Implicit wait for finding UI elements
    logger.info("Driver started, app launched")
    yield _driver
    _driver.quit()
    logger.info("Driver quit, session closed")
