# booklist
Booklist fullstack project using Spring Boot microservices for backend and Angular micro frontends for UI.

User authentication using JWT refresh & access tokens, ability to view, paginate, add or remove books from/to 'My Books' or 'Wishlist'.

Application is using Mongo NoSQL database which is populated on `books-service` backend application startup using [JSON file](https://github.com/MindaugasJasiunas/booklist/blob/main/books-service/src/main/resources/books.json).

Backend services communication with frontend is implemented through Spring Cloud API Gateway & Eureka Service Discovery using `localhost:9090` URL.

Frontend application is accessible through URL `localhost:4201`.
Shell (base) micro-frontend application loads books micro-frontend as remote using WebPack Module Federation exporting `remoteEntry.js` file. 

## Run application's Docker containers
`docker compose -f "docker-compose.yml" up -d`

## Use application

As a guest (not logged-in user) you can navigate through books and search for book by author or title. Base URL to use is `http://localhost:4201/`


## Use application as logged-in user

### Backend applications endpoints

#### Books microservice
- Get book by ISBN
```
[GET] http://localhost:9090/api/v1/books/{ISBN}
```
- Get books with optional pagination (by default returns first 10 results)
```
[GET] http://localhost:9090/api/v1/books?page=0&size=10
```
- Create new book (requires authenticated user with 'book:create' authority set in JWT token as Authorization header)
```
[POST] http://localhost:9090/api/v1/books
```
```
{
    "author":  "J. K. Rowling",
    "title": "Harry Potter and the Order of the Phoenix",
    "isbn": "054579143X",
    "pages": "576",
    "hardTop": "true",
    "ebook": "false",
    "imageUrl": "https://m.media-amazon.com/images/I/51bZujlJxlL._SX422_BO1,204,203,200_.jpg"
}
```
- Update a book by ISBN (requires authenticated user with 'book:update' authority set in JWT token as Authorization header)
```
[PUT] http://localhost:9090/api/v1/books/054579143X
```
```
{
    "author":  "J. K. Rowling",
    "title": "Harry Potter and the Prisoner of Azkaban",
    "isbn": "9780439554923",
    "imageUrl": "https://m.media-amazon.com/images/P/1408855674.01._SCLZZZZZZZ_SX500_.jpg"
    "pages": "576"
}
```
- Partially update a book by ISBN (requires authenticated user with 'book:update' authority set in JWT token as Authorization header)
```
[PATCH] http://localhost:9090/api/v1/books/054579143X
```
```
{
    "pages": "577"
}
```
- Delete a book by ISBN (requires authenticated user with 'book:delete' authority set in JWT token as Authorization header)
```
[DELETE] http://localhost:9090/api/v1/books/{ISBN}
```
- Get My-Books with optional pagination (by default returns first 10 results) (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[GET] http://localhost:9090/api/v1/my-books
```
- Add new book by ISBN to My-Books (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[POST] http://localhost:9090/api/v1/my-books/{ISBN}
```
- Delete a book by ISBN from My-Books (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[DELETE] http://localhost:9090/api/v1/my-books/{ISBN}
```
- Get Wishlist with optional pagination (by default returns first 10 results) (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[GET] http://localhost:9090/api/v1/wishlist
```
- Add new book by ISBN to Wishlist (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[POST] http://localhost:9090/api/v1/wishlist/{ISBN}
```
- Delete a book by ISBN from Wishlist (requires authenticated user with 'book:read' (default) authority set in JWT token as Authorization header)
```
[DELETE] http://localhost:9090/api/v1/wishlist/{ISBN}
```

#### Users microservice (authentication)
- Register to create new user with default 'booklister' role and 'book:read' authority. Created user is retured as response.
```
[POST] http://localhost:9090/register
```
```
{
    "firstName" : "Jacob",
    "lastName": "Oliver",
    "email": "jacob.oliver@example.com",
    "password": "password"
}
```
- Login to get JWT refresh token as `Authorization` header. JSON object should be sent with "email" and "password" fields.
```
[POST] http://localhost:9090/login
```
```
{
    "email": "jacob.oliver@example.com",
    "password": "password"
}
```
- Reset access token using refresh token stored in Local Storage after login (if using endpoints manually - refresh token should be sent as `Authorization` header & new access token in 'Authorization' header is returned)
```
[POST] http://localhost:9090/resettoken
```

## Elastic Stack (formerly ELK) log aggregation
Microservices logs is accessible in Elastic Stack using Kibana URL [http://localhost:5601/app/kibana#/discover](http://localhost:5601/app/kibana#/discover).
#### Directions for first-time startup
- Go to Kibana [http://localhost:5601/app/kibana#/management/kibana/index_patterns](http://localhost:5601/app/kibana#/management/kibana/index_patterns) URL & add new index `logstash-*`
- Go to Kibana [http://localhost:5601/app/kibana#/discover](http://localhost:5601/app/kibana#/discover) URL and now you can see all backend microservices logging (aggregated logs) in one place.
