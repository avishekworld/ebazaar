#create the ProductsDb

CREATE DATABASE ProductsDb;


#create the AccountsDb

CREATE DATABASE AccountsDb;
USE AccountsDb;

#create AccountsDb.Customer

CREATE TABLE Customer (
    custid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    fname VARCHAR(50),
    lname VARCHAR(50),
    ssn VARCHAR(20),
    login VARCHAR(20),
    password VARCHAR(20),
    shipaddress1 VARCHAR(50),
    shipaddress2 VARCHAR(50),
    shipcity VARCHAR(25),
    shipstate VARCHAR(20),
    shipzipcode VARCHAR(15),
    billaddress1 VARCHAR(50),
    billaddress2 VARCHAR(50),
    billcity VARCHAR(25),
    billstate VARCHAR(20),
    billzipcode VARCHAR(15),
    nameoncard VARCHAR(50),
    expdate VARCHAR(20),
    cardtype VARCHAR(20),
    cardnum VARCHAR(20),
    PRIMARY KEY (custid)
);

INSERT INTO Customer VALUES
(NULL,'John','Smith','555882121','1','1','1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','John','01/31/2020','MasterCard','5555666655556666');
INSERT INTO Customer VALUES
(NULL,'Jim', 'Jacob','515282426','2','2',  '40 N. 4th St.',   NULL,    'Fairfield', 'IA','52556','40 N. 4th St.',  NULL,       'Fairfield', 'IA','52556','James','04/30/2022','Visa','4444666633336666');

SELECT custid, fname, ssn FROM Customer WHERE custid=1;

#create AccountsDb.Ord

CREATE TABLE Ord (
    orderid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    custid SMALLINT UNSIGNED NOT NULL,
    shipaddress1 VARCHAR(50),
    shipaddress2 VARCHAR(50),
    shipcity VARCHAR(25),
    shipstate VARCHAR(20),
    shipzipcode VARCHAR(15),
    billaddress1 VARCHAR(50),
    billaddress2 VARCHAR(50),
    billcity VARCHAR(25),
    billstate VARCHAR(20),
    billzipcode VARCHAR(15),
    nameoncard VARCHAR(50),
    expdate VARCHAR(20),
    cardtype VARCHAR(20),
    cardnum VARCHAR(20),
    orderdate VARCHAR(20),
    shippeddate VARCHAR(20),
    delivereddate VARCHAR(20),
    shipstatus VARCHAR(20),
    totalpriceamount DOUBLE,
    totalshipmentcost DOUBLE,
    totaltaxamount DOUBLE,
    totalamountcharged DOUBLE,
    PRIMARY KEY (orderid)
);

INSERT INTO Ord VALUES
(NULL,1,'1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','John','01/31/2020','MasterCard','5555666655556666','11/11/2002','11/15/2002','11/20/2002','delivered',50.0,5.0,5.0,60.0);

SELECT c.custid, c.fname, c.lname, c.custid, o.orderid FROM Customer c, Ord o WHERE c.custid=o.custid;

#create AccountsDb.OrderItem

CREATE TABLE OrderItem (
    orderitemid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    orderid SMALLINT UNSIGNED NOT NULL,
    productid SMALLINT UNSIGNED NOT NULL,
    quantity INT UNSIGNED,
    totalprice DOUBLE,
    shipmentcost DOUBLE,
    taxamount DOUBLE,
    PRIMARY KEY (orderitemid)
);

INSERT INTO OrderItem VALUES
(NULL,1,1,1,20.0,2.0,1.5);
INSERT INTO OrderItem VALUES
(NULL,1,2,2,30.0,3.0,3.5);

SELECT c.custid, c.fname, c.lname, o.orderid, oi.orderitemid FROM Customer c, Ord o, OrderItem oi WHERE o.orderid=oi.orderid AND c.custid=o.custid;


#create AccountsDb.AltShipAddress

CREATE TABLE AltShipAddress (
    addressid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    custid SMALLINT UNSIGNED NOT NULL,
    street VARCHAR(50),
    city VARCHAR(50),
    state VARCHAR(25),
    zip VARCHAR(25),    
    PRIMARY KEY (addressid)
);

INSERT INTO AltShipAddress VALUES
(NULL,1,"101 N COURT", "FAIRFIELD","IA","52556");
INSERT INTO AltShipAddress VALUES
(NULL,1,"59 BEETLE", "PALO ALTO","CA","94301");
INSERT INTO AltShipAddress VALUES
(NULL,2,"5 ADAMS", "FAIRFIELD","IA","52556");

