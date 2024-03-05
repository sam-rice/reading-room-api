# Reading Room API

**[Deployed Site](https://reading-room-ui.vercel.app/)**

<br />

This is the home of the Reading Room API, the back-end service of a full-stack personal project still in development. 

Repository for the project's React/Next.JS UI [can be found here](https://github.com/sam-rice/reading-room-ui/).

<br />

#### Back End

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit-ED8B00?style=for-the-badge&logoColor=white" />
  <img src="https://img.shields.io/badge/Heroku-430098?style=for-the-badge&logo=heroku&logoColor=white" />
</p>

#### [Front End](https://github.com/sam-rice/reading-room-ui/)

<p align="left">
  <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" />
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=next.js&logoColor=white" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" />
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" />
  <img src="https://img.shields.io/badge/prettier-1A2C34?style=for-the-badge&logo=prettier&logoColor=F7BA3E" />
  <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white" />
  <img src="https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white" />
</p>

## Abstract

Reading Room is a full-stack web application that allows users to browse and catalogue a virtual library of books. New users can be registered, authenticated, and create custom "shelves"—individual collections of books. Books with corresponding user notes can then be edited or added/removed from each shelf. Within the "browse" section of the app, users can search for new books by title or author name. The app's target users are modern book-lovers who need an instant, mobile way to search for new books and keep track of thier book collections.

The REST API is built with Java/Spring Boot, Docker, and PostgreSQL, and leverages the [Open Library API](https://openlibrary.org/developers/api) for all book and author data. The service is deployed via Heroku and configured with unrestricted access for demoing purposes. See [API Reference](#api-reference) below for demoing the API with Postman. Instructions for registering/authenticating users via JSON Web Token, creating/updating/deleting shelves and books, and querying data are also outlined below, in addition to [project setup instructions](#local-setup-instructions) for running the application locally. 

The project also includes a JUnit integration test suite for all repository classes, which leverages an H2 in-memory database.

<br />

## Table of Contents

- [Local Setup Instructions](#local-setup-instructions)
- [API Reference](#api-reference)
  - [Usage Overview](#usage-overview)
  - [Persistence Endpoints](#persistence-endpoints)
    - [User](#user-endpoints)
    - [Shelf](#shelf-endpoints)
    - [Book](#book-endpoints)
  - [Library Search Endpoints](#library-search-endpoints)
    - [Author Search by Name](#author-search-by-name)
    - [Author Details](#author-details)
    - [Book Search by Title](#book-search-by-title)
    - [Book Details](#book-details)

<br />

## Local Setup Instructions

Running this project and/or integration tests locally requires installations of [JRE](https://www.java.com/en/download/manual.jsp), [JDK 17](https://www.oracle.com/java/technologies/downloads/), [Docker Desktop](https://www.docker.com/products/docker-desktop/), and an IDE of your choice. Maven is also required and can either be installed locally, or accessed via IDE plugin.

1. Clone this repository to your machine.
2. Open Docker Desktop.
3. From the command line, navigate to the top level of the project repository and run `docker-compose up` to start the Postgres database.
4. Open the project with your IDE and run the application to spin up the server.

Note that when running locally, the project is configured to seed all database tables with data for demo purposes. Stopping and starting the server will drop, create, and re-seed the tables.

<br />

## API Reference

### Usage Overview

All endpoints besides "User" endpoints (user log in and register) require a valid authentication token in the request header to recieve a successful response. To recieve an auth token cookie, use the `/users/login` endpoint to log in as an existing user or create a new one using the `/users/register` endpoint ([User endpoints](#user-endpoints)). Auth tokens are valid for 2 hours. Proper header formatting shown below (note the space between "Bearer" and token):

```
{ "Authorization": "Bearer <Auth Token>" }
```

To get started, login as an existing demo user or register a new user.

#### Demo User:
<code>email: `jimmy@mail.com`
password: `guitar`</code>

### A Note on Semantics

The [Open Library API](https://openlibrary.org/developers/api) refers to an author's individual works as "works," while treating individual editions of a work as "books." The current version of this application simply treats an author's individual works as "books" and does not expose data unique to any specific edition. As a result, the code for this project includes POJO interfaces for JSON deserialization that refer to works and books according to Open Library's semantics. This is important to keep in mind for understanding the source code of this project. For example, a JSON response from Open Library being recieved by this API may start as a "work," and once deserialized, be morphed into a "book" along with other data returned from a set of aggregated requests to Open Library.

Any book saved to a user's shelf includes a `bookId` and `libraryKey` field. A `bookId` is unique to every saved book, while a `libraryKey` is used for integrating with Open Library, and is used for fetching a book's details via the [book details endpoint](#book-details). For example, a user could save several "copies" of one book—all of which have the same `libraryKey` field—that each have a unique `bookId`.

<br />

#### Base URL

```http
  DEPLOYED: https://reading-room-api-d84cba6ce967.herokuapp.com/api

  LOCAL: http://localhost:8080/api
```

<br />

## Persistence Endpoints

### `User` Endpoints

#### Register New User

```http
  /users/register
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td><td>Set-Cookie Header Value Example</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "firstName": string,
  "lastName": string,
  "email": string,
  "password": string
}</code>
      </td>
      <td>
<code>{
  "firstName": string,
  "lastName": string,
  "email": string
}</code>
      </td>
      <td>
        "token=eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOj; Path=/; Expires=Sat, 01 Jan 72000 08:00:00 GMT; Secure; HttpOnly"
      </td>
    </tr>
  </tbody>
</table>

\* Correct email format required.

<br />

#### Log In Existing User

```http
  /users/login
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td><td>Set-Cookie Header Value Example</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "email": string,
  "password": string
}</code>
      </td>
      <td>
<code>{
  "firstName": string,
  "lastName": string,
  "email": string
}</code>
      </td>
      <td>
        "token=eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOj; Path=/; Expires=Sat, 01 Jan 72000 08:00:00 GMT; Secure; HttpOnly"
      </td>
    </tr>
  </tbody>
</table>

##

<br />

### `Shelf` Endpoints

#### Get All Shelves by Active User

```http
  /shelves
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
        n/a
      </td>
      <td>
<code>{
  "shelfId": number,
  "userId": number,
  "title": string,
  "description": string,
  "totalSavedBooks": number
}[]</code>
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "shelfId": number,
  "userId": number,
  "title": string,
  "description": string,
  "totalSavedBooks": number,
  "books": {
    "bookId": number,
    "shelfId": number,
    "userId": number,
    "libraryKey": string,
    "title": string,
    "authors": {
        name: string,
        libraryKey: string
      }[],
    "coverUrl": string | null,
    "userNote": string | null,
    "savedDate": number
  }[] | null
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "title": string,
  "description": string
}</code>
      </td>
      <td>
<code>{
  "shelfId": number,
  "userId": number,
  "title": string,
  "description": string,
  "totalSavedBooks": number
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>PUT</code></td>
      <td>
<code>{
  "title": string,
  "description": string
}</code>
      </td>
      <td>
<code>{ "success": boolean }</code>
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>DELETE</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{ "success": boolean }</code>
      </td>
    </tr>
  </tbody>
</table>

##

<br />

### `Book` Endpoints

#### Get Book by Id

```http
  /shelves/{shelfId}/books/{bookId}
```

<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "bookId": number,
  "shelfId": number,
  "userId": number,
  "libraryKey": string,
  "title": string,
  "authors": {
      name: string,
      libraryKey: string
    }[],
  "coverUrl": string | null,
  "userNote": string | null,
  "savedDate": number
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>POST</code></td>
      <td>
<code>{
  "libraryKey": string
}</code>
      </td>
      <td>
<code>{
  "bookId": number,
  "shelfId": number,
  "userId": number,
  "libraryKey": string,
  "title": string,
  "authors": {
      name: string,
      libraryKey: string
    }[],
  "coverUrl": string | null,
  "userNote": string | null,
  "savedDate": number
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>PUT</code></td>
      <td>
<code>{ "userNote": string | null }</code>
      </td>
      <td>
<code>{ "success": boolean }</code>
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
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>DELETE</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{ "success": boolean }</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

## Library Search Endpoints

Note: query parameters in endpoints should replace whitespace with `%20`

### Author Search Endpoints

#### Author Search by Name

```http
  /search/authors?q={authorName}&size={numberOfResultsPerPage}&page={pageNumber}
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "totalResults": number,
  "pageSize": number,
  "pageNum": number,
  "results": {
    "libraryKey": string,
    "name": string,
    "birthDate": string | null,
    "deathDate": string | null,
    "topBook": string | null,
    "topSubjects": string[] | null
  }[]
}</code>
      </td>
      </tr>
  </tbody>
</table>

<br />

#### Author Details

```http
  /search/authors/{authorLibraryKey}
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "libraryKey": string,
  "name": string,
  "bio": string | null,
  "photoUrl": string | null,
  "birthDate": string | null,
  "deathDate": string | null,
  "books": {
    "libraryKey": string,
    "title": string,
    "publishDate": string,
    "primaryAuthor": {
      "name": string,
      "libraryKey": string  
    },
    "byMultipleAuthors": boolean,
    "coverUrl": string | null,
    "subjects": string[] | null
  }[] | null,
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

### Book Search Endpoints

#### Book Search by Title

```http
  /search/books?q={bookTitle}&size={numberOfResultsPerPage}&page={pageNumber}
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "totalResults": number,
  "pageSize": number,
  "pageNum": number,
  "results: {
    "libraryKey": string,
    "title": string,
    "publishYear": number | null,
    "editionCount": number,
    "authors": {
        "name": string,
        "libraryKey": string
      }[],
    "coverUrl": string | null,
    "subjects": string[] | null
  }[]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />

#### Book Details

```http
  /search/books/{bookLibraryKey}
```
<table>
  <tbody>
    <tr>
      <td>Method</td><td>Request Body</td><td>Successful Response</td>
    </tr>
    <tr>
      <td><code>GET</code></td>
      <td>
<code>n/a</code>
      </td>
      <td>
<code>{
  "libraryKey": string,
  "title": string,
  "description": string | null,
  "publishDate": string | null,
  "authors": {
      "name": string,
      "libraryKey": string
    }[]
  "coverUrl": string | null,
  "subjects": string[] | null,
  "associatedShelves": {
    "shelfId": number,
    "title": string
  }[]
}</code>
      </td>
    </tr>
  </tbody>
</table>

<br />
