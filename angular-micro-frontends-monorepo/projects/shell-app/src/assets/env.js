(function (window) {
  window["env"] = window["env"] || {};  // make a special (global) variable __env available in our browser window containing the environment variables for our application.

  // Environment variables
  window["env"]["loginUrl"] = 'http://localhost:9090/login';
  window["env"]["registerUrl"] = 'http://localhost:9090/register';
  window["env"]["resetAccessTokenUrl"] = 'http://localhost:9090/resettoken';
  window["env"]["getBookUrl"] = 'http://localhost:9090/api/v1/books';
  window["env"]["getBooksUrl"] = 'http://localhost:9090/api/v1/books/';
  window["env"]["searchBooksUrl"] = 'http://localhost:9090/api/v1/books?search=';
  window["env"]["booksRemoteEntry"] = 'http://localhost:4202/remoteEntry.js';
})(this);
