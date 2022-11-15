export const environment = {
  production: true,
  booksUrl: (window as { [key: string]: any })["env"]["booksUrl"] || 'http://localhost:9090/api/v1/books/',
  searchBooksUrl: (window as { [key: string]: any })["env"]["searchBooksUrl"] || 'http://localhost:9090/api/v1/books?search=',
  myBooksUrl: (window as { [key: string]: any })["env"]["myBooksUrl"] || 'http://localhost:9090/api/v1/my-books/',
  wishlistUrl: (window as { [key: string]: any })["env"]["wishlistUrl"] || 'http://localhost:9090/api/v1/wishlist/',
};
