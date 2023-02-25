create database OnlineBookStore;
use OnlineBookStore;

CREATE TABLE buyer (
    buyerId int primary key auto_increment,
    userName varchar(40),
    passWord VARCHAR(20),
    address VARCHAR(225),
    phone long
);
select * from buyer;
insert into buyer values (6, "gopi", "gp6699","chennai", 7896541230);
CREATE TABLE seller (
    sellerId int primary key auto_increment,
    sell_userName varchar(40),
    passWord VARCHAR(20),
    store_name varchar(225),
    address VARCHAR(225),
    phone long
);


CREATE TABLE books (
    book_id int primary key ,
    book_name varchar(40),
    price int,
    quantity VARCHAR(225),
    book_type long,
    author varchar(225)
);

insert into books values (115, "Python Programming", 250, 36, "Study", "VP");

select * from books;
SELECT * FROM books WHERE book_name="harry potter vol 1";

commit;
SELECT * FROM buyer;
SELECT * FROM seller;
-- drop table books;
-- drop database onlinebookstore;

create table delivery (
 book_id int,
 delivery_id varchar(225),
 buyer_id int,
 quantity int,
 address varchar(225),
 shop_id int,
 order_date date,
 order_delievry_date date
 );

-- drop table delivery; 
select * from delivery;
SELECT * FROM books WHERE author="gopu" and quantity>0;