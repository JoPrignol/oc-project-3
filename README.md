# Chatop
Here is the back-end of the Chatop application. Chatop is a rentals application connecting landlords and tenants. This app is built using Java and Spring

## Features 

• User authentication and profile view.

• Property listings: create, view, and update rentals.

• Message sending

• Cloudinary integration for image storage.

• API documentation with Swagger for easy access to endpoints.


## Preriquisites

To run this application locally, make sure you have the following installed:

  • Java 17 +
  
  • Maven
  
  • MySQL for the DB
  
  • Cloudinary account
  

## Getting started

1- Clone the repo

```
git clone https://github.com/JoPrignol/oc-project-3
cd oc-project-3
```

2- Setup the DB

Create a new DB and import the structure :

```
CREATE DATABASE chatop;
USE chatop;
SOURCE path/to/chatop.sql;
```

3- Configure environment variables

Create a .env file and put all of your environment variables in it like so: 

```
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
DB_URL=your_db_url
JWT_SECRET=your_jwt_secret
```

4- Build and run 

```
cd chatop
mvn clean install
```
the run the app

5- Use it 

The API will be available at http://localhost:3001
The swagger documentation will be available at : http://localhost:3001/swagger-ui/index.html#/

## Licence 

This project is licensed under the MIT License.




