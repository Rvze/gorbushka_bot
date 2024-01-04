create table stats
(
    at_date  date primary key,
    raw_json text
);

alter table stats
    owner to nmakarov;
