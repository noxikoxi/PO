import pytest
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from utils.test_utils import verify_page_title, login, nav_logged, nav_quest, get_div_children


# 12 Asserts

def get_curr_url(base_url):
    return base_url + "/machines"


@pytest.mark.nondestructive
def test_title(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    verify_page_title(selenium, "Maszyny")


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
    assert h3.text == "Nasze Maszyny"


@pytest.mark.nondestructive
def test_content_quest(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    items = get_div_children(selenium, "div.machine-item")
    assert len(items) == 3


@pytest.mark.nondestructive
def test_content_logged(selenium, base_url):
    login(selenium, base_url)
    selenium.get(get_curr_url(base_url))
    WebDriverWait(selenium, 10).until(EC.url_contains("machines"))
    children = get_div_children(selenium, "div.machine-item")
    assert len(children) == 4
    reservation = selenium.find_element(By.CSS_SELECTOR, "div.machine-reserve")
    a = reservation.find_element(By.TAG_NAME, "a")
    assert a


@pytest.mark.nondestructive
def test_sort(selenium, base_url):
    selenium.get(get_curr_url(base_url))
    select = selenium.find_element(by=By.CSS_SELECTOR, value="select[name='sortOption']")
    options = select.find_elements(by=By.CSS_SELECTOR, value="option")
    assert len(options) == 4
    assert options[0].text == "Sortuj"
    assert options[1].get_attribute("value") == "byId"
    assert options[1].text == "Domyślne"
    assert options[2].get_attribute("value") == "byName"
    assert options[2].text == "Nazwa"
    assert options[3].get_attribute("value") == "byType"
    assert options[3].text == "Typ"
