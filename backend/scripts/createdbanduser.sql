CREATE DATABASE IF NOT EXISTS flights;
GRANT ALL ON *.* TO 'flightsapp' IDENTIFIED BY 'topsecret';
FLUSH PRIVILEGES;

create schema if not exists flights collate utf8_general_ci;

create table if not exists flightinfo_companies
(
    id bigint auto_increment
        primary key,
    name varchar(100) null
)
    engine=MyISAM;

create table if not exists flightinfo_flights
(
    id binary(16) not null
        primary key,
    arrival_airport varchar(5) null,
    date_departure datetime null,
    departure_airport varchar(5) null,
    flight_id varchar(15) null,
    number_of_passengers int null,
    number_of_transfers int null,
    company_id bigint not null
)
    engine=MyISAM;

create index FKiquvj2mkqrq9fpsnb2bohaxlh
    on flightinfo_flights (company_id);
