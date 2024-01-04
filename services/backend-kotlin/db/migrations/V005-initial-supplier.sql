create table supplier
(
    id         bigint,
    username   varchar(255) NOT NULL,
    title      varchar(255),
    created_at timestamp    NOT NULL,
    PRIMARY KEY (id)
);

create unique index supplier_username_uindex
    on tg_bot_user (username);

alter table supplier
    owner to nmakarov;
