[![Build Status](https://travis-ci.org/ismaelcabanas/test-web-application.svg?branch=develop)](https://travis-ci.org/ismaelcabanas/test-web-application)

# test-web-application
The **test-web-application** is an technical test for backend Java developer for 
building an application server.

# Getting Started

1. **test-web-application** is a Java application developed with 1.8.0_101 version, so start by downloading [Java 1.8.0_101](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or greater
2. **test-web-application** is built using Maven, so download [Maven 3.3.9 or greater](https://maven.apache.org/download.cgi)
3. Clone the git repository with `git clone --branch master https://github.com/ismaelcabanas/test-web-application.git` into a local folder
4. Run `mvn clean install` will start executing **unit tests**, **integration tests** and **acceptance-tests**
    * If you want skip tests write `mvn clean install -DskipTests=true`
    * If you want skip just unit tests write `mvn clean install -DskipUTs=true`
    * If you want skip just integration and acceptance test write `mvn clean install -DskipITs=true`
5. The application distribution archive can be found `/target` directory
6. Run the application from command line `java -jar /target/test-web-application.jar`
    * The application has two optional parameters:
        * **-Dport**: indicating the listening port for the application, by default is **8080**
        * **-DsessionTimeout**: indicating the max age of session in seconds, by default is **300** seconds (5 minutes) 
7. After the application has started, the web application should be available in [http://localhost:8080](http://localhost:8080), if you started it with default optional parameters.

# The test-web-application

This application is a web application building with [HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) API. The goal is for the application server to serve content by trying to follow the MVC pattern.

## Initialization

The application has four roles: **ADMIN**, **PAGE_1**, **PAGE_2**, **PAGE_3**. 
And the application is loaded with four users with following roles:
* Admin user:
    * username: admin
    * password: admin
    * roles: ADMIN
* User1 user:
    * username: user1
    * password: changeit
    * roles: PAGE_1
* User2 user:
    * username: user2
    * password: changeit
    * roles: PAGE_2
* User3 user:
    * username: user3
    * password: changeit
    * roles: PAGE_3        

In this way, users with role PAGE_1 can access to resources with role PAGE_1, users with role PAGE_2 can access to resources with role PAGE_2, and users with role PAGE_3 can access to resources with role PAGE_3. The users with ADMIN role can access to any resource.

## Configuration

The **test-web-application** manages two context:
    * `/` context for the web resources
    * `/users` context for the REST user resources
    
In the **Main** java class is configured the security of the application, what web resources are private and what permissions have each private web resource. In this moment, 
there are three private resources `/page1`, `/pag2`, and `/page3` associated to roles `PAGE_1`, `PAGE_2` and `PAGE_3` respectively.

The REST resources are private resources and you need a valid credentials for accessing it.

## The Views and Controllers

The web and rest resources are managed by Controllers. Each resource is managed for one controller and they generate a particular view.

If you want manage a new resource in the web application, you must create a controller and a view for the resource; if the new 
 resource is a web resource, you must create a controller in the `cabanas.garcia.ismael.opportunity.controller.web` package and the 
 class web controller must extend of `cabanas.garcia.ismael.opportunity.controller.Controller` class;
 if the new resource is a rest resource, you must create a controller in the `cabanas.garcia.ismael.opportunity.controller.rest` and the 
 class rest controller must extend of `cabanas.garcia.ismael.opportunity.controller.Controller` class.

The views must implement `cabanas.garcia.ismael.opportunity.view.View` interface.

# REST API

**Create users**
----

Create a new user resource with username, password and separated comma list of roles
    
* **URL**

  `/users`

* **Method:**
  
  `POST`

 
*  **URL Params**

   Not apply 

*  **Content-type accepted**

   application/x-www-form-urlencoded
   
* **Data Params**

    **Example**
  `username=user4&password=changeit&roles=PAGE_1%2C+PAGE_2`

* **Success Response:**
  
  * **Code:** 201 <br />
    **Content:** `User(username=user4, password=********, roles=Roles(roleList=[Role(name=PAGE_1), Role(name= PAGE_2)]))`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 403 Forbidden <br />
    **Content:** 

**Update users**
----

Update roles of a given user resource

* **URL**

  `/users/{username}`
  
  **Example**
  `/users/user2`

* **Method:**
  
  `PUT`
 
*  **URL Params**

   Not apply 

*  **Content-type accepted**

   application/x-www-form-urlencoded
   
* **Data Params**

    **Example**
  `roles=ADMIN%2C+PAGE_2`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `User(username=user2, password=********, roles=Roles(roleList=[Role(name=ADMIN), Role(name=PAGE_2)]))`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 403 Forbidden <br />
    **Content:** 
  
  * **Code:** 404 Not found <br />
      **Content:**   
    
**Delete users**
----

Delete a given user resource

* **URL**

  `/users/{username}`
  
  **Example**
  `/users/user2`

* **Method:**
  
  `DELETE`
 
*  **URL Params**

   Not apply 

*  **Content-type accepted**

   Not apply
   
* **Data Params**

    Not apply

* **Success Response:**
  
  * **Code:** 204 <br />
    **Content:** 
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 403 Forbidden <br />
    **Content:** 

  * **Code:** 404 Not found <br />
    **Content:**
    
**Get user**
----

Get a given user resource

* **URL**

  `/users/{username}`
  
  **Example**
  `/users/user3`

* **Method:**
  
  `GET`
 
*  **URL Params**

   Not apply 

*  **Content-type accepted**

   Not apply
   
* **Data Params**

    Not apply

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `User(username=user3, password=********, roles=Roles(roleList=[Role(name=PAGE_3)]))`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 404 Not found <br />
    **Content:**