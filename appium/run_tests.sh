#!/usr/bin/env bash
#
# Purpose:
#   This script automates running Appium-based login tests for the IHRM Android app.
#
# How it works:
#   - Ensures script runs from its own directory.
#   - Activates or creates a Python virtual environment as needed.
#   - Installs required Python dependencies.
#   - Verifies the Android APK file exists (default: debug build output).
#   - Runs pytest, passing any user-supplied arguments.
#
# Prerequisites:
#   - Appium server running (http://127.0.0.1:4723)
#   - Android device or emulator ready
#   - The app already built (debug APK)
# Run Appium login tests for IHRM.
# Prereqs: Appium server (http://127.0.0.1:4723), Android device/emulator, app built.
set -e  # Exit immediately if a command exits with a non-zero status

# Get the directory where this script is located and switch to it
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# --- Python Virtual Environment Setup (for beginners) ---

# Check if we're inside a Python virtual environment (VIRTUAL_ENV is empty if not)
if [[ -z "${VIRTUAL_ENV}" ]]; then
  # If the ".venv" directory does not exist, create a new virtual environment there
  if [[ ! -d .venv ]]; then
    python3 -m venv .venv  # Create a virtual environment in .venv folder using Python 3
  fi
  . .venv/bin/activate  # Activate the virtual environment
  pip install -r requirements.txt  # Install dependencies from requirements.txt inside the venv
fi

# --- APK File Check (Make sure the app is built) ---

# Set ROOT to the parent directory of this script ("project root")
ROOT="$(cd .. && pwd)"

# Choose the APK to use:
#   - If the IHRM_APK environment variable is set, use its value.
#   - Otherwise, use the default debug APK path.
APK="${IHRM_APK:-$ROOT/app/build/outputs/apk/debug/app-debug.apk}"

# Check if the APK file actually exists
if [[ ! -f "$APK" ]]; then
  # If not found, print error and instructions for building it, then quit the script
  echo "APK not found at $APK. Build with: cd $ROOT && ./gradlew assembleDebug"
  exit 1
fi

# Export the APK path so tests can find it (makes it available to child processes)
export IHRM_APK="$APK"

# --- Run the actual tests ---

# "$@" passes any extra arguments you provided to pytest (such as "-v", "-k", etc)
pytest "$@"
#python -m pytest "$@"