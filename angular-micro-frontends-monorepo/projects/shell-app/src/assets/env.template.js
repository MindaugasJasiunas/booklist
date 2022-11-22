(function (window) {
  window["env"] = window["env"] || {};

  // Environment variables
  window["env"]["loginUrl"] = "${LOGIN_URL}";
  window["env"]["registerUrl"] = "${REGISTER_URL}";
  window["env"]["resetAccessTokenUrl"] = "${RESET_ACCESS_TOKEN_URL}";
  window["env"]["getBookUrl"] = "${GET_BOOK_URL}";
  window["env"]["getBooksUrl"] = "${GET_BOOKS_URL}";
  window["env"]["searchBooksUrl"] = "${SEARCH_BOOKS_URL}";
  window["env"]["booksRemoteEntry"] = "${BOOKS_REMOTE_ENTRY_URL}";
})(this);

