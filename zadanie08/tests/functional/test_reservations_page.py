import pytest
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from utils.test_utils import verify_page_title, nav_logged, nav_quest, login


# 20 asserts

def get_curr_url(base_url):
    return base_url + "/reservations"


@pytest.mark.nondestructive
def test_title(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    verify_page_title(selenium, "Rezerwacje")


@pytest.mark.nondestructive
def test_nav_quest(selenium, base_url):
    nav_quest(selenium, get_curr_url(base_url))


@pytest.mark.nondestructive
def test_nav_logged(selenium, base_url):
    nav_logged(selenium, base_url, get_curr_url(base_url))


@pytest.mark.nondestructive
def test_content(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    h3 = selenium.find_element(by=By.CSS_SELECTOR, value="h3")
    assert h3.text == "Rezerwacje"


@pytest.mark.nondestructive
def test_sort(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    select = selenium.find_element(by=By.CSS_SELECTOR, value="select[name='sortOption']")
    options = select.find_elements(by=By.CSS_SELECTOR, value="option")
    assert len(options) == 5
    assert options[0].text == "Sortuj"
    assert options[1].get_attribute("value") == "byDate"
    assert options[1].text == "Data Rezerwacji"
    assert options[2].get_attribute("value") == "byMachineName"
    assert options[2].text == "Nazwa Maszyny"
    assert options[3].get_attribute("value") == "byMachineType"
    assert options[3].text == "Typ Maszyny"
    assert options[4].get_attribute("value") == "byUserEmail"
    assert options[4].text == "Email Rezerwującego"


@pytest.mark.nondestructive
def test_my_reservations_page(selenium, base_url):
    login(selenium, base_url)
    selenium.get(get_curr_url(base_url) + "/user")
    h3 = selenium.find_element(by=By.CSS_SELECTOR, value="h3")
    assert h3.text == "Moje Rezerwacje"


@pytest.mark.nondestructive
def test_my_reservations_sort(selenium, base_url):
    login(selenium, base_url)
    selenium.get(get_curr_url(base_url) + '/user')
    WebDriverWait(selenium, 10).until(EC.url_to_be(get_curr_url(base_url) + '/user'))
    select = selenium.find_element(by=By.CSS_SELECTOR, value="select[name='sortOption']")
    options = select.find_elements(by=By.CSS_SELECTOR, value="option")
    assert len(options) == 4
    assert options[0].text == "Sortuj"
    assert options[1].get_attribute("value") == "byDate"
    assert options[1].text == "Data Rezerwacji"
    assert options[2].get_attribute("value") == "byMachineName"
    assert options[2].text == "Nazwa Maszyny"
    assert options[3].get_attribute("value") == "byMachineType"
    assert options[3].text == "Typ Maszyny"
