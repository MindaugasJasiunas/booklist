export const environment = {
  production: true,
  // loginUrl: (window as { [key: string]: any })["env"]["loginUrl"] as string,
  loginUrl: (window as { [key: string]: any })["env"]["loginUrl"] || 'http://localhost:9090/login',
  registerUrl: (window as { [key: string]: any })["env"]["registerUrl"] || 'http://localhost:9090/register',
  resetAccessTokenUrl: (window as { [key: string]: any })["env"]["resetAccessTokenUrl"] || 'http://localhost:9090/resettoken',
  getBookUrl: (window as { [key: string]: any })["env"]["getBookUrl"] || 'http://localhost:9090/api/v1/books',
  getBooksUrl: (window as { [key: string]: any })["env"]["getBooksUrl"] || 'http://localhost:9090/api/v1/books/',// + ISBN
  searchBooksUrl: (window as { [key: string]: any })["env"]["searchBooksUrl"] || 'http://localhost:9090/api/v1/books?search=',
  booksRemoteEntry: (window as { [key: string]: any })["env"]["booksRemoteEntry"] || 'http://localhost:4202/remoteEntry.js',
};
