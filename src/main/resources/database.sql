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

create table token
(
  id    bigint  not null
    constraint token_user_id_fk
    references "user",
  token varchar not null
);

alter table token
  owner to postgres;

create unique index token_user_id_uindex
  on token (id);

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
    references conversation
);

alter table user_conversation
  owner to postgres;

create table user_main_info
(
  id         bigint not null
    constraint user_main_info_user_id_fk
    references "user",
  first_name varchar,
  last_name  varchar
);

alter table user_main_info
  owner to postgres;

create unique index user_main_info_id_uindex
  on user_main_info (id);

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