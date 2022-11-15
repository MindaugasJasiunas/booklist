(function (window) {
  window["env"] = window["env"] || {};  // make a special (global) variable __env available in our browser window containing the environment variables for our application.

  // Environment variables
  window["env"]["booksUrl"] = 'http://localhost:9090/api/v1/books/',
  window["env"]["searchBooksUrl"] = 'http://localhost:9090/api/v1/books?search=',
  window["env"]["myBooksUrl"] = 'http://localhost:9090/api/v1/my-books/',
  window["env"]["wishlistUrl"] = 'http://localhost:9090/api/v1/wishlist/'
})(this);
