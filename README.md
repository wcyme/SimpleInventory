# Introduction

This is a Java Web Application using Spring Boot as the backend application server.

Angular 9.0 is used as the frontend development framework.

A Microsoft SQL server is used as the Database server.

The application was generated using JHipster 6.10.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.10.3](https://www.jhipster.tech/documentation-archive/v6.10.3).

## Dependecies and Tools

You need the following dependencies to run the application:

1. Java jdk 11+
2. Node.js
3. Maven
4. Angular-cli
5. Docker (Optional, You can configure the database yourself but i would recommand using docker.)
6. Microsoft Windows 10 (Optional, You can use linux either but the code is developed under Windows Environment)

## Start The Application in Develop Mode (Windows 10)

1. Go to the directory ...yourPathToTheProject/Simple Inventory System/src/main/docker and run the following command to start the MsSQL database.

```
docker-compose -f mssql.yml up -d
```

2. Go to the directory ...yourPathToTheProject/Simple Inventory System and run the following command to install project dependencies

```
npm install
```

3. Run the following commands in two cmd prompt seperately to build and start the application

```
mvnw
```

```
npm start
```

4. Access the Web Application with a internet browser, recommended to use Chrome.

```
localhost:9000
```

## Using the Web Application

1. Login to the system with the following default account, shown as below:

```
User
login:    user
password: user
```

2. Click "Import CSV Data" on the navbar to import data to the database, the csv files are also provided at the same directory with this README.md file.

There are some restrictions on the format of csv file, basically, your csv file must end with ".csv" and you must contain the head row with specific headers in this csv files. You can add or modify data row of the sample csv files to create your own test.

3. Click "Inventory" on the navbar to view all the products and their inventory level in the system.

Within this "Inventory" page, you can create a new product and edit existing product with the buttons.

Press the "Manage" button to view the stock of that product at different locations.

Within the "Product Management" page, you can perform operations of internal transfer, stock in, stock out. Noted that, successful internal transfer operations will automatically create a internal transfer log.

4. Click "Internal Transfer Log" on the navbar to view the Internal Transfer Logs. You can sort the logs by any field in the table by clicking the header of the column.

## Common Issues Q &(A)

1. Firewall Blocking server port.

2. Same port are using.

3. Keep showing disconnected log after runing "npm start". (You need to run "mvnw" in another cmd prompt at the same time.)

4. Can't receive email after registering. (You need to configure your mail server yourself, I did have one either, just use the default account to access the system!)

## Remarks

I have tried my best to explain the application with this README.md. If you find something missing or could not run the application, please let me know. My email is shown as below:

```
wcy_me@hotmail.com
```
