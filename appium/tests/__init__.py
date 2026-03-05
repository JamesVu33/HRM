# Appium tests

# Path	                    Purpose
#   appium/config.py	        App package, activity, APK path, waits, Appium URL
#   appium/conftest.py	        Pytest fixtures: driver (UiAutomator2), capabilities
#   appium/pages/login_page.py	Page object: Welcome, Username, Password, Login, Forgot Password, errors
#   appium/tests/test_login.py	Four login tests (see below)
#   appium/requirements.txt	    Appium-Python-Client, selenium, pytest, pytest-html
#   appium/pytest.ini	        testpaths = tests, pythonpath = .
#   appium/run_tests.sh	        Helper to install deps, check APK, run pytest
