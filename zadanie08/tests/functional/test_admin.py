import pytest
from selenium.webdriver import Keys
from selenium.webdriver.common.alert import Alert
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from utils.test_utils import login, nav_admin, wait_and_get, get_danger_text


# 13 Asserts


def get_create_user_inputs(selenium):
    email = wait_and_get(selenium, "input#email")
    password = wait_and_get(selenium, "input#password")
    return email, password


def get_user_table_row(selenium, row):
    table = WebDriverWait(selenium, 10).until(EC.presence_of_element_located((By.TAG_NAME, "tbody")))
    rows = table.find_elements(By.TAG_NAME, "tr")
    return rows[row]


def delete_user(selenium, base_url, email_text="correct@wp.pl"):
    login(selenium, base_url, admin=True)
    selenium.get(base_url + "/admin/users")
    WebDriverWait(selenium, 10).until(EC.url_contains("admin"))
    initial_rows_count = len(selenium.find_elements(By.CSS_SELECTOR, "table tbody tr"))

    last_row = selenium.find_elements(By.CSS_SELECTOR, "table tbody tr")[-1]
    email = last_row.find_elements(By.CSS_SELECTOR, "td")[3]
    assert email.text == email_text

    # przycisk
    last_row.find_element(By.TAG_NAME, "button").click()

    WebDriverWait(selenium, 10).until(EC.alert_is_present())
    alert = Alert(selenium)
    alert.accept()

    # czekam aż liczba wierszy się zmniejszy
    WebDriverWait(selenium, 10).until(
        lambda d: len(d.find_elements(By.CSS_SELECTOR, "table tbody tr")) < initial_rows_count
    )
    email = get_user_table_row(selenium, -1).find_elements(By.TAG_NAME, "td")[3]
    assert email.text != email_text


@pytest.mark.nondestructive
def test_navigation(selenium, base_url):
    nav_admin(selenium, base_url)


@pytest.mark.nondestructive
def test_login(selenium, base_url):
    login(selenium, base_url, admin=True)
    selenium.get(base_url + "/admin/users")
    WebDriverWait(selenium, 10).until(EC.url_contains("admin"))
    assert selenium.current_url.endswith("/admin/users")


@pytest.mark.nondestructive
def test_users(selenium, base_url):
    login(selenium, base_url, admin=True)
    WebDriverWait(selenium, 10).until(EC.url_contains("users"))
    selenium.get(base_url + "/admin/users")
    WebDriverWait(selenium, 10).until(EC.url_to_be(base_url + "/admin/users"))
    info = wait_and_get(selenium, "p.info.text-lg")
    assert info.text == "Użytkownicy"
    add_user = wait_and_get(selenium, "a.button-like")
    add_user.click()
    WebDriverWait(selenium, 10).until(EC.url_contains("create"))
    assert selenium.current_url == base_url + "/admin/users/create"


def prepare_create_test(selenium, base_url):
    login(selenium, base_url, admin=True)
    selenium.get(base_url + "/admin/users/create")
    WebDriverWait(selenium, 10).until(EC.url_contains("create"))
    btn = wait_and_get(selenium, "button.formButton")
    email, password = get_create_user_inputs(selenium)
    return btn, email, password


@pytest.mark.nondestructive
def test_create_users_empty(selenium, base_url):
    btn, _, _ = prepare_create_test(selenium, base_url)
    btn.click()
    dangers = WebDriverWait(selenium, 10).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, "p.danger")))
    assert len(dangers) == 2
    assert dangers[0].text == "Nieprawidłowy email"
    assert dangers[1].text == "Hasło musi mieć przynajmniej 6 znaków"


@pytest.mark.nondestructive
def test_create_user_short_password(selenium, base_url, email_text="correct@wp.pl", password_text="123"):
    btn, email, password = prepare_create_test(selenium, base_url)
    email.send_keys(email_text)
    password.send_keys(password_text)
    password.send_keys(Keys.ENTER)
    danger = get_danger_text(selenium)
    assert danger.text == "Hasło musi mieć przynajmniej 6 znaków"


@pytest.mark.nondestructive
def test_create_user_correct(selenium, base_url, email_text="correct@wp.pl", password_text="correct1234"):
    btn, email, password = prepare_create_test(selenium, base_url)
    email.send_keys(email_text)
    password.send_keys(password_text)
    password.send_keys(Keys.ENTER)
    WebDriverWait(selenium, 10).until(EC.url_to_be(base_url + "/admin/users"))

    assert selenium.current_url == base_url + "/admin/users"
    last_row = get_user_table_row(selenium, -1)
    email = last_row.find_elements(By.TAG_NAME, "td")[3]
    assert email.text == email_text


@pytest.mark.nondestructive
def test_create_user_already_exists(selenium, base_url, email_text="correct@wp.pl", password_text="correct1234"):
    btn, email, password = prepare_create_test(selenium, base_url)
    email.send_keys(email_text)
    password.send_keys(password_text)
    password.send_keys(Keys.ENTER)
    WebDriverWait(selenium, 10).until(EC.url_to_be(base_url + "/admin/users/create"))

    assert selenium.current_url == base_url + "/admin/users/create"
    p = get_danger_text(selenium)
    assert p.text == "Użytkownik o takim adresie email już istnieje"


@pytest.mark.nondestructive
def test_delete_user(selenium, base_url, email_text="correct@wp.pl"):
    delete_user(selenium, base_url, email_text)
