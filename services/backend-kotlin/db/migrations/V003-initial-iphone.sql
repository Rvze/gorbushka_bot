create table iphone
(
    id     varchar(255),
    model  varchar(64) NOT NULL,
    memory varchar(8)  NOT NULL,
    color  varchar(64) NOT NULL,
    PRIMARY KEY (id)
);

alter table iphone
    owner to nmakarov;

create table supplier_iphone
(
    supplier_id    bigint REFERENCES supplier (id)     NOT NULL,
    iphone_id      varchar(255) REFERENCES iphone (id) NOT NULL,
    country        varchar(64)                         NOT NULL,

    price_amount   decimal(17, 4)                      NOT NULL,
    price_currency varchar(9)                          NOT NULL,
    modified_at    timestamp                           NOT NULL,

    PRIMARY KEY (supplier_id, iphone_id, country)
);

create index supplier_iphone_price_currency_and_price_amount_index
    on supplier_iphone (price_currency, price_amount);

alter table supplier_iphone
    owner to nmakarov;
