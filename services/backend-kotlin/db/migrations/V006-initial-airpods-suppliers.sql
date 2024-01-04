create table supplier_airpods
(
    supplier_id    bigint references supplier not null,
    airpods_id     varchar(255)               not null,
    country        varchar(64)                not null,
    price_amount   numeric(17, 4)             not null,
    price_currency varchar(9)                 not null,
    modified_at    timestamp                  not null,

    primary key (supplier_id, airpods_id, country)
);

create index supplier_airpods_price_currency_and_price_amount_index
    on supplier_airpods (price_currency, price_amount)