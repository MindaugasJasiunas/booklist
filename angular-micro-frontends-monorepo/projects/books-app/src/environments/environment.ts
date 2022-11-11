// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  booksUrl: 'http://localhost:9090/api/v1/books/',
  searchBooksUrl: 'http://localhost:9090/api/v1/books?search=',

  myBooksUrl: 'http://localhost:9090/api/v1/my-books/',
  wishlistUrl: 'http://localhost:9090/api/v1/wishlist/',
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
