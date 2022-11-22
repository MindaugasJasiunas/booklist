(function (window) {
  window["env"] = window["env"] || {};

  // Environment variables
  window["env"]["booksUrl"] = "${BOOKS_URL}";
  window["env"]["searchBooksUrl"] = "${SEARCH_BOOKS_URL}";
  window["env"]["myBooksUrl"] = "${MY_BOOKS_URL}";
  window["env"]["wishlistUrl"] = "${WISHLIST_URL}";
})(this);
