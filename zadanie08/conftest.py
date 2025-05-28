import pytest
import logging


@pytest.fixture(scope="session", autouse=True)
def configure_logger():
    logger = logging.getLogger()
    logger.setLevel(logging.DEBUG)

    # Format logów
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')

    # Logowanie do pliku
    file_handler = logging.FileHandler("test_log.log", mode='w')
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)

    return logger


@pytest.fixture
def chrome_options(chrome_options):
    chrome_options.add_experimental_option("prefs", {
        "credentials_enable_service": False,
        "profile.password_manager_enabled": False,
    })
    chrome_options.add_argument("--guest")
    chrome_options.add_argument("--headless")
    return chrome_options
