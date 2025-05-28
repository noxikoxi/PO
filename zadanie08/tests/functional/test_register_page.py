import pytest
from selenium.webdriver import Keys
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from utils.test_utils import verify_page_title, wait_and_get, get_email_password, get_danger_text, main_page_link
from test_admin import delete_user


# 15 asserts

def get_curr_url(base_url):
    return base_url + "/register"


@pytest.mark.nondestructive
def test_title(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    verify_page_title(selenium, "register")


@pytest.mark.nondestructive
def test_link(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    link = wait_and_get(selenium, "a.additionalInfo")
    link.click()
    assert selenium.current_url == base_url + "/login"


@pytest.mark.nondestructive
def test_register_content(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    h3 = selenium.find_element(By.CSS_SELECTOR, "h3")
    assert h3.text == "Rejestracja"
    registerLink = selenium.find_element(By.CSS_SELECTOR, "a[href='/login']")
    assert registerLink.text == "Masz już konto? Zaloguj się!"
    email_label = selenium.find_element(By.CSS_SELECTOR, "label[for='email']")
    assert email_label.text == "Email:"
    password_label = selenium.find_element(By.CSS_SELECTOR, "label[for='password'")
    assert password_label.text == "Hasło:"


def prepare_test(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    register_btn = wait_and_get(selenium, "button.formButton")
    email, password = get_email_password(selenium)
    return register_btn, email, password


@pytest.mark.nondestructive
def test_register_empty(selenium, base_url):
    register_btn, email, password = prepare_test(selenium, base_url)
    assert password.get_attribute("type") == "password"
    register_btn.click()
    assert selenium.current_url == get_curr_url(base_url)


@pytest.mark.nondestructive
def test_register_bad_email(selenium, base_url):
    register_btn, email, password = prepare_test(selenium, base_url)
    email.send_keys("testowy")
    password.send_keys("testowy12345")
    password.send_keys(Keys.ENTER)
    assert selenium.current_url == get_curr_url(base_url)
    danger = get_danger_text(selenium)
    assert danger.text == "Nieprawidłowy email"


@pytest.mark.nondestructive
def test_register_bad_password(selenium, base_url):
    register_btn, email, password = prepare_test(selenium, base_url)
    email.send_keys("test12345@wp.pl")
    password.send_keys("test")
    password.send_keys(Keys.ENTER)
    assert selenium.current_url == get_curr_url(base_url)
    danger = get_danger_text(selenium)
    assert danger.text == "Hasło musi mieć przynajmniej 6 znaków"


@pytest.mark.nondestructive
def test_register_existing_user(selenium, base_url):
    register_btn, email, password = prepare_test(selenium, base_url)
    email.send_keys("testowy@wp.pl")
    password.send_keys("test123456")
    password.send_keys(Keys.ENTER)
    danger = get_danger_text(selenium)
    assert danger.text == "Użytkownik o takim adresie email już istnieje"
    assert selenium.current_url == get_curr_url(base_url)


@pytest.mark.nondestructive
def test_main_page_link(selenium, base_url):
    main_page_link(selenium, base_url, get_curr_url(base_url))


@pytest.mark.nondestructive
def test_register_correct(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    login, password = get_email_password(selenium)

    login.send_keys("testowy123@wp.pl")
    password.send_keys("testowyPassword")
    password.send_keys(Keys.ENTER)
    WebDriverWait(selenium, 10).until(EC.url_contains("login"))

    assert selenium.current_url == base_url + "/login"

    delete_user(selenium, base_url, email_text="testowy123@wp.pl")
