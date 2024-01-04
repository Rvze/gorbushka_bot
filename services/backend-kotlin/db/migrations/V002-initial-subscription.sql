create table subscription_audit
(
    id                serial,
    tg_bot_user_id    bigint      NOT NULL,
    client_type       varchar(64) NOT NULL,
    subscription_type varchar(64) NOT NULL,
    subscription_date date        NOT NULL,
    expire_date       date        NOT NULL,
    primary key (id)
);

alter table subscription_audit
    owner to nmakarov;

------------------------------------------------------------------------------

create table subscription
(
    tg_bot_user_id    bigint      NOT NULL,
    client_type       varchar(64) NOT NULL,
    subscription_type varchar(64) NOT NULL,
    subscription_date date        NOT NULL,
    expire_date       date        NOT NULL,
    primary key (tg_bot_user_id, client_type)
);

alter table subscription
    owner to nmakarov;
