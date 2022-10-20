## Description
Blog-jdbc is an educational console CRUD application that has the following entities:

* Writer
* Post
* Label

Application uses MySQL as DBMS. To run application locally you need first to create database manually 
on your instance of MySQL and then specify credentials to that DB in files:

* liquibase.properties
* database.properties

## Requirements
To build & run the application you need Java-11 and Maven-3.8.5 installed.

## Build and Run the Application
For creating required tables in the database you need to run first the Maven command:

`mvn clean package`

Application can be run in IDE by running Main.main() method.