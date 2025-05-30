from selenium.webdriver import Keys
from selenium.webdriver.common.alert import Alert
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

def verify_page_title(driver, expected_title):
    """Sprawdza, czy tytuł strony jest poprawny."""
    assert driver.title == expected_title, f"Oczekiwano tytułu '{expected_title}', ale otrzymano '{driver.title}'"


def get_elements_by_selector(driver, selector):
    try:
        wait = WebDriverWait(driver, 10)
        return wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, selector)))
    except TimeoutException:
        raise AssertionError("Linki nawigacyjne nie zostały znalezione")


def get_email_password(driver):
    try:
        wait = WebDriverWait(driver, 10)
        email = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input#email")))
        password = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input#password")))
        return email, password
    except TimeoutException:
        raise AssertionError


def wait_and_get(driver, selector):
    try:
        wait = WebDriverWait(driver, 10)
        elem = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, selector)))
        return elem
    except TimeoutException:
        raise AssertionError


def get_danger_text(driver):
    try:
        wait = WebDriverWait(driver, 10)
        return wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "p.danger")))
    except TimeoutException:
        raise AssertionError


def main_page_link(driver, base_url, url):
    driver.get(url)
    link = wait_and_get(driver, "div.middleContainer a")
    link.click()
    WebDriverWait(driver, 0.5)
    assert driver.current_url == base_url + "/", f"URL nie pasuje. Oczekiwano: {base_url}, ale uzyskano: {driver.current_url}"


def login(driver, base_url, admin=False):
    driver.get(base_url + "/login")
    email, password = get_email_password(driver)
    email.clear()
    password.clear()
    if admin:
        email.send_keys("admin")
        password.send_keys("admin")
    else:
        email.send_keys("testtest123@wp.pl")
        password.send_keys("testtest123")

    password.send_keys(Keys.ENTER)
    WebDriverWait(driver, 10).until(EC.url_contains("users"))


def nav_logged(driver, base_url, url):
    login(driver, base_url)
    driver.get(url)
    WebDriverWait(driver, 10).until(EC.url_to_be(url))
    nav_elements = get_elements_by_selector(driver, "nav ul li")
    assert len(nav_elements) == 4
    first_nav = nav_elements[0]
    second_nav = nav_elements[1]
    third_nav = nav_elements[2]
    fourth_nav = nav_elements[3]
    labels = ("Moje Rezerwacje", "Rezerwacje", "Maszyny", "Profil")
    for idx, element in enumerate((first_nav, second_nav, third_nav, fourth_nav)):
        assert element.text == labels[idx]
    for element in (first_nav, second_nav, third_nav, fourth_nav):
        assert element.is_enabled() is True


def nav_admin(driver, base_url):
    login(driver, base_url, admin=True)
    nav_elements = get_elements_by_selector(driver, "nav ul li")
    assert len(nav_elements) == 4
    first_nav = nav_elements[0]
    second_nav = nav_elements[1]
    third_nav = nav_elements[2]
    fourth_nav = nav_elements[3]
    labels = ("Użytkownicy", "Rezerwacje", "Maszyny", "Profil")
    for idx, element in enumerate((first_nav, second_nav, third_nav, fourth_nav)):
        assert element.text == labels[idx]
    for element in (first_nav, second_nav, third_nav, fourth_nav):
        assert element.is_enabled() is True


def nav_quest(driver, url):
    driver.get(url)
    nav_elements = get_elements_by_selector(driver, "nav ul li")
    assert len(nav_elements) == 2
    first_nav = nav_elements[0]
    second_nav = nav_elements[1]
    labels = ("Rezerwacje", "Maszyny")
    for idx, element in enumerate((first_nav, second_nav)):
        assert element.text == labels[idx]
    for element in (first_nav, second_nav):
        assert element.is_enabled() is True


def get_div_children(driver, parent_div_selector):
    WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.CSS_SELECTOR, parent_div_selector)))
    parent_div = driver.find_element(By.CSS_SELECTOR, parent_div_selector)
    children = parent_div.find_elements(By.CSS_SELECTOR, "div")  # '>*' oznacza wszystkie bezpośrednie dzieci
    return children

