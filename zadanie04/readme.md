Jeden endpoint na domyślnym porcie echo (1323).

localhost:1323/{nazwa miasta}

Aplikacja pobiera dane o pogodzie i zwraca json.
Dane są przechowywane w bazie lokalnie w sqlite.
W przypadku kolejnego zapytania dane są zwracane z bazy danych chyba że zostały zakutalizowane później niż godzinę temu.

Aplikacja korzysta z serwisu https://home.openweathermap.org.

Aby możliwe było pobranie danych z ich api wymagane jest dodanie klucza do pliku .env

Plik ".env"
TOKEN={your_token}