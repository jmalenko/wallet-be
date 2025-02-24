# Overview

## Assignment

Klientská peněženka

Cíl projektu
Vytvořit funkční aplikaci peněženky pro měnu EUR a CZK, kterou bude používat koncový uživatel,
která zahrnuje následující funkcionality:

Databáze

- Návrh databáze pro potřeby projektu

Backend

- Vytvoření virtuální peněženky (aby bylo možné otestovat funkcionalitu na jiné peněžence)
- Nabití peněženky
- Výběr prostředků z peněženky
- Logika transakcí a zaúčtování
- Historie peněženky
- Validace vstupů

Frontend

- Přehled zůstatku na peněžence
- Nabití peněženky – např. platební instrukce podle měny, QR kód
- Výběr prostředků – jednoduchý formulář (částka, účet, jednoduchá identifikace osoby/platby,
  atd.)
- Historie transakcí
- Chybové hlášky – (jednoduché)

Technologický stack a očekávaný výstup

- API based
- Backend: dle výběru
- Frontend: dle výběru
- Databáze: PostgreSQL
- Hosting/Deployment: Docker (pro zjednodušené nasazení a kontrolu, kódu a logiky)
- Inicializace repository na GitHubu
- Popis README.md s instrukcemi pro spuštění aplikace
- Dokumentace / wiki
  Výstup:
- Zaslání odkazu na GitHub s repository pro odzkoušení funkčnosti aplikace
- Prezentace výstupu

## Testing

Run WalletControllerTest.

## Technical notes

### Package

```
./mvnw clean package
java -jar target/...
```

## Improvements

- Business logic is implemented in the service. In enterprise systems, such logic involves many systems (security, AML,
  core...)
- Security
    - Currently, every user can do everything
