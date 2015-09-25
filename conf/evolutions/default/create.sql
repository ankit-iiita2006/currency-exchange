
CREATE DATABASE play;

GRANT ALL PRIVILEGES ON play.* TO 'root'@'%' WITH GRANT OPTION;

USE DATABASE play;

create table currencymap (
  id                        bigint not null AUTO_INCREMENT,
  name                      varchar(255) not null,
  currencyCode              varchar(255) not null,
  lastUpdatedBy             varchar(255),
  isBase                    BOOL,
  valuation                 DECIMAL(12,4) not null,
  created_at                DATETIME ,
  updated_at                DATETIME,
  constraint pk_company primary key (id))engine=innodb
;


create table User (
  id                        bigint not null AUTO_INCREMENT,
  name                      varchar(255) not null,
  email              varchar(255) not null,
  password             varchar(255),
  constraint pk_company primary key (id))engine=innodb
;




