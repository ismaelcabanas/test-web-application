[![Build Status](https://travis-ci.org/ismaelcabanas/test-web-application.svg?branch=develop)](https://travis-ci.org/ismaelcabanas/test-web-application)

# test-web-application
The **test-web-application** is an technical test for backend Java developer for 
building an application server.

# Getting Started

1. **test-web-application** is a Java application developed with 1.8.0_101 version, so start by downloading [Java 1.8.0_101](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or greater
2. **test-web-application** is built using Maven, so download [Maven 3.3.9 or greater](https://maven.apache.org/download.cgi)
3. Clone the git repository git clone [https://github.com/ismaelcabanas/test-web-application.git](https://github.com/ismaelcabanas/test-web-application.git) into a local folder
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