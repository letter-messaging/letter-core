create table conversation
(
    id         bigserial not null
        constraint conversation_pkey
            primary key,
    chat_name  varchar,
    creator_id bigint
);

create unique index conversation_id_uindex
    on conversation (id);

create table "user"
(
    id    bigserial not null
        constraint user_pkey
            primary key,
    login varchar   not null,
    hash  varchar   not null
);

create unique index user_id_uindex
    on "user" (id);

create unique index user_login_uindex
    on "user" (login);

create table message
(
    id              bigserial                not null
        constraint message_pkey
            primary key,
    conversation_id bigint                   not null
        constraint message_conversation_id_fk
            references conversation,
    sent            timestamp with time zone not null,
    text            varchar,
    sender_id       bigint                   not null
        constraint message_user_id_fk
            references "user"
);

create unique index message_id_uindex
    on message (id);

create table forwarded_message
(
    parent_message_id    bigint not null
        constraint forwarded_message_message_id_fk
            references message,
    forwarded_message_id bigint not null
        constraint forwarded_message_message_id_fk_2
            references message
);

create table token
(
    id      bigserial not null
        constraint token_pk
            primary key,
    user_id bigint    not null
        constraint token_user_id_fk
            references "user",
    token   varchar   not null
);

create table user_info
(
    id                 bigserial not null
        constraint user_main_info_2_pk
            primary key,
    user_id            bigint    not null
        constraint user_main_info_2_user_id_fk
            references "user",
    first_name         varchar   not null,
    last_name          varchar   not null,
    gender             boolean,
    birth_date         date,
    marital_status     varchar,
    country            varchar,
    city               varchar,
    location           varchar,
    phone_number       varchar,
    mail               varchar,
    place_of_education varchar,
    place_of_work      varchar,
    about              varchar
);

create unique index user_main_info_2_id_uindex
    on user_info (id);

create table user_online
(
    id      bigserial                not null
        constraint user_online_pk
            primary key,
    user_id bigint                   not null
        constraint user_online_user_id_fk
            references "user",
    seen    timestamp with time zone not null
);

create unique index user_online_id_uindex
    on user_online (id);

create table avatar
(
    id       bigserial not null
        constraint avatar_pk
            primary key,
    user_id  bigint    not null
        constraint avatar_user_id_fk
            references "user",
    path     varchar   not null,
    uploaded date      not null
);

create table image
(
    id         bigserial not null
        constraint image_pk
            primary key,
    user_id    bigint    not null
        constraint image_user_id_fk
            references "user",
    message_id bigint    not null
        constraint image_message_id_fk
            references message,
    path       varchar   not null,
    uploaded   date      not null
);

create unique index image_id_uindex
    on image (id);

create unique index image_path_uindex
    on image (path);

create unique index document_id_uindex
    on image (id);

create unique index document_path_uindex
    on image (path);

create table document
(
    id         bigserial not null
        constraint document_pkey
            primary key,
    user_id    bigint    not null
        constraint document_user_id_fk
            references "user",
    message_id bigint    not null
        constraint document_message_id_fk
            references message,
    path       varchar   not null,
    uploaded   date      not null
);

create table user_conversation
(
    id              bigserial                not null
        constraint user_conversation_pk
            primary key,
    user_id         bigint                   not null
        constraint user_conversation_user_id_fk
            references "user",
    conversation_id bigint                   not null
        constraint user_conversation_conversation_id_fk
            references conversation,
    hidden          boolean default false,
    last_read       timestamp with time zone not null,
    kicked          boolean default false
);

create unique index user_conversation_id_uindex
    on user_conversation (id);

INSERT INTO public."user" (id, login, hash)
VALUES (0, 'system', '9ORQk/lZ9xIqQRHYpKUnFIPoAz31kj0xCz47u8FHE7k=$/5l1c6S8EZGjrFaJoMXpI0fGSHisgBj3lv9Ldiejou0=');

INSERT INTO public.user_info (id, user_id, first_name, last_name, gender, birth_date, marital_status, country, city,
                              location, phone_number, mail, place_of_education, place_of_work, about)
VALUES (0, 0, 'system', '', null, null, null, null, null, null, null, null, null, null, null);