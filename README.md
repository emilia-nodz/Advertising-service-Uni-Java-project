Version [PL](#serwis-ogłoszeniowy) | [ENG](#advertising-service)

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

*Maven:*
- Maven: 3.9.9

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
- Podmień potrzebne informacje w pliku `EmailService.java`

---
# Advertising service
A portal for browsing and managing advertisements. The application was developed as part of the course "Business Application Programming with the Java Platform."

## Table of Contents
- [Team](#team)
- [Functionalities](#functionalities)
- [Tech stack](#tech-stack)
- [How to use](#ihow-to-use)

## Team
  - [emilia-nodz](https://github.com/emilia-nodz)
    - Managing advertisements (adding, editing, deleting)
    - Filtering ads by category
    - Sending emails about ad expiration
  - [wersosn](https://github.com/wersosn)
    - Authentication
    - User management (role changes, deleting users)
    - Category management (adding, editing, deleting)
  - [TamaTamaTamaTamaTama](https://github.com/TamaTamaTamaTamaTama)
    - Advertisement moderation (approval by moderation)
    - Sending emails upon ad acceptance and publication

## Functionalities
*User:*
- Authentication
- Viewing public advertisements
- Filtering ads by category
- Adding new ads (submitted for moderation approval)
- Editing own ads
- Deleting own ads
- Receiving emails:
  - When the ad is approved and published
  - When the ad expires
 
*Moderator:*
- Viewing ads pending approval
- Approving or rejecting ads

*Admin:*
- User management (e.g., assigning roles)
- Category management (adding, editing, deleting)
- Setting system parameters (e.g., ad expiration time, notification email content)

## Tech stack
*JDK:*
- Java Development Kit: 17

*Maven:*
- Maven: 3.9.9

*Application server:*
- Jakarta EE: 10.0.0
- Payara 6

*Database:*
- H2 Database: 2.2.224 (for testing/development)
- PostgreSQL JDBC Driver: 42.7.3 (production database)

*Libraries:*
- Jakarta EE Web API: 10.0.0
- Hibernate ORM: 6.2.7 (JPA implementation)
- Jakarta Persistence API: 3.1.0 (JPA specification)
- Weld SW (CDI): 5.1.0 (Context and Dependency Injection implementation)
- PrimeFaces: 12.0.0
- Log4j2: 2.20.0 (logging)

*Unit testing:*
- JUnit 5: 5.12.0
- Mockito: 5.16.0
- JaCoCo: 0.8.11

## How to use
### 1. Clone this repository:
```bash
git clone https://github.com/emilia-nodz/Advertising-service-Uni-Java-project.git
```

### 2. Unpack and build the project:
```bash
cd project-directory
mvn clean package
```

### 3. Download:
- [PostgreSQL](https://www.postgresql.org/download/)
- [Payara6](https://www.payara.fish/downloads/payara-platform-community-edition/)

### 4. Configure PostgreSQL Database:
- Start the database
- Create a new database: **Database → Create → Database**
- Name it: **javaDB**
- Copy the file **postgresql-42.7.3.jar** (example path: *../.m2/repository/org/postgresql/postgresql/42.7.3*)
- Place it in the Payara folder, e.g., */payara6/glassfish/domains/domain-name/lib*

### 5. Configure Payara Application Server:
- In terminal, navigate to Payara's bin directory: *../payara6/bin*
- Start the application server:
```bash
asadmin start-domain domain-name
```

- Access the admin panel:
```bash
http://localhost:4848/ - Payara Admin Console
```

- Configure **JDBC Connection Pool** (Resources → JDBC → JDBC Connection Pools):
Create a new connection pool:
```bash
Pool Name: PostgresPool
Resource Type: javax.sql.DataSource
Datasource Connection: org.postgresql.ds.PGSimpleDataSource

# In "Additional Properties", add:
User - postgres
URL - jdbc:postgresql://localhost:5432/javaDB
Password - postgres
DriverClass - org.postgresql.Driver
# Make sure to use the actual username and password set up in your PostgreSQL database!

# Click 'Ping' to verify the connection. You should see "Ping Succeeded."
```

- Configure **JDBC Resources** (Resources → JDBC → JDBC Resources):
Create a new resource:
```bash
JNDI Name: jdbc/myPostgres
Pool Name: PostgresPool
```

### 6. Deploy the application:
- Create a new deployment (Appliactions → Deploy → Select .war file → (optional) Set custom Application Name → Click OK)
- If everything goes well, open:
```bash
http://localhost:8080/application-name
```

### 7. After opening the page, you will have access to all implemented features :)

### *8. Email sending configuration:
- Create an account on [Mailtrap](https://mailtrap.io/)
- In the Sandbox environment, check the generated: Username, Password, Host, and Port
- Replace the necessary information in the `EmailService.java` file
