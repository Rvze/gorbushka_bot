create table supplier_macbook
(
    mac_id         varchar(255)                    not null,
    supplier_id    bigint references supplier (id) not null,
    country        varchar(64)                     not null,
    price_amount   numeric(17, 4)                  not null,
    price_currency varchar(9)                      not null,
    modified_at    timestamp                       not null,
    primary key (mac_id, supplier_id, country)
);