# oc-project-3

Here is the Chatop's project's API. It is developed in Java with Spring 3.3.4. 

## Preriquisites

• Java 17+
• Maven (for dependency management and building the project)
• MySQL (for the DB)

## Installation

1. Clone the repo

```
git clone https://github.com/JoPrignol/oc-project-3.git
cd oc-project-3
```

2. Install dependencies

```
mvn clean install
```

3. Create the DB

Create a new DB with MySQL and import the DB structure from chatop.sql

```
CREATE DATABASE db_name;
mysql -u YOUR_USERNAME -p db_name < chatop.sql
```

5. Plug this DB to the app via application.properties
   
6. Enter your cloudinary keys in configuration/CloudinaryConfig.java

7. Run the app

## Usage 

The app runs on http://localhost:3001

You can find the API documentation at http://localhost:3001/swagger-ui/index.html#/

