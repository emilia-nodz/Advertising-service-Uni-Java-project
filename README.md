# Serwis ogłoszeniowy
Portal umożliwiający przeglądanie i zarządzanie ogłoszeniami. Aplikacja stworzona na potrzeby przedmiotu "Programowanie aplikacji biznesowych w oparciu o platformę Java".

## Spis treści
- [Autorzy i podział pracy](#autorzy-i-podział-pracy)
- [Funkcjonalności](#funkcjonalności)
- [Użyte technologie oraz biblioteki](#użyte-technologie-oraz-biblioteki)
- [Instrukcja użytkowania](#instrukcja-użytkowania)

## Autorzy i podział pracy
  - [emilia-nodz](https://github.com/emilia-nodz)
  - [wersosn](https://github.com/wersosn)
    -  Uwierzytelnianie
    -  Zarządzanie użytkownikami (zmiana roli)
    -  Zarządzanie kategoriami
         
[//]: # (Potem jakoś inaczej to powpisujemy)
  - [TamaTamaTamaTamaTama](https://github.com/TamaTamaTamaTamaTama)

## Funkcjonalności
*Użytkownik:*
- Uwierzytelnianie
- Przeglądanie publicznych ogłoszeń
- Filtrowanie ogłoszeń po kategoriach
- Dodawanie nowego ogłoszenia (trafiającego do akceptacji przez moderację)
- Edycja własnych ogłoszeń (dopóki nie zostały zatwierdzone/przed publikacją)
- Usuwanie własnych ogłoszeń
- Otrzymywanie maili:
  - o zaakceptowaniu i publikacji ogłoszenia;
  - o wygaśnięciu ogłoszenia
 
*Moderator:*
- Przeglądanie ogłoszeń oczekujących na akceptację
- Zatwierdzanie i odrzucanie ogłoszeń

*Administrator:*
- Zarządzanie użytkownikami (np. nadawanie ról, blokowanie kont)
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