SELECT c.custid, c.fname, c.lname, a.* FROM Customer c, AltShipAddress a WHERE c.custid = a.custid;


#create AccountsDb.ShopCartItem
CREATE TABLE ShopCartItem (
    cartitemid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    shopcartid SMALLINT UNSIGNED NOT NULL,
    productid SMALLINT UNSIGNED NOT NULL,
    quantity INT UNSIGNED,
    totalprice DOUBLE,
    shipmentcost DOUBLE,
    taxamount DOUBLE,    
    PRIMARY KEY (cartitemid)
);

INSERT INTO ShopCartItem VALUES
(NULL,1,3,1,50.0, 2.0, 1.5);
INSERT INTO ShopCartItem VALUES
(NULL,1,2,1,15.0, 1.50, 1.0);

#create AccountsDb.ShopCartTbl

CREATE TABLE ShopCartTbl (
    shopcartid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    custid SMALLINT UNSIGNED NOT NULL,
    shipaddress1 VARCHAR(50),
    shipaddress2 VARCHAR(50),
    shipcity VARCHAR(25),
    shipstate VARCHAR(20),
    shipzipcode VARCHAR(15),
    billaddress1 VARCHAR(50),
    billaddress2 VARCHAR(50),
    billcity VARCHAR(25),
    billstate VARCHAR(20),
    billzipcode VARCHAR(15),
    nameoncard VARCHAR(50),
    expdate VARCHAR(20),
    cardtype VARCHAR(20),
    cardnum VARCHAR(20),   
    totalpriceamount DOUBLE,
    totalshipmentcost DOUBLE,
    totaltaxamount DOUBLE,
    totalamountcharged DOUBLE, 
    PRIMARY KEY (shopcartid)
);

INSERT INTO ShopCartTbl VALUES
(NULL,1,'1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','1000 N. 4th St.','PO Box 23','Fairfield', 'IA','52557','John','01/31/2020','MasterCard','5555666655556666',65.0, 3.50, 2.50, 71.0);

SELECT c.custid, c.fname, c.lname, sc.shopcartid, sc.totalamountcharged, sci.cartitemid FROM Customer c, ShopCartTbl sc, ShopCartItem sci WHERE c.custid=sc.custid AND sc.shopcartid=sci.shopcartid;

USE ProductsDb;

#create ProductsDb.CatalogType

CREATE TABLE CatalogType (
    catalogid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    catalogname VARCHAR(50),
    PRIMARY KEY(catalogid)
);

INSERT INTO CatalogType VALUES
(NULL, 'Books');
INSERT INTO CatalogType VALUES
(NULL, 'Clothing');

SELECT * FROM CatalogType;

#create ProductsDb.Product

CREATE TABLE Product (
    productid SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    catalogid SMALLINT UNSIGNED NOT NULL,
    productname VARCHAR(50),
    totalquantity INT UNSIGNED,
    priceperunit DOUBLE,
    mfgdate VARCHAR(20),
    description VARCHAR(255),
    PRIMARY KEY(productid)
);

INSERT INTO Product VALUES
(NULL,1,'Gone With The Wind', 100, 20.0, '10/10/2001','A moving classic that tells a tale of love and a tale of courage.');
INSERT INTO Product VALUES
(NULL,1,'Messiah of Dune', 30, 15.0, '05/20/2000','You saw how good Dune was. This is Part 2 of this unforgettable trilogy.');
INSERT INTO Product VALUES
(NULL,1,'Garden of Rama', 200, 50.0, '02/20/1995','Highly acclaimed Book of Isaac Asimov. A real nail-biter.');
INSERT INTO Product VALUES
(NULL,2,'Pants', 400, 25.0, '06/10/2002','I have seen the Grand Canyon. I have camped at Yellowstone. But nothing on earth compares to the glory of wearing these pants.');
INSERT INTO Product VALUES
(NULL,2,'T-Shirts', 100, 12.0, '10/05/2000','Can be worn by men or women. Always in style.');
INSERT INTO Product VALUES
(NULL,2,'Skirts', 80, 30.0, '01/01/2004','Once this brand of skirts becomes well-known, watch out!');

SELECT p.productid, c.catalogname, p.productname FROM CatalogType c, Product p WHERE c.catalogid = p.catalogid;
