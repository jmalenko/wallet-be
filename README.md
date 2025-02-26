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

### Database

Start database in docker.

```
sudo mkdir -p /private/var/lib/postgresql
sudo  chown -R jaro:jaro /private

docker compose -f docker/docker-compose.yml -p docker_wallet up -d db
```

In the dev profile (which is active), database structure is updated automatically based on the model.

The following may be useful for deploying to production (prod profile).

```
sudo apt install postgresql-client

PGPASSWORD=password psql -h 127.0.0.1 -p 5432 -U postgres -d postgres
```

### Needed OS Packages

You may need to:

```
apt-get install libtcnative

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/x86_64-linux-gnu
```

### Package and Run

```
./mvnw clean package
java -jar target/wallet-0.0.1-SNAPSHOT.jar
```

## Improvements

- Security
    - Currently, every user can do everything
- Business logic is implemented in the service. In enterprise systems, such logic involves many systems (security, AML,
  core...)
- Database structure is automatically updated by hibernate. This is not a good practice for production.
- Response JSONs have only the relevant data, but not in getUsers. This is for debugging. Accounts and transactions may
  be removed from the responses.
- Technical proposals:
  - Refactor operations with amounts
  - Refactor operations with currency
  - Migrate from Date to LocalDateTime. This is ok, except in one situation: In the InitDataLoader, the past
    transactions (which don't have date s now) have year bigger by 1900.  
