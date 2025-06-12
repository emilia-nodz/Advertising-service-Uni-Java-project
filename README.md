# Serwis ogłoszeniowy
Portal umożliwiający przeglądanie i zarządzanie ogłoszeniami. Aplikacja stworzona na potrzeby przedmiotu "Programowanie aplikacji biznesowych w oparciu o platformę Java".

## Spis treści
- [Autorzy i podział pracy](#autorzy-i-podział-pracy)
- [Funkcjonalności](#funkcjonalności)
- [Użyte technologie oraz biblioteki](#użyte-technologie-oraz-biblioteki)
- [Instrukcja użytkowania](#instrukcja-użytkowania)

## Autorzy i podział pracy
  - [emilia-nodz](https://github.com/emilia-nodz)
    -  Zarządzanie ogłoszeniami (dodawanie, edycja, usuwanie)
    -  Filtrowanie ogłoszeń po kategoriach
    -  Wysyłanie maili o wygaśnięciu ogłoszenia
  - [wersosn](https://github.com/wersosn)
    -  Uwierzytelnianie
    -  Zarządzanie użytkownikami (zmiana roli, usuwanie użytkowników)
    -  Zarządzanie kategoriami (dodawanie, edycja, usuwanie)
  - [TamaTamaTamaTamaTama](https://github.com/TamaTamaTamaTamaTama)
    - Zarządzanie ogłoszeniami (akceptacja przez moderację)
    - Wysyłanie maili o zaakceptowaniu i publikacji ogłoszenia

## Funkcjonalności
*Użytkownik:*
- Uwierzytelnianie
- Przeglądanie publicznych ogłoszeń
- Filtrowanie ogłoszeń po kategoriach
- Dodawanie nowego ogłoszenia (trafiającego do akceptacji przez moderację)
- Edycja własnych ogłoszeń
- Usuwanie własnych ogłoszeń
- Otrzymywanie maili:
  - o zaakceptowaniu i publikacji ogłoszenia;
  - o wygaśnięciu ogłoszenia
 
*Moderator:*
- Przeglądanie ogłoszeń oczekujących na akceptację
- Zatwierdzanie i odrzucanie ogłoszeń

*Administrator:*
- Zarządzanie użytkownikami (np. nadawanie ról)
- Zarządzanie kategoriami (dodawanie, edycja, usuwanie)
- Ustawianie parametrów systemowych (np. czas wygaśnięcia ogłoszeń, treść maili dotyczących powiadomień)

## Użyte technologie oraz biblioteki
*JDK:*
- Java Development Kit: 17

*Serwer aplikacyjny:*
- Jakarta EE: 10.0.0
- Payara 6

*Baza danych:*
- H2 Database: 2.2.224 (do testów/developmentu)
- PostgreSQL JDBC Driver: 42.7.3 (produkcyjna baza danych)

*Biblioteki:*
- Jakarta EE Web API: 10.0.0
- Hibernate ORM: 6.2.7 (implementacja JPA)
- Jakarta Persistence API: 3.1.0 (specyfikacja JPA)
- Weld SW (CDI): 5.1.0 (implementacja Context and Dependency Injection)
- PrimeFaces: 12.0.0
- Log4j2: 2.20.0 (dziennik zdarzeń)

*Testy jednostkowe:*
- JUnit 5: 5.12.0
- Mockito: 5.16.0
- JaCoCo: 0.8.11

## Instrukcja użytkowania
### 1. Sklonuj repozytorium:
```bash
git clone https://github.com/emilia-nodz/Advertising-service-Uni-Java-project.git
```

### 2. Rozpakuj i zbuduj projekt:
```bash
cd katalog-z-projektem
mvn clean package
```

### 3. Pobierz:
- [PostgreSQL](https://www.postgresql.org/download/)
- [Payara6](https://www.payara.fish/downloads/payara-platform-community-edition/)

### 4. Konfiguracja bazy danych PostgreSQL:
- Uruchom bazę danych
- Utwórz nową bazę: **Database → Create → Database**
- Nadaj jej nazwę: **javaDB**
- Następnie należy skopiować plik **postgresql-42.7.3.jar** (przykładowa ścieżka: *../.m2/repository/org/postgresql/postgresql/42.7.3*)
- Należy go umieścić w folderze, gdzie znajduje się Payara: np. */payara6/glassfish/domains/domain-name/lib*

### 5. Konfiguracja serwera aplikacyjnego Payara:
- W terminalu przejdź do folderu zawierającego Payarę: *../payara6/bin*
- Uruchom serwer aplikacyjny za pomocą:
```bash
asadmin start-domain domain-name
```

- Wejdź na stronę:
```bash
http://localhost:4848/ - panel administracyjny Payara
```

- Skonfiguruj **JDBC Connection Pool** (Zakładka Resources → JDBC → JDBC Connection Pools):
Utwórz nowe źródło danych:
```bash
Pool Name: PostgresPool
Resource Type: javax.sql.DataSource
Datasource Connection: org.postgresql.ds.PGSimpleDataSource

# W "Additional Properties" należy dodać:
User - postgres
URL - jdbc:postgresql://localhost:5432/javaDB
Password - postgres
DriverClass - org.postgresql.Driver
# Uwaga! W przypadku User i Password należy podać odpowiednią nazwę i hasło konta, które zostało założone w bazie PostgreSQL!

# Aby przetestować działanie należy kliknąć opcję 'Ping' i sprawdzić, czy pojawi się komunikat "Ping Succeeded"
```

- Skonfiguruj **JDBC Resources** (Zakładka Resources → JDBC → JDBC Resources):
Utwórz nowy resource:
```bash
JNDI Name: jdbc/myPostgres
Pool Name: PostgresPool
```

### 6. Wdrożenie aplikacji:
- Utwórz nowy deployment (Appliactions → Deploy → Wybierz plik .war → (opcjonalnie) Nadaj własną nawę projektu w Application Name → Kliknij OK)
- Jeżeli wszystko przebiegło pomyślnie, przejdź do:
```bash
http://localhost:8080/nazwa-aplikacji
```

### 7. Po wejściu na stronę będziesz mieć dostęp do wszystkich zaimplementowanych funkcjonalności :)

### *8. Konfiguracja wysyłania maila:
- Utwórz konto na platformie [Mailtrap](https://mailtrap.io/)
- Sprawdź wygenerowane w Sandbox: Username, Password, Host oraz Port
- Podmień potrzebne informacje w pliku EmailService.java
