drop database readingroomdb;
drop user readingroom;
create user readingroom with password 'password';
create database readingroomdb with template=template0 owner=readingroom;
\connect readingroomdb;
alter default privileges grant all on tables to readingroom;
alter default privileges grant all on sequences to readingroom;

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
title varchar(20) not null,
description varchar(100) not null
);
alter table rr_shelves add constraint shelves_user_fk
foreign key (user_id) references rr_users(user_id);

create table rr_saved_books(
book_id integer primary key not null,
shelf_id integer not null,
user_id integer not null,
ol_id varchar(11) not null,
note varchar(100),
saved_date bigint not null
);
alter table rr_saved_books add constraint saved_books_shelf_fk
foreign key (shelf_id) references rr_shelves(shelf_id);
alter table rr_saved_books add constraint saved_books_user_fk
foreign key (user_id) references rr_users(user_id);

create sequence rr_users_seq increment 1 start 1;
create sequence rr_shelves_seq increment 1 start 1;
create sequence rr_saved_books_seq increment 1 start 10000;
