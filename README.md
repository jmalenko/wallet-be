# Overview

## Assignment

Authentication service generates information about transactions. Each transaction contains following
information:

- ID (integer)
- Timestamp
- Type (string)
- Actor (string)
- Transaction data (key-value map of strings)

The transactions must be collected by a new service. The service should receive the data at HTTP
interface, store them in SQL database and make them available via the HTTP interface.

Implement the service for CRUD (Create Update Delete) and search operations. Suggest and design what
the search operation should look like to be usable.

Implement it as a Spring application using MySQL database.

## Testing

Run TransactionControllerTest.

## Technical notes

### Database

```
docker compose -f docker/docker-compose.yml -p docker up -d db

apt-get install mysql-client

mysql -h 127.0.0.1 -P 3306 -u user -D db -p

CREATE TABLE transaction (
    id          bigint primary key,
    timestamp   bigint,
    type        varchar(64),
    actor       varchar(64)
);

CREATE TABLE key_value (
    id              bigint primary key auto_increment,
    transaction_id  bigint,
--    transaction  bigint,
--    transaction_key  bigint,
    key_            varchar(64),
    value           varchar(64),
    foreign key (transaction_id) references transaction(id)
);

```

### Package

```
./mvnw clean package
java -jar target/...
```

## Improvements

- The Data is a set, but it's implemented as a list.
    - Technical reason: JPA doesn't support sets.
    - This is negatively impacting the performance of this use case: given a transaction and a key, get the data with
      the key for the transaction, which is O(n) instead of O(1).
- Business logic: Split "getting new transactions" and "CRUD" (without C?)
    - Maybe: Enforce security
- Improve testing:
    - The TransactionService is tested properly, in the sense that the changes are not reflected in the repository.
    - The TransactionController should be tested similarly. Specifically, the changes should not be done in the
      repository. The currently implemented test is a functional test.
    - The TransactionRepository should be tested independently (with changes to the persisted data).
