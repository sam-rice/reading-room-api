drop table if exists rr_users cascade;
drop table if exists rr_shelves cascade;
drop table if exists rr_saved_books cascade;
drop sequence if exists rr_users_seq;
drop sequence if exists rr_shelves_seq;
drop sequence if exists rr_saved_books_seq;

create table rr_users(
user_id integer primary key not null,
first_name varchar(30) not null,
last_name varchar(30) not null,
email varchar(40) not null,
password varchar(255) not null
);

create table rr_shelves(
shelf_id integer primary key not null,
user_id integer not null,
title varchar(40) not null,
description varchar(150) not null
);
alter table rr_shelves add constraint shelves_user_fk
foreign key (user_id) references rr_users(user_id);

create table rr_saved_books(
book_id integer primary key not null,
shelf_id integer not null,
user_id integer not null,
library_key varchar(12) not null,
title varchar(60) not null,
authors json,
cover_url varchar(50),
user_note text,
saved_date bigint not null
);
alter table rr_saved_books add constraint saved_books_shelf_fk
foreign key (shelf_id) references rr_shelves(shelf_id);
alter table rr_saved_books add constraint saved_books_user_fk
foreign key (user_id) references rr_users(user_id);

create sequence rr_users_seq increment 1 start 1;
create sequence rr_shelves_seq increment 1 start 1;
create sequence rr_saved_books_seq increment 1 start 10000;
