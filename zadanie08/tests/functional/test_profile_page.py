import pytest
from selenium.webdriver import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from utils.test_utils import login, verify_page_title, wait_and_get, get_danger_text, nav_logged


# 18 asserts

def get_profile_inputs(selenium):
    name = wait_and_get(selenium, "input#given_name")
    surname = wait_and_get(selenium, "input#surname")
    phone = wait_and_get(selenium, "input#phone")
    address = wait_and_get(selenium, "input#address")
    email = wait_and_get(selenium, "input#email")
    return name, surname, phone, address, email


@pytest.mark.nondestructive
def test_profile_content(selenium, base_url):
    login(selenium, base_url)
    h1 = selenium.find_element(By.CSS_SELECTOR, value="h1")
    assert h1.text == "Profil Użytkownika"

    inputs = selenium.find_elements(By.CSS_SELECTOR, value="input")
    assert len(inputs) == 5

    logout = selenium.find_element(By.CSS_SELECTOR, value="a.logout-button")
    assert logout.text == "Wyloguj się"

    button = selenium.find_element(By.CSS_SELECTOR, value="button")
    assert button.text == "Zapisz"


@pytest.mark.nondestructive
def test_profile_input_labels(selenium, base_url):
    login(selenium, base_url)
    email_label = selenium.find_element(By.CSS_SELECTOR, value="label[for='email']")
    assert email_label.text == "Email:"
    name_label = selenium.find_element(By.CSS_SELECTOR, value="label[for='given_name']")
    assert name_label.text == "Imię:"
    surname_label = selenium.find_element(By.CSS_SELECTOR, value="label[for='surname']")
    assert surname_label.text == "Nazwisko:"
    phone_label = selenium.find_element(By.CSS_SELECTOR, value="label[for='phone']")
    assert phone_label.text == "Numer Telefonu:"
    address_label = selenium.find_element(By.CSS_SELECTOR, value="label[for='address']")
    assert address_label.text == "Adres:"


@pytest.mark.nondestructive
def test_title_and_logout(selenium, base_url):
    login(selenium, base_url)
    logout_btn = wait_and_get(selenium, "a.logout-button")
    verify_page_title(selenium, "Profil")
    logout_btn.click()
    WebDriverWait(selenium, 10).until(
        EC.url_to_be(base_url + "/login")
    )
    assert selenium.current_url == base_url + "/login"


@pytest.mark.nondestructive
def test_profile_invalid(selenium, base_url):
    login(selenium, base_url)
    name, surname, phone, address, email = get_profile_inputs(selenium)
    assert name and surname and phone and address and email
    assert not email.is_enabled()
    for elem in (name, surname, phone, address, email):
        assert elem.is_displayed()
    phone.clear()
    phone.send_keys("555")
    phone.send_keys(Keys.ENTER)
    danger = get_danger_text(selenium)
    assert danger.is_displayed() and danger.text == "Numer telefonu musi składać się z dokładnie 9 cyfr."


@pytest.mark.nondestructive
def test_profile_correct(selenium, base_url):
    login(selenium, base_url)
    name, surname, phone, address, email = get_profile_inputs(selenium)
    save = wait_and_get(selenium, "button.formButton")
    for elem in (name, surname, phone, address, email):
        if elem.is_enabled():
            elem.clear()
    phone.send_keys("555111222")
    name.send_keys("NAME")
    surname.send_keys("SURNAME")
    address.send_keys("ADDRESS")
    address.send_keys(Keys.ENTER)
    WebDriverWait(selenium, 10).until(
        lambda d: get_profile_inputs(d)[0].get_attribute("value") == "NAME"
    )
    name, surname, phone, address, email = get_profile_inputs(selenium)
    assert name.get_attribute("value") == "NAME"
    assert surname.get_attribute("value") == "SURNAME"
    assert phone.get_attribute("value") == "555111222"
    assert address.get_attribute("value") == "ADDRESS"
