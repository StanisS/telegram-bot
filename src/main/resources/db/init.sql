CREATE TABLE ep
(
    ep_id     uuid primary key,
    scope     varchar(255),
    date      datetime,
    number    int,
    create_at datetime,
    create_by  varchar(255)
);
