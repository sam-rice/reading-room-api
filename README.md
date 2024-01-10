# Reading Room API

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-%23039BE5.svg?&style=for-the-badge&logo=Docker&logoColor=white" />
</p>

Reading Room is a REST API for a book cataloging application built with Java/Spring Boot, using the Java JDBC API to connect to a Docker-contained PostgreSQL database. Users have access to a variety of CRUD operations for interacting with `Shelf` entities (to which `Book` entities are associated) and adding/removing books from each shelf. Instructions for registering/authenticating users via JSON Web Token, creating/updating/deleting shelves and books, and querying data are outlined below, in addition to project setup instructions. 

The project also includes a JUnit integration test suite for all repository classes, which leverages an H2 in-memory database.

## Project Instructions

### Setup

Running this project requires local installations of [JRE](https://www.java.com/en/download/manual.jsp), [JDK 17](https://www.oracle.com/java/technologies/downloads/), [Docker Desktop](https://www.docker.com/products/docker-desktop/), and an IDE of your choice. Maven is also required and can either be installed locally, or accessed via IDE plugin.

1. Clone this repository to your machine.
2. Open Docker Desktop.
3. From the command line, navigate to the top level of the project repository and run `docker-compose up` to start the Postgres database.
4. Open the project with your IDE and run the application to spin up the server.

### Using the API

All `Book` and `Shelf`-related endpoints require a valid authentication token in the request header to recieve a successful response. To recieve an auth token, use the `/users/register` endpoint to "login" as a user. Auth tokens are valid for 2 hours. Proper header formatting shown below (note the space between "Bearer" and token):

```
{ "Authorization": "Bearer <Auth Token>" }
```

Also note that the project is configured to seed all database tables with data for demo purposes. Stopping and starting the server will drop, create, and re-seed the tables.

To quickly get started, login as a demo user, or register a new user.

Demo User:
```
{
  "email": "jimmy@mail.com",
  "password": "guitar"
}
```

## API Reference

#### Base URL

```http
  http://localhost:8080/api
```

<br />

### `User` Endpoints

#### Register New User

```http
  /users/register
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
  "userData": {
    "firstName": [string],
    "lastName": [string],
    "userId": [number],
    "email": [string]
  },
  "token": &lt;Auth Token&gt;
}</code>
      </td>
    </tr>
  </tbody>
</table>

\* Note that correct email format is required.

<br />

#### Login Existing User

```http
  /users/login
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
  "userData": {
    "firstName": [string],
    "lastName": [string],
    "userId": [number],
    "email": [string]
  },
  "token": &lt;Auth Token&gt;
}</code>
      </td>
    </tr>
  </tbody>
</table>

##

### `Shelf` Endpoints

#### Get All Shelves by Active User

```http
  /shelves
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
        n/a
      </td>
      <td>
<code>[{
  "shelfId": [number],
  "userId": [number],
  "title": [string],
  "description": [string],
  "totalSavedBooks": [number]
},
...]</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Get Shelf by Id

```http
  /shelves/{shelfId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "shelfId": [number],
  "userId": [number],
  "title": [string],
  "description": [string],
  "totalSavedBooks": [number]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Create New Shelf

```http
  /shelves
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
  "title": [string],
  "description": [string]
}</code>
      </td>
      <td>
<code>{
  "shelfId": [number],
  "userId": [number],
  "title": [string],
  "description": [string],
  "totalSavedBooks": [number]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Update Shelf

```http
  /shelves/{shelfId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>PUT</code></td>
      <td>
<code>{
  "title": [string],
  "description": [string]
}</code>
      </td>
      <td>
<code>{ "success": true }</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Delete Shelf

```http
  /shelves/{shelfId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>DELETE</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{ "success": true }</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

##

### `Book` Endpoints

#### Get All Books From Shelf

```http
  /shelves/{shelfId}/books
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
        n/a
      </td>
      <td>
<code>[{
  "bookId": [number],
  "shelfId": [number],
  "userId": [number],
  "isbn": [string],
  "olKey": [string || null],
  "title": [string],
  "author": [string || null],
  "userNote": [string || null],
  "savedDate": [number]
},
...]</code>
      </td>
    </tr>
  </tbody>
</table>

\* Note: `olKey` field is used for integrating with the [Open Library API](https://openlibrary.org/developers/api).
<br />

#### Get Book by Id

```http
  /shelves/{shelfId}/books/{bookId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "bookId": [number],
  "shelfId": [number],
  "userId": [number],
  "isbn": [string],
  "olKey": [string || null],
  "title": [string],
  "author": [string || null],
  "userNote": [string || null],
  "savedDate": [number]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Create New Book/Add to Shelf

```http
  /shelves/{shelfId}/books
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
  "isbn": [string],
  "olKey": [string || null],
  "title": [string],
  "author": [string || null],
  "userNote": [string || null],
}</code>
      </td>
      <td>
<code>{
  "bookId": [number],
  "shelfId": [number],
  "userId": [number],
  "isbn": [string],
  "olKey": [string || null],
  "title": [string],
  "author": [string || null],
  "userNote": [string || null],
  "savedDate": [number]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Update Book 

```http
  /shelves/{shelfId}/books/{bookId}
```

\* Note: Only `userNote` field can be updated.

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>PUT</code></td>
      <td>
<code>{ "userNote": [string || null] }</code>
      </td>
      <td>
<code>{ "success": true }</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Delete Book

```http
  /shelves/{shelfId}/books/{bookId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Sample Successful Response</td>
    </tr>
    <tr>
      <td><code>DELETE</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{ "success": true }</code>
      </td>
    </tr>
  </tbody>
</table>
