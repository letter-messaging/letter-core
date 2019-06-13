create table conversation
(
  id bigserial not null
    constraint conversation_pkey
      primary key
);

alter table conversation
  owner to postgres;

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

alter table "user"
  owner to postgres;

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
  read            boolean                  not null,
  sender_id       bigint                   not null
    constraint message_user_id_fk
      references "user"
);

alter table message
  owner to postgres;

create unique index message_id_uindex
  on message (id);

create unique index user_id_uindex
  on "user" (id);

create unique index user_login_uindex
  on "user" (login);

create table user_conversation
(
  user_id         bigint not null
    constraint user_conversation_user_id_fk
      references "user",
  conversation_id bigint not null
    constraint user_conversation_conversation_id_fk
      references conversation,
  hidden          boolean default false
);

alter table user_conversation
  owner to postgres;

create table forwarded_message
(
  parent_message_id    bigint not null
    constraint forwarded_message_message_id_fk
      references message,
  forwarded_message_id bigint not null
    constraint forwarded_message_message_id_fk_2
      references message
);

alter table forwarded_message
  owner to postgres;

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

alter table token
  owner to postgres;

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

alter table user_info
  owner to postgres;

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

alter table user_online
  owner to postgres;

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

alter table avatar
  owner to postgres;

