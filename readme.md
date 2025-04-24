# Projektowanie Obiektowe

### Zadanie 01 Pascal

:white_check_mark: 3.0 Procedura do generowania 50 losowych liczb od 0 do 100.

:white_check_mark: 3.5 Procedura do sortowania liczb.

:white_check_mark: 4.0 Dodanie parametrów do procedury losującej określającymi zakres losowania: od, do, ile.

:white_check_mark: 4.5 5 testów jednostkowych testujące procedury.

:white_check_mark: 5.0 Skrypt w bashu do uruchamiania aplikacji w Pascalu via docker.

Commit -> https://github.com/noxikoxi/PO/commit/5251c8233a645d1366dba4f136a82cc945521725

Kod -> https://github.com/noxikoxi/PO/tree/main/zadanie01

Demo -> https://github.com/noxikoxi/PO/blob/main/demos/zadanie01-Pascal.zip

### Zadanie 02 PHP + Symfony

:white_check_mark: 3.0 Należy stworzyć jeden model z kontrolerem z produktami, zgodnie z CRUD.

:white_check_mark: 3.5 Należy stworzyć skrypty do testów endpointów via cur.

:white_check_mark: 4.0 Należy stworzyć dwa dodatkowe kontrolery wraz z modelami.

:white_check_mark: 4.5 Należy stworzyć widoki do wszystkich kontrolerów.

:white_check_mark: 5.0 Stworzenie panelu administracyjnego z mockowanym logowaniem.

Commit -> https://github.com/noxikoxi/PO/commit/b12a6b88a25e8dc19fb71707a6802e4e6a638370

Kod -> https://github.com/noxikoxi/PO/tree/main/zadanie02

Demo -> https://github.com/noxikoxi/PO/blob/main/demos/zadanie02-php.zip

### Zadanie 03 Kotlin + Spring Boot

:white_check_mark: 3.0 Należy stworzyć jeden kontroler wraz z danymi wyświetlanymi z listy na endpoint’cie w formacie JSON - Kotlin + Spring Boot.

:white_check_mark: 3.5 Należy stworzyć klasę do autoryzacji (mock) jako Singleton w formie eager.

:white_check_mark: 4.0 Należy obsłużyć dane autoryzacji przekazywane przez użytkownika.

:white_check_mark: 4.5 Należy wstrzyknąć singleton do głównej klasy via @Autowired.

:white_check_mark: 5.0 Obok wersji Eager do wyboru powinna być wersja Singletona w wersji lazy

Commit -> https://github.com/noxikoxi/PO/commit/5985b8f423a304dd958580b09bfda147584bda19

Kod -> https://github.com/noxikoxi/PO/tree/main/zadanie03

Demo -> https://github.com/noxikoxi/PO/blob/main/demos/zadanie03-kotlin-spring.zip

### Zadanie 04 Proxy, Go + Echo + gorm

:white_check_mark: 3.0 Należy stworzyć aplikację we frameworki echo w j. Go, która będzie
miała kontroler Pogody, która pozwala na pobieranie danych o pogodzie (lub akcjach giełdowych)

:white_check_mark: 3.5 Należy stworzyć model Pogoda (lub Giełda) wykorzystując gorm, a dane załadować z listy przy uruchomieniu

:white_check_mark: 4.0 Należy stworzyć klasę proxy, która pobierze dane z serwisu zewnętrznego podczas zapytania do naszego kontrolera

:white_check_mark: 4.5 Należy zapisać pobrane dane z zewnątrz do bazy danych

:white_check_mark: 5.0 Należy rozszerzyć endpoint na więcej niż jedną lokalizację (Pogoda), lub akcje (Giełda) zwracając JSONa

Commit -> https://github.com/noxikoxi/PO/commit/6ada8f2570a250bb83efe256aa58477f1db1a6ec

Kod -> https://github.com/noxikoxi/PO/tree/main/zadanie04

Demo -> https://github.com/noxikoxi/PO/blob/main/demos/zadanie04-GO.zip

### Zadanie 05 Wzorce behawioralne, React

:white_check_mark: 3.0 W ramach projektu należy stworzyć dwa komponenty: Produkty oraz Płatności; Płatności powinny wysyłać do aplikacji serwerowej dane, a w Produktach powinniśmy pobierać dane o produktach z aplikacji serwerowej.

:white_check_mark: 3.5 Należy dodać Koszyk wraz z widokiem; należy wykorzystać routing.

:white_check_mark: 4.0 Dane pomiędzy wszystkimi komponentami powinny być przesyłane za pomocą React hook.

:white_check_mark: 4.5 Należy dodać skrypt uruchamiający aplikację serwerową oraz kliencką na dockerze via docker-compose.

:white_check_mark: 5.0 Należy wykorzystać axios’a oraz dodać nagłówki pod CORS.

Link do commita -> https://github.com/noxikoxi/ebiznes/commit/94dda5219c9774e65ae9f7ca29085efbdc991d5f

Kod -> https://github.com/noxikoxi/ebiznes/tree/main/zadanie05

Demo -> https://github.com/noxikoxi/ebiznes/blob/main/demos/zadanie05-frontend.zip
