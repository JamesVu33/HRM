# from appium import webdriver
# from appium.options.android import UiAutomator2Options
#
# # 1. Cấu hình để Appium biết cần điều khiển máy nào
# options = UiAutomator2Options()
# options.platform_name = 'Android'
# options.device_name = 'emulator-5554' # Tên máy ảo của bạn (lấy bằng lệnh 'adb devices')
# options.automation_name = 'UiAutomator2'
# options.app_package = 'com.android.settings' # Ví dụ mở app Cài đặt
# options.app_activity = '.Settings'
#
# # 2. Kết nối tới Appium Server
# driver = webdriver.Remote("http://127.0.0.1:4723", options=options)
#
# # 3. In ra thông báo thành công
# print("Đã kết nối và mở ứng dụng thành công!")
#
# # 4. Đóng app
# # driver.quit()