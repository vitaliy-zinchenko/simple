
drop table if exists user_permission;
drop table if exists role_permission;
drop table if exists user;
drop table if exists role;
drop table if exists permission;

drop table if exists order_item;
drop table if exists order_goods;
drop table if exists comment;
drop table if exists goods;
drop table if exists goods_type;

CREATE TABLE user
(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  role_id BIGINT,
  permission_id bigint
);

CREATE TABLE role
(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  permission_id bigint
);

create table permission 
(
  id bigint primary key auto_increment,
  prm_key varchar(255)
);

create table user_permission (
  user_id bigint,
  permission_id bigint 
);

create table role_permission (
  role_id bigint,
  permission_id bigint
);

create table goods_type (
  id bigint,
  name varchar(255)
);

create table goods (
  id bigint,
  name varchar(255),
  price FLOAT,
  goods_type_id bigint
);

create table comment (
  id bigint,
  message text,
  create_date datetime,
  goods_id bigint,
  user_id bigint
);

create table order_goods (
  id bigint,
  payed BOOL
);

create table order_item (
  id bigint,
  count int(11),
  goods_id bigint,
  order_id bigint
);


-- ----------------------------------


drop table if exists new_profile;
drop table if exists new_user;

create table new_user (
  id int(11) primary key AUTO_INCREMENT,
  password varchar (255)
);

create table new_profile (
  user_id int(11) primary key,
  name varchar(255)
);

alter table new_profile add constraint new_profile_fk_to_new_user_id foreign key (user_id) references new_user (id);

insert into new_user (id, password) values (1, 'passwd_1');
insert into new_user (id, password) values (2, 'passwd_2');
insert into new_user (id, password) values (3, 'passwd_3');
insert into new_user (id, password) values (4, 'passwd_4');

insert into new_profile (user_id, name) values (1, 'name_1');
insert into new_profile (user_id, name) values (2, 'name_2');
insert into new_profile (user_id, name) values (3, 'name_3');

select u.id, u.password, p.user_id, p.name from new_user u
  left outer join new_profile p on u.id = p.user_id;

select u.id, u.password, p.user_id, p.name from new_profile p
  right outer join new_user u on p.user_id =  u.id;
