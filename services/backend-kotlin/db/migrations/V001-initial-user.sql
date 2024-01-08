
create table tg_bot_user
(
    id           bigint,
    account_type varchar(64)                           NOT NULL,
    username     varchar(255),

    firstname    varchar(255),
    lastname     varchar(255),
    title        varchar(255),

    created_at   timestamp                             NOT NULL,

--     bot_user_id  varchar(255) REFERENCES bot_user (id) NOT NULL,
    PRIMARY KEY (id)
);

create unique index tg_bot_user_username_uindex
    on tg_bot_user (username);

alter table tg_bot_user
    owner to nmakarov;
