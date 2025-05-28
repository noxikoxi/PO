import pytest
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from utils.test_utils import verify_page_title, get_elements_by_selector, wait_and_get, get_email_password, login


# 10 asserts

@pytest.mark.nondestructive
def test_title(selenium, base_url):
    selenium.get(base_url)
    verify_page_title(selenium, "Express")


@pytest.mark.nondestructive
def test_navigation_links_count(selenium, base_url, expected_count=4):
    selenium.get(base_url)
    links = get_elements_by_selector(selenium, "div.middleContainer a")
    assert len(links) == expected_count, f"Oczekiwano {expected_count} linków, znaleziono {len(links)}"


@pytest.mark.nondestructive
def test_links_clickable(selenium, base_url):
    selenium.get(base_url)
    links = get_elements_by_selector(selenium, "div.middleContainer a")
    for link in links:
        assert link.is_enabled(), f"Link '{link.text}' nie jest klikalny"


@pytest.mark.nondestructive
def test_links_text_quest(selenium, base_url):
    selenium.get(base_url)
    links = get_elements_by_selector(selenium, "div.middleContainer a")
    assert links[0].find_element(by=By.CSS_SELECTOR, value="p").text == "MaszynaNaDzień"
    assert links[1].find_element(by=By.CSS_SELECTOR, value="p").text == "Zaloguj Się"
    assert links[2].find_element(by=By.CSS_SELECTOR, value="p").text == "Dostepne Maszyny"
    assert links[3].find_element(by=By.CSS_SELECTOR, value="p").text == "Aktualne Rezerwacje"


@pytest.mark.nondestructive
def test_links_text_logged(selenium, base_url):
    login(base_url=base_url, admin=False, driver=selenium)
    selenium.get(base_url)
    wait = WebDriverWait(selenium, 10)
    wait.until(EC.title_contains("Express"))

    links = get_elements_by_selector(selenium, "div.middleContainer a")
    assert links[0].find_element(by=By.CSS_SELECTOR, value="p").text == "MaszynaNaDzień"
    assert links[1].find_element(by=By.CSS_SELECTOR, value="p").text == "Profil Użytkownika"
    assert links[2].find_element(by=By.CSS_SELECTOR, value="p").text == "Dostepne Maszyny"
    assert links[3].find_element(by=By.CSS_SELECTOR, value="p").text == "Aktualne Rezerwacje"
