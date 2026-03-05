"""Appium test configuration for IHRM Android app."""
import os

# Project root (parent of appium/)
PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DEFAULT_APK = os.path.join(
    PROJECT_ROOT, "app", "build", "outputs", "apk", "debug", "app-debug.apk"
)

APP_PACKAGE = "com.example.ihrm"
APP_ACTIVITY = "com.example.ihrm.MainActivity"

# Appium server (default for local)
APPIUM_URL = os.environ.get("APPIUM_URL", "http://127.0.0.1:4723")

# Implicit / explicit wait (seconds)
IMPLICIT_WAIT = 5
EXPLICIT_WAIT = 15

# Splash duration before Login appears (from SplashScreen.kt)
SPLASH_DURATION_SEC = 2
