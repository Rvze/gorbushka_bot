create type revision as enum ('2','3');
create type edition as enum ('Digital', 'Disk');
create table playstation
(
    id       varchar(255) primary key not null,
    edition  edition                  not null,
    year     int                      not null,
    revision revision                 not null,
    model    int                      not null
);

create table playstation_supplier
(
    supplier_id    bigint references supplier (id)          not null,
    playstation_id varchar(255) references playstation (id) not null,
    country        varchar(64)                              not null,
    price_amount   numeric(17, 4)                           not null,
    price_currency varchar(9)                               not null,
    modified_at    timestamp                                not null,
    primary key (supplier_id, playstation_id, country)
);

create index supplier_playstation_price_currency_and_price_amount_index
    on playstation_supplier (price_currency, price_amount)

