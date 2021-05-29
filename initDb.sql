create table balance
(
    id          int auto_increment
        primary key,
    customer_id int              not null,
    balance     bigint default 0 not null
)
    comment 'Баланс бонусов';

create table balance_history_change
(
    id                int auto_increment
        primary key,
    balance_id        int      not null,
    operation_type_id int      not null,
    sum_change        bigint   not null,
    total_sum         bigint   not null,
    date_change       datetime not null
)
    comment 'История изменения баланса';

create table customers
(
    id              int auto_increment
        primary key,
    first_name      varchar(100) not null,
    middle_name     varchar(100) null,
    last_name       varchar(100) null,
    gender          varchar(100) null,
    birthday        datetime     not null,
    phone_number    varchar(100) not null,
    registered_date datetime     not null
)
    comment 'Покупатели';

create table operations
(
    id   int auto_increment
        primary key,
    name varchar(100) not null
)
    comment 'Операции изменения баланса';


