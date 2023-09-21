create database port;

use port;

drop table Customer;
drop table Item;
drop table Cost;

drop view item_buyer;
drop view item_cost;

create table Customer (
       cid int auto_increment primary key,
       firstname varchar (50),
       lastname varchar (50),
       phonenumber varchar(10)
);

create table Item (
       iid int auto_increment primary key,
       itype varchar (10),
       iname varchar (100),
       container varchar (40),
       origin varchar (100),
       destination varchar (100),
       imp bool,
       price int,
       buyer int
);

create table cost (
       coid int auto_increment primary key,
       cprice decimal(15, 2),
       serviceCharge decimal(15, 2),
       taxrate decimal(15, 2),
       subtotal decimal(15, 2),
       total decimal(15, 2)
);


alter table Item
add constraint countries
check (origin in ('Ethiopia', 'China', 'India', 'Taiwan', 'Germany', 'USA', 'UK', 'Kenya'));


alter table Item
add constraint dest_places
check (destination in ('Ethiopia', 'China', 'India', 'Taiwan', 'Germany', 'USA', 'UK', 'Kenya'));

alter table Item
add constraint collision
check (origin in ('Ethiopia') or destination in ('Ethiopia'));

alter table Item
add constraint fk_buyer
foreign key (buyer) references customer (cid);

alter table Item
add constraint fk_cost
foreign key (price) references cost (coid);

insert into customer (firstname, lastname, phonenumber) values
('Abebe', 'Balcha', '0909090909');

select * from customer;

create view item_cost as
select iid, itype, iname, container, origin, destination, total, buyer from item left join cost on price = coid;

create view item_buyer as
select iid, itype, iname, container, origin, destination, total, firstname, lastname from item_cost left join customer on buyer = cid;

select * from Customer;
