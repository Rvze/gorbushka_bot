create table notification_subscription
(
    id        bigserial primary key,
    user_id   bigint       not null,
    entity_id varchar(400) not null,
    type      varchar(255) not null,
    UNIQUE (user_id, entity_id, type)
);

create table notification
(
    id              bigserial primary key,
    user_id         bigint       not null,
    subscription_id bigint references notification_subscription (id) ON DELETE CASCADE,
    text            text         not null,
    status          varchar(255) not null,
    read_at         timestamptz,
    created_at      timestamptz  not null
);
