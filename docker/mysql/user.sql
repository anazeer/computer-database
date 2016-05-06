use computer-database-db;

drop table if exists user_role;
drop table if exists user;

CREATE TABLE if not exists user (
  id bigint not null auto_increment,
  username varchar(255) NOT NULL ,
  password varchar(255) NOT NULL ,
  unique(username),
  PRIMARY KEY (id));

CREATE TABLE if not exists user_role (
  id bigint not null auto_increment,
  role varchar(255) NOT NULL,
  user_id bigint not null,
  PRIMARY KEY (id),
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user (id));


insert into user (id,username,password) values (1,'admin','$2a$10$hvVye7n1ib21eQamI8Ub5O/5KdG7cdiVB7yziBZUjpzxuFPGer5nK');
insert into user (id,username,password) values (2,'user','$2a$10$Q0TY7lnDjZkoxdir6JiZw.BIabMGcGbqKRUUzZEfArgGERsig/CGG');

insert into user_role (id,role,user_id) values (1,'ADMIN',1);
insert into user_role (id,role,user_id) values (2,'USER',1);
insert into user_role (id,role,user_id) values (3,'USER',2);