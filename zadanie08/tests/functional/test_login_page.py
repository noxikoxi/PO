import pytest
from selenium.webdriver import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from utils.test_utils import verify_page_title, wait_and_get, get_email_password, get_danger_text, main_page_link, login


# 14 Asserts

def get_curr_url(base_url):
    return base_url + "/login"


@pytest.mark.nondestructive
def test_login_without_credentials(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    login_btn = selenium.find_element(By.CSS_SELECTOR, "button.formButton")
    login_btn.click()
    assert selenium.current_url == base_url + "/login"


def prepare_test(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    login_btn = wait_and_get(selenium, "button.formButton")
    email, password = get_email_password(selenium)
    return login_btn, email, password


@pytest.mark.nondestructive
def test_login_empty(selenium, base_url):
    login_btn, _, _ = prepare_test(selenium, base_url)
    login_btn.click()
    assert selenium.current_url == get_curr_url(base_url)


@pytest.mark.nondestructive
def test_login_content(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    h3 = selenium.find_element(By.CSS_SELECTOR, "h3")
    assert h3.text == "Logowanie"
    registerLink = selenium.find_element(By.CSS_SELECTOR, "a[href='/register']")
    assert registerLink.text == "Nie masz jeszcze konta? Zarejestruj się!"
    email_label = selenium.find_element(By.CSS_SELECTOR, "label[for='email']")
    assert email_label.text == "Email:"
    password_label = selenium.find_element(By.CSS_SELECTOR, "label[for='password'")
    assert password_label.text == "Hasło:"


@pytest.mark.nondestructive
def test_login_bad_email(selenium, base_url):
    login_btn, email, password = prepare_test(selenium, base_url)
    assert password.get_attribute("type") == "password"
    email.send_keys("testowy")
    password.send_keys("testowy")
    password.send_keys(Keys.ENTER)
    assert selenium.current_url == get_curr_url(base_url)
    danger = get_danger_text(selenium)
    assert danger.text == "Nieprawidłowy email"
    assert selenium.current_url == get_curr_url(base_url)


@pytest.mark.nondestructive
def test_invalid_credentials(selenium, base_url):
    login_btn, email, password = prepare_test(selenium, base_url)
    email.send_keys("testowy@wp.pl")
    password.send_keys("testowy")
    password.send_keys(Keys.ENTER)
    danger = get_danger_text(selenium)
    assert danger.text == "Nieprawidłowe dane logowania"
    assert selenium.current_url == get_curr_url(base_url)


@pytest.mark.nondestructive
def test_title(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    verify_page_title(selenium, "login")


@pytest.mark.nondestructive
def test_link(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    link = wait_and_get(selenium, "a.additionalInfo")
    link.click()
    WebDriverWait(selenium, 10).until(EC.url_to_be(base_url + "/register"))
    assert selenium.current_url == base_url + "/register"


@pytest.mark.nondestructive
def test_main_page_link(selenium, base_url):
    main_page_link(selenium, base_url, get_curr_url(base_url))


@pytest.mark.nondestructive
def test_correct_login(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    login(selenium, base_url)
    WebDriverWait(selenium, 10).until(EC.url_contains("users"))
    h1 = selenium.find_element(By.CSS_SELECTOR, "h1")
    assert h1.text == "Profil Użytkownika"
