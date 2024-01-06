# Reading Room API

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-%23039BE5.svg?&style=for-the-badge&logo=Docker&logoColor=white" />
</p>

Reading Room API is a REST API for a book cataloging application built with Java/Spring Boot and PostgreSQL. Users can create a "shelf" and add/remove books from each shelf. Endpoints for registering new users via JSON Web Token, adding/removing shelves and books, and reading data are outlined below, in addition to project setup instructions. 

The project also includes a JUnit integration test suite for all repository classes, which leverages an H2 in-memory database.

## Project Setup Instructions

Running this project requires local installations of [Java Runtime Environment (JRE)](https://www.java.com/en/download/manual.jsp), [Java Development Kit 17 (JDK)](https://www.oracle.com/java/technologies/downloads/), [Docker Desktop](https://www.docker.com/products/docker-desktop/), and an IDE of your choice. Maven is also required and can either be installed locally, or accessed via IDE plugin.

1. Clone your this repository to your machine.
2. Open Docker Desktop.
3. From the command line, navigate to the top level of the project repository and run `docker-compose up` to start the Postgres database.
4. Open the project with your IDE and run the application to spin up the server.

## API Reference

#### Base URL

```http
  http://localhost:8080/api
```

<br />

### USER Endpoints

#### Register New User

```http
  POST /users/register
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "firstName": [string],
  "lastName": [string],
  "email": [string],
  "password": [string]
}</code>
      </td>
      <td>
<code>{ 
  "token": &lt;Auth Token&gt;
}</code>
      </td>
    </tr>
  </tbody>
</table>

\* Note that correct email format is required.

<br />

#### Authenticate Existing User (Login)

```http
  POST /users/login
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "email": [string],
  "password": [string]
}</code>
      </td>
      <td>
<code>{ 
  "token": &lt;Auth Token&gt;
}</code>
      </td>
    </tr>
  </tbody>
</table>
